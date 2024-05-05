package com.initial.settings.impl;

import com.initial.settings.*;

public class StringSet extends Setting
{
    public String text;
    
    public StringSet(final String name, final String defaultText) {
        this.name = name;
        this.text = defaultText;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
