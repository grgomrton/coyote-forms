package com.coyoteforms.example.dto;

import com.coyoteforms.example.validation.ValidTriangle;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ValidTriangle
public class TriangleDto {

    @Min(1)
    private int alpha;

    @Min(1)
    private int beta;

    @Min(1)
    private int gamma;

}
