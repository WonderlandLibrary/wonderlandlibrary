/*
 * Decompiled with CFR 0.150.
 */
package ru.smertnix.celestial.feature.impl.player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketDisconnect;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class AutoLeave
extends Feature {
    public static ListSetting leavemode = new ListSetting("Mode", "Disconnect", () -> true, "Disconnect", "Spawn");
    public static NumberSetting range = new NumberSetting("Distance To Entity", 64, 12.0f, 64f, 1f, () -> true);
    public static NumberSetting hp = new NumberSetting("Health", 15.0f, 5.0f, 20.0f, 1f, () -> true);

    public AutoLeave() {
        super("Auto Leave", "Автоматически выходит из сервера", FeatureCategory.Player);
        this.addSettings(leavemode, range, hp);
    }

    @EventTarget
    public void eventPreMotion(EventPreMotion event) {
        for (Entity e : mc.world.loadedEntityList) {
        	if (!this.isEnabled()) 
            	return;
        	if (e instanceof EntityPlayer && e != mc.player && (mc.player.getDistanceToEntity(e) < range.getNumberValue() || mc.player.getHealth() < hp.getNumberValue())) {
                if (AutoLeave.leavemode.currentMode.equalsIgnoreCase("spawn")) {
                    mc.player.sendChatMessage("/spawn");
                }
                if (AutoLeave.leavemode.currentMode.equalsIgnoreCase("disconnect")) {
                	mc.player.connection.sendPacket(new SPacketDisconnect());
                }
                this.toggle();
        	}
        }
    }
}

