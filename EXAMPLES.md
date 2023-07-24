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