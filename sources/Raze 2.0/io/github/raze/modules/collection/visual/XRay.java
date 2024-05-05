package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.game.EventUpdate;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class XRay extends AbstractModule {

    public XRay() {
        super("XRay", "Only renders ores", ModuleCategory.VISUAL);
    }

    public static final ArrayList<Block> xrayBlocks = new ArrayList<>();

    @Listen
    public void onUpdate(EventUpdate eventUpdate) {
        mc.gameSettings.gammaSetting = 10f;
    }

    @Override
    public void onEnable() {
        xrayBlocks.add(Block.getBlockFromName("coal_ore"));
        xrayBlocks.add(Block.getBlockFromName("iron_ore"));
        xrayBlocks.add(Block.getBlockFromName("gold_ore"));
        xrayBlocks.add(Block.getBlockFromName("redstone_ore"));
        xrayBlocks.add(Block.getBlockFromName("lapis_ore"));
        xrayBlocks.add(Block.getBlockFromName("diamond_ore"));
        xrayBlocks.add(Block.getBlockFromName("emerald_ore"));
        xrayBlocks.add(Block.getBlockFromName("quartz_ore"));
        Block.isXRay = true;
        mc.gameSettings.gammaSetting = 10f;
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        Block.isXRay = false;
        mc.gameSettings.gammaSetting = 0.5f;
        mc.renderGlobal.loadRenderers();
    }

    public static boolean isXRayBlock(Block blockToCheck) {
        return xrayBlocks.contains(blockToCheck);
    }

}
