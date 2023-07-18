package com.coyoteforms.validator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RuleSet {

    private List<DiscreteRule> discreteValueRules;

    private List<PassThroughRule> passThroughRules;

}
