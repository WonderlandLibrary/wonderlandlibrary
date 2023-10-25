/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.text2speech;

import com.mojang.text2speech.Narrator;

public class NarratorDummy
implements Narrator {
    @Override
    public void say(String msg) {
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean active() {
        return false;
    }
}

