package org.dreamcore.client.feature.impl.visual;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.dreamcore.client.cmd.impl.XrayCommand;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.event.events.impl.render.EventRenderBlock;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.world.BlockHelper;
import org.dreamcore.client.helpers.world.EntityHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XRay extends Feature {

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
        super("XRay", "Иксрей, позволяющий обойти анти-иксрей на серверах", Type.Misc);
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

    private boolean isEnabledOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;
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
        for (Integer integer : XrayCommand.blockIDS) {
            if (integer != 0) {
                check7 = integer;
            }
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return BlockHelper.getAllInBox(min, max);
    }

    @Override
    public void onEnable() {
        if (brutForce.getBoolValue()) {
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
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String allDone = done == all ? "" + "Done: " + all : "" + done + " / " + all;
        if (brutForce.getBoolValue()) {
            setSuffix(allDone);
            for (int i = 0; i < checkSpeed.getNumberValue(); i++) {
                if (toCheck.size() < 1)
                    return;
                BlockPos pos = toCheck.remove(0);
                done++;
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (brutForce.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange) e.getPacket();
                if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    if (!mc.world.isAirBlock(p.getBlockPosition())) {
                        ores.add(p.getBlockPosition());
                    }
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();
                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                        if (!mc.world.isAirBlock(dat.getPos())) {
                            ores.add(dat.getPos());
                        }
                    }
                }
            }
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
    public void onRender3D(EventRender3D event) {
        if (brutForce.getBoolValue()) {
            for (BlockPos pos : ores) {
                IBlockState state = mc.world.getBlockState(pos);
                Block block = state.getBlock();

                if (toCheck.size() <= 0 || Block.getIdFromBlock(block) == 0)
                    continue;

                switch (Block.getIdFromBlock(block)) {
                    case 56:
                        if (diamond.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 255, 255);
                        break;
                    case 14:
                        if (gold.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 255, 215, 0);
                        break;
                    case 15:
                        if (iron.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 213, 213, 213);
                        break;
                    case 129:
                        if (emerald.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 255, 77);
                        break;
                    case 73:
                        if (redstone.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 255, 0, 0);
                        break;
                    case 16:
                        if (coal.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 0, 0);
                        break;
                    case 21:
                        if (lapis.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 38, 97, 156);
                        break;

                }
                for (Integer integer : XrayCommand.blockIDS) {
                    if (Block.getIdFromBlock(block) == integer) {
                        RenderHelper.blockEspFrame(pos, ClientHelper.getClientColor().getRed() / 255F, ClientHelper.getClientColor().getGreen() / 255F, ClientHelper.getClientColor().getBlue() / 255F);
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

                if (EntityHelper.getDistance(mc.player.posX, mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > renderDist.getNumberValue()) {
                    blocks.remove(neededBlock);
                    continue;
                }

                switch (Block.getIdFromBlock(block)) {
                    case 56:
                        if (diamond.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 255, 255);
                        break;
                    case 14:
                        if (gold.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 255, 215, 0);
                        break;
                    case 15:
                        if (iron.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 213, 213, 213);
                        break;
                    case 129:
                        if (emerald.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 255, 77);
                        break;
                    case 73:
                        if (redstone.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 255, 0, 0);
                        break;
                    case 16:
                        if (coal.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 0, 0, 0);
                        break;
                    case 21:
                        if (lapis.getBoolValue())
                            RenderHelper.blockEspFrame(pos, 38, 97, 156);
                        break;

                }
                for (Integer integer : XrayCommand.blockIDS) {
                    if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == integer) {
                        RenderHelper.blockEspFrame(pos, ClientHelper.getClientColor().getRed() / 255F, ClientHelper.getClientColor().getGreen() / 255F, ClientHelper.getClientColor().getBlue() / 255F);
                    }
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
        int check7 = 0;

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
        for (Integer integer : XrayCommand.blockIDS) {
            if (integer != 0) {
                check7 = integer;
            }
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7;
    }
}
