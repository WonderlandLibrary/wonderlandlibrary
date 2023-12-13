// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.impl.EventMove;
import vip.Resolute.events.impl.EventCollide;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.events.impl.EventTick;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Phase extends Module
{
    public ModeSetting mode;
    private double distance;
    private int delay;
    boolean shouldSpeed;
    double rot1;
    double rot2;
    float yaw;
    float pitch;
    private TimerUtil timer;
    
    public Phase() {
        super("Phase", 0, "Allows you to phase through blocks", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "Skip", new String[] { "AAC 4.4.2", "Skip", "Watchdog", "Full", "Pos", "Aris" });
        this.shouldSpeed = false;
        this.timer = new TimerUtil();
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        Phase.mc.timer.timerSpeed = 1.0f;
        this.shouldSpeed = this.isInsideBlock();
        Step.cancelStep = true;
        this.distance = 1.2;
    }
    
    @Override
    public void onDisable() {
        Step.cancelStep = false;
        Phase.mc.timer.timerSpeed = 1.0f;
        this.delay = 0;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventTick && this.mode.is("Pos") && Phase.mc.thePlayer.isCollidedHorizontally && Phase.mc.gameSettings.keyBindSprint.isKeyDown()) {
            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 0.05, Phase.mc.thePlayer.posZ, true));
            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, true));
            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 0.05, Phase.mc.thePlayer.posZ, true));
        }
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof C03PacketPlayer && this.mode.is("Pos") && !MovementUtils.isMoving() && Phase.mc.thePlayer.posY == Phase.mc.thePlayer.lastTickPosY) {
            e.setCancelled(true);
        }
        if (e instanceof EventMotion) {
            final EventMotion eventMotion = (EventMotion)e;
            if (e.isPre() && this.mode.is("Full")) {
                if (this.shouldSpeed || this.isInsideBlock()) {
                    if (!Phase.mc.thePlayer.isSneaking()) {
                        Phase.mc.thePlayer.lastReportedPosY = 0.0;
                    }
                    Phase.mc.thePlayer.lastReportedPitch = 999.0f;
                    Phase.mc.thePlayer.onGround = false;
                    Phase.mc.thePlayer.noClip = true;
                    Phase.mc.thePlayer.motionX = 0.0;
                    Phase.mc.thePlayer.motionZ = 0.0;
                    if (Phase.mc.gameSettings.keyBindJump.isKeyDown() && Phase.mc.thePlayer.posY == (int)Phase.mc.thePlayer.posY) {
                        Phase.mc.thePlayer.jump();
                    }
                    Phase.mc.thePlayer.jumpMovementFactor = 0.0f;
                }
                ++this.rot1;
                if (this.rot1 < 3.0) {
                    if (this.rot1 == 1.0) {
                        this.pitch += 15.0f;
                    }
                    else {
                        this.pitch -= 15.0f;
                    }
                }
                if (Phase.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Phase.mc.thePlayer.lastReportedPitch = 999.0f;
                    final double X = Phase.mc.thePlayer.posX;
                    final double Y = Phase.mc.thePlayer.posY;
                    final double Z = Phase.mc.thePlayer.posZ;
                    if (!MovementUtils.isMoving()) {
                        if (MovementUtils.isOnGround(0.001) && !this.isInsideBlock()) {
                            Phase.mc.thePlayer.lastReportedPosY = -99.0;
                            eventMotion.setY(Y - 1.0);
                            Phase.mc.thePlayer.setPosition(X, Y - 1.0, Z);
                            this.timer.reset();
                            Phase.mc.thePlayer.motionY = 0.0;
                        }
                        else if (this.timer.hasElapsed(100L) && Phase.mc.thePlayer.posY == (int)Phase.mc.thePlayer.posY) {
                            Phase.mc.thePlayer.setPosition(X, Y - 0.3, Z);
                        }
                    }
                }
                if (this.isInsideBlock() && this.rot1 >= 3.0) {
                    if (this.shouldSpeed) {
                        this.teleport(0.617);
                        final float sin = (float)Math.sin(this.rot2) * 0.1f;
                        final float cos = (float)Math.cos(this.rot2) * 0.1f;
                        final EntityPlayerSP thePlayer = Phase.mc.thePlayer;
                        thePlayer.rotationYaw += sin;
                        final EntityPlayerSP thePlayer2 = Phase.mc.thePlayer;
                        thePlayer2.rotationPitch += cos;
                        ++this.rot2;
                    }
                    else {
                        this.teleport(0.031);
                    }
                }
            }
            if (e.isPre() && this.mode.is("Aris") && Phase.mc.thePlayer.isCollidedHorizontally && Phase.mc.thePlayer.onGround && MovementUtils.isMoving()) {
                final float yaw = eventMotion.getYaw();
                Phase.mc.thePlayer.boundingBox.offsetAndUpdate(this.distance * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, this.distance * Math.sin(Math.toRadians(yaw + 90.0f)));
            }
        }
        if (e instanceof EventCollide && this.mode.is("Watchdog")) {
            e.setCancelled(true);
        }
        if (e instanceof EventMove) {
            final EventMove eventMove = (EventMove)e;
            if (this.mode.is("Pos") && this.isInsideBlock()) {
                if (Phase.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EventMove eventMove2 = eventMove;
                    final EntityPlayerSP thePlayer3 = Phase.mc.thePlayer;
                    eventMove2.setY(thePlayer3.motionY += 0.09000000357627869);
                }
                else if (Phase.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EventMove eventMove3 = eventMove;
                    final EntityPlayerSP thePlayer4 = Phase.mc.thePlayer;
                    eventMove3.setY(thePlayer4.motionY -= 0.0);
                }
                else {
                    eventMove.setY(Phase.mc.thePlayer.motionY = 0.0);
                }
                this.setMoveSpeed(eventMove, 0.3);
            }
            if (this.mode.is("Watchdog")) {
                if (Phase.mc.thePlayer.isCollidedHorizontally) {
                    for (int i = 0; i < 4; ++i) {
                        final double[] push = MovementUtils.yawPos(0.05);
                        Phase.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + push[0], Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + push[1], Phase.mc.thePlayer.onGround));
                    }
                }
                if (MovementUtils.isInsideBlock()) {
                    final double[] push2 = MovementUtils.yawPos(0.74);
                    eventMove.setY(Phase.mc.thePlayer.motionY = 0.0143);
                    Phase.mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(push2[0], 0.0, push2[1]);
                }
            }
            if (this.mode.is("Skip")) {
                if (Phase.mc.thePlayer.isCollidedHorizontally && Phase.mc.thePlayer.onGround && MovementUtils.isMoving()) {
                    if (Phase.mc.timer.timerSpeed == 0.2f) {
                        final float var2 = this.getDirection();
                        Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + Phase.mc.thePlayer.motionX * 0.3925, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + Phase.mc.thePlayer.motionZ * 0.3925, Phase.mc.thePlayer.onGround));
                        Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + 0.842 * Math.cos(Math.toRadians(var2 + 90.0f)), Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + 0.842 * Math.sin(Math.toRadians(var2 + 90.0f)));
                        for (int j = 0; j < 2; ++j) {
                            if (this.isInsideBlock()) {
                                Phase.mc.timer.timerSpeed = 1.0f;
                                Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + 0.352 * Math.cos(Math.toRadians(var2 + 90.0f)), Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + 0.352 * Math.sin(Math.toRadians(var2 + 90.0f)));
                            }
                        }
                        eventMove.setY(0.0);
                    }
                    else {
                        final EntityPlayerSP thePlayer5 = Phase.mc.thePlayer;
                        thePlayer5.moveForward *= 0.2f;
                        final EntityPlayerSP thePlayer6 = Phase.mc.thePlayer;
                        thePlayer6.moveStrafing *= 0.2f;
                        Phase.mc.timer.timerSpeed = 0.2f;
                        final EntityPlayerSP thePlayer7 = Phase.mc.thePlayer;
                        thePlayer7.cameraPitch += 18.0f;
                        final EntityPlayerSP thePlayer8 = Phase.mc.thePlayer;
                        ++thePlayer8.cameraYaw;
                    }
                }
                else {
                    Phase.mc.timer.timerSpeed = 1.0f;
                }
            }
        }
        if (e instanceof EventUpdate) {
            if (this.mode.is("Pos")) {
                final double multiplier = 0.3;
                final double mx = -Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw));
                final double mz = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw));
                final MovementInput movementInput = Phase.mc.thePlayer.movementInput;
                final double n = MovementInput.moveForward * multiplier * mx;
                final MovementInput movementInput2 = Phase.mc.thePlayer.movementInput;
                final double x = n + MovementInput.moveStrafe * multiplier * mz;
                final MovementInput movementInput3 = Phase.mc.thePlayer.movementInput;
                final double n2 = MovementInput.moveForward * multiplier * mz;
                final MovementInput movementInput4 = Phase.mc.thePlayer.movementInput;
                final double z = n2 - MovementInput.moveStrafe * multiplier * mx;
                if (Phase.mc.thePlayer.isCollidedHorizontally && !Phase.mc.thePlayer.isOnLadder()) {
                    Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z, false));
                    for (int k = 1; k < 10; ++k) {
                        Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, 8.988465674311579E307, Phase.mc.thePlayer.posZ, false));
                    }
                    Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z);
                }
                final float dist = 2.0f;
                if (Phase.mc.thePlayer.isCollidedHorizontally && Phase.mc.thePlayer.moveForward != 0.0f) {
                    ++this.delay;
                    final String lowerCase3;
                    final String lowerCase = lowerCase3 = Phase.mc.getRenderViewEntity().getHorizontalFacing().name().toLowerCase();
                    switch (lowerCase3) {
                        case "east": {
                            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + 9.999999747378752E-6, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, false));
                            break;
                        }
                        case "west": {
                            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX - 9.999999747378752E-6, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, false));
                            break;
                        }
                        case "north": {
                            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ - 9.999999747378752E-6, false));
                            break;
                        }
                        case "south": {
                            Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + 9.999999747378752E-6, false));
                            break;
                        }
                    }
                    if (this.delay >= 1) {
                        final String lowerCase4;
                        final String lowerCase2 = lowerCase4 = Phase.mc.getRenderViewEntity().getHorizontalFacing().name().toLowerCase();
                        switch (lowerCase4) {
                            case "east": {
                                Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + 2.0, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, false));
                                break;
                            }
                            case "west": {
                                Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX - 2.0, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, false));
                                break;
                            }
                            case "north": {
                                Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ - 2.0, false));
                                break;
                            }
                            case "south": {
                                Phase.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + 2.0, false));
                                break;
                            }
                        }
                        this.delay = 0;
                    }
                }
            }
            if (e.isPre()) {
                if (this.mode.is("AAC 4.4.2") && Phase.mc.thePlayer.isCollidedHorizontally) {
                    Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 1.0E-8, Phase.mc.thePlayer.posZ, Phase.mc.thePlayer.rotationYaw, Phase.mc.thePlayer.rotationPitch, false));
                    Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 1.0E-6, Phase.mc.thePlayer.posZ, Phase.mc.thePlayer.rotationYaw, Phase.mc.thePlayer.rotationPitch, false));
                }
                if (this.mode.is("Dev")) {
                    Phase.mc.thePlayer.setSneaking(true);
                    if (Phase.mc.thePlayer.isCollidedHorizontally) {
                        Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + -Math.sin(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw)) * 0.01, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + Math.cos(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw)) * 0.01);
                    }
                    else if (this.isInsideBlock()) {
                        Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + -Math.sin(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw)) * 0.3, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + Math.cos(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw)) * 0.3);
                        MovementUtils.setSpeed(0.0);
                    }
                }
            }
        }
    }
    
    private void setMoveSpeed(final EventMove event, final double speed) {
        final MovementInput movementInput = Phase.mc.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        final MovementInput movementInput2 = Phase.mc.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
        float yaw = Phase.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }
    
    private void teleport(final double dist) {
        final MovementInput movementInput = Phase.mc.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        final MovementInput movementInput2 = Phase.mc.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
        float yaw = Phase.mc.thePlayer.rotationYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        final double x = Phase.mc.thePlayer.posX;
        final double y = Phase.mc.thePlayer.posY;
        final double z = Phase.mc.thePlayer.posZ;
        final double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0f));
        final double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0f));
        Phase.mc.thePlayer.setPosition(x + xspeed, y, z + zspeed);
    }
    
    private float getDirection() {
        float direction = Phase.mc.thePlayer.rotationYaw;
        final boolean back = Phase.mc.gameSettings.keyBindBack.isKeyDown() && !Phase.mc.gameSettings.keyBindForward.isKeyDown();
        final boolean forward = !Phase.mc.gameSettings.keyBindBack.isKeyDown() && Phase.mc.gameSettings.keyBindForward.isKeyDown();
        if (Phase.mc.gameSettings.keyBindLeft.isKeyDown()) {
            direction -= (back ? 135 : (forward ? 45 : 90));
        }
        if (Phase.mc.gameSettings.keyBindRight.isKeyDown()) {
            direction += (back ? 135 : (forward ? 45 : 90));
        }
        if (back && direction == Phase.mc.thePlayer.rotationYaw) {
            direction += 180.0f;
        }
        return direction;
    }
    
    public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
