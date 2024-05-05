/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;

public class Hitbox
extends Mod {
    public static Value<Double> size = new Value<Double>("Hitbox_Size", 0.5, 0.1, 1.0, 0.1);

    public Hitbox() {
        super("Hitbox", Category.COMBAT);
    }
}

