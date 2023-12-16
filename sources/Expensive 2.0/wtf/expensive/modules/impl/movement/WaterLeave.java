package wtf.expensive.modules.impl.movement;

import com.sun.jna.platform.win32.WinNT;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Pose;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.events.impl.player.EventWorldChange;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.movement.MoveUtil;

@FunctionAnnotation(name = "Water Leave", type = Type.Movement)
public class WaterLeave extends Function {

    private boolean inWater;
    private int ticks;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            if (e.getPacket() instanceof SPlayerPositionLookPacket p) {
                if (mc.player == null) toggle();
                mc.player.setPosition(p.getX(), p.getY(), p.getZ());
                mc.player.connection.sendPacket(new CConfirmTeleportPacket(p.getTeleportId()));
                e.setCancel(true);
                mc.player.motion.y += 0.5;
                toggle();
            }
        }
        if (event instanceof EventUpdate e) {
            if (mc.player.isInWater() && mc.player.fallDistance > 0) {
                mc.player.motion.y = 3;
            }
        }
    }

    @Override
    public void onDisable() {
        inWater = false;
        mc.timer.timerSpeed = 1f;
        ticks = 0;
        super.onDisable();
    }

}
