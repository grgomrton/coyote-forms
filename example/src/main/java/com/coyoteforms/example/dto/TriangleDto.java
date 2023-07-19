package com.coyoteforms.example.dto;

import com.coyoteforms.example.validation.ValidTriangle;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ValidTriangle
public class TriangleDto {

    //@Min(0) // alternatively
    private Integer alpha;

    private Integer beta;

    private Integer gamma;

}
