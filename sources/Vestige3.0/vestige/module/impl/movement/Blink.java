package vestige.module.impl.movement;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.PacketReceiveEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.util.player.PendingVelocity;

public class Blink extends Module {

    private final BooleanSetting suspendPositionOnly = new BooleanSetting("Suspend position only", false);
    private final BooleanSetting stackVelocity = new BooleanSetting("Stack velocity", true);

    private PendingVelocity lastVelocity;

    public Blink() {
        super("Blink", Category.MOVEMENT);
        this.addSettings(suspendPositionOnly, stackVelocity);
    }

    @Override
    public void onEnable() {
        lastVelocity = null;

        if(suspendPositionOnly.isEnabled()) {
            Vestige.instance.getPacketBlinkHandler().startBlinkingMove();
        } else {
            Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
        }
    }

    @Override
    public void onDisable() {
        Vestige.instance.getPacketBlinkHandler().stopAll();

        if(stackVelocity.isEnabled() && lastVelocity != null) {
            mc.thePlayer.motionX = lastVelocity.getX();
            mc.thePlayer.motionY = lastVelocity.getY();
            mc.thePlayer.motionZ = lastVelocity.getZ();
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = event.getPacket();

            if(packet.getEntityID() == mc.thePlayer.getEntityId() && stackVelocity.isEnabled()) {
                event.setCancelled(true);
                lastVelocity = new PendingVelocity(packet.getMotionX() / 8000.0, packet.getMotionY() / 8000.0, packet.getMotionZ() / 8000.0);
            }
        }
    }

}
