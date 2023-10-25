/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.world.sound;

import com.github.steveice10.mc.protocol.data.game.world.sound.Sound;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class CustomSound
implements Sound {
    private String name;

    public CustomSound(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomSound)) {
            return false;
        }
        CustomSound that = (CustomSound)o;
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.name);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

