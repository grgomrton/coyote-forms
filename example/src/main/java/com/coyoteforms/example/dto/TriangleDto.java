package com.coyoteforms.example.dto;

import com.coyoteforms.example.validation.ValidTriangle;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ValidTriangle
public class TriangleDto {

    @Min(1)
    private Integer alpha;

    @Min(1)
    private Integer beta;

    @Min(1)
    private Integer gamma;

}
