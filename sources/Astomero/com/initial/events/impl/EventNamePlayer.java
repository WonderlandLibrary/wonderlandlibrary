package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.entity.*;

public class EventNamePlayer extends Event
{
    public Entity p;
    
    public EventNamePlayer(final Entity p2) {
        this.p = p2;
    }
}
