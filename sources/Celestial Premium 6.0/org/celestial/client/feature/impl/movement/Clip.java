/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Clip
extends Feature {
    private final NumberSetting clipPower;
    private final ListSetting clipMode = new ListSetting("Clip Mode", "Clip Down", () -> true, "Clip Down", "Clip Up", "Clip Horizontal");

    public Clip() {
        super("Clip", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u0441\u043a\u0432\u043e\u0437\u044c \u0431\u043b\u043e\u043a\u0438", Type.Movement);
        this.clipPower = new NumberSetting("Clip Power", 15.0f, 1.0f, 100.0f, 1.0f, () -> true);
        this.addSettings(this.clipMode, this.clipPower);
    }

    @Override
    public void onEnable() {
        double x = Clip.mc.player.posX;
        double y = Clip.mc.player.posY;
        double z = Clip.mc.player.posZ;
        double yaw = (double)Clip.mc.player.rotationYaw * 0.017453292;
        String mode = this.clipMode.getOptions();
        if (Clip.mc.player == null && Clip.mc.world == null) {
            return;
        }
        int tp = (int)this.clipPower.getCurrentValue();
        if (mode.equalsIgnoreCase("Clip Up")) {
            Clip.mc.player.setPosition(x, y + (double)tp, z);
        } else if (mode.equalsIgnoreCase("Clip Down")) {
            Clip.mc.player.setPosition(x, y - (double)tp, z);
        } else if (mode.equalsIgnoreCase("Clip Horizontal")) {
            Clip.mc.player.setPosition(x - Math.sin(yaw) * (double)tp, y, z + Math.cos(yaw) * (double)tp);
        }
        this.toggle();
        super.onEnable();
    }
}

