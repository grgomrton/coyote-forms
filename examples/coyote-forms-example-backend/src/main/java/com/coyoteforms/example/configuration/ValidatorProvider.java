package com.coyoteforms.example.configuration;

import com.coyoteforms.example.dto.DateIntervalDto;
import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.example.validation.DateIntervalConnector;
import com.coyoteforms.example.validation.LocationDtoConnector;
import com.coyoteforms.example.validation.TriangletoConnector;
import com.coyoteforms.validator.Connector;
import com.coyoteforms.validator.CoyoteFormsValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class ValidatorProvider {

    @Value("classpath:static/LocationFormRuleSet.json")
    private Resource locationRuleSet;

    @Value("classpath:static/TriangleFormRuleSet.json")
    private Resource triangleRuleSet;

    @Value("classpath:static/VacationFormRuleSet.json")
    private Resource vacationRuleSet;

    @Bean
    public CoyoteFormsValidator<LocationDto> getLocationValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(locationRuleSet.getURI()));
        Connector<LocationDto> connector = new LocationDtoConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

    @Bean
    public CoyoteFormsValidator<TriangleDto> getTriangleValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(triangleRuleSet.getURI()));
        Connector<TriangleDto> connector = new TriangletoConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

    @Bean
    public CoyoteFormsValidator<DateIntervalDto> getDateIntervalValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(vacationRuleSet.getURI()));
        Connector<DateIntervalDto> connector = new DateIntervalConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

}
