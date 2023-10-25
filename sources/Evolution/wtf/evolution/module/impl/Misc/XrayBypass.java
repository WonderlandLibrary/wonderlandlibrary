package wtf.evolution.module.impl.Misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

import java.awt.Color;
import java.util.ArrayList;

@ModuleInfo(name = "XrayBypass", type = Category.Misc)
public class XrayBypass extends Module {


    public SliderSetting range = new SliderSetting("Radius", 20, 0, 100, 1).call(this);
    public SliderSetting height = new SliderSetting("Height", 20, 0, 100, 1).call(this);
    public SliderSetting delay = new SliderSetting("Speed", 7, 0, 10, 1).call(this);

    ArrayList<BlockPos> ores = new ArrayList<>();
    ArrayList<BlockPos> toCheck = new ArrayList<>();
    public static int done;
    public static int all;

    @Override
    public void onEnable() {
     if (mc.player != null) {
         ores.clear();
         toCheck.clear();

         int radXZ = (int) range.get();
         int radY = (int) height.get();
         ArrayList<BlockPos> blockPositions = getBlocks(radXZ, radY, radXZ);

         for (BlockPos pos : blockPositions) {
             IBlockState state = mc.world.getBlockState(pos);
             if (isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
                 toCheck.add(pos);
             }
         }

         all = toCheck.size();
         done = 0;
         super.onEnable();
     }
    }

    @Override
    public void onDisable() {
        mc.getMinecraft().renderGlobal.loadRenderers();
        super.onDisable();
    }

    public BlockPos clicked;

    @EventTarget
    public void onUpdate(EventMotion e) {
        setSuffix(done + "/" + all);
        if (toCheck.size() > 0) {
            double spd = delay.get();
            for (int i = 0; i < (int) spd; i++) {
                if (toCheck.size() < 1)
                    return;
                BlockPos pos = toCheck.remove(0);
                clicked = pos;
                done++;
                mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            }
        }
        if(mc.player.ticksExisted % 10 == 0) {
            for (BlockPos b : ores) {
                mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, b, EnumFacing.UP));
            }
        }
    }

    private boolean isCheckableOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (id != 0) {
            check = 56;
        }
        if (id != 0) {
            check1 = 14;
        }
        if (id != 0) {
            check2 = 15;
        }
        if (id != 0) {
            check3 = 129;
        }
        if (id != 0) {
            check4 = 73;
        }
        if (id != 0) {
            check5 = 16;
        }
        if (id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private boolean isEnabledOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (id != 0) {
            check = 56;
        }
        if (id != 0) {
            check1 = 14;
        }
        if (id != 0) {
            check2 = 15;
        }
        if (id != 0) {
            check3 = 129;
        }
        if (id != 0) {
            check4 = 73;
        }
        if (id != 0) {
            check5 = 16;
        }
        if (id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }



    @EventTarget
    public void onReceivePacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange p = (SPacketBlockChange) e.getPacket();

            if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock())) && !ores.contains(p.getBlockPosition())) {
                    ores.add(p.getBlockPosition());
                //Main.notify.call("Xray", "Found ore at " + p.getBlockPosition().getX() + " " + p.getBlockPosition().getY() + " " + p.getBlockPosition().getZ(), NotificationType.INFO);
            }
        } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
            SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();

            for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                if (isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock())) && !ores.contains(dat.getPos())) {
                        ores.add(dat.getPos());
                    //Main.notify.call("Xray", "Found ore at " + dat.getPos().getX() + " " + dat.getPos().getY() + " " + dat.getPos().getZ(), NotificationType.INFO);

                }
            }
        }
    }

    @EventTarget
    public void onRender(EventRender event) {
        if (toCheck.size() > 0)
            RenderUtil.blockEsp(clicked, new Color(72, 198, 255, 255));
        for (BlockPos pos : ores) {
            IBlockState state = mc.world.getBlockState(pos);

            Block mat = state.getBlock();
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56) {
                    if (Block.getIdFromBlock(mat) == 56) {

                        RenderUtil.blockEsp(pos, new Color(0, 255, 255, 50));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14) {
                    if (Block.getIdFromBlock(mat) == 14) {
                        RenderUtil.blockEsp(pos, new Color(255, 215, 0, 100));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15) {
                    if (Block.getIdFromBlock(mat) == 15) {
                        RenderUtil.blockEsp(pos, new Color(213, 213, 213, 100));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129) {
                    if (Block.getIdFromBlock(mat) == 129) {
                        RenderUtil.blockEsp(pos, new Color(0, 255, 77, 100));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73) {
                    if (Block.getIdFromBlock(mat) == 73) {
                        RenderUtil.blockEsp(pos, new Color(255, 0, 0, 100));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16) {
                    if (Block.getIdFromBlock(mat) == 16) {
                        RenderUtil.blockEsp(pos, new Color(0, 0, 0, 100));
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21) {
                    if (Block.getIdFromBlock(mat) == 21) {
                        RenderUtil.blockEsp(pos, new Color(38, 97, 156, 100));
                    }
                }
        }
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return getAllInBox(min, max);
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()),
                Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()),
                Math.max(from.getZ(), to.getZ()));

        for (int x = min.getX(); x <= max.getX(); x++)
            for (int y = min.getY(); y <= max.getY(); y++)
                for (int z = min.getZ(); z <= max.getZ(); z++)
                    blocks.add(new BlockPos(x, y, z));

        return blocks;
    }


}
