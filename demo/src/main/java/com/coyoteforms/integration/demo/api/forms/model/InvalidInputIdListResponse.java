package com.coyoteforms.integration.demo.api.forms.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvalidInputIdListResponse {

    public List<String> invalidInputIds;

}
