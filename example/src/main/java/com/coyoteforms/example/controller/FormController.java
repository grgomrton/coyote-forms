package com.coyoteforms.example.controller;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/forms/location-form")
public class FormController {

    private CoyoteFormValidator<LocationDto> validator;

    @Autowired
    public FormController(CoyoteFormValidator<LocationDto> validator) {
        this.validator = validator;
    }

    @PostMapping(path = "/inputs/{inputId}/allowed-values")
    public List<String> queryAllowedValues(@PathVariable String inputId, @RequestBody LocationDto inputValues) {
        return validator.queryAllowedValues(inputId, inputValues);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody LocationDto location) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
