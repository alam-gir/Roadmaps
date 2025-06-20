package com.roadmaps.Roadmaps.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.List;

public class Utils {
    public static <T> Jackson2JsonRedisSerializer<T> listSerializer(ObjectMapper mapper, Class<?> elementType) {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return new Jackson2JsonRedisSerializer<>(mapper, javaType);
    }

    public static <T> Jackson2JsonRedisSerializer<T> valueSerializer(ObjectMapper mapper, Class<T> clazz) {
        return new Jackson2JsonRedisSerializer<>(mapper, clazz);
    }
}
