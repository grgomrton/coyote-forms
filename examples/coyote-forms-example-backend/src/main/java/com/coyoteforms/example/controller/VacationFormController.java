package com.coyoteforms.example.controller;

import com.coyoteforms.example.dto.DateIntervalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/forms/vacation-form")
@CrossOrigin(originPatterns = "http://localhost:[*]")
public class VacationFormController {

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody DateIntervalDto interval) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
