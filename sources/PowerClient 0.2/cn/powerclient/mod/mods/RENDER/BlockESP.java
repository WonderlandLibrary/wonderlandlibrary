/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender;
import me.AveReborn.events.EventRenderBlock;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;

public class BlockESP
extends Mod {
    private static ArrayList<Integer> blockIds = new ArrayList();
    private ArrayList<BlockPos> toRender = new ArrayList();
    public Value<Double> limit = new Value<Double>("BlockESP_BlockLimit", 250.0, 10.0, 1000.0, 1.0);

    public BlockESP() {
        super("BlockESP", Category.RENDER);
    }

    @Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
        this.toRender.clear();
        super.onEnable();
    }

    @EventTarget
    public void onRenderBlock(EventRenderBlock event) {
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if ((double)this.toRender.size() < (double)this.limit.getValueState().intValue() && !this.toRender.contains(pos) && blockIds.contains(new Integer(Block.getIdFromBlock(event.getBlock())))) {
            this.toRender.add(pos);
        }
        int i2 = 0;
        while (i2 < this.toRender.size()) {
            BlockPos pos_1 = this.toRender.get(i2);
            int id2 = Block.getIdFromBlock(this.mc.theWorld.getBlockState(pos_1).getBlock());
            if (!blockIds.contains(id2)) {
                this.toRender.remove(i2);
            }
            ++i2;
        }
    }

    @EventTarget
    public void onRender(EventRender event) {
        for (BlockPos pos : this.toRender) {
            this.renderBlock(pos);
        }
    }

    private void renderBlock(BlockPos pos) {
        this.mc.getRenderManager();
        this.mc.getRenderManager();
        double x2 = (double)pos.getX() - RenderManager.renderPosX;
        this.mc.getRenderManager();
        this.mc.getRenderManager();
        double y2 = (double)pos.getY() - RenderManager.renderPosY;
        this.mc.getRenderManager();
        this.mc.getRenderManager();
        double z2 = (double)pos.getZ() - RenderManager.renderPosZ;
        RenderUtil.drawSolidBlockESP(x2, y2, z2, 0.0f, 0.5f, 1.0f, 0.25f);
    }

    public static ArrayList<Integer> getBlockIds() {
        return blockIds;
    }
}

