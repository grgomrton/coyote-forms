# Coyote Forms Examples

You will need `JDK 11`, `maven` and `npm` to build and run the examples.

To build the backend, execute from the project root

`mvn clean install`

This will build the lib and the example backend modules.

To start the backend, execute from the project root:

`java -jar examples\coyote-forms-example-backend\target\coyote-forms-example-0.1-SNAPSHOT.jar`

The backend will listen onn port 8080.

To build the frontend, execute from the project root:
```
cd examples\coyote-forms-example-frontend
npm install
```
This will download necessary libraries and create a development build. To start the frontend, execute from the current folder:

`npm start`

The frontend server will listen on port 3000: [http://localhost:3000](http://localhost:3000)

## First example - Location

The first example is a location selector, which performs the standard one selector defines the next one behavior.
In this example it has a three-level depth.

The listed values are defined only in the validator, the frontend acquires them through a rest endpoint.

The most important thing is how the rules are defined.

```json
{
    "constraints": [
        {
            "inputIds": ["region"],
            "condition": [ "always" ],
            "permittedValues": [ "Americas", "EMEA" ]
        },
    ]
}
```

The first-level rule is defined with the "always" condition, meaning
under all circumstances.
The intermediate elements depend on the previous levels:

```json
{
    "constraints": [
        {
            "inputIds": ["country"],
            "condition": [ "region is Americas" ],
            "permittedValues": [ "U.S.A.", "Mexico" ]
        },
    ]
}
```

The `Connector` class is very simple:

```java
public class LocationDtoConnector implements Connector<LocationDto> {

    @Override
    public Map<String, String> collectInputValues(LocationDto obj) {
        return Map.of(
                "region", obj.getRegion() == null ? "" : obj.getRegion(),
                "country", obj.getCountry() == null ? "" : obj.getCountry(),
                "city", obj.getCity() == null ? "" : obj.getCity()
        );
    }

}
```

It maps every input field to the corresponding input, if missing, it fills with empty string.

This configuration is enough to instantiate a validator:

```java
new CoyoteFormsValidator<>(ruleSet, connector);
```

The validator is exposed through the `LocationFormController`, that provides the following endpoints:

```java
@PostMapping(path = "/inputs/{inputId}/permitted-values")
public List<String> queryValidValues(@PathVariable String inputId, @RequestBody LocationDto inputValues) {
    return validator.queryValidValues(inputId, inputValues);
}
```

```java
@PostMapping
public ResponseEntity<?> save(@Valid @RequestBody LocationDto location) {
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
```

The save endpoint simply sends a `CREATED` status back, if the dto passes validation - see the `@Valid` annotation.

Note: `@Valid` annotation to have this effect, needs additional plumbing which I don't explain in details, but working examples can be found in the example backend project.

# Example - Triangle

The next example is about non-discrete field validation. The imagined form is intended to set the angles of a triangle.
The angles must add up to 180.

## Custom inputs and regular expression matching

The rule to be tested is whether the sum of the three input fields equals 180. In order to validate that, 
we will define a custom input - an input that is not connected directly to any fields. 
See the following rule definition:

```json
{
  "constraints": [
    {
      "inputIds": [ "sumOfAngles" ],
      "condition": [ "always" ],
      "permittedValues": [ ".*" ]
    },
  ]
}
```

One might notice the strange value in the permittedValues field. Although in the previous example it looked like 
text values are matched against each-other, in reality the values are matched with a regular expression matcher. For simple 
texts it is the same, but there are a couple of special characters, that has special meaning. 
This combination, dot star means any text, including empty text.
Any amount of any characters.

The use of this input becomes clear, once we see the rest of the rule set.

```json
    {
      "inputIds": [ "alpha" ],
      "condition": [ "sumOfAngles is 180" ],
      "permittedValues": [ ".+" ]
    },
    {
      "inputIds": [ "beta" ],
      "condition": [ "sumOfAngles is 180" ],
      "permittedValues": [ ".+" ]
    },
    {
      "inputIds": [ "gamma" ],
      "condition": [ "sumOfAngles is 180" ],
      "permittedValues": [ ".+" ]
    }
```

These lines mean `if sumOfAngles is 180 then alpha can be any, at least one character long text`.
Yes, the special characters dot plus means at least one character long text.

This rule might be strange since we are talking about numbers, but for the validator everything is a text.

The last element of the solution is the `Connector<TriangleDto>`:

```java
public class TriangletoConnector implements Connector<TriangleDto> {
    @Override
    public Map<String, String> collectInputValues(TriangleDto obj) {
        Map<String, String> result = new HashMap<>();

        result.put("alpha", Integer.toString(obj.getAlpha()));
        result.put("beta", Integer.toString(obj.getBeta()));
        result.put("gamma", Integer.toString(obj.getGamma()));

        result.put("sumOfAngles", Integer.toString(obj.getAlpha() + obj.getBeta() + obj.getGamma()));

        return result;
    }
```

The custom input is filled with the result of the mathematical expression. Using the `Connector`s we can bind the results of complex expressions to inputs, and let the validator evaluate them.

Vigilant readers might notice that this validation can be hacked. Indeed if the user enters 100, 100, -20, that would also pass this evaluation.
In this example, it is solved by using a javax.validation annotation on the data transfer object:

```java
public class TriangleDto {

    @Min(1)
    private int alpha;

    @Min(1)
    private int beta;

    @Min(1)
    private int gamma;

}
```

This prevents entering negative values. In general, I think it is a good idea to use `javax` validation for single field validation.

## Example - Vacation

The last example is the vacation form, where a vacation request is validated against the following rules:

- Up to three days long vacation, the start date must be at least one week later
- Over three days long vacation, the start date must be at least two week before

This example is the most complex one. Let's start with the rule set. The set of the custom inputs is:

```json
    {
      "inputIds": [
        "intervalLength",
        "intervalLengthIsMoreThan3Days",
        "daysInAdvanceAtLeastOneWeek",
        "daysInAdvanceAtLeasTwoWeeks",
        "endDateIsAfterStart" ],
      "condition": [ "always" ],
      "permittedValues": [ ".*" ]
    },
```

The only notable difference here is that we can see the same constraint applied to multiple inputs.

The rest of the rule set is as follows:

```json
    {
      "inputIds": [ "startDate", "endDate" ],
      "condition": [ 
          "intervalLength is 1", 
          "daysInAdvanceAtLeastOneWeek is true", 
          "endDateIsAfterStart is true" ],
      "permittedValues": [ ".+" ],
      "helperText": "INTERVAL_UP_TO_THREE_DAYS_RULE"
    },
    {
      "inputIds": [ "startDate", "endDate" ],
      "condition": [ 
          "intervalLength is 2", 
          "daysInAdvanceAtLeastOneWeek is true", 
          "endDateIsAfterStart is true" ],
      "permittedValues": [ ".+" ],
      "helperText": "INTERVAL_UP_TO_THREE_DAYS_RULE"
    },
    {
      "inputIds": [ "startDate", "endDate" ],
      "condition": [ 
          "intervalLength is 3", 
          "daysInAdvanceAtLeastOneWeek is true", 
          "endDateIsAfterStart is true" ],
      "permittedValues": [ ".+" ],
      "helperText": "INTERVAL_UP_TO_THREE_DAYS_RULE"
    },
    {
      "inputIds": [ "startDate", "endDate" ],
      "condition": [ 
          "intervalLengthIsMoreThan3Days is true", 
          "daysInAdvanceAtLeasTwoWeeks is true", 
          "endDateIsAfterStart is true" ],
      "permittedValues": [ ".+" ],
      "helperText": "INTERVAL_MORE_THAN_THREE_DAYS_RULE"
    }
```

These rules read as follows: 

`startDate is valid if intervalLength is 1 and daysInAdvanceAtLeastOneWeek is true and endDateIsAfterStart is true or if intervalLength is 2 and daysInAdvanceAtLeastOneWeek is true and endDateIsAfterStart is true or if ...`

The validator will evaluate these possibilities and returns a list of validation failures if any.

To make this work, of course the `Connector` must be implemented. Here is the extract of the `Connector<DateIntervalDto>`:

```java
    @Override
    public Map<String, String> collectInputValues(DateIntervalDto interval) {
        Map<String, String> inputValues = new HashMap<>();

        // add field inputs to the map in order to be validated, use empty string if the value is not present
        inputValues.put("startDate", interval.getStartDate() == null ? "" : interval.getStartDate().toString());
        inputValues.put("endDate", interval.getEndDate() == null ? "" : interval.getEndDate().toString());

        // add the custom inputs to the map if they can be computed, otherwise leave it out
        if (interval.getStartDate() != null) {
            if (LocalDate.now(clock).plusDays(6).isBefore(interval.getStartDate())) {
                inputValues.put("daysInAdvanceAtLeastOneWeek", "true");
            }
            if (LocalDate.now(clock).plusDays(13).isBefore(interval.getStartDate())) {
                inputValues.put("daysInAdvanceAtLeasTwoWeeks", "true");
            }
        }
        if (interval.getStartDate() != null && interval.getEndDate() != null) {
            boolean endDateIsAfterStart = interval.getEndDate().isAfter(interval.getStartDate());
            inputValues.put("endDateIsAfterStart", Boolean.toString(endDateIsAfterStart));

            if (endDateIsAfterStart) {
                long workDayCount = calculateWorkDaysCountBetween(interval.getStartDate(), interval.getEndDate());
                inputValues.put("intervalLength", Long.toString(workDayCount));
                if (workDayCount > 3) {
                    inputValues.put("intervalLengthIsMoreThan3Days", "true");
                }
            }
        }
        return inputValues;
    }
```

Since this rule set has the most inputs, this connector will the longest one.

This sums up the examples in this project.

Thank you for reading, and happy coding!
