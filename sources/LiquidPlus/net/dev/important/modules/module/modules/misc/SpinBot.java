/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.misc;

import net.dev.important.event.EventTarget;
import net.dev.important.event.TickEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import org.jetbrains.annotations.Nullable;

@Info(name="SpinBot", spacedName="Spin Bot", description="CS-GO Feeling but client side (by the old solegit)", category=Category.MISC, cnName="\u4f4e\u5934\u673a\u5668\u4eba")
public class SpinBot
extends Module {
    public final ListValue yawMode = new ListValue("Yaw", new String[]{"Static", "Offset", "Random", "Jitter", "Spin", "Off"}, "Offset");
    public final ListValue pitchMode = new ListValue("Pitch", new String[]{"Static", "Offset", "Random", "Jitter", "Off"}, "Offset");
    private final IntegerValue YawSet = new IntegerValue("YawSet", 0, -180, 180, "\u00b0");
    private final IntegerValue PitchSet = new IntegerValue("PitchSet", 0, -180, 180, "\u00b0");
    private final IntegerValue YawJitterTimer = new IntegerValue("YawJitterTimer", 1, 1, 40, " tick");
    private final IntegerValue PitchJitterTimer = new IntegerValue("PitchJitterTimer", 1, 1, 40, " tick");
    private final IntegerValue YawSpin = new IntegerValue("YawSpin", 5, -50, 50, "\u00b0");
    public static float pitch = -4.9531336E7f;
    public static float lastSpin;
    public static float yawTimer;
    public static float pitchTimer;

    @Override
    public void onDisable() {
        super.onDisable();
        pitch = -4.9531336E7f;
        lastSpin = 0.0f;
        yawTimer = 0.0f;
        pitchTimer = 0.0f;
    }

    @EventTarget
    public void onTick(TickEvent e) {
        String s2;
        String s = (String)this.yawMode.get();
        float yaw = 0.0f;
        switch (s) {
            case "Static": {
                yaw = ((Integer)this.YawSet.get()).intValue();
                break;
            }
            case "Offset": {
                yaw = SpinBot.mc.field_71439_g.field_70177_z + (float)((Integer)this.YawSet.get()).intValue();
                break;
            }
            case "Random": {
                yaw = (float)Math.floor(Math.random() * 360.0 - 180.0);
                break;
            }
            case "Jitter": {
                yawTimer += 1.0f;
                if (yawTimer % (float)((Integer)this.YawJitterTimer.get() * 2) >= (float)((Integer)this.YawJitterTimer.get()).intValue()) {
                    yaw = SpinBot.mc.field_71439_g.field_70177_z;
                    break;
                }
                yaw = SpinBot.mc.field_71439_g.field_70177_z - 180.0f;
                break;
            }
            case "Spin": {
                yaw = lastSpin += (float)((Integer)this.YawSpin.get()).intValue();
                break;
            }
            default: {
                yaw = ((Integer)this.YawSet.get()).intValue();
            }
        }
        if (!((String)this.yawMode.get()).equalsIgnoreCase("off")) {
            SpinBot.mc.field_71439_g.field_70761_aq = yaw;
            SpinBot.mc.field_71439_g.field_70759_as = yaw;
        }
        lastSpin = yaw;
        switch (s2 = (String)this.pitchMode.get()) {
            case "Static": {
                pitch = ((Integer)this.PitchSet.get()).intValue();
                break;
            }
            case "Offset": {
                pitch = SpinBot.mc.field_71439_g.field_70125_A + (float)((Integer)this.PitchSet.get()).intValue();
                break;
            }
            case "Random": {
                pitch = (float)Math.floor(Math.random() * 180.0 - 90.0);
                break;
            }
            case "Jitter": {
                pitchTimer += 1.0f;
                if (pitchTimer % (float)((Integer)this.PitchJitterTimer.get() * 2) >= (float)((Integer)this.PitchJitterTimer.get()).intValue()) {
                    pitch = 90.0f;
                    break;
                }
                pitch = -90.0f;
                break;
            }
            default: {
                pitch = ((Integer)this.PitchSet.get()).intValue();
            }
        }
        if (((String)this.pitchMode.get()).equalsIgnoreCase("off")) {
            pitch = -4.9531336E7f;
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return "Yaw " + (String)this.yawMode.get() + ", Pitch " + (String)this.pitchMode.get();
    }
}

