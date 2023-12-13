package info.sigmaclient.module.impl.combat;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.LongJump;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2APacketParticles;

public class Criticals extends Module {

    private static String PACKET = "MODE";
    private static String HURTTIME = "HURTTIME";
    //0.0625101D
    public Criticals(ModuleData data) {
        super(data);
        settings.put(PACKET, new Setting<>(PACKET, new Options("Mode", "Packet", new String[]{"Packet", "Jump"}), "Critical attack method."));
        settings.put(HURTTIME, new Setting<>(HURTTIME, 15, "The hurtTime tick to crit at.", 1, 0, 20));
    }

    @Override
    @RegisterEvent(events = {EventPacket.class, EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setSuffix(((Options) settings.get(PACKET).getValue()).getSelected() + " " + ((Number) settings.get(HURTTIME).getValue()).intValue());
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if(ep.getPacket() instanceof S2APacketParticles || ep.getPacket().toString().contains("S2APacketParticles")) {
                return;
            }
            try {
                if (ep.isOutgoing() && ep.getPacket() instanceof C02PacketUseEntity && !(ep.getPacket() instanceof S2APacketParticles) && !(ep.getPacket() instanceof C0APacketAnimation)) {
                    C02PacketUseEntity packet = (C02PacketUseEntity) ep.getPacket();
                    if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && Killaura.allowCrits && hurtTimeCheck(packet.getEntityFromWorld(mc.theWorld))) {
                        if(Client.getModuleManager().isEnabled(LongJump.class) || Client.getModuleManager().isEnabled(Bhop.class))
                            return;
                        switch (((Options) settings.get(PACKET).getValue()).getSelected()) {
                            case "Packet":
                                doCrits();
                                break;
                            case "Jump":
                                if(!mc.thePlayer.isJumping)
                                mc.thePlayer.jump();
                                break;
                        }
                    }
                }
            } catch (Exception ignored) {

            }
        }
    }

    private boolean hurtTimeCheck(Entity entity) {
        return entity != null && entity.hurtResistantTime <= ((Number) settings.get(HURTTIME).getValue()).intValue();
    }

    static void doCrits() {
        for (double offset : new double[]{0.06, 0, 0.03, 0})
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
    }

}
