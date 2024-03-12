package de.theBest.MythicX.modules.visual;

import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import net.minecraft.block.Block;

import java.awt.*;
import java.util.ArrayList;

public class Xray extends Module {
    public static boolean Xray;
    public ArrayList<Block> xrayBlocks = new ArrayList<Block>();
    public Xray() {
        super("Xray", Type.Visual, 0, Category.VISUAL, Color.green, "Sees through Blocks and shows Materials");
    }

    @Override
    public void onEnable() {
        this.Xray = true;
        mc.gameSettings.ambientOcclusion = 0;
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        this.Xray = false;
        mc.gameSettings.ambientOcclusion = 1;
        mc.renderGlobal.loadRenderers();
    }

    public boolean shouldXrayBlock(Block blockid) {
        if(this.xrayBlocks.contains(blockid)) {
            return true;
        }
        return false;

    }

}
