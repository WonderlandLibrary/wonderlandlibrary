package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "CaveFinder", description = "Allows you to see through walls.", category = ModuleCategory.RENDER)
public class CaveFinder extends Module {

    public List<Block> xrayBlocks = new ArrayList<Block>(Arrays.asList(
            Blocks.coal_ore,
            Blocks.iron_ore,
            Blocks.gold_ore,
            Blocks.redstone_ore,
            Blocks.lapis_ore,
            Blocks.diamond_ore,
            Blocks.emerald_ore,
            Blocks.quartz_ore,
            Blocks.clay,
            Blocks.glowstone,
            Blocks.crafting_table,
            Blocks.torch,
            Blocks.ladder,
            Blocks.tnt,
            Blocks.coal_block,
            Blocks.iron_block,
            Blocks.gold_block,
            Blocks.diamond_block,
            Blocks.emerald_block,
            Blocks.redstone_block,
            Blocks.lapis_block,
            Blocks.fire,
            Blocks.mossy_cobblestone,
            Blocks.mob_spawner,
            Blocks.end_portal_frame,
            Blocks.enchanting_table,
            Blocks.bookshelf,
            Blocks.command_block,
            Blocks.lava,
            Blocks.flowing_lava,
            Blocks.water,
            Blocks.flowing_water,
            Blocks.furnace,
            Blocks.lit_furnace));

    public static IntegerValue opacity = new IntegerValue("Opacity", 160, 0, 255);
    public static BoolValue caveFinder = new BoolValue("CaveFinder", true);

    @Override
    public void onToggle(final boolean state) {
        mc.renderGlobal.loadRenderers();
    }

}
