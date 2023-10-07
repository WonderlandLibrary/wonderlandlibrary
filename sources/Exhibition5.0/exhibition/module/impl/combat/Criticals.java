// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import exhibition.event.RegisterEvent;
import exhibition.module.impl.movement.Bhop;
import exhibition.module.impl.movement.LongJump;
import exhibition.Client;
import net.minecraft.world.World;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S2APacketParticles;
import exhibition.event.impl.EventPacket;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.Options;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Criticals extends Module
{
    private static String PACKET;
    private static String HURTTIME;
    
    public Criticals(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Options>>)this.settings).put(Criticals.PACKET, new Setting<Options>(Criticals.PACKET, new Options("Mode", "Packet", new String[] { "Packet", "Jump" }), "Critical attack method."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(Criticals.HURTTIME, new Setting<Integer>(Criticals.HURTTIME, 15, "The hurtTime tick to crit at.", 1.0, 0.0, 20.0));
    }
    
    @RegisterEvent(events = { EventPacket.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            this.setSuffix(((HashMap<K, Setting<Options>>)this.settings).get(Criticals.PACKET).getValue().getSelected() + " " + ((HashMap<K, Setting<Number>>)this.settings).get(Criticals.HURTTIME).getValue().intValue());
        }
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.getPacket() instanceof S2APacketParticles || ep.getPacket().toString().contains("S2APacketParticles")) {
                return;
            }
            try {
                if (ep.isOutgoing() && ep.getPacket() instanceof C02PacketUseEntity && !(ep.getPacket() instanceof S2APacketParticles) && !(ep.getPacket() instanceof C0APacketAnimation)) {
                    final C02PacketUseEntity packet = (C02PacketUseEntity)ep.getPacket();
                    if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && Criticals.mc.thePlayer.isCollidedVertically && Killaura.allowCrits && this.hurtTimeCheck(packet.getEntityFromWorld(Criticals.mc.theWorld))) {
                        if (Client.getModuleManager().isEnabled(LongJump.class) || Client.getModuleManager().isEnabled(Bhop.class)) {
                            return;
                        }
                        final String selected = ((HashMap<K, Setting<Options>>)this.settings).get(Criticals.PACKET).getValue().getSelected();
                        switch (selected) {
                            case "Packet": {
                                doCrits();
                                break;
                            }
                            case "Jump": {
                                if (!Criticals.mc.thePlayer.isJumping) {
                                    Criticals.mc.thePlayer.jump();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    private boolean hurtTimeCheck(final Entity entity) {
        return entity != null && entity.hurtResistantTime <= ((HashMap<K, Setting<Number>>)this.settings).get(Criticals.HURTTIME).getValue().intValue();
    }
    
    static void doCrits() {
        for (final double offset : new double[] { 0.06, 0.0, 0.03, 0.0 }) {
            Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset, Criticals.mc.thePlayer.posZ, false));
        }
    }
    
    static {
        Criticals.PACKET = "MODE";
        Criticals.HURTTIME = "HURTTIME";
    }
}
