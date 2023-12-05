package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.*;
import rip.helium.utils.SpeedUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;

public class Phase extends Cheat {

    private final StringsProperty prop_mode = new StringsProperty("Mode", "",
            null, false, false, new String[]{"Aris", "Vanilla", "Faithful"}, new Boolean[]{true, false, false});
    Stopwatch timer;
    double distance = 2.5;
    private int moveUnder;

    public Phase() {
        super("Phase", "", CheatCategory.PLAYER);
        registerProperties(prop_mode);
        timer = new Stopwatch();
    }

    @Collect
    public void tickEvent(RunTickEvent e) {
        if (mc.thePlayer == null) return;
        if (prop_mode.getValue().get("Vanilla")) {
            if (mc.thePlayer != null && moveUnder == 1) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
                moveUnder = 0;
            }
            if (mc.thePlayer != null && moveUnder == 1488) {
                double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
                double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
                double x = MovementInput.moveForward * mx + MovementInput.moveStrafe * mz;
                double z = MovementInput.moveForward * mz - MovementInput.moveStrafe * mx;
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
                moveUnder = 0;
            }
        }
    }

    @Collect
    public void boundingBoxEvent(BoundingBoxEvent event) {
        if (prop_mode.getValue().get("Vanilla")) {
            if (mc.thePlayer.isCollidedHorizontally && !isInsideBlock()) {
                double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
                double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
                double x = MovementInput.moveForward * mx + MovementInput.moveStrafe * mz;
                double z = MovementInput.moveForward * mz - MovementInput.moveStrafe * mx;
                event.setBoundingBox(null);
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                moveUnder = 69;
            }
            if (isInsideBlock()) event.setBoundingBox(null);
        }
    }

    @Collect
    public void packetEvent(ProcessPacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
                event.setCancelled(true);
            }
        }
        if (prop_mode.getValue().get("Vanilla") && event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
            moveUnder = 1;
        }
        if (prop_mode.getValue().get("Vanilla") && event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 69) {
            moveUnder = 1488;
        }
    }

    @Collect
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (prop_mode.getValue().get("Vanilla")) {
            if (isInsideBlock()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = 1.2);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = -1.2);
                } else {
                    event.setY(mc.thePlayer.motionY = 0.0);
                }
                SpeedUtils.setPlayerSpeed(event, 0.3);
            }
        } else if (prop_mode.getValue().get("Faithful")) {
            if ((isInsideBlock()) && (mc.thePlayer.isSneaking())) {
                float yaw = mc.thePlayer.rotationYaw;
                mc.thePlayer.boundingBox.offsetAndUpdate(1.5 * Math.cos(Math.toRadians(yaw + 90.0F)), 0.0D, 1.5 * Math.sin(Math.toRadians(yaw + 90.0F)));


            }
        }
    }

    @Collect
    public void playerUpdateEvent(PlayerUpdateEvent e) {
        if (prop_mode.getValue().get("Vanilla") && mc.gameSettings.keyBindSneak.isPressed() && !isInsideBlock()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
            moveUnder = 2;
        } else if (prop_mode.getValue().get("Faithful")) {
            if (mc.thePlayer.isSneaking())
                if (mc.thePlayer.isCollidedHorizontally && !e.isPre()) {
                    double x = -MathHelper.sin(mc.thePlayer.getDirection()) * 1.5,
                            z = MathHelper.cos(mc.thePlayer.getDirection()) * 1.5;

                }
        }
    }

    public String getDirection() {
        return Minecraft.getMinecraft().thePlayer.getHorizontalFacing().getName();
    }

    public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(
                mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(
                    mc.thePlayer.getEntityBoundingBox().minY + 1.0D); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY)
                    + 2; ++y) {
                for (int z = MathHelper.floor_double(
                        mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ)
                        + 1; ++z) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
                                mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }

                        if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Collect
    public void onBoundingBox(BoundingBoxEvent boundingBoxEvent) {
        if ((prop_mode.getValue().get("Aris"))
                && (isInsideBlock() && mc.gameSettings.keyBindJump.pressed || !isInsideBlock() && boundingBoxEvent.getBoundingBox() != null && boundingBoxEvent.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY)) {
            if (mc.thePlayer.isMoving()) {
                mc.thePlayer.setSpeed(0.625F);
            }
            mc.thePlayer.motionY = 0;
            boundingBoxEvent.setBoundingBox(null);
        }


    }

    @Collect
    public void bounding(BoundingBoxEvent eventBb) {
        if (prop_mode.getValue().get("Faithful")) {
            if ((eventBb.getBoundingBox() != null) && (eventBb.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY) && (mc.thePlayer.isSneaking())) {
                eventBb.setBoundingBox(null);
            }
        }

    }

    @Collect
    public void onBlockPush(BlockPushEvent e) {
        if (prop_mode.getValue().get("Aris")) {
            e.setCancelled(true);
            mc.thePlayer.motionY = 0;
        }
    }

    @Collect
    public void onProcessPacket(ProcessPacketEvent event) {

        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            packet.setPitch(mc.thePlayer.rotationPitch);
            packet.setYaw(mc.thePlayer.rotationYaw);

            if (moveUnder == 2) {
                moveUnder = 1;
            }
        }
    }


//	@Collect
//	public void onRenderInside(EventRenderInsideBlocks e) {
//		if (prop_mode.getValue().get("Infinity") || prop_mode.getValue().get("FMC")) {
//			e.setCancelled(true);
//		}
//	}

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (playerUpdateEvent.isPre()) {

        } else {
            if (prop_mode.getValue().get("Aris")) {
                setMode("Aris");
                if (mc.thePlayer.isSneaking()) {
                    if (!mc.thePlayer.isOnLadder()) {
                        mc.thePlayer.setSpeed(mc.thePlayer.isCollidedHorizontally ? .3 : .05);
                        mc.thePlayer.getEntityBoundingBox().offset(
                                1.2 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)), 0.0,
                                1.2 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)));

                        if (mc.getCurrentServerData() != null
                                && !mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                            double offset = 1.35;
                            Number playerYaw = mc.thePlayer.getDir(mc.thePlayer.rotationYaw);
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                                    mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
                                    mc.thePlayer.posY + .3,
                                    mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
                            mc.thePlayer.setPositionAndUpdate(
                                    mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
                                    mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
                        } else {
                            double offset = 1.2;
                            Number playerYaw = mc.thePlayer.getDir(mc.thePlayer.rotationYaw);
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                                    mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
                                    mc.thePlayer.posY + .3,
                                    mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
                            mc.thePlayer.setPositionAndUpdate(
                                    mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
                                    mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
                            playerUpdateEvent.setPosX(mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset);
                            playerUpdateEvent.setPosZ(mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
                        }
                    }

                }
            }
        }
    }
}
