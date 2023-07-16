package com.coyoteforms.internal;

import java.util.Map;

public interface Connector<T> {

    Map<String, String> collectInputValues(T obj);

}
