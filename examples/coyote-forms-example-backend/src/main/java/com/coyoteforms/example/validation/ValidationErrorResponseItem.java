package com.coyoteforms.example.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationErrorResponseItem {

    private String fieldId;

    private String reason;

}