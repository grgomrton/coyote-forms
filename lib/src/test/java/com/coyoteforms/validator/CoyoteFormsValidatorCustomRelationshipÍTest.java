package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormsValidatorCustomRelationshipÍTest {

    // Planned input
    //  {
    //	"passThroughRules": [
    //		{
    //			"inputId": "startDate",
    //			"condition": [ "intervalBeginsTomorrow is 'true'", "intervalLengthDays is '14'" ]
    //		},
    //		{
    //			"inputId": "endDate",
    //			"condition": [ "intervalLengthDays is '14'" ]
    //		}
    //	]
    // }

    private static String ruleSet = " {" +
            "  \"discreteValueRules\": [" +
            "    {" +
            "      \"inputId\": \"intervalBeginsTomorrow\"," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \"true\", \"false\" ]" +
            "    }," +
            "    {" +
            "      \"inputId\": \"intervalLengthDays\"," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }," +
            "    {" +
            "      \"inputId\": \"startDate\"," +
            "      \"condition\": [ \"intervalBeginsTomorrow is 'true'\", \"intervalLengthDays is '14'\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }," +
            "    {" +
            "      \"inputId\": \"endDate\"," +
            "      \"condition\": [ \"intervalLengthDays is '14'\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }" +
            "  ]" +
            " };";

    public static class IntervalConnector implements Connector<Interval> {

        @Override
        public Map<String, String> collectInputValues(Interval interval) {
            Map<String, String> inputValues = new HashMap<>();
            if (interval.getStartDate() != null) {
                inputValues.put(
                        "intervalBeginsTomorrow",
                        Boolean.toString(interval.getStartDate().isAfter(LocalDate.now())));
            }
            if (interval.getStartDate() != null && interval.getEndDate() != null) {
                inputValues.put(
                        "intervalLengthDays",
                        Long.toString(Period.between(interval.getStartDate(), interval.getEndDate()).getDays()));
            }
            return inputValues;
        }
    }

    @Builder
    @Data
    public static class Interval {

        LocalDate startDate;

        LocalDate endDate;

    }

    @Test
    public void validatorShouldLetTwoWeeksLongIntervalAfterTodayThrough() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(ruleSet, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(16))
                .build();
        List<String> invalidInputIds = validator.validate(interval);
        assertThat(invalidInputIds).isEmpty();
    }

}
