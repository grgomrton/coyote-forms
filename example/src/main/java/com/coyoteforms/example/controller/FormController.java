package com.coyoteforms.example.controller;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormController {

    private CoyoteFormValidator<LocationDto> validator;

    @Autowired
    public FormController(CoyoteFormValidator<LocationDto> validator) {
        this.validator = validator;
    }

//    public List<String> queryAllowedValues(String inputId, LocationDto inputValues) {
//
//    }

}
