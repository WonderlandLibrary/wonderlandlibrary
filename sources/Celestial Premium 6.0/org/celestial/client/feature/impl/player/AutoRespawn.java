/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class AutoRespawn
extends Feature {
    private double ticks;
    private double x;
    private double y;
    private double z;
    private final BooleanSetting tpBack = new BooleanSetting("TP Back", false, () -> true);
    private final BooleanSetting autoHome = new BooleanSetting("Auto Home", false, () -> true);

    public AutoRespawn() {
        super("AutoRespawn", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0440\u0435\u0441\u043f\u0430\u0432\u043d \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438", Type.Player);
        this.addSettings(this.tpBack, this.autoHome);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (AutoRespawn.mc.player.getHealth() < 0.0f || !AutoRespawn.mc.player.isEntityAlive() || AutoRespawn.mc.currentScreen instanceof GuiGameOver) {
            if (this.tpBack.getCurrentValue()) {
                event.setOnGround(false);
                this.x = AutoRespawn.mc.player.posX;
                this.y = AutoRespawn.mc.player.posY;
                this.z = AutoRespawn.mc.player.posZ;
            }
            this.ticks += 1.0;
            AutoRespawn.mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
        if (this.autoHome.getCurrentValue() && this.ticks > 0.0) {
            AutoRespawn.mc.player.sendChatMessage("/home home");
            this.ticks = 0.0;
        }
        if (this.tpBack.getCurrentValue() && this.ticks > 0.0) {
            event.setOnGround(false);
            AutoRespawn.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.x + 0.5, this.y + 0.5, this.z + 0.5, false));
            AutoRespawn.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.x, this.y, this.z, true));
            AutoRespawn.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.x + 0.5, this.y + 0.5, this.z + 0.5, true));
            this.ticks = 0.0;
        }
    }
}

