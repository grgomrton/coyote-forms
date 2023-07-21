package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormsValidatorCustomRelationshipTest {

    private static String RULE_SET = " {" +
            "  \"constraints\": [" +
            "    {" +
            "      \"inputIds\": [ \"intervalBeginsEarliestTomorrow\" ]," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \"true\", \"false\" ]" +
            "    }," +
            "    {" +
            "      \"inputIds\": [ \"intervalLengthDays\" ]," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }," +
            "    {" +
            "      \"inputIds\": [ \"startDate\" ]," +
            "      \"condition\": [ \"intervalBeginsEarliestTomorrow is true\", \"intervalLengthDays is 14\" ]," +
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"Interval must be two weeks long. Start date must be earliest tomorrow.\"" +
            "    }," +
            "    {" +
            "      \"inputIds\": [ \"endDate\" ]," +
            "      \"condition\": [ \"intervalLengthDays is 14\" ]," +
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"Interval must be two weeks long.\"" +
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
                        "intervalBeginsEarliestTomorrow",
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
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(RULE_SET, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(16))
                .build();

        Map<String, Set<String>> invalidInputs = validator.validate(interval);

        assertThat(invalidInputs).isEmpty();
    }

    @Test
    public void validatorShouldCatchIfIntervalStartsBeforeToday() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(RULE_SET, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(13))
                .build();
        Map<String, Set<String>> invalidInputs = validator.validate(interval);
        assertThat(invalidInputs.keySet()).containsExactlyInAnyOrder("startDate");
        assertThat(invalidInputs.get("startDate"))
                .containsExactlyInAnyOrder("Interval must be two weeks long. Start date must be earliest tomorrow.");
    }

    @Test
    public void validatorShouldCatchIfIntervalLengthIsNotTwoWeeks() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(RULE_SET, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(13))
                .build();

        Map<String, Set<String>> invalidInputs = validator.validate(interval);

        assertThat(invalidInputs.keySet()).containsExactlyInAnyOrder("startDate", "endDate");
        assertThat(invalidInputs.get("startDate"))
                .containsExactlyInAnyOrder("Interval must be two weeks long. Start date must be earliest tomorrow.");
        assertThat(invalidInputs.get("endDate"))
                .containsExactlyInAnyOrder("Interval must be two weeks long.");
    }

    @Test
    public void validatorShouldCatchIfStartDateIsBeforeTodayAndIntervalLengthIsNotTwoWeeks() {
        CoyoteFormValidator<Interval> validator = new CoyoteFormValidator<>(RULE_SET, new IntervalConnector());
        Interval interval = Interval.builder()
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().plusDays(13))
                .build();

        Map<String, Set<String>> invalidInputs = validator.validate(interval);

        assertThat(invalidInputs.keySet()).containsExactlyInAnyOrder("startDate", "endDate");
        assertThat(invalidInputs.get("startDate"))
                .containsExactlyInAnyOrder("Interval must be two weeks long. Start date must be earliest tomorrow.");
        assertThat(invalidInputs.get("endDate"))
                .containsExactlyInAnyOrder("Interval must be two weeks long.");
    }

}
