package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author kroko
 * @created on 14.11.2020 : 23:02
 */

@ModuleInfo(category = Module.Category.RENDER, name = "Xray", description = "You can see through walls")
public class XRay extends Module {

    public Setting mode = new Setting("Mode", new String[] {"Transparent", "Invisible"}, "Invisible", this);

    public ArrayList<Block> blocks = new ArrayList<>();

    public XRay() {
        Collections.addAll(blocks, Blocks.diamond_ore, Blocks.redstone_ore, Blocks.lapis_ore, Blocks.coal_ore, Blocks.emerald_ore, Blocks.iron_ore, Blocks.gold_ore, Blocks.quartz_ore, Blocks.lit_redstone_ore, Blocks.water, Blocks.flowing_water,Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest);
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
    }
}
