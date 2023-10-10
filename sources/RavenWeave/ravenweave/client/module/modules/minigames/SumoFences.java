package ravenweave.client.module.modules.minigames;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.ComboSetting;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SumoFences extends Module {
    public static DescriptionSetting a;
    public static DescriptionSetting d;
    public static SliderSetting fenceHeight;
    public static SliderSetting c;
    private final ComboSetting<SumoBlockType> sumoBlockType;
    private Timer timer;
    private final List<String> map = Arrays.asList("Sumo", "Space Mine", "White Crystal");
    private static final List<BlockPos> fencePositions = Arrays.asList(new BlockPos(9, 65, -2), new BlockPos(9, 65, -1),
            new BlockPos(9, 65, 0), new BlockPos(9, 65, 1), new BlockPos(9, 65, 2), new BlockPos(9, 65, 3),
            new BlockPos(8, 65, 3), new BlockPos(8, 65, 4), new BlockPos(8, 65, 5), new BlockPos(7, 65, 5),
            new BlockPos(7, 65, 6), new BlockPos(7, 65, 7), new BlockPos(6, 65, 7), new BlockPos(5, 65, 7),
            new BlockPos(5, 65, 8), new BlockPos(4, 65, 8), new BlockPos(3, 65, 8), new BlockPos(3, 65, 9),
            new BlockPos(2, 65, 9), new BlockPos(1, 65, 9), new BlockPos(0, 65, 9), new BlockPos(-1, 65, 9),
            new BlockPos(-2, 65, 9), new BlockPos(-3, 65, 9), new BlockPos(-3, 65, 8), new BlockPos(-4, 65, 8),
            new BlockPos(-5, 65, 8), new BlockPos(-5, 65, 7), new BlockPos(-6, 65, 7), new BlockPos(-7, 65, 7),
            new BlockPos(-7, 65, 6), new BlockPos(-7, 65, 5), new BlockPos(-8, 65, 5), new BlockPos(-8, 65, 4),
            new BlockPos(-8, 65, 3), new BlockPos(-9, 65, 3), new BlockPos(-9, 65, 2), new BlockPos(-9, 65, 1),
            new BlockPos(-9, 65, 0), new BlockPos(-9, 65, -1), new BlockPos(-9, 65, -2), new BlockPos(-9, 65, -3),
            new BlockPos(-8, 65, -3), new BlockPos(-8, 65, -4), new BlockPos(-8, 65, -5), new BlockPos(-7, 65, -5),
            new BlockPos(-7, 65, -6), new BlockPos(-7, 65, -7), new BlockPos(-6, 65, -7), new BlockPos(-5, 65, -7),
            new BlockPos(-5, 65, -8), new BlockPos(-4, 65, -8), new BlockPos(-3, 65, -8), new BlockPos(-3, 65, -9),
            new BlockPos(-2, 65, -9), new BlockPos(-1, 65, -9), new BlockPos(0, 65, -9), new BlockPos(1, 65, -9),
            new BlockPos(2, 65, -9), new BlockPos(3, 65, -9), new BlockPos(3, 65, -8), new BlockPos(4, 65, -8),
            new BlockPos(5, 65, -8), new BlockPos(5, 65, -7), new BlockPos(6, 65, -7), new BlockPos(7, 65, -7),
            new BlockPos(7, 65, -6), new BlockPos(7, 65, -5), new BlockPos(8, 65, -5), new BlockPos(8, 65, -4),
            new BlockPos(8, 65, -3), new BlockPos(9, 65, -3));
    private final String c1;
    private int ymod;

    public SumoFences() {
        super("Sumo Fences", ModuleCategory.minigames);
        this.c1 = "Mode: Sumo Duel";
        this.registerSetting(a = new DescriptionSetting("Fences for Hypixel sumo."));
        this.registerSetting(fenceHeight = new SliderSetting("Fence height", 4.0D, 1.0D, 16.0D, 1.0D));
        this.registerSetting(sumoBlockType = new ComboSetting("Block Type:", SumoBlockType.FENCE));
    }

    public void onEnable() {
        (this.timer = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 500L);
    }

    public void onDisable() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }

        for (BlockPos p : fencePositions) {
            for (int i = 0; (double) i < fenceHeight.getInput(); ++i) {
                BlockPos p2 = new BlockPos(p.getX(), p.getY() + i, p.getZ());
                if (mc.theWorld.getBlockState(p2).getBlock() == sumoBlockType.getMode().blockType) {
                    mc.theWorld.setBlockState(p2, Blocks.air.getDefaultState());
                }
            }
        }

    }

    @SubscribeEvent
    public void onForgeEvent(MouseEvent e) {
        if (!this.enabled) return;
        if (e.getButtonState() && (e.getButton() == 0 || e.getButton() == 1) && Utils.Player.isPlayerInGame() && this.shouldPlaceFences()) {
            MovingObjectPosition mop = mc.objectMouseOver;
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                int x = mop.getBlockPos().getX();
                int z = mop.getBlockPos().getZ();

                for (BlockPos pos : fencePositions) {
                    if (pos.getX() == x && pos.getZ() == z) {
                        e.setCancelled(true);
                        if (e.getButton() == 0) {
                            Utils.Player.swing();
                        }

                        Mouse.poll();
                        break;
                    }
                }
            }
        }
    }


    public TimerTask t() {
        return new TimerTask() {
            public void run() {
                if (SumoFences.this.shouldPlaceFences()) {

                    for (BlockPos p : fencePositions) {
                        for (int i = 0; (double) i < fenceHeight.getInput(); ++i) {
                            BlockPos p2 = new BlockPos(p.getX(), p.getY() + i + ymod, p.getZ());
                            if (Module.mc.theWorld.getBlockState(p2).getBlock() == Blocks.air) {
                                Module.mc.theWorld.setBlockState(p2, sumoBlockType.getMode().blockType.getDefaultState());
                            }
                        }
                    }

                }
            }
        };
    }

    private boolean shouldPlaceFences() {
        if (Utils.Client.isHyp()) {

            for (String l : Utils.Client.getPlayersFromScoreboard()) {
                String s = Utils.Java.str(l);
                if (s.startsWith("Map:")) {
                    if (this.map.contains(s.substring(5))) {
                        ymod = s.contains("Fort Royale") ? 7 : 0;
                        return true;
                    }
                } else if (s.equals(this.c1)) {
                    return true;
                }
            }
        }

        return false;
    }


    public enum SumoBlockType {
        LEAVES(Blocks.leaves),
        GLASS(Blocks.glass),
        BARRIER(Blocks.barrier),
        FENCE(Blocks.oak_fence);

        public Block blockType;
        SumoBlockType(Block blockType) {
            this.blockType = blockType;
        }
    }
}
