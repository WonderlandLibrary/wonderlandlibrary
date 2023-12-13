/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall
extends Module {
    public NoFall() {
        super("NoFall", Category.Player);
    }

    @EventTarget
    public void onPacket(EventUpdate event) {
        if (Minecraft.player.isAirBorne) {
            if (Minecraft.player.fallDistance > 3.0f) {
                Minecraft.player.motionY -= 0.965f;
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
            }
        }
    }
}

