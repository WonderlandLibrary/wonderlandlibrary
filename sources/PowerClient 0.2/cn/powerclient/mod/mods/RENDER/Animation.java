/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;

public class Animation
extends Mod {
    public static Value<String> mode = new Value("Animation", "Mode", 0);
    public static Value<Boolean> sanimation = new Value<Boolean>("Animation_SwingAnimation", true);

    public Animation() {
        super("Animation", Category.RENDER);
        Animation.mode.mode.add("Push");
        Animation.mode.mode.add("Sigma");
    }
}

