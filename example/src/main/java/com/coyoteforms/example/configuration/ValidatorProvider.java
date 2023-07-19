package com.coyoteforms.example.configuration;

import com.coyoteforms.example.dto.LocationDto;
import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.example.validation.LocationDtoConnector;
import com.coyoteforms.example.validation.TriangletoConnector;
import com.coyoteforms.validator.Connector;
import com.coyoteforms.validator.CoyoteFormValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class ValidatorProvider {

    @Value("classpath:LocationRuleSet.json")
    private Resource ruleSetFile;

    @Value("classpath:TriangleFormRuleSet.json")
    private Resource triangleRuleSet;

    @Bean
    public CoyoteFormValidator<LocationDto> getLocationValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(ruleSetFile.getURI()));
        Connector<LocationDto> connector = new LocationDtoConnector();
        return new CoyoteFormValidator<>(ruleSet, connector);
    }

    @Bean
    public CoyoteFormValidator<TriangleDto> getTriangleValidator() throws IOException {
        String ruleSet = Files.readString(Path.of(triangleRuleSet.getURI()));
        Connector<TriangleDto> connector = new TriangletoConnector();
        return new CoyoteFormValidator<>(ruleSet, connector);
    }

}
