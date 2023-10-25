/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.celestial.client.cmd.impl.XrayCommand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.event.events.impl.render.EventRenderBlock;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.world.BlockHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class XRay
extends Feature {
    public static int done;
    public static int all;
    public static BooleanSetting bypass;
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
    public static ArrayList<BlockPos> ores;
    private final ArrayList<BlockPos> toCheck = new ArrayList();
    private final List<Vec3i> blocks = new CopyOnWriteArrayList<Vec3i>();

    public XRay() {
        super("XRay", "\u0418\u043a\u0441\u0440\u0435\u0439, \u043f\u043e\u0437\u0432\u043e\u043b\u044f\u044e\u0449\u0438\u0439 \u043e\u0431\u043e\u0439\u0442\u0438 \u0430\u043d\u0442\u0438-\u0438\u043a\u0441\u0440\u0435\u0439 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445", Type.Misc);
        bypass = new BooleanSetting("Bypass", false, () -> true);
        this.renderDist = new NumberSetting("Render Distance", 35.0f, 15.0f, 150.0f, 5.0f, () -> !bypass.getCurrentValue());
        diamond = new BooleanSetting("Diamond", Block.getBlockById(56), true, () -> true);
        gold = new BooleanSetting("Gold", Block.getBlockById(14), false, () -> true);
        iron = new BooleanSetting("Iron", Block.getBlockById(15), false, () -> true);
        emerald = new BooleanSetting("Emerald", Block.getBlockById(129), false, () -> true);
        redstone = new BooleanSetting("Redstone", Block.getBlockById(73), false, () -> true);
        lapis = new BooleanSetting("Lapis", Block.getBlockById(21), false, () -> true);
        coal = new BooleanSetting("Coal", Block.getBlockById(16), false, () -> true);
        this.checkSpeed = new NumberSetting("CheckSpeed", 4.0f, 1.0f, 10.0f, 1.0f, bypass::getCurrentValue);
        this.rxz = new NumberSetting("Radius XZ", 20.0f, 5.0f, 200.0f, 1.0f, bypass::getCurrentValue);
        this.ry = new NumberSetting("Radius Y", 6.0f, 2.0f, 50.0f, 1.0f, bypass::getCurrentValue);
        this.addSettings(this.renderDist, bypass, this.checkSpeed, this.rxz, this.ry, diamond, gold, iron, emerald, redstone, lapis, coal);
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
        if (diamond.getCurrentValue() && id != 0) {
            check = 56;
        }
        if (gold.getCurrentValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getCurrentValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getCurrentValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getCurrentValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getCurrentValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getCurrentValue() && id != 0) {
            check6 = 21;
        }
        for (Integer integer : XrayCommand.blockIDS) {
            if (integer == 0) continue;
            check7 = integer;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(XRay.mc.player.posX - (double)x, XRay.mc.player.posY - (double)y, XRay.mc.player.posZ - (double)z);
        BlockPos max = new BlockPos(XRay.mc.player.posX + (double)x, XRay.mc.player.posY + (double)y, XRay.mc.player.posZ + (double)z);
        return BlockHelper.getAllInBox(min, max);
    }

    @Override
    public void onEnable() {
        if (bypass.getCurrentValue()) {
            int radXZ = (int)this.rxz.getCurrentValue();
            int radY = (int)this.ry.getCurrentValue();
            ArrayList<BlockPos> blockPositions = this.getBlocks(radXZ, radY, radXZ);
            for (BlockPos pos : blockPositions) {
                IBlockState state = BlockHelper.getState(pos);
                if (!this.isCheckableOre(Block.getIdFromBlock(state.getBlock()))) continue;
                this.toCheck.add(pos);
            }
            all = this.toCheck.size();
            done = 0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        XRay.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String allDone;
        String string = allDone = done == all ? "Done: " + all : "" + done + " / " + all;
        if (bypass.getCurrentValue()) {
            this.setSuffix(allDone);
            int i = 0;
            while ((float)i < this.checkSpeed.getCurrentValue()) {
                if (this.toCheck.size() < 1) {
                    return;
                }
                BlockPos pos = this.toCheck.remove(0);
                ++done;
                XRay.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.SOUTH));
                ++i;
            }
        }
        if (!bypass.getCurrentValue() && XRay.mc.player.ticksExisted % 100 == 0) {
            XRay.mc.renderGlobal.loadRenderers();
            ChatHelper.addChatMessage("Reloading chunks...");
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (bypass.getCurrentValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
                if (this.isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock())) && !XRay.mc.world.isAirBlock(p.getBlockPosition())) {
                    ores.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (!this.isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock())) || XRay.mc.world.isAirBlock(dat.getPos())) continue;
                    ores.add(dat.getPos());
                }
            }
        }
    }

    @EventTarget
    public void onRenderBlock(EventRenderBlock event) {
        if (!bypass.getCurrentValue()) {
            BlockPos pos = event.getPos();
            IBlockState blockState = event.getState();
            if (this.isEnabledOre(Block.getIdFromBlock(blockState.getBlock()))) {
                Vec3i vec3i = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
                this.blocks.add(vec3i);
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (bypass.getCurrentValue()) {
            for (BlockPos pos : ores) {
                IBlockState state = BlockHelper.getState(pos);
                Block stateBlock = state.getBlock();
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 56 && diamond.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 56) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 14 && gold.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 14) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 15 && iron.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 15) {
                    RenderHelper.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 129 && emerald.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 129) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 73 && redstone.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 73) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 16 && coal.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 16) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 21 && lapis.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 21) {
                    RenderHelper.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
                for (Integer integer : XrayCommand.blockIDS) {
                    if (Block.getIdFromBlock(stateBlock) == 0 || Block.getIdFromBlock(stateBlock) != integer) continue;
                    RenderHelper.blockEspFrame(pos, (float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f);
                }
            }
        } else {
            for (Vec3i neededBlock : this.blocks) {
                BlockPos pos = new BlockPos(neededBlock);
                IBlockState state = BlockHelper.getState(pos);
                Block stateBlock = state.getBlock();
                Block block = XRay.mc.world.getBlockState(pos).getBlock();
                if (block instanceof BlockAir) continue;
                if (EntityHelper.getDistance(XRay.mc.player.posX, XRay.mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > (double)this.renderDist.getCurrentValue()) {
                    this.blocks.remove(neededBlock);
                    continue;
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 56 && diamond.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 56) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 14 && gold.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 14) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 15 && iron.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 15) {
                    RenderHelper.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 129 && emerald.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 129) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 73 && redstone.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 73) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 16 && coal.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 16) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 21 && lapis.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 21) {
                    RenderHelper.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
                for (Integer integer : XrayCommand.blockIDS) {
                    if (Block.getIdFromBlock(stateBlock) == 0 || Block.getIdFromBlock(stateBlock) != integer) continue;
                    RenderHelper.blockEspFrame(pos, (float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f);
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
        if (diamond.getCurrentValue() && id != 0) {
            check = 56;
        }
        if (gold.getCurrentValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getCurrentValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getCurrentValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getCurrentValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getCurrentValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getCurrentValue() && id != 0) {
            check6 = 21;
        }
        for (Integer integer : XrayCommand.blockIDS) {
            if (integer == 0) continue;
            check7 = integer;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7;
    }

    static {
        ores = new ArrayList();
    }
}

