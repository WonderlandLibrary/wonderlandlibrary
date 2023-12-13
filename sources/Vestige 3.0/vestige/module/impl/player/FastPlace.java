package vestige.module.impl.player;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import vestige.util.network.PacketUtil;
import vestige.event.Listener;
import vestige.event.impl.PacketSendEvent;
import vestige.event.impl.RenderEvent;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.ModeSetting;

public class FastPlace extends Module {

    private final ModeSetting delay = new ModeSetting("Delay", "Every tick", "Every tick", "Every 2 ticks", "No CPS cap", "Every tick on render");

    private C08PacketPlayerBlockPlacement placement;

    private boolean placedBlock;

    public FastPlace() {
        super("Fastplace", Category.PLAYER);
        this.addSettings(delay);
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
        placement = null;
    }

    @Listener
    public void onTick(TickEvent event) {
        if(delay.is("Every tick")) {
            mc.rightClickDelayTimer = 0;
        } else if(delay.is("Every 2 ticks")) {
            if(mc.thePlayer.ticksExisted % 2 == 0) {
                mc.rightClickDelayTimer = 0;
            }
        }

        if(delay.is("Every tick on render")) {
            if(placement != null) {
                PacketUtil.sendPacketFinal(placement);
                placement = null;
            }
        }

        placedBlock = false;
    }

    @Listener
    public void onRender(RenderEvent event) {
        if(delay.is("No CPS cap") || (delay.is("Every tick on render") && !placedBlock)) {
            if(mc.gameSettings.keyBindUseItem.pressed || Mouse.isButtonDown(1)) {
                mc.rightClickDelayTimer = 0;
                mc.rightClickMouse();
            }
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if(event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = event.getPacket();

            if(delay.is("No CPS cap") || delay.is("Every tick on render")) {
                if(packet.getPosition().equals(new BlockPos(-1, -1, -1))) {
                    event.setCancelled(true);
                } else {
                    placedBlock = true;

                    if(delay.is("Every tick on render")) {
                        event.setCancelled(true);
                        placement = packet;
                    }
                }
            }
        }
    }

}
