package com.coyoteforms.example.validation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ValidationErrorResponse {

    private List<ValidationErrorResponseItem> invalidFields;

}

