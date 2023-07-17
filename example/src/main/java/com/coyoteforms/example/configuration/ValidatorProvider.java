package com.coyoteforms.example.configuration;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.validation.LocationDtoConnector;
import com.coyoteforms.validator.Connector;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ValidatorProvider {

    @Value("classpath:RuleSet.json")
    private Resource ruleSetFile;

    @Bean
    public CoyoteFormValidator<LocationDto> getValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(ruleSetFile.getURI()));
        Connector<LocationDto> connector = new LocationDtoConnector();
        return new CoyoteFormValidator<>(ruleSet, connector);
    }


}
