package com.coyoteforms.example.dto;

import com.coyoteforms.example.validation.ValidInterval;
import lombok.Data;

import java.time.LocalDate;

@Data
@ValidInterval
public class DateIntervalDto {

    private LocalDate startDate;

    private LocalDate endDate;

}
