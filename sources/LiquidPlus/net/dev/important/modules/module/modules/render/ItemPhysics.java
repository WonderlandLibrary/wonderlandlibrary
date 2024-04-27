/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.render;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.FloatValue;

@Info(name="PhysicsFall", spacedName="Item Physics", description="newton hits", category=Category.RENDER, cnName="\u7269\u7406\u6389\u843d")
public class ItemPhysics
extends Module {
    public final FloatValue itemWeight = new FloatValue("Weight", 0.5f, 0.0f, 1.0f, "x");

    @Override
    public String getTag() {
        return ((Float)this.itemWeight.get()).toString();
    }
}

