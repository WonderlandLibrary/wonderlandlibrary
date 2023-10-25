package fun.expensive.client.feature.impl.visual;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.event.events.impl.player.BlockHelper;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.event.events.impl.render.EventRenderBlock;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XRay extends Feature {
    public static TimerHelper timerHelper = new TimerHelper();
    public static int done;
    public static int all;
    public static BooleanSetting brutForce;
    public static BooleanSetting diamond;
    public static BooleanSetting gold;
    public static BooleanSetting iron;
    public static BooleanSetting emerald;
    public static BooleanSetting redstone;
    public static BooleanSetting lapis;
    public static BooleanSetting coal;
    private final NumberSetting checkSpeed;
    private final NumberSetting renderDist;
    private final NumberSetting rxz;
    private final NumberSetting ry;
    private final ArrayList<BlockPos> ores = new ArrayList<>();
    private final ArrayList<BlockPos> toCheck = new ArrayList<>();
    private final List<Vec3i> blocks = new CopyOnWriteArrayList<>();

    public XRay() {
        super("XRay", "»ксрей, позвол€ющий обойти анти-иксрей на серверах", FeatureCategory.Misc);
        brutForce = new BooleanSetting("BrutForce", false, () -> true);
        renderDist = new NumberSetting("Render Distance", 35, 15, 150, 5, () -> !brutForce.getBoolValue());
        diamond = new BooleanSetting("Diamond", true, () -> true);
        gold = new BooleanSetting("Gold", false, () -> true);
        iron = new BooleanSetting("Iron", false, () -> true);
        emerald = new BooleanSetting("Emerald", false, () -> true);
        redstone = new BooleanSetting("Redstone", false, () -> true);
        lapis = new BooleanSetting("Lapis", false, () -> true);
        coal = new BooleanSetting("Coal", false, () -> true);
        checkSpeed = new NumberSetting("CheckSpeed", 4, 1, 10, 1, brutForce::getBoolValue);
        rxz = new NumberSetting("Radius XZ", 20, 5, 200, 1, brutForce::getBoolValue);
        ry = new NumberSetting("Radius Y", 6, 2, 50, 1, brutForce::getBoolValue);
        addSettings(renderDist, brutForce, checkSpeed, rxz, ry, diamond, gold, iron, emerald, redstone, lapis, coal);
    }


    @Override
    public void onEnable() {
        if (brutForce.getBoolValue()) {
            ores.clear();
            toCheck.clear();

            int radXZ = (int) rxz.getNumberValue();
            int radY = (int) ry.getNumberValue();

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

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (brutForce.getBoolValue()) {
            String allDone = done == all ? "" + "Done: " + all : "" + done + " / " + all;
            this.setSuffix(allDone);
            if (timerHelper.hasReached(checkSpeed.getNumberValue())) {
                if (toCheck.size() >= 1) {
                    BlockPos blockPos = toCheck.get(0);
                    mc.playerController.clickBlock(blockPos, EnumFacing.UP);
                    toCheck.remove(0);
                    done++;
                }
            }
            timerHelper.reset();

        }
    }

    @EventTarget
    public void onRenderBlock(EventRenderBlock event) {
        BlockPos pos = event.getPos();
        IBlockState blockState = event.getState();
        if (isEnabledOre(Block.getIdFromBlock(blockState.getBlock()))) {
            Vec3i vec3i = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            blocks.add(vec3i);
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (brutForce.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange) e.getPacket();

                if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    ores.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();

                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                        ores.add(dat.getPos());

                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D e) {
        if (brutForce.getBoolValue()) {
            for (BlockPos pos : ores) {
                IBlockState state = mc.world.getBlockState(pos);
                Block mat = state.getBlock();
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && diamond.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 56) {
                        RenderUtils.blockEspFrame(pos, 0, 255, 255);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && gold.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 14) {
                        RenderUtils.blockEspFrame(pos, 255, 215, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && iron.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 15) {
                        RenderUtils.blockEspFrame(pos, 213, 213, 213);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && emerald.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 129) {
                        RenderUtils.blockEspFrame(pos, 0, 255, 77);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && redstone.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 73) {
                        RenderUtils.blockEspFrame(pos, 255, 0, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && coal.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 16) {
                        RenderUtils.blockEspFrame(pos, 0, 0, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21 && lapis.getBoolValue()) {
                    if (Block.getIdFromBlock(mat) == 21) {
                        RenderUtils.blockEspFrame(pos, 38, 97, 156);
                    }
                }
            }
        } else {
            for (Vec3i neededBlock : blocks) {
                BlockPos pos = new BlockPos(neededBlock);
                IBlockState state = mc.world.getBlockState(pos);
                Block stateBlock = state.getBlock();

                Block block = mc.world.getBlockState(pos).getBlock();

                if (block instanceof BlockAir || Block.getIdFromBlock(block) == 0)
                    continue;

                if (RotationHelper.getDistance(mc.player.posX, mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > renderDist.getNumberValue()) {
                    blocks.remove(neededBlock);
                    continue;
                }

                switch (Block.getIdFromBlock(block)) {
                    case 56:
                        if (diamond.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 0, 255, 255);
                        break;
                    case 14:
                        if (gold.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 255, 215, 0);
                        break;
                    case 15:
                        if (iron.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 213, 213, 213);
                        break;
                    case 129:
                        if (emerald.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 0, 255, 77);
                        break;
                    case 73:
                        if (redstone.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 255, 0, 0);
                        break;
                    case 16:
                        if (coal.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 0, 0, 0);
                        break;
                    case 21:
                        if (lapis.getBoolValue())
                            RenderUtils.blockEspFrame(pos, 38, 97, 156);
                        break;

                }
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
        if (diamond.getBoolValue() && id != 0) {
            check = 56;
        }
        if (gold.getBoolValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getBoolValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getBoolValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getBoolValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getBoolValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getBoolValue() && id != 0) {
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
        if (diamond.getBoolValue() && id != 0) {
            check = 56;
        }
        if (gold.getBoolValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getBoolValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getBoolValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getBoolValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getBoolValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getBoolValue() && id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return BlockHelper.getAllInBox(min, max);
    }

}
