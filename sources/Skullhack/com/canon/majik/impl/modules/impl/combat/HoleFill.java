package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.PlayerUpdateEvent;
import com.canon.majik.api.utils.player.BlockUtil;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.api.utils.player.rotation.Rotation;
import com.canon.majik.api.utils.player.rotation.RotationUtil;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;

public class HoleFill extends Module {

    NumberSetting holeRadius = setting("Hole Radius", 6.0f, 0.0f, 6f);
    NumberSetting delay = setting("Delay", 6.0f, 0.0f, 50f);
    BooleanSetting packet = setting("Packet", true);
    BooleanSetting rotate = setting("Rotate", true);
    BooleanSetting autoS = setting("Switch", true);

    public HoleFill(String name, Category category) {
        super(name, category);
    }
    HashSet<BlockPos> holes = new HashSet<>();
    Rotation spoofed = new Rotation(0,0);
    private long time;

    @EventListener
    public void onTick(PlayerUpdateEvent event){
        if(nullCheck()) return;
        if (!holes.isEmpty()) {
            int obbySlot = mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? mc.player.inventory.currentItem : -1;
            if (obbySlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                        obbySlot = l;
                        break;
                    }
                }
            }

            boolean offhand = false;
            if (mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                offhand = true;
            } else if (obbySlot == -1) {
                return;
            }

            if (!offhand && mc.player.inventory.currentItem != obbySlot) {
                if (autoS.getValue()) {
                    PlayerUtils.switchToSlot(obbySlot);
                }
                return;
            }
            holes.forEach(pos -> {
                if (System.currentTimeMillis() - time > delay.getValue().floatValue()) {
                    if (rotate.getValue()) {
                        rotateOnly(event, new Vec3d(pos));
                    }
                    if (packet.getValue()) {
                        mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    } else {
                        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(pos), EnumHand.MAIN_HAND);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
                time = System.currentTimeMillis();
            });
        }

        if (!holes.isEmpty())
            holes.clear();
        if (!holes.isEmpty())
            holes.clear();
        searchHoles();
    }

    private void rotateOnly(PlayerUpdateEvent event, Vec3d safe) {
        spoofed = RotationUtil.facePos(safe, event, spoofed, 45);
    }

    public void searchHoles() {
        for (BlockPos pos : BlockUtil.getBlocksInRadius(holeRadius.getValue().floatValue(), BlockUtil.AirMode.AirOnly)) {
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                holes.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK)) {
                holes.add(pos);
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) {
                holes.add(pos);
                holes.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK)) {
                holes.add(pos);
                holes.add(pos.north());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK) {
                holes.add(pos);
                holes.add(pos.west());
                continue;
            }
            if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().down()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.west()).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().south()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().north()).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west().west()).getBlock() == Blocks.OBSIDIAN)) {
                holes.add(pos);
                holes.add(pos.west());
            }
        }
    }
}
