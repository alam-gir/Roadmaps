package com.roadmaps.Roadmaps.modules.roadmap.repository.specification;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestFiltersDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.enumeration.ROADMAP_STATUS;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoadmapSpecification {
    public static Specification<Roadmap> build(RoadmapRequestFiltersDto filters){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // filter statuses :: here i use an extra filter such that if we pass invalid filter it's not throw illegalArgException.
            if(filters.getStatuses() != null && !filters.getStatuses().isEmpty()){
                List<ROADMAP_STATUS> statuses = filters.getStatuses().stream()
                        .map(String::toUpperCase)
                        .filter(s -> {
                            try {
                                ROADMAP_STATUS.valueOf(s);
                                return true;
                            } catch (Exception ex) {
                                return false;
                            }
                        })
                        .map(ROADMAP_STATUS::valueOf)
                        .toList();

                predicates.add(root.get("status").in(statuses));
            }

            // category ids
            if(filters.getCategoryIds() != null && !filters.getCategoryIds().isEmpty()){
                List<UUID> categoryIds = filters.getCategoryIds().stream()
                        .filter(id ->{
                            try{
                                UUID.fromString(id);
                                return true;
                            } catch (Exception ex){
                                return false;
                            }
                        })
                        .map(UUID::fromString).toList();

                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            // sort and order
            // get the sortBy and orderBy
            List<String> availableSortBy = List.of("popularity");
            List<String> availableOrderBy = List.of("ascending", "descending");
            String sortBy = filters.getSortBy() != null && availableSortBy.contains(filters.getSortBy().toLowerCase()) ? filters.getSortBy() : "";
            String orderBy = filters.getOrderBy() != null && availableOrderBy.contains(filters.getOrderBy().toLowerCase()) ? filters.getOrderBy() : "descending";

            if("popularity".equals(sortBy)){
                Join<Object, Object> upvotes = root.join("upvotes", JoinType.LEFT);
                assert query != null;
                query.groupBy(root.get("id"));
                query.orderBy("ascending".equals(orderBy)
                        ? criteriaBuilder.asc(criteriaBuilder.count(upvotes))
                        : criteriaBuilder.desc(criteriaBuilder.count(upvotes))
                );
            } else {
                assert query != null;
                query.orderBy("ascending".equals(orderBy)
                    ? criteriaBuilder.asc(root.get("createdAt"))
                        : criteriaBuilder.desc(root.get("createdAt"))
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
