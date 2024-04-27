/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.opengl.Display;

@ModuleInfo(name="CustomTitle", description="Title", category=ModuleCategory.HYT)
public class CustomTitle
extends Module {
    private final TextValue Title = new TextValue("Title", "LiKingSense b1");
    private int S;
    private int HM;
    private int M;
    private int H;

    public final int getS() {
        return this.S;
    }

    public final int getM() {
        return this.M;
    }

    public final int getH() {
        return this.H;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        ++this.HM;
        if (this.HM == 20) {
            ++this.S;
            this.HM = 0;
        }
        if (this.S == 60) {
            ++this.M;
            this.S = 0;
        }
        if (this.M == 60) {
            ++this.H;
            this.M = 0;
        }
        Display.setTitle((String)((String)this.Title.get() + this.H + "\u65f6 " + this.M + "\u5206 " + this.S + "\u79d2"));
    }

    @Override
    public void onDisable() {
        Display.setTitle((String)"LiKingSense");
    }
}

