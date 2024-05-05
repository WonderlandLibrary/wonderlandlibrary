package rosegoldclient.features;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PacketReceivedEvent;

public class NoRotate {

    private boolean doneLoadingTerrain;

    @SubscribeEvent
    public void onPacket(PacketReceivedEvent event) {
        if(event.packet instanceof SPacketPlayerPosLook) {
            if(!Main.configFile.noRotate || Main.mc.player == null) return;
            if(Main.mc.isIntegratedServerRunning()) return;
            event.setCanceled(true);
            EntityPlayerSP entityPlayer = Main.mc.player;
            double d0 = ((SPacketPlayerPosLook) event.packet).getX();
            double d1 = ((SPacketPlayerPosLook) event.packet).getY();
            double d2 = ((SPacketPlayerPosLook) event.packet).getZ();
            float f = ((SPacketPlayerPosLook) event.packet).getYaw();
            float f1 = ((SPacketPlayerPosLook) event.packet).getPitch();
            if (((SPacketPlayerPosLook) event.packet).getFlags().contains(SPacketPlayerPosLook.EnumFlags.X)) {
                d0 += entityPlayer.posX;
            }
            if (((SPacketPlayerPosLook) event.packet).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
                d1 += entityPlayer.posY;
            } else {
                entityPlayer.motionY = 0.0;
            }
            if (((SPacketPlayerPosLook) event.packet).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
                d2 += entityPlayer.posZ;
            }
            if (((SPacketPlayerPosLook) event.packet).getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
                f1 += entityPlayer.rotationPitch;
            }
            if (((SPacketPlayerPosLook) event.packet).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
                f += entityPlayer.rotationYaw;
            }
            entityPlayer.setPositionAndRotation(d0, d1, d2, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
            Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.PositionRotation(entityPlayer.posX, entityPlayer.getEntityBoundingBox().minY, entityPlayer.posZ, f, f1, false));
            if (!doneLoadingTerrain) {
                Main.mc.player.prevPosX = Main.mc.player.posX;
                Main.mc.player.prevPosY = Main.mc.player.posY;
                Main.mc.player.prevPosZ = Main.mc.player.posZ;
                Main.mc.displayGuiScreen(null);
            }
            doneLoadingTerrain = true;
        }
        if (event.packet instanceof SPacketRespawn) {
            doneLoadingTerrain = false;
        }
    }
}
