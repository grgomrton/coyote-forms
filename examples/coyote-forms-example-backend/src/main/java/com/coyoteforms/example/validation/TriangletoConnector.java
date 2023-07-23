package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.TriangleDto;
import com.coyoteforms.validator.Connector;

import java.util.HashMap;
import java.util.Map;

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

}
