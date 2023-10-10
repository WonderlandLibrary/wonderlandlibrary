package vestige.module.impl.visual;

import net.minecraft.block.Block;
import vestige.module.Module;
import vestige.module.Category;

public class Xray extends Module {

    private float oldGamma;

    private final int[] blockIds = {14, 15, 56, 129};

    public Xray() {
        super("Xray", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;

        mc.gameSettings.gammaSetting = 10F;
        mc.gameSettings.ambientOcclusion = 0;

        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        mc.gameSettings.ambientOcclusion = 1;

        mc.renderGlobal.loadRenderers();
    }

    public boolean shouldRenderBlock(Block block) {
        for(int id : blockIds) {
            if(block == Block.getBlockById(id)) {
                return true;
            }
        }

        return false;
    }

}
