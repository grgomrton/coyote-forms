package com.coyoteforms.validator;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // todo do we need this?
@ToString
public class ValidationFailure {

    private String inputId;

    private String helperText;

}
