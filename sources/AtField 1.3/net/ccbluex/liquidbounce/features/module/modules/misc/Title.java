/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.opengl.Display;

@ModuleInfo(name="Title", description="Custom title.", category=ModuleCategory.MISC)
public class Title
extends Module {
    private static final TextValue defaultText;
    private int seconds;
    private static final BoolValue time;
    private String timeText = "";
    private int ticks;
    private int minutes;
    private int hours;

    static {
        time = new BoolValue("TimeDisplay", false);
        defaultText = new TextValue("Title", "Example title");
    }

    @EventTarget
    public void update(UpdateEvent updateEvent) {
        ++this.ticks;
        if (this.ticks == 20) {
            ++this.seconds;
            this.ticks = 0;
        }
        if (this.seconds == 60) {
            ++this.minutes;
            this.seconds = 0;
        }
        if (this.minutes == 60) {
            ++this.hours;
            this.minutes = 0;
        }
        this.timeText = this.hours + " \u65f6 " + this.minutes + " \u5206 " + this.seconds + " \u79d2 ";
        Display.setTitle((String)((String)defaultText.get() + ((Boolean)time.get() != false ? " " + this.timeText : "")));
    }

    @Override
    public void onDisable() {
        try {
            Display.setTitle((String)"AtField v1.3r");
        }
        catch (Exception exception) {
            Display.setTitle((String)"AtField v1.3r");
        }
    }
}

