package com.coyoteforms.validator;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ValidationFailure {

    private String inputId;

    private String helperText;

}
