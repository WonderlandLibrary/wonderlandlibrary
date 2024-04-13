package dev.tenacity.module.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import static net.minecraft.command.CommandBase.getPlayer;

public final class PhaseModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    public PhaseModule() {
        super("Phase", "Phase through blocks", ModuleCategory.PLAYER);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        EntityPlayer player = event.getPlayer();
        if (this.isEnabled()) {
            event.setCanceled(true);
            mc.thePlayer.serverPosX = (int) (mc.thePlayer.posX);
        }
    };
    private EntityPlayer player;
}