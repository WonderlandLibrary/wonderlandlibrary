package com.initial.events;

import java.lang.reflect.*;

public class Data
{
    public final Object source;
    public final Method target;
    public final byte priority;
    
    Data(final Object source, final Method target, final byte priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }
}
