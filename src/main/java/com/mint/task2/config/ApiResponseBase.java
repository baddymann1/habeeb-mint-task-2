package com.mint.task2.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseBase<T> {
    private T Response;
    private String successMessage;
    private boolean hasError;
    private String errorMessage;


    private String errorCode;
    private String errorScope;
    @JsonProperty
    private List<Exception> exceptions;
}