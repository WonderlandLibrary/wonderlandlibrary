package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.player.MoveUtil;

@Getter@Setter
public class AntiVoid extends Module {
    public AntiVoid(){
        super("AntiVoid","Do not do da fall", Category.PLAYER, ServerType.All);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(!isBlockUnder() && mc.thePlayer.fallDistance > 3){
            e.setY(15);
            System.out.println("void");
            mc.thePlayer.fallDistance = 0;
        }
    }

    private boolean isBlockUnder() {
        if (!(mc.thePlayer.posY < 0.0D)) {
            for (int offset = 0; offset < (int) mc.thePlayer.posY + 2; offset += 2) {
                AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);
                if (!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }

        }
        return false;
    }


}
