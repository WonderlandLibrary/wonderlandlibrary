// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.Event;

public class MouseEvent implements Event
{
    public int button;
    
    public MouseEvent(final int button) {
        this.button = button;
    }
}
