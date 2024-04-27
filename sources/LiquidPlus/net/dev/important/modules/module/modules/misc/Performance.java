/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.misc;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;

@Info(name="Performance", category=Category.MISC, description="Optimize functions and improve render performance.", canEnable=false, cnName="\u66f4\u597d\u7684\u6e32\u67d3")
public class Performance
extends Module {
    public static BoolValue staticParticleColorValue = new BoolValue("StaticParticleColor", false);
    public static BoolValue fastEntityLightningValue = new BoolValue("FastEntityLightning", false);
    public static BoolValue fastBlockLightningValue = new BoolValue("FastBlockLightning", false);
}

