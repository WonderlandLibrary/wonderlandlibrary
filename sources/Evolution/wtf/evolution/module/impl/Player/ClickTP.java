package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.Event;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.event.events.impl.MouseEvent;
import wtf.evolution.helpers.Castt;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "ClickTP", type = Category.Player)
public class ClickTP extends Module {

    @EventTarget
    public void onUpdate(MouseEvent e) {
        if (e.button == 1) {
            RayTraceResult r = Castt.rayTrace(500, mc.player.rotationYaw, mc.player.rotationPitch);
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ,false));
            for (int i = 0; i < 2; i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(r.getBlockPos().getX() + 0.5f, r.getBlockPos().getY() + 1, r.getBlockPos().getZ() + 0.5f,true));
            }
        }
    }

    @EventTarget
    public void onRender(EventRender e) {
        RayTraceResult r = Castt.rayTrace(150, mc.player.rotationYaw, mc.player.rotationPitch);
        if (r != null) {
            RenderUtil.blockEsp(r.getBlockPos(), Color.WHITE);
        }
    }

}
