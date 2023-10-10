package today.flux.module.implement.Movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.module.Category;
import today.flux.module.Module;

/**
 * Created by Admin on 2017/02/21.
 */
public class PullBack extends Module {
    public PullBack() {
        super("PullBack", Category.Movement, false);
    }

    public void onEnable() {
        if(this.mc.thePlayer != null && this.mc.theWorld != null){
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 11, this.mc.thePlayer.posZ, false));
        }
        this.setEnabled(false);
    }
}
