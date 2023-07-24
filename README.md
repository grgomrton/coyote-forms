# Coyote Forms

Coyote Forms is a cross-field input validator. It lets you create complex rule sets for your forms  using simple expressions and a couple of bindings.

## Features

### Intuitive syntax

Rules can be defined using similar syntax to:

```json
{
    {
      "inputIds": ["city"],
      "condition": [
        "region is EMEA",
        "country is United Kingdom"
      ],
      "permittedValues": [ "London" ]
    },
}
```

Which reads as `if region is EMEA and country is United Kingdom then city can be London`.

### Easy integration

Integration takes place by creating bindings from your pojo to the validator:

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

### Simple API

The validator can be instantiated with a rule set and a connector. After instantiation the following methods will be available:

```java 
public List<String> queryValidValues(String inputId, T inputValues)
```

For retrieving the values of a discrete value set input. And

```java
public List<ValidationFailure> validate(T inputValues)
```

For validating the set of inputs.

## Examples

The project ships with three example projects located in the examples folder. See details in the [Examples](EXAMPLES.md) readme.

## Future

First, I would like to publish the 0.1 release. After that, the most beneficial next step would be to implement the validator 
in javascript, and therefore make it possible to perform validation on the front-end using the same ruleset as used by the 
backend. Faster response times, less load and less code duplication would be the benefit. After that, we will see.

