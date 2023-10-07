package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.MoveEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.pathfinding.Vec3;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

@ModuleInfo(name = "ClickTP", description = "Teleports you to where you're looking when you press your use item key", category = Category.MOVEMENT)
public final class ClickTP extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Vulcan", "Karhu", "Purple Prison"/*, "Hypixel Fireball"*/);

    private boolean countTicks, startTeleport;
    private double tpX, tpY, tpZ;
    private int ticks;

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        // Checking if the use item button is down and that the delay has been reached.
        if (mc.gameSettings.keyBindUseItem.isKeyDown() && timer.hasReached(500L)) {
            // Creating a variable that gets the block that the user is looking at and creating another variable with incremented Y position of the position so that the user teleports on top of the block.
            final BlockPos pos = mc.thePlayer.rayTrace(999, 1).getBlockPos();
            final BlockPos tpPos = pos.up();

            // Checking if the block at the position is air and returning and checking if the teleport position isn't air and returning if it isn't.
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir || !(mc.theWorld.getBlockState(tpPos).getBlock() instanceof BlockAir))
                return;

            // Checking the mode that the user is currently using.
            switch (mode.getMode()) {
                case "Normal": {
                    // Creating a new thread to prevent lag
                    new Thread(() -> {
                        // Getting the path to teleport along.
                        final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                                new Vec3(tpPos.getX(), tpPos.getY(), tpPos.getZ()));

                        // Teleporting along the path to the block.
                        for (final Vec3 point : path)
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(point.getX(), point.getY(), point.getZ(), true));

                        // Setting the users position to the block to make the teleport look smoother.
                        mc.thePlayer.setPosition(tpPos.getX(), tpPos.getY(), tpPos.getZ());
                    }).start();
                    break;
                }

                case "Vulcan":
                    tpX = tpPos.getX();
                    tpY = tpPos.getY();
                    tpZ = tpPos.getZ();
                    startTeleport = true;
                    break;

                case "Karhu": {
                    // Abuses lagbacks on Karhu by spamming packets to the teleport position and back to teleport.
                    for (int i = 1; i <= 5; ++i) {
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX(), tpPos.getY(), tpPos.getZ(), false));
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    }

                    // Setting the users position to the block to make the teleport look smoother.
                    mc.thePlayer.setPosition(tpPos.getX(), tpPos.getY(), tpPos.getZ());
                    break;
                }

                case "Purple Prison": {
                    // Abuses lagbacks on Purple Prison by spamming packets to the teleport position and back to teleport.
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX(), tpPos.getY(), tpPos.getZ(), false));

                    // Setting the users position to the block to make the teleport look smoother.
                    mc.thePlayer.setPosition(tpPos.getX(), tpPos.getY(), tpPos.getZ());
                    break;
                }

                case "Hypixel Fireball": {
                    final Integer slot = PlayerUtil.findItem(Items.fire_charge);

                    if (slot != null) {
                        tpX = tpPos.getX();
                        tpY = tpPos.getY();
                        tpZ = tpPos.getZ();

                        countTicks = true;
                    } else
                        this.registerNotification("You need a Fireball for " + this.getModuleInfo().name() + " to work.");
                    break;
                }
            }

            // Resetting the teleport delay timer.
            timer.reset();
        }

        if (startTeleport && mode.is("Vulcan")) {

            event.setX(mc.thePlayer.posX);
            event.setY(mc.thePlayer.posY + 1);
            event.setZ(mc.thePlayer.posZ);

            if (countTicks && ticks == 1) {
                // Getting the path to teleport along.
                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(tpX, tpY, tpZ));

                // Teleporting along the path to the block.
                for (final Vec3 point : path)
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(point.getX(), point.getY(), point.getZ(), true));

                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(tpX, tpY, tpZ + 0.1, true));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(tpX, tpY, tpZ - 0.1, true));
            } else if (countTicks && ticks > 1) {
                event.setX(tpX);
                event.setY(tpY + 1);
                event.setZ(tpZ);
            }
        }

        if (mode.is("Hypixel Fireball") && countTicks) {
            final Integer slot = PlayerUtil.findItem(Items.fire_charge);
            if (slot != null) {
                event.setPitch((float) (90 - Math.random() * 2));
                switch (ticks) {
                    case 1:
                        if (slot != mc.thePlayer.inventory.currentItem)
                            PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
                        break;

                    case 2:
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(slot)));
                        break;

                    case 3:
                        if (slot != mc.thePlayer.inventory.currentItem)
                            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        break;
                }
            }

            if (mc.thePlayer.hurtTime > 0) {
                // Getting the path to teleport along.
                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(tpX, tpY, tpZ));

                // Teleporting along the path to the block.
                for (final Vec3 point : path)
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(point.getX(), point.getY(), point.getZ(), true));

                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(tpX, tpY, tpZ, true));

                // Setting the users position to the block to make the teleport look smoother.
                mc.thePlayer.setPosition(tpX, tpY, tpZ);

                countTicks = false;
                ticks = 0;
            }
        }

        if (countTicks)
            ticks++;
    }

    @Override
    public void onMove(final MoveEvent event) {
        if (mode.is("Hypixel Fireball") && countTicks)
            event.setCancelled(true);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Packet p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 20 && mode.is("Vulcan")) {
            S08PacketPlayerPosLook s08 = ((S08PacketPlayerPosLook) p);
            if (mc.thePlayer.getDistanceSq(s08.getX(), s08.getY(), s08.getZ()) < 2 * 2) {
                event.setCancelled(true);
                countTicks = true;
            } else {
                countTicks = false;
                ticks = 0;
                startTeleport = false;
            }
        }
    }

    @Override
    protected void onEnable() {
        countTicks = false;
        ticks = 0;
        startTeleport = false;

        tpX = mc.thePlayer.posX;
        tpY = mc.thePlayer.posY;
        tpZ = mc.thePlayer.posZ;
    }
}