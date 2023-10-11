package com.initial.utils.render;

import com.initial.modules.impl.visual.*;
import net.minecraft.block.*;

public class XRayUtils
{
    public static void initXrayBlocks() {
        XRay.xrayBlocks.add(Block.getBlockFromName("coal_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("iron_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("gold_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("redstone_ore"));
        XRay.xrayBlocks.add(Block.getBlockById(74));
        XRay.xrayBlocks.add(Block.getBlockFromName("lapis_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("diamond_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("emerald_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("quartz_ore"));
        XRay.xrayBlocks.add(Block.getBlockFromName("clay"));
        XRay.xrayBlocks.add(Block.getBlockFromName("glowstone"));
        XRay.xrayBlocks.add(Block.getBlockById(8));
        XRay.xrayBlocks.add(Block.getBlockById(9));
        XRay.xrayBlocks.add(Block.getBlockById(10));
        XRay.xrayBlocks.add(Block.getBlockById(11));
        XRay.xrayBlocks.add(Block.getBlockFromName("crafting_table"));
        XRay.xrayBlocks.add(Block.getBlockById(61));
        XRay.xrayBlocks.add(Block.getBlockById(62));
        XRay.xrayBlocks.add(Block.getBlockFromName("torch"));
        XRay.xrayBlocks.add(Block.getBlockFromName("ladder"));
        XRay.xrayBlocks.add(Block.getBlockFromName("tnt"));
        XRay.xrayBlocks.add(Block.getBlockFromName("coal_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("iron_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("gold_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("diamond_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("emerald_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("redstone_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("lapis_block"));
        XRay.xrayBlocks.add(Block.getBlockFromName("fire"));
        XRay.xrayBlocks.add(Block.getBlockFromName("mossy_cobblestone"));
        XRay.xrayBlocks.add(Block.getBlockFromName("mob_spawner"));
        XRay.xrayBlocks.add(Block.getBlockFromName("end_portal_frame"));
        XRay.xrayBlocks.add(Block.getBlockFromName("enchanting_table"));
        XRay.xrayBlocks.add(Block.getBlockFromName("bookshelf"));
        XRay.xrayBlocks.add(Block.getBlockFromName("command_block"));
    }
    
    public static boolean isXrayBlock(final Block b) {
        return XRay.xrayBlocks.contains(b);
    }
}
