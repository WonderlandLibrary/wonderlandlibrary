/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.command.impl.SetArea;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.render.Freecam;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.pathfinding.Vec3;
import dev.rise.util.player.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

@ModuleInfo(name = "Nuker", description = "Breaks blocks around you", category = Category.OTHER)
public final class Nuker extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Teleport", "Area");
    private final NumberSetting delay = new NumberSetting("Delay", this, 100, 0, 5000, 50);
    private final NumberSetting range = new NumberSetting("Range", this, 4, 1, 7, 0.5);
    private final NumberSetting tick = new NumberSetting("Tick", this, 1, 1, 20, 1);
    private final BooleanSetting instantOnly = new BooleanSetting("Instant Only", this, false);
    private final BooleanSetting scatter = new BooleanSetting("Scatter", this, false);
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        delay.hidden = !(mode.is("Teleport") || mode.is("Area"));

        tick.hidden = !mode.is("Normal");
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        final double radius = this.range.getValue() - 1;

        if (mc.thePlayer.ticksExisted < 5) {
            this.toggleModule();
            this.registerNotification("Disabled " + this.getModuleInfo().name() + " due to world change.");
            return;
        }

        if (mc.thePlayer.ticksExisted % tick.getValue() == 0 || !mode.is("Normal")) {
            switch (mode.getMode()) {
                case "Normal": {
                    nuke(radius, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    break;
                }

                case "Teleport": {
                    if (mc.gameSettings.keyBindUseItem.isKeyDown() && timer.hasReached((long) delay.getValue())) {
                        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();

                        final BlockPos pos = mc.thePlayer.rayTrace(999, 1).getBlockPos();

                        if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
                            return;

                        new Thread(() -> {
                            double x = mc.thePlayer.posX;
                            double y = mc.thePlayer.posY;
                            double z = mc.thePlayer.posZ;

                            if (freecamEnabled) {
                                x = Freecam.startX;
                                y = Freecam.startY;
                                z = Freecam.startZ;
                            }

                            final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(x, y, z), new Vec3(pos.getX(), pos.getY(), pos.getZ()));

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                            nuke(radius, pos.getX(), pos.getY(), pos.getZ());
                            Collections.reverse(path);

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                        }).start();

                        timer.reset();
                    }
                    break;
                }

                case "Area": {
                    final Vec3 pos1 = SetArea.pos1;
                    final Vec3 pos2 = SetArea.pos2;

                    if (pos1 == null || pos2 == null) {
                        this.registerNotification("Set a position for Area Nuker by doing .setarea <1/2/reset> at the positions.");
                        this.toggleModule();
                        return;
                    }

                    if (timer.hasReached((long) delay.getValue())) {
                        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();
                        final double nukeX = randomPoint(pos1.getX(), pos2.getX());
                        final double nukeY = randomPoint(pos1.getY(), pos2.getY());
                        final double nukeZ = randomPoint(pos1.getZ(), pos2.getZ());

                        new Thread(() -> {
                            double x = mc.thePlayer.posX;
                            double y = mc.thePlayer.posY;
                            double z = mc.thePlayer.posZ;

                            if (freecamEnabled) {
                                x = Freecam.startX;
                                y = Freecam.startY;
                                z = Freecam.startZ;
                            }

                            final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(x, y, z), new Vec3(nukeX, nukeY, nukeZ));

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                            nuke(radius, nukeX, nukeY, nukeZ);
                            Collections.reverse(path);

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                        }).start();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S02PacketChat) {
            final S02PacketChat packetChat = (S02PacketChat) p;
            final String message = packetChat.getChatComponent().getUnformattedText();
            if (!packetChat.isChat() && message.equals("You can't build outside the plot!"))
                event.setCancelled(true);
        }
    }

    private void nuke(final double range, final double xPos, final double yPos, final double zPos) {
        if (range == 0) {
            final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

            if (block instanceof BlockAir || (block.getBlockHardness() > 0 && instantOnly.isEnabled()))
                return;

            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));

            if (swing.isEnabled())
                mc.thePlayer.swingItem();
        } else {
            for (double x = -range; x < range; x++) {
                for (double y = range; y > -range; y--) {
                    for (double z = -range; z < range; z++) {
                        if (scatter.isEnabled() && !((mc.thePlayer.ticksExisted % 2 == 0 ? x : z) % 2 == 0))
                            continue;

                        final BlockPos blockPos = new BlockPos(xPos + x, yPos + y, zPos + z);
                        final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                        if (block instanceof BlockAir || (block.getBlockHardness() > 0 && instantOnly.isEnabled()))
                            continue;

                        PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                        PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                        if (swing.isEnabled())
                            mc.thePlayer.swingItem();
                    }
                }
            }
        }
    }

    private double randomPoint(final double one, final double two) {
        if (one == two)
            return one;

        final double delta = two - one;
        final double offset = new Random().nextFloat() * delta;
        return one + offset;
    }
}