package com.roadmaps.Roadmaps.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationError {
    private String field;
    private Object invalidValue;
    private String message;
}
