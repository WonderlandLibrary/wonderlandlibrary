package koks.module.debug;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.pathfinder.PathFinderHelper;
import koks.api.registry.module.Module;
import koks.api.utils.InventoryUtil;
import koks.api.utils.MovementUtil;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author kroko
 * @created on 20.02.2021 : 04:27
 */
@Module.Info(name = "Debug2", category = Module.Category.DEBUG, description = "test 2")
public class Debug2 extends Module {

    public final TimeHelper timeHelper = new TimeHelper();
    public int spam = 0;
    public double fakeMotionY = 0;
    public long delay, delayTime;
    public BlockPos blockPos;
    boolean endBlink, sendPackets;

    public final PathFinderHelper pathFinderHelper = new PathFinderHelper();

    private final HashMap<String, Integer> packets = new HashMap<>();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();

/*final BlockPos blockPos = searchBlock(getPlayer().getPositionVector().addVector(0, -1, 0), 1);
            if (blockPos != null && getPlayer().getHeldItem() != null)
                getPlayerController().onPlayerRightClick(getPlayer(), getWorld(), getPlayer().getHeldItem(), blockPos, EnumFacing.getFacingFromVector(blockPos.getX(), blockPos.getY(), blockPos.getZ()), new Vec3(blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5));*/

        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.SEND) {
                //System.out.println(packet);

            }

        }

        if (event instanceof NoClipEvent noClipEvent) {
            //noClipEvent.setNoClip(true);
           /* if (getPlayer().isCollidedHorizontally && isMoving()) {
                double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(getYaw()))) * 1;
                double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(getYaw()))) * 1;
                if (getWorld().getBlockState(new BlockPos(getX() + motionX, (int)getY() + 1, getZ() + motionZ)).getBlock() != Blocks.air) {
                    ((NoClipEvent) event).setNoClip(true);
                }
            }*/
        }

        if (event instanceof ItemSyncEvent) {
            //event.setCanceled(true);
        }

        if (event instanceof UpdateEvent) {
            final RandomUtil randomUtil = RandomUtil.getInstance();



            /*
            * if (!getPlayer().onGround) {
                getTimer().timerSpeed = 0.06F;
                if (getPlayer().ticksExisted % 3 == 0) {
                    getPlayer().motionY = -0.005;
                }
            }
            * */
                /*if(getPlayer().ticksExisted % 25 == 0) {
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 12, getZ(), getPlayer().onGround));
            }*/
            /*if (getPlayer().isCollidedHorizontally) {
                getTimer().timerSpeed = 0.3F;
                if (getPlayer().ticksExisted % 2 == 0) {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(),getY() + 0.42, getZ(), true));
                    movementUtil.blinkTo(0.2,getY(), getYaw(), false);
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(),getY() + 0.2, getZ(), false));
                } else {
                    getPlayer().motionY = 0;
                    setMotion(0);
                }
            } else {
                getTimer().timerSpeed = 1;
            }*/

            /*if (getHurtTime() == 0)
                getPlayer().motionY = 0;*/

            /*sendMessage(fakeMotionY);
            if (spam == 0) {
                blockPos = getPlayer().getPosition();
                getPlayer().motionY = 0.42F;
                fakeMotionY = 0.42F;
            } else {
                if (spam < 25) {

                } else {
                    spam = 0;
                    System.out.println(fakeMotionY);
                    fakeMotionY = fakeMotionY * 0.6D * 0.9800000190734863D * 0.800000011920929D;
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), blockPos.getY() + (fakeMotionY), getZ(), false));
                }
                if (this.fakeMotionY < -0.15D) {
                    this.fakeMotionY = -0.15D;
                }
                getPlayer().motionY = 0;
            }
            spam++;*/
           /* if(getPlayer().fallDistance > 3.5 && !endBlink) {
                sendPacketUnlogged(new C03PacketPlayer(true));
                getPlayer().motionY = 0;
                endBlink = true;
            }
            if(endBlink) {
                getPlayer().fallDistance = 0;
                getPlayer().onGround = true;
            }*/
            /*if(getHurtTime() == 10 && getPlayer().onGround) {
                sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX() + 6, getY() + 1, getZ() + 6, true));
            }*/
            /*getPlayer().motionY = -0.01;*/
        }

        /*if (event instanceof PacketEvent) {
            final PacketEvent packetEvent = (PacketEvent) event;
            final Packet<?> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.SEND) {
                if (!endBlink)
                    if (!(packet instanceof C00PacketKeepAlive)) {
                        packets.add(packet);
                        event.setCanceled(true);
                    }
            }
        }


        if (event instanceof UpdateEvent) {
            if (!timeHelper.hasReached(delay)) {
                endBlink = false;
                getGameSettings().keyBindForward.pressed = Keyboard.isKeyDown(getGameSettings().keyBindForward.getKeyCode());
                getTimer().timerSpeed = 2F;
                sendPackets = true;
            } else {
                endBlink = true;
                setMotion(0);
                getGameSettings().keyBindForward.pressed = false;
                getTimer().timerSpeed = 1;
                if(sendPackets) {
                    long time = System.currentTimeMillis();
                    packets.forEach(packet -> {
                        sendPacket(packet, 5);
                    });
                    packets.clear();
                    time = System.currentTimeMillis() - time;
                    delayTime = delay + time;
                    sendPackets = false;
                }
                if(timeHelper.hasReached(delayTime)) {
                    endBlink = false;
                    delay = randomInRange(400,500);
                    timeHelper.reset();
                }
            }*/
            /*if(timeHelper.hasReached(550)) {
                double motionX = -Math.sin(Math.toRadians(getPlayer().rotationYaw)) * 0.2;
                double motionZ = Math.cos(Math.toRadians(getPlayer().rotationYaw)) * 0.2;
                final BlockPos blockPos = new BlockPos(getX() + motionX, getY(), getZ() + motionZ);

                pathFinderHelper.clear();

                final ArrayList<Vec3i> startPath = pathFinderHelper.findPath(new Vec3i(getX(), getY(), getZ()), new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ()));

                startPath.forEach(vec3i -> {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(vec3i.getX(), vec3i.getY(), vec3i.getZ(), getPlayer().onGround));
                });

                getPlayer().setPositionAndUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                timeHelper.reset();
            }*/
            /*if (getPlayer().getHealth() <= 2) {
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), true));
                getPlayer().onGround = false;
                getPlayer().fallDistance = 4;
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));
                timeHelper.reset();
                if (getHurtTime() != 0) {
                    getPlayer().fallDistance = 0;
                    getPlayer().motionY = 1.5;
                }

            }*/
        /*}/*

        if (event instanceof UpdateEvent) {

            /*if (isMoving()) {
                if(getPlayer().onGround) {
                    if (timeHelper.hasReached(260)) {
                        movementUtil.teleportTo(0.3);
                        timeHelper.reset();
                    }
                    getPlayer().jump();
                }
            }*/
            /*if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && getGameSettings().keyBindAttack.pressed) {
                getGameSettings().keyBindAttack.pressed = false;
                boolean ground = false;
                *//*for (double i = 0; i < 250; i+= 0.42) {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + i, getZ(), ground));
                    ground = !ground;
                }
                sendPacket(new C03PacketPlayer(true));*//*
            }*/
        /*}*/
    }

    @Override
    public void onEnable() {
        endBlink = false;
        sendPackets = false;
        fakeMotionY = getY();
        timeHelper.reset();
        spam = (int) getY();

/*
        getTimer().timerSpeed = 0.3F;
*/
        blockPos = null;
    }

    public BlockPos searchBlock(Vec3 position, int radius) {
        Vec3 find = null;
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                final Vec3 pos = position.addVector(x, 0, z);
                final BlockPos blockPos = new BlockPos(pos);
                if (getWorld().getBlockState(blockPos).getBlock() == Blocks.air) {
                    if (find == null || pos.distanceTo(position) < find.distanceTo(position))
                        find = pos;
                }
            }
        }
        return find != null ? new BlockPos(find) : null;
    }

    @Override
    public void onDisable() {
      /*  packets.forEach(this::sendPacketUnlogged);
        packets.clear();*/
        getTimer().timerSpeed = 1;
    }
}
