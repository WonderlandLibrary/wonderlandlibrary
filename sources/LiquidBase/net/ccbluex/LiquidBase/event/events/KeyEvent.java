package net.ccbluex.LiquidBase.event.events;

import net.ccbluex.LiquidBase.event.Event;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class KeyEvent extends Event {

    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}