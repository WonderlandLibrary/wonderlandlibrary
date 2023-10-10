package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import today.flux.event.*;
import today.flux.module.SubModule;
import today.flux.utility.PlayerUtils;

public class Hypixel extends SubModule {
    public Hypixel() {
        super("Hypixel", "Phase");
    }

    private int moveUnder;

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.thePlayer != null && this.moveUnder == 1) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
            this.moveUnder = 0;
        }
        if (mc.thePlayer != null && this.moveUnder == 1488) {
            final double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
            final double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            final double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
            final double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
            this.moveUnder = 0;
        }
    }

    public static boolean isInsideBlock() {
        final EntityPlayerSP player = PlayerUtils.mc.thePlayer;
        final WorldClient world = PlayerUtils.mc.theWorld;
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventTarget
    public void onBoundingBox(BBSetEvent event) {
        if (isInsideBlock()) {
            event.setBoundingBox(null);
        }
    }

    public void onPush(EventPushOutOfBlocks event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onMotionUpdate(PreUpdateEvent event) {
        final double multiplier = 0.3;
        final double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
        final double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
        final double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
        final double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + 0.001, mc.thePlayer.posZ + z, false));
            for (int i = 1; i < 10; ++i) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.22, mc.thePlayer.posZ, false));
            }
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }

    @EventTarget
    public void onReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat)event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (isInsideBlock()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer = mc.thePlayer;
                final double n = thePlayer.motionY + 0.09000000357627869;
                thePlayer.motionY = n;
                event.y = n;
            }
            else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer2 = mc.thePlayer;
                final double n2 = thePlayer2.motionY - 0.0;
                thePlayer2.motionY = n2;
                event.y = n2;
            }
            else {
                final EntityPlayerSP thePlayer3 = mc.thePlayer;
                final double n3 = 0.0;
                thePlayer3.motionY = n3;
                event.y = n3;
            }
            PlayerUtils.setSpeed(0.3);
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                final EntityPlayerSP thePlayer4 = mc.thePlayer;
                final double n4 = thePlayer4.motionY + 0.09000000357627869;
                thePlayer4.motionY = n4;
                event.y = n4;
            }
        }
    }
}

