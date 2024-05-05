package slavikcodd3r.rainbow.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.Minecraft;

public class FlyUtils
{
    public static Minecraft mc;
    
    public static double getGroundLevel() {
        for (int i = (int)Math.round(FlyUtils.mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = FlyUtils.mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (isColliding(box) && box.minY <= FlyUtils.mc.thePlayer.posY) {
                return i;
            }
        }
        return 0.0;
    }
    
    public static boolean isColliding(final AxisAlignedBB box) {
        return FlyUtils.mc.theWorld.checkBlockCollision(box);
    }
    
    public static double fall() {
        double i;
        for (i = FlyUtils.mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
            if (i < getGroundLevel()) {
                i = getGroundLevel();
            }
            FlyUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(FlyUtils.mc.thePlayer.posX, i, FlyUtils.mc.thePlayer.posZ, true));
        }
        return i;
    }
    
    public static void ascend() {
        for (double i = getGroundLevel(); i < FlyUtils.mc.thePlayer.posY; i += 8.0) {
            if (i > FlyUtils.mc.thePlayer.posY) {
                i = FlyUtils.mc.thePlayer.posY;
            }
            FlyUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(FlyUtils.mc.thePlayer.posX, i, FlyUtils.mc.thePlayer.posZ, true));
        }
    }
    
    static {
        FlyUtils.mc = Minecraft.getMinecraft();
    }
}
