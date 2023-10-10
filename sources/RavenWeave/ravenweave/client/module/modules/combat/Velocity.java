package ravenweave.client.module.modules.combat;

import me.pianopenguin471.mixins.S12PacketEntityVelocityAccessor;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.Entity;
import net.weavemc.loader.api.event.SubscribeEvent;
import ravenweave.client.event.impl.PacketEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;

public class Velocity extends Module {
    public static SliderSetting horizontal, vertical, chance;
    public static TickSetting invertHorizontal, invertVertical;

    public Velocity() {
        super("Velocity", ModuleCategory.combat);
        this.registerSetting(horizontal = new SliderSetting("Horizontal", 90.0D, 0.0D, 200.0D, 1.0D));
        this.registerSetting(invertHorizontal = new TickSetting("Invert Horizontal", false));
        this.registerSetting(vertical = new SliderSetting("Vertical", 100.0D, 0.0D, 200.0D, 1.0D));
        this.registerSetting(invertVertical= new TickSetting("Invert Vertical", false));
        this.registerSetting(chance = new SliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent e) {
        if (e.isIncoming()) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                if (chance.getInput() != 100.0D) {
                    double ch = Math.random() * 100;
                    if (ch >= chance.getInput()) {
                        return;
                    }
                }

                Entity entity = mc.theWorld.getEntityByID(((S12PacketEntityVelocity) e.getPacket()).getEntityID());

                if (entity == mc.thePlayer) {
                    velo(e);
                }
            }
        }
    }

    public void velo(PacketEvent e) {
        S12PacketEntityVelocity packet = e.getPacket();
        S12PacketEntityVelocityAccessor accessorPacket = (S12PacketEntityVelocityAccessor) packet;

        if (invertHorizontal.isToggled()) {
            accessorPacket.setMotionX((int) (packet.getMotionX() * -horizontal.getInput()/100));
            accessorPacket.setMotionZ((int) (packet.getMotionZ() * -horizontal.getInput()/100));
        } else {
            accessorPacket.setMotionX((int) (packet.getMotionX() * horizontal.getInput()/100));
            accessorPacket.setMotionZ((int) (packet.getMotionZ() * horizontal.getInput()/100));
        }

        if (invertVertical.isToggled()) accessorPacket.setMotionY((int) (packet.getMotionY() * -vertical.getInput()/100));
        else accessorPacket.setMotionY((int) (packet.getMotionY() * vertical.getInput()/100));

        e.setPacket(accessorPacket);
    }
}
