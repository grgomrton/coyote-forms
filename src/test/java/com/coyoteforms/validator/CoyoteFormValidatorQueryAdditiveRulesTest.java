package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoyoteFormValidatorQueryAdditiveRulesTest {

    //Planned input:
    //{
    //	"rules": [
    //	  {
    //		"inputId": "franceCheckbox",
    //		"condition": [ "always" ],
    //		"permittedValues": [ "checked", "unchecked" ]
    //	  },
    //	  {
    //		"inputId": "germanyCheckbox",
    //		"condition": [ "always" ],
    //		"permittedValues": [ "checked", "unchecked" ]
    //	  },
    //	  {
    //		"inputId": "city",
    //		"condition": [ "franceCheckbox is 'checked'" ],
    //		"permittedValues": [ "Paris", "Marseilles" ]
    //	  },
    //	  {
    //		"inputId": "city",
    //		"condition": [ "germanyCheckbox is 'checked'" ],
    //		"permittedValues": [ "Munich", "Berlin", "Hamburg" ]
    //	  }
    //	]
    //}

    private static String RULE_SET = "    {" +
            "      \"rules\": [" +
            "        {" +
            "          \"inputId\": \"franceCheckbox\"," +
            "          \"condition\": [ \"always\" ]," +
            "          \"permittedValues\": [ \"checked\", \"unchecked\" ]" +
            "        }," +
            "        {" +
            "          \"inputId\": \"germanyCheckbox\"," +
            "          \"condition\": [ \"always\" ]," +
            "          \"permittedValues\": [ \"checked\", \"unchecked\" ]" +
            "        }," +
            "        {" +
            "          \"inputId\": \"city\"," +
            "          \"condition\": [ \"franceCheckbox is 'checked'\" ]," +
            "          \"permittedValues\": [ \"Paris\", \"Marseilles\" ]" +
            "        }," +
            "        {" +
            "          \"inputId\": \"city\"," +
            "          \"condition\": [ \"germanyCheckbox is 'checked'\" ]," +
            "          \"permittedValues\": [ \"Munich\", \"Berlin\", \"Hamburg\" ]" +
            "        }" +
            "      ]" +
            "    }";

    private static CoyoteFormValidator<SelectedPossibleDestinationCountries> validator;

    @BeforeAll
    public static void init() {
        validator = new CoyoteFormValidator<>(RULE_SET, new EveryValuePassingConnector());
    }

    @Data
    @Builder
    public static class SelectedPossibleDestinationCountries {

        List<String> selectedCountryNames;

    }

    public static class EveryValuePassingConnector implements Connector<SelectedPossibleDestinationCountries> {

        @Override
        public Map<String, String> collectInputValues(SelectedPossibleDestinationCountries selectedCountriesDto) {
            Map<String, String> result = new HashMap<>();
            List<String> selectedCountryNames = selectedCountriesDto.getSelectedCountryNames();

            result.put("franceCheckbox", selectedCountryNames.contains("France") ? "checked" : "unchecked");
            result.put("germanyCheckbox", selectedCountryNames.contains("Germany") ? "checked" : "unchecked");

            return result;
        }
    }


}
