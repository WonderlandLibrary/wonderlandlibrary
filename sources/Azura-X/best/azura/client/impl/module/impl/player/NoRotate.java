package best.azura.client.impl.module.impl.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "No Rotation Set", category = Category.PLAYER, description = "Do not rotate when the server sets your rotations")
public class NoRotate extends Module {

    private final BooleanValue antiDetect = new BooleanValue("Anti Detect", "Silently reset rotations", false);
    private final float[] lastRotations = new float[2];

    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packetIn = e.getPacket();
            if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.ticksExisted > 5) {
                if (antiDetect.getObject()) {
                    e.setCancelled(true);
                    EntityPlayer entityplayer = mc.thePlayer;
                    double d0 = packetIn.getX(), d1 = packetIn.getY(), d2 = packetIn.getZ();
                    float f = packetIn.getYaw(), f1 = packetIn.getPitch();

                    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) d0 += entityplayer.posX;
                    else entityplayer.motionX = 0.0D;

                    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) d1 += entityplayer.posY;
                    else entityplayer.motionY = 0.0D;

                    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) d2 += entityplayer.posZ;
                    else entityplayer.motionZ = 0.0D;

                    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) f1 += entityplayer.rotationPitch;

                    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) f += entityplayer.rotationYaw;

                    lastRotations[0] = mc.thePlayer.rotationYaw;
                    lastRotations[1] = mc.thePlayer.rotationPitch;
                    entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(d0, d1, d2, f, f1, false));
                    if (!mc.thePlayer.sendQueue.doneLoadingTerrain)
                    {
                        mc.thePlayer.prevPosX = mc.thePlayer.posX;
                        mc.thePlayer.prevPosY = mc.thePlayer.posY;
                        mc.thePlayer.prevPosZ = mc.thePlayer.posZ;
                        mc.thePlayer.sendQueue.doneLoadingTerrain = true;
                        mc.displayGuiScreen(null);
                    }
                    entityplayer.setPositionAndRotation(entityplayer.posX, entityplayer.posY, entityplayer.posZ, lastRotations[0], lastRotations[1]);
                } else {
                    packetIn.yaw = mc.thePlayer.rotationYaw;
                    packetIn.pitch = mc.thePlayer.rotationPitch;
                }
            }
        }
    };


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
