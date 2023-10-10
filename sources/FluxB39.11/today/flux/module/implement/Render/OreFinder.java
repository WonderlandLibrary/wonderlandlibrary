package today.flux.module.implement.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import today.flux.module.ModuleManager;
import today.flux.utility.ChatUtils;
import today.flux.utility.PlayerUtils;
import today.flux.module.value.FloatValue;

import java.util.ArrayList;
import java.util.List;

public class OreFinder extends Thread {
    public Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<BlockPos> diamond = new ArrayList<>();
    public static BlockESP blockESP = ModuleManager.blockEspMod;
    public List<BlockPos> result;

    public FloatValue range, depth;
    public int rangeInt = -1;

    public boolean isRunning = false;

    public OreFinder(FloatValue range, FloatValue depth) {
        this.range = range;
        this.depth = depth;
    }

    public OreFinder(int range, FloatValue depth) {
        this.rangeInt = range;
        this.depth = depth;
    }


    @Override
    public void run() {
        while (ModuleManager.blockEspMod.isEnabled() && mc.theWorld != null) {
            isRunning = true;
            long time = System.currentTimeMillis();
            int r = rangeInt != -1 ? rangeInt : range.getValue().intValue();
            List<BlockPos> tmp = new ArrayList<>();
            for (int x = -r; x <= r; x++)
                for (int y = 1; y <= 128; y++)
                    for (int z = -r; z <= r; z++) {
                        BlockPos pos = new BlockPos(mc.thePlayer.posX + x, y, mc.thePlayer.posZ + z);
                        if (blockESP.isTarget(pos) && blockESP.oreTest(pos, depth.getValue().intValue())) {
                            tmp.add(pos);
                            if (mc.theWorld.getBlockState(pos).getBlock() == Blocks.diamond_ore && !diamond.contains(pos)) {
                                diamond.add(pos);
                                if (ModuleManager.blockEspMod.prompt.getValue()){
                                    PlayerUtils.tellPlayer(String.format("Found Diamond! X:%d Y:%d Z:%d", pos.getX(), pos.getY(), pos.getZ()));
                                }
                            }
                        }
                    }
            result = tmp;
            ChatUtils.debug("Using time:" + (System.currentTimeMillis() - time));
        }
        isRunning = false;
    }
}
