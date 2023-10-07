package com.ohare.client.utils.value.impl;

import com.ohare.client.utils.value.Value;

/**
 * made by Xen for OhareWare
 *
 * @since 7/21/2019
 **/
public class StringValue extends Value<String> {

    public StringValue(String label, String value) {
        super(label, value);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
