/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class LongJump
extends Module {
    int air;

    public LongJump() {
        super("LongJump", Category.Movement);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!Minecraft.player.onGround) {
            ++this.air;
        } else if (this.air > 3) {
            this.setToggled(false);
        }
        if (Minecraft.player.onGround || this.air == 0) {
            Minecraft.player.jump();
        }
        Minecraft.player.jumpMovementFactor = 0.15f;
        Minecraft.player.motionY += 0.05;
    }
}

