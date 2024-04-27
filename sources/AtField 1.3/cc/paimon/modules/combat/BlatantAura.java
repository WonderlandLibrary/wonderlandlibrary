/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package cc.paimon.modules.combat;

import cc.paimon.modules.combat.OldVelocity;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.injection.backend.PacketImplKt;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleInfo(name="BlatantAura", description="L", category=ModuleCategory.COMBAT)
public class BlatantAura
extends Module {
    private Float karange;
    public final BoolValue debug = new BoolValue("Debug", false);
    private String oldvelMode;

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        Packet packet = PacketImplKt.unwrap(packetEvent.getPacket());
        if (packet instanceof CPacketPlayer) {
            packetEvent.cancelEvent();
            if (((Boolean)this.debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76Skyrim\u00a77]\u00a7f Cancel C03");
            }
        }
    }

    @Override
    public void onDisable() {
        KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        OldVelocity oldVelocity = (OldVelocity)LiquidBounce.moduleManager.getModule(OldVelocity.class);
        killAura.setState(false);
        killAura.getRangeValue().set((Object)this.karange);
        oldVelocity.getModeValue().set(this.oldvelMode);
    }

    @Override
    public void onEnable() {
        KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        OldVelocity oldVelocity = (OldVelocity)LiquidBounce.moduleManager.getModule(OldVelocity.class);
        this.karange = (Float)killAura.getRangeValue().get();
        this.oldvelMode = (String)oldVelocity.getModeValue().get();
        killAura.getRangeValue().set((Object)Float.valueOf(5.2f));
        oldVelocity.getModeValue().set("HytCancel");
        killAura.setState(true);
        oldVelocity.setState(true);
        if (((Boolean)this.debug.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76Skyrim\u00a77]\u00a7f Auto Change Value");
        }
    }
}

