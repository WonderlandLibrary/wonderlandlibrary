package com.ohare.client.utils.value.impl;

import java.util.Optional;

import com.ohare.client.utils.value.Value;
import com.ohare.client.utils.value.parse.BooleanParser;

public class BooleanValue extends Value<Boolean> {

    public BooleanValue(String label, Boolean value) {
        super(label, value);
    }

    @Override
    public void setValue(String input) {
        Optional<Boolean> result = BooleanParser.parse(input);
        result.ifPresent(aBoolean -> this.value = aBoolean);
    }

    public void toggle() {
        this.value ^= true;
    }

    public boolean isEnabled() {
        return this.value;
    }

    public void setEnabled(boolean enabled) {
        this.value = enabled;
    }
}