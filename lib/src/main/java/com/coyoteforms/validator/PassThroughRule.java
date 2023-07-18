package com.coyoteforms.validator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassThroughRule {

    private String inputId;

    private List<String> condition;

    private String errorMessage; // todo set this as reason

}
