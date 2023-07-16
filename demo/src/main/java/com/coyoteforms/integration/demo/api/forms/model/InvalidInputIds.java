package com.coyoteforms.integration.demo.api.forms.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvalidInputIds {

    public List<String> invalidInputIds;

}
