package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.misc.EvictingList;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.ArrayList;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "XRay(WIP)", description = "Allows you to see blocks through walls", category = Category.RENDER)
public final class XRay extends Module {

    public static ArrayList blocks = null;
    public static EvictingList<BlockPos> interactedBlocks = new EvictingList<>(2000);
    private int lastRange;

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Hypixel");
    private final NumberSetting range = new NumberSetting("Range", this, 5, 5, 100, 1);

    @Override
    public void onUpdateAlwaysInGui() {
        range.hidden = !mode.is("Hypixel");
        final int range = (int) this.range.getValue();

        if (lastRange != range) {
            lastRange = range;
            interactedBlocks = new EvictingList<>(2000);
        }
    }

    @Override
    protected void onEnable() {
        blocks = new ArrayList() {{
            add(Blocks.diamond_ore);
            add(Blocks.emerald_ore);
            add(Blocks.iron_ore);
            add(Blocks.gold_ore);
            add(Blocks.coal_ore);
            add(Blocks.water);
            add(Blocks.flowing_water);
            add(Blocks.lava);
            add(Blocks.flowing_lava);
            add(Blocks.redstone_ore);
            add(Blocks.lit_redstone_ore);
        }};
        mc.renderGlobal.loadRenderers();
        interactedBlocks.clear();
    }

    @Override
    protected void onDisable() {
        blocks = null;
        interactedBlocks.clear();
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted % 5 == 0) return;

        switch (mode.getMode()) {
            case "Hypixel":
                final int range = 6;

                int amount = 0;
                boolean ranXRay = false;
                for (int x = -range; x <= range; ++x) {
                    for (int y = -range; y <= range; ++y) {
                        for (int z = -range; z <= range; ++z) {
                            final EntityPlayer player = mc.thePlayer;
                            final int posX = ((int) player.posX) + x;
                            final int posY = ((int) player.posY) + y;
                            final int posZ = ((int) player.posZ) + z;

                            final BlockPos blockPos = new BlockPos(posX, posY, posZ);
                            final Block block = PlayerUtil.getBlock(posX, posY, posZ);
                            if (!interactedBlocks.contains(blockPos)) {
                                interactedBlocks.add(blockPos);

                                if (mc.thePlayer.getDistance(posX, posY, posZ) <= 1.5 || block instanceof BlockAir)
                                    return;

                                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                                ranXRay = true;
                                amount++;
                                if (amount >= 50) return;
                            }
                        }
                    }
                }

                if (!ranXRay) interactedBlocks.clear();
                break;
        }
    }
}
