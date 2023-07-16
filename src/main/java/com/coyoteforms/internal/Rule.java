package com.coyoteforms.internal;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rule {

    private String inputId;

    private List<String> condition;

    private List<String> permittedValues;

}