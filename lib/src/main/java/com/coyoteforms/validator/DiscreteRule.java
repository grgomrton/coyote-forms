package com.coyoteforms.validator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DiscreteRule {

    private String inputId;

    private List<String> condition;

    private List<String> permittedValues;

}
