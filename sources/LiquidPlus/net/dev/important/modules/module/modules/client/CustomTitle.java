/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package net.dev.important.modules.module.modules.client;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.TextValue;
import org.lwjgl.opengl.Display;

@Info(name="CustomTitle", spacedName="Custom Title", description="Custom Title", cnName="\u81ea\u5b9a\u4e49\u6807\u9898", category=Category.CLIENT)
public class CustomTitle
extends Module {
    private final TextValue title = new TextValue("Title", "LiquidPlus DevBuild");

    @Override
    public void onEnable() {
        Display.setTitle((String)((String)this.title.get()));
        this.setState(false);
    }
}

