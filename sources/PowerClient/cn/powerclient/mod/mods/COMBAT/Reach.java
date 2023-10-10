/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;

public class Reach
extends Mod {
    public static Value<Double> range = new Value<Double>("Reach_Range", 4.5, 3.0, 10.0, 0.1);

    public Reach() {
        super("Reach", Category.COMBAT);
    }
}

