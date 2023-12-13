/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
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
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@ModuleInfo(name = "AutoBuild", description = "Builds things around you", category = Category.OTHER)
public final class AutoBuild extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Teleport");
    private final ModeSetting build = new ModeSetting("Build", this, "Penis", "Penis",/* "Swastika",*/ "Surround", "Singular");
    private final NumberSetting height = new NumberSetting("Height", this, 2, 0, 2, 1);
    private final NumberSetting tick = new NumberSetting("Tick", this, 1, 1, 20, 1);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, false);
    private final BooleanSetting nextSlot = new BooleanSetting("Next Slot", this, false);
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        height.hidden = !build.is("Penis");

        tick.hidden = (!mode.is("Normal") || autoDisable.isEnabled());
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (mc.thePlayer.ticksExisted < 5) {
            this.toggleModule();
            this.registerNotification("Disabled " + this.getModuleInfo().name() + " due to world change.");
            return;
        }

        if (mc.thePlayer.ticksExisted % tick.getValue() == 0 || (!mode.is("Normal") || autoDisable.isEnabled())) {
            switch (mode.getMode()) {
                case "Normal": {
                    build(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    break;
                }

                case "Teleport": {
                    if (mc.gameSettings.keyBindUseItem.isKeyDown() && timer.hasReached(100L)) {
                        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();

                        final BlockPos pos = mc.thePlayer.rayTrace(999, 1).getBlockPos();
                        BlockPos tpPos = pos;

                        switch (build.getMode()) {
                            case "Penis":
                            case "Surround":
                                tpPos = tpPos.offset(EnumFacing.EAST, 2).offset(EnumFacing.SOUTH, 2);
                                break;
                        }

                        if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
                            return;

                        final BlockPos finalTpPos = tpPos;
                        new Thread(() -> {
                            double x = mc.thePlayer.posX;
                            double y = mc.thePlayer.posY;
                            double z = mc.thePlayer.posZ;

                            if (freecamEnabled) {
                                x = Freecam.startX;
                                y = Freecam.startY;
                                z = Freecam.startZ;
                            }

                            final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(x, y, z), new Vec3(finalTpPos.getX(), finalTpPos.getY(), finalTpPos.getZ()));

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                            build(pos.getX(), pos.getY(), pos.getZ());
                            Collections.reverse(path);

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                        }).start();

                        timer.reset();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement)
            event.setCancelled(true);
    }

    private void build(double xPos, final double yPos, double zPos) {
        if (nextSlot.isEnabled())
            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem == 8 ? 0 : mc.thePlayer.inventory.currentItem + 1));

        switch (build.getMode()) {
            case "Penis": {
                if (mode.is("Normal")) {
                    xPos += 2;
                    zPos += 2;
                }

                final BlockPos pos = new BlockPos(xPos, yPos, zPos);

                // The middle
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos, EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));

                // The balls
                switch (mc.thePlayer.getHorizontalFacing()) {
                    case EAST:
                    case WEST:
                        PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(), EnumFacing.NORTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                        PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                        break;

                    case NORTH:
                    case SOUTH:
                        PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(), EnumFacing.EAST.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                        PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(), EnumFacing.WEST.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                        break;
                }

                // The penises height
                for (int i = 1; i <= height.getValue()/*Change this variable to control the penises height*/; i++)
                    PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(i), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                break;
            }

//            case "Swastika": {
//                if (mode.is("Normal")) {
//                    xPos += 2;
//                    zPos += 2;
//                }
//
//                final BlockPos pos = new BlockPos(xPos, yPos, zPos);
//
//                // The middle
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos, EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//
//                // The swastikas height
//                for (int i = 1; i <= 4/*Change this variable to control the swastikas height (don't change)*/; i++)
//                    PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(i), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//
//                // The right bottom
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.offset(EnumFacing.NORTH, 2), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up().offset(EnumFacing.NORTH, 2), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(2).offset(EnumFacing.NORTH, 2), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(3).offset(EnumFacing.NORTH), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//
//                // The right top
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(5), EnumFacing.NORTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(5).offset(EnumFacing.NORTH), EnumFacing.NORTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//
//                // The left bottom
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up().offset(EnumFacing.SOUTH), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//
//                // The left top
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(3), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(3).offset(EnumFacing.SOUTH), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(4).offset(EnumFacing.SOUTH, 2), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.up(5).offset(EnumFacing.SOUTH, 2), EnumFacing.SOUTH.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//                break;
//            }

            case "Surround": {
                final BlockPos pos = new BlockPos(xPos, yPos, zPos);

                // Creating a for loop and offsetting in each direction to create a surround
                for (final EnumFacing facing : EnumFacing.VALUES) {
                    if (facing == EnumFacing.UP || facing == EnumFacing.DOWN)
                        continue;

                    PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos.offset(facing), EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                }
                break;
            }

            case "Singular": {
                final BlockPos pos = new BlockPos(xPos, yPos, zPos);
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(pos, EnumFacing.UP.getIndex(), mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                break;
            }
        }

        if (autoDisable.isEnabled())
            this.toggleModule();

        if (nextSlot.isEnabled())
            PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }
}