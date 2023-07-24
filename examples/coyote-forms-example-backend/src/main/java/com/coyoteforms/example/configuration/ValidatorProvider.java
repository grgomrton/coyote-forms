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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
        String ruleSet = new BufferedReader(new InputStreamReader(locationRuleSet.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
        Connector<LocationDto> connector = new LocationDtoConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

    @Bean
    public CoyoteFormsValidator<TriangleDto> getTriangleValidator() throws IOException {
        String ruleSet = new BufferedReader(new InputStreamReader(triangleRuleSet.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
        Connector<TriangleDto> connector = new TriangletoConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

    @Bean
    public CoyoteFormsValidator<DateIntervalDto> getDateIntervalValidator() throws IOException {
        String ruleSet = new BufferedReader(new InputStreamReader(vacationRuleSet.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
        Connector<DateIntervalDto> connector = new DateIntervalConnector();
        return new CoyoteFormsValidator<>(ruleSet, connector);
    }

}
