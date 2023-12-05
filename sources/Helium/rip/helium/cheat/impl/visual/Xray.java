package rip.helium.cheat.impl.visual;

import net.minecraft.block.Block;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class Xray extends Cheat {

    public static ArrayList<Block> xrayBlocks = new ArrayList();

    public Xray() {
        super("Xray", "See through blocks!", CheatCategory.VISUAL);
        xrayBlocks.add(Block.getBlockFromName("coal_ore"));
        xrayBlocks.add(Block.getBlockFromName("iron_ore"));
        xrayBlocks.add(Block.getBlockFromName("gold_ore"));
        xrayBlocks.add(Block.getBlockFromName("redstone_ore"));
        xrayBlocks.add(Block.getBlockById(74));
        xrayBlocks.add(Block.getBlockFromName("lapis_ore"));
        xrayBlocks.add(Block.getBlockFromName("diamond_ore"));
        xrayBlocks.add(Block.getBlockFromName("emerald_ore"));
        xrayBlocks.add(Block.getBlockFromName("quartz_ore"));
        xrayBlocks.add(Block.getBlockFromName("clay"));
        xrayBlocks.add(Block.getBlockFromName("glowstone"));
        xrayBlocks.add(Block.getBlockById(8));
        xrayBlocks.add(Block.getBlockById(9));
        xrayBlocks.add(Block.getBlockById(10));
        xrayBlocks.add(Block.getBlockById(11));
        xrayBlocks.add(Block.getBlockFromName("crafting_table"));
        xrayBlocks.add(Block.getBlockById(61));
        xrayBlocks.add(Block.getBlockById(62));
        xrayBlocks.add(Block.getBlockFromName("torch"));
        xrayBlocks.add(Block.getBlockFromName("ladder"));
        xrayBlocks.add(Block.getBlockFromName("tnt"));
        xrayBlocks.add(Block.getBlockFromName("coal_block"));
        xrayBlocks.add(Block.getBlockFromName("iron_block"));
        xrayBlocks.add(Block.getBlockFromName("gold_block"));
        xrayBlocks.add(Block.getBlockFromName("diamond_block"));
        xrayBlocks.add(Block.getBlockFromName("emerald_block"));
        xrayBlocks.add(Block.getBlockFromName("redstone_block"));
        xrayBlocks.add(Block.getBlockFromName("lapis_block"));
        xrayBlocks.add(Block.getBlockFromName("fire"));
        xrayBlocks.add(Block.getBlockFromName("mossy_cobblestone"));
        xrayBlocks.add(Block.getBlockFromName("mob_spawner"));
        xrayBlocks.add(Block.getBlockFromName("end_portal_frame"));
        xrayBlocks.add(Block.getBlockFromName("enchanting_table"));
        xrayBlocks.add(Block.getBlockFromName("bookshelf"));
        xrayBlocks.add(Block.getBlockFromName("command_block"));
        xrayBlocks.add(Block.getBlockFromName("bone_block"));
    }

    public static boolean isXRayBlock(Block blockToCheck) {
        return xrayBlocks.contains(blockToCheck);
    }

    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    public void onDisable() {
        mc.renderGlobal.loadRenderers();

    }

}
