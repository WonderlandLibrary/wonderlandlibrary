/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.debug;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererWater
implements DebugRenderer.IDebugRenderer {
    private final Minecraft minecraft;
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;

    public DebugRendererWater(Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }

    @Override
    public void render(float p_190060_1_, long p_190060_2_) {
        this.player = this.minecraft.player;
        this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)p_190060_1_;
        this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)p_190060_1_;
        this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)p_190060_1_;
        BlockPos blockpos = this.minecraft.player.getPosition();
        World world = this.minecraft.player.world;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(6.0f);
        for (BlockPos blockpos1 : BlockPos.getAllInBox(blockpos.add(-10, -10, -10), blockpos.add(10, 10, 10))) {
            IBlockState iblockstate = world.getBlockState(blockpos1);
            if (iblockstate.getBlock() != Blocks.WATER && iblockstate.getBlock() != Blocks.FLOWING_WATER) continue;
            double d0 = BlockLiquid.func_190972_g(iblockstate, world, blockpos1);
            RenderGlobal.renderFilledBox(new AxisAlignedBB((float)blockpos1.getX() + 0.01f, (float)blockpos1.getY() + 0.01f, (float)blockpos1.getZ() + 0.01f, (float)blockpos1.getX() + 0.99f, d0, (float)blockpos1.getZ() + 0.99f).offset(-this.xo, -this.yo, -this.zo), 1.0f, 1.0f, 1.0f, 0.2f);
        }
        for (BlockPos blockpos2 : BlockPos.getAllInBox(blockpos.add(-10, -10, -10), blockpos.add(10, 10, 10))) {
            IBlockState iblockstate1 = world.getBlockState(blockpos2);
            if (iblockstate1.getBlock() != Blocks.WATER && iblockstate1.getBlock() != Blocks.FLOWING_WATER) continue;
            Integer integer = iblockstate1.getValue(BlockLiquid.LEVEL);
            double d1 = integer > 7 ? 0.9 : 1.0 - 0.11 * (double)integer.intValue();
            String s = iblockstate1.getBlock() == Blocks.FLOWING_WATER ? "f" : "s";
            DebugRenderer.renderDebugText(s + " " + integer, (double)blockpos2.getX() + 0.5, (double)blockpos2.getY() + d1, (double)blockpos2.getZ() + 0.5, p_190060_1_, -16777216);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}

