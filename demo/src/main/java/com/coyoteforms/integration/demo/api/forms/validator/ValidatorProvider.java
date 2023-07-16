package com.coyoteforms.integration.demo.api.forms.validator;

import com.coyoteforms.integration.demo.api.forms.model.Location;
import com.coyoteforms.validator.CoyoteFormValidator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Builds and provides a Coyote Forms validator instance.
 */
public class ValidatorProvider {

    private static final String RULE_SET = "RuleSet.json";
    private static CoyoteFormValidator<Location> VALIDATOR;

    public static CoyoteFormValidator<Location> get() {
        synchronized (RULE_SET) {
            if (VALIDATOR == null) {
                String ruleSet = loadRuleSet();
                VALIDATOR = new CoyoteFormValidator<>(ruleSet, new Connector());
            }
        }
        return VALIDATOR;
    }

    private static String loadRuleSet() {
        try {
            return Files.readString(Paths.get(ClassLoader.getSystemResource(RULE_SET).toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
