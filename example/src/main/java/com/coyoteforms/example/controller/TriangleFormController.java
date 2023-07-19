package com.coyoteforms.example.controller;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/forms/triangle-form")
@CrossOrigin(originPatterns = "http://localhost:[*]")
public class TriangleFormController {

    private CoyoteFormValidator<TriangleDto> validator;

    @Autowired
    public TriangleFormController(CoyoteFormValidator<TriangleDto> validator) {
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody TriangleDto triangle) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
