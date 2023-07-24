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
This will download necessary libraries and create a development build. To start the front-end, execute from the current folder:

`npm start`

The front-end will listen on port 3000, open it at [http://localhost:3000](http://localhost:3000) .

## First example - Location

The first example is a location selector, which performs the standard one selector defines the next one behaviour. 
In this example it has a three-level depth.

The field values are defined only in the validator, the frontend acquires them through a rest endpoint.

The most important thing is the way the rules are defined. The first-level rule is defined with the "always" condition, meaning
in all circumstances the following values are allowed.

```json
{
    "constraints": [
        {
            "inputIds": ["region"],
            "condition": [ "always" ],
            "permittedValues": [ "Americas", "EMEA" ]
        },
    ...
    ]
}
```

The intermediate elements always depend on the previous level:

```json
{
    "constraints": [
    ...
        {
            "inputIds": ["country"],
            "condition": [ "region is Americas" ],
            "permittedValues": [ "U.S.A.", "Mexico" ]
        },
    ...
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

It maps every input field to the corresponding input, if missing, then fills with empty string.

This configuration is enough to instantiate a validator with the command

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

# Example - Triangle

The next example is about non-discrete value field validation. The imagined form is intended to receive angles of a triangle.
The angles must add up to 180.

## Custom inputs and regular expression matching

The rule to be tested is whether the value of the three input fields add up to 180 degrees. In order to validate that, 
we will define a custom input - an input that is not connected to any fields. 
See the following rule definition:

```json
{
  "constraints": [
    {
      "inputIds": [ "sumOfAngles" ],
      "condition": [ "always" ],
      "permittedValues": [ ".*" ]
    },
   ...
  ]
}
```

Also one might note the strange value in the permittedValues field. Although in the previous example it looked like simply 
text values are matched against each-other, in reality the values are matched with a regular expression matcher. For simple 
texts it is the same, but there are a couple of special characters, that has special meaning. 
This combination, dot star means any text, including empty text.
Any characters of any amount.

So this input is totally unbound always can be anything.

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

These lines mean `if sumOfAngles is 180 then alpha can be any at least one character long text`.
Yes, the special characters dot plus means at least one character long text.

This rule might be strange since we are talking about numbers, but for the validator everything is a text.

The last element of the validation solution is the `Connector<TriangleDto>`:

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

The custom input is filled with the result of the mathematical expression, which is evaluated by the java runtime.
This way we can bind the results of complex expressions that are written in Java, and let the validator do the evaluation.

Active listeners might notice that this validation can be hacked. Indeed if the user enters 100, 100, -20, that would also pass this evaluation.
In this example, it is solved by using the very rich set of javax.validation on the pojo:

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

This prevents entering negative values. In general I think it could be a good idea to use javax validation
for single variable validation, and keep the cross-field validation in CoyoteFormsValidator, but I let the end-user decide.

