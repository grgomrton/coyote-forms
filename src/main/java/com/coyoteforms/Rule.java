package com.coyoteforms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Rule {

    private String inputId;

    private String condition;

    private List<String> permittedValues;

}
