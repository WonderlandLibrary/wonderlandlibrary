package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;


public class XRay extends Module {

    public XRay() {
        super("XRay", Category.Render, false);
    }

    public static FloatValue opacity = new FloatValue("XRay", "Opacity", 120f, 0f, 255f, 5f);
    public static BooleanValue caveFinder = new BooleanValue("XRay", "CaveFinder", false);

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    public static boolean isOreBlock(Block block) {
        return block instanceof BlockOre || block instanceof BlockRedstoneOre;
    }
}
