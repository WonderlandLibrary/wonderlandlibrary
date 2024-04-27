/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
 */
package net.dev.important.modules.module.modules.world;

import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.value.BoolValue;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;

@Info(name="Lightning", description="Checks for lightning spawn and notify you.", category=Category.WORLD, cnName="\u83b7\u53d6\u6253\u96f7\u4f4d\u7f6e")
public class Lightning
extends Module {
    public final BoolValue chatValue = new BoolValue("Chat", true);
    public final BoolValue notifValue = new BoolValue("Notification", false);

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2CPacketSpawnGlobalEntity && ((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149053_g() == 1) {
            S2CPacketSpawnGlobalEntity entity = (S2CPacketSpawnGlobalEntity)event.getPacket();
            double x = (double)entity.func_149051_d() / 32.0;
            double y = (double)entity.func_149050_e() / 32.0;
            double z = (double)entity.func_149049_f() / 32.0;
            int distance = (int)Lightning.mc.field_71439_g.func_70011_f(x, Lightning.mc.field_71439_g.func_174813_aQ().field_72338_b, z);
            if (((Boolean)this.chatValue.get()).booleanValue()) {
                ClientUtils.displayChatMessage("\u00a77[\u00a76\u00a7lLightning\u00a77] \u00a7fDetected lightning at \u00a7a" + x + " " + y + " " + z + " \u00a77(" + distance + " blocks away)");
            }
            if (((Boolean)this.notifValue.get()).booleanValue()) {
                Client.hud.addNotification(new Notification("Detected lightning at " + x + " " + y + " " + z + " (" + distance + " blocks away)", Notification.Type.WARNING, 3000L));
            }
        }
    }
}

