/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiVelocity
extends Module {
    public static String HORIZONTAL = "HORIZONTAL";
    public static String VERTICAL = "VERTICAL";

    public AntiVelocity(ModuleData data) {
        super(data);
        this.settings.put(HORIZONTAL, new Setting<Integer>(HORIZONTAL, 0, "Horizontal velocity factor.", 10.0, 0.0, 100.0));
        this.settings.put(VERTICAL, new Setting<Integer>(VERTICAL, 0, "Vertical velocity factor.", 10.0, 0.0, 100.0));
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        S12PacketEntityVelocity packet;
        EventPacket ep = (EventPacket)event;
        if (ep.isOutgoing()) {
            return;
        }
        if (ep.getPacket() instanceof S12PacketEntityVelocity && (packet = (S12PacketEntityVelocity)ep.getPacket()).getEntityID() == AntiVelocity.mc.thePlayer.getEntityId()) {
            int vertical = ((Number)((Setting)this.settings.get(VERTICAL)).getValue()).intValue();
            int horizontal = ((Number)((Setting)this.settings.get(HORIZONTAL)).getValue()).intValue();
            if (vertical != 0 || horizontal != 0) {
                packet.setMotX(horizontal * packet.func_149412_c() / 100);
                packet.setMotY(vertical * packet.func_149411_d() / 100);
                packet.setMotZ(horizontal * packet.func_149410_e() / 100);
            } else {
                ep.setCancelled(true);
            }
        }
        if (ep.getPacket() instanceof S27PacketExplosion) {
            ep.setCancelled(true);
        }
    }
}

