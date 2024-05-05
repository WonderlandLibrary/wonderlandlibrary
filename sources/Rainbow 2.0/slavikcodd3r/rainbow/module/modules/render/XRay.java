package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.init.Blocks;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import java.util.ArrayList;

@Mod(displayName = "XRay")
public class XRay extends Module
{
    public static int opacity;
    public static boolean isXray;
    private boolean wasFullbright;
    public static ArrayList<Block> xrayBlocks;
    public static ArrayList<Integer> intBlocks;
    
    static {
        XRay.opacity = 120;
        XRay.isXray = false;
        XRay.xrayBlocks = new ArrayList<Block>();
        XRay.intBlocks = new ArrayList<Integer>();
        Minecraft mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void enable() {
        super.enable();
        if (!(this.wasFullbright = ModuleManager.getModule("Brightness").isEnabled())) {
            ModuleManager.getModule("Brightness").toggle();
        }
        this.addXrayBlocks();
        XRay.isXray = true;
        Minecraft.getMinecraft();
        ClientUtils.mc().renderGlobal.loadRenderers();
    }
    
    @Override
    public void disable() {
        super.disable();
        if (!this.wasFullbright) {
            ModuleManager.getModule("Brightness").toggle();
        }
        this.removeXrayBlocks();
        XRay.isXray = false;
        Minecraft.getMinecraft();
        ClientUtils.mc().renderGlobal.loadRenderers();
    }
    
    public void addXrayBlocks() {
        XRay.xrayBlocks.add(Block.getBlockById(8));
        XRay.xrayBlocks.add(Block.getBlockById(9));
        XRay.xrayBlocks.add(Block.getBlockById(10));
        XRay.xrayBlocks.add(Block.getBlockById(11));
        XRay.xrayBlocks.add(Block.getBlockById(14));
        XRay.xrayBlocks.add(Block.getBlockById(15));
        XRay.xrayBlocks.add(Block.getBlockById(16));
        XRay.xrayBlocks.add(Block.getBlockById(21));
        XRay.xrayBlocks.add(Block.getBlockById(56));
        XRay.xrayBlocks.add(Block.getBlockById(73));
        XRay.xrayBlocks.add(Block.getBlockById(88));
        XRay.xrayBlocks.add(Block.getBlockById(112));
        XRay.xrayBlocks.add(Block.getBlockById(129));
        XRay.xrayBlocks.add(Block.getBlockById(153));
    }
    
    public void removeXrayBlocks() {
        XRay.xrayBlocks.remove(Block.getBlockById(8));
        XRay.xrayBlocks.remove(Block.getBlockById(9));
        XRay.xrayBlocks.remove(Block.getBlockById(10));
        XRay.xrayBlocks.remove(Block.getBlockById(11));
        XRay.xrayBlocks.remove(Block.getBlockById(14));
        XRay.xrayBlocks.remove(Block.getBlockById(15));
        XRay.xrayBlocks.remove(Block.getBlockById(16));
        XRay.xrayBlocks.remove(Block.getBlockById(21));
        XRay.xrayBlocks.remove(Block.getBlockById(56));
        XRay.xrayBlocks.remove(Block.getBlockById(73));
        XRay.xrayBlocks.remove(Block.getBlockById(88));
        XRay.xrayBlocks.remove(Block.getBlockById(112));
        XRay.xrayBlocks.remove(Block.getBlockById(129));
        XRay.xrayBlocks.remove(Block.getBlockById(153));
    }
    
    public static boolean filterBlocks(final Block b) {
        return !XRay.xrayBlocks.contains(Blocks.leaves) && !XRay.xrayBlocks.contains(Blocks.leaves2) && XRay.xrayBlocks.contains(b);
    }
}
