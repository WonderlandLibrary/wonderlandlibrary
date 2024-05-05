package rosegoldclient.features;

import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PacketReceivedEvent;
import rosegoldclient.events.TickEndEvent;

public class AutoSneak {

    public static boolean abilityReady = false;
    private static long flameCount = 0;
    private static long debounce = 0;

    @SubscribeEvent
    public void onPacket(PacketReceivedEvent event) {
        if(event.packet instanceof SPacketTitle) {
            SPacketTitle packet = (SPacketTitle) event.packet;
            if(packet.getMessage() != null && packet.getMessage().getUnformattedText() != null) {
                String text = ((SPacketTitle) event.packet).getMessage().getUnformattedText();
                if (text.contains("✹")) {
                    if(Main.spellAura) {
                        abilityReady = true;
                    }
                    if(Main.killAura) {
                        abilityReady = true;
                    }
                }
            }
        }
        if(event.packet instanceof SPacketParticles) {
            SPacketParticles packet = (SPacketParticles) event.packet;
            Vec3d pos = new Vec3d(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
            if(pos.distanceTo(Main.mc.player.getPositionVector()) < 3 && packet.getParticleType() == EnumParticleTypes.FLAME) {
                flameCount++;
                debounce = 20;
                if(flameCount > 50) {
                    abilityReady = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(debounce > 0) {
            debounce--;
        } else {
            flameCount = 0;
        }
    }
}
