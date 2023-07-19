package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormsValidatorCustomRelationshipTest {

    private static String ruleSet = " {" +
            "  \"constraints\": [" +
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
            " }";

    public static class IntervalConnector implements Connector<Interval> {

        @Override
        public Map<String, String> collectInputValues(Interval interval) {
            Map<String, String> inputValues = new HashMap<>();

            inputValues.put("startDate", interval.getStartDate() != null ? interval.getStartDate().toString() : "");
            inputValues.put("endDate", interval.getEndDate() != null ? interval.getEndDate().toString() : "");

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

    @Test
    public void validatorShouldCatchIfIntervalStartsBeforeToday() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(ruleSet, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(13))
                .build();
        List<String> invalidInputIds = validator.validate(interval);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("startDate");
    }

    @Test
    public void validatorShouldCatchIfIntervalLengthIsNotTwoWeeks() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(ruleSet, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(13))
                .build();
        List<String> invalidInputIds = validator.validate(interval);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("startDate", "endDate");
    }

    @Test
    public void validatorShouldCatchIfStartDateIsBeforeTodayAndIntervalLengthIsNotTwoWeeks() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(ruleSet, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().plusDays(13))
                .build();
        List<String> invalidInputIds = validator.validate(interval);
        assertThat(invalidInputIds).containsExactlyInAnyOrder("startDate", "endDate");
    }

}
