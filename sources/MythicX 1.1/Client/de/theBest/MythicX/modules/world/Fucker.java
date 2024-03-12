package de.theBest.MythicX.modules.world;

import de.Hero.settings.Setting;
import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventPacket;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.utils.RotationUtil;
import de.theBest.MythicX.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.awt.*;

public class Fucker extends Module {
    public Fucker() {
        super("Fucker", Type.World, 0, Category.WORLD, Color.green, "Auto breaks beds arround you");
    }
    TimeUtils time = new TimeUtils();
    public static BlockPos pos;
    double range = 4.5D;

    public static void lookAtPos(double x, double y, double z) {
        double dirx = mc.thePlayer.posX - x;
        double diry = mc.thePlayer.posY - y;
        double dirz = mc.thePlayer.posZ - z;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        float yaw = (float)Math.atan2(dirz, dirx);
        float pitch = (float)Math.asin(diry);
        pitch = (float)(pitch * 180.0D / Math.PI);
        yaw = (float)(yaw * 180.0D / Math.PI);
        yaw = (float)(yaw + 90.0D);
        float f2 = (Minecraft.getMinecraft()).gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f3 = f2 * f2 * f2 * 1.2F;
        yaw -= yaw % f3;
        pitch -= pitch % f3 * f2;
        RotationUtil.setYaw(yaw, 180.0F);
        RotationUtil.setPitch(pitch, 90.0F);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(EventPacket event) {
            for (int y = (int)this.range; y >= -this.range; y--) {
                for (int x = (int)-this.range; x <= this.range; x++) {
                    for (int z = (int)-this.range; z <= this.range; z++) {
                        int posX = (int)(mc.thePlayer.posX - 0.5D + x);
                        int posZ = (int)(mc.thePlayer.posZ - 0.5D + z);
                        int posY = (int)(mc.thePlayer.posY - 0.5D + y);
                        pos = new BlockPos(posX, posY, posZ);
                        Block block = mc.theWorld.getBlockState(pos).getBlock();
                        if (block instanceof net.minecraft.block.BlockBed || block instanceof net.minecraft.block.BlockCake || block instanceof net.minecraft.block.BlockDragonEgg) {
                            PlayerControllerMP playerController = mc.playerController;
                            long timeLeft = (long)(PlayerControllerMP.curBlockDamageMP / 2.0F);
                            if(MythicX.setmgr.getSettingByName("Rotate").getValBoolean()){
                                lookAtPos(posX, posY, posZ);
                            }
                            if (this.time.hasReached(800L)) {
                                mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
                                mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                                mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                                mc.getNetHandler().addToSendQueue((Packet)new C0APacketAnimation());
                                this.time.reset();
                            }
                        }
                    }
                }
            }
    }

    public void setup(){
        MythicX.setmgr.rSetting(new Setting("Rotate", this, false));
    }
}
