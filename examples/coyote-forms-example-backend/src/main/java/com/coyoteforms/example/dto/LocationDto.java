package com.coyoteforms.example.dto;

import com.coyoteforms.example.validation.ValidLocation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ValidLocation
public class LocationDto {

    private String region;

    private String country;

    private String city;

}
