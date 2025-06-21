package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.UpvoteResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpvoteMapper {
    public List<UpvoteResponseDto> toUpvoteResponseDtoList(List<Upvote> upvotes) {
        return upvotes.stream().map(upvote -> new UpvoteResponseDto(upvote.getUser().getName())).toList();
    };
}
