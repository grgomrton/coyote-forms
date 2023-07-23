package com.coyoteforms.example.controller;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.validator.CoyoteFormsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/forms/location-form")
@CrossOrigin(originPatterns = "http://localhost:[*]")
public class LocationFormController {

    private CoyoteFormsValidator<LocationDto> validator;

    @Autowired
    public LocationFormController(CoyoteFormsValidator<LocationDto> validator) {
        this.validator = validator;
    }

    @PostMapping(path = "/inputs/{inputId}/permitted-values")
    public List<String> queryValidValueSet(@PathVariable String inputId, @RequestBody LocationDto inputValues) { // here we don't validate - the form is in an intermediate state
        return validator.queryValidValues(inputId, inputValues);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody LocationDto location) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
