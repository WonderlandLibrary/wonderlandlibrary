// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Step extends Module
{
    public Step() {
        super("Step", 0, true, Category.Player, "Steps you up things");
    }
    
    @Override
    public void onUpdate() {
        if (Step.mc.thePlayer.isCollidedHorizontally && Step.mc.thePlayer.onGround) {
            Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + 0.42, Step.mc.thePlayer.posZ, true));
            Step.mc.thePlayer.stepHeight = 1.0f;
        }
        else {
            Step.mc.thePlayer.stepHeight = 0.6f;
        }
    }
}
