/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.render;

import java.util.ArrayList;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class XRay
extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Ores");
    private float old;
    public static ArrayList<Block> normalXray = new ArrayList();
    public static ArrayList<Block> oreXray = new ArrayList();

    public XRay() {
        super("XRay", 0, Module.Category.RENDER);
        this.addSettings(mode);
    }

    @Override
    public void onEnable() {
        if (mode.is("Normal")) {
            normalXray.add(Block.getBlockFromName("coal_ore"));
            normalXray.add(Block.getBlockFromName("iron_ore"));
            normalXray.add(Block.getBlockFromName("gold_ore"));
            normalXray.add(Block.getBlockFromName("redstone_ore"));
            normalXray.add(Block.getBlockById(74));
            normalXray.add(Block.getBlockFromName("lapis_ore"));
            normalXray.add(Block.getBlockFromName("diamond_ore"));
            normalXray.add(Block.getBlockFromName("emerald_ore"));
            normalXray.add(Block.getBlockFromName("quartz_ore"));
            normalXray.add(Block.getBlockFromName("glowstone"));
            normalXray.add(Block.getBlockById(8));
            normalXray.add(Block.getBlockById(9));
            normalXray.add(Block.getBlockById(10));
            normalXray.add(Block.getBlockById(11));
            normalXray.add(Block.getBlockFromName("crafting_table"));
            normalXray.add(Block.getBlockById(61));
            normalXray.add(Block.getBlockById(62));
            normalXray.add(Block.getBlockFromName("torch"));
            normalXray.add(Block.getBlockFromName("ladder"));
            normalXray.add(Block.getBlockFromName("tnt"));
            normalXray.add(Block.getBlockFromName("coal_block"));
            normalXray.add(Block.getBlockFromName("iron_block"));
            normalXray.add(Block.getBlockFromName("gold_block"));
            normalXray.add(Block.getBlockFromName("diamond_block"));
            normalXray.add(Block.getBlockFromName("emerald_block"));
            normalXray.add(Block.getBlockFromName("redstone_block"));
            normalXray.add(Block.getBlockFromName("lapis_block"));
            normalXray.add(Block.getBlockFromName("mossy_cobblestone"));
            normalXray.add(Block.getBlockFromName("mob_spawner"));
            normalXray.add(Block.getBlockFromName("enchanting_table"));
            normalXray.add(Block.getBlockFromName("bookshelf"));
        }
        if (mode.is("Ores")) {
            oreXray.add(Block.getBlockFromName("coal_ore"));
            oreXray.add(Block.getBlockFromName("iron_ore"));
            oreXray.add(Block.getBlockFromName("gold_ore"));
            oreXray.add(Block.getBlockFromName("redstone_ore"));
            oreXray.add(Block.getBlockById(74));
            oreXray.add(Block.getBlockFromName("lapis_ore"));
            oreXray.add(Block.getBlockFromName("diamond_ore"));
            oreXray.add(Block.getBlockFromName("emerald_ore"));
            oreXray.add(Block.getBlockFromName("quartz_ore"));
            oreXray.add(Block.getBlockFromName("coal_block"));
            oreXray.add(Block.getBlockFromName("iron_block"));
            oreXray.add(Block.getBlockFromName("gold_block"));
            oreXray.add(Block.getBlockFromName("diamond_block"));
            oreXray.add(Block.getBlockFromName("emerald_block"));
            oreXray.add(Block.getBlockFromName("redstone_block"));
            oreXray.add(Block.getBlockFromName("lapis_block"));
        }
        Block.isXRayEnabled = true;
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        this.old = this.mc.gameSettings.gammaSetting;
        this.mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    public void onDisable() {
        Block.isXRayEnabled = false;
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        this.mc.gameSettings.gammaSetting = this.old;
    }

    public static boolean isXrayBlock(Block blockToCheck) {
        if (mode.is("Normal") && normalXray.contains(blockToCheck)) {
            return true;
        }
        return mode.is("Ores") && oreXray.contains(blockToCheck);
    }
}

