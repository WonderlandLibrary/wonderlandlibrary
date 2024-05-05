package skidmonke;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.module.enums.ModuleCategory;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.modules.movement.Flight;
import tech.dort.dortware.impl.utils.movement.MotionUtils;
import tech.dort.dortware.impl.utils.networking.PacketUtil;
import tech.dort.dortware.impl.utils.player.ChatUtil;

public class FuckOff extends Module {

    static ModuleData a;
    private float speed;
    private Flight nigger;

    public FuckOff() {
        super(a = new ModuleData("Test", 0, ModuleCategory.HIDDEN));
    }

    public void toggle() {
        ChatUtil.displayChatMessage("Sloth disabler test.");
    }

    public void onEnable() {
        speed = 0.0f;
    }

    public void onDisable() {

    }

    @Subscribe
    public void a(UpdateEvent event) {
        if (mc.thePlayer.ticksExisted % 3 == 0) {
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(-1, -6, -4, -3, -8, true));
        }
        if (speed >= 6.0F) {
            speed = 0;
            return;
        }
        if (nigger.isToggled()) {
            mc.thePlayer.motionY = 0.4F;
            speed += 0.5D;
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
        MotionUtils.setMotion(speed);
    }

    public boolean b(String a) {
        return p(a) == this && "".equals(a) && nigger != null;
    }

    public FuckOff p(String a) {
        if ("".equals(a) && nigger != null) {
            return this;
        }
        return null;
    }

    public void setNigger(Flight flight) {
        nigger = flight;
    }
}
