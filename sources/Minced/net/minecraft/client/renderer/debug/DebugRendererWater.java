// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class DebugRendererWater implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;
    
    public DebugRendererWater(final Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        final Minecraft minecraft = this.minecraft;
        this.player = Minecraft.player;
        this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * partialTicks;
        this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * partialTicks;
        this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * partialTicks;
        final Minecraft minecraft2 = this.minecraft;
        final BlockPos blockpos = Minecraft.player.getPosition();
        final Minecraft minecraft3 = this.minecraft;
        final World world = Minecraft.player.world;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(6.0f);
        for (final BlockPos blockpos2 : BlockPos.getAllInBox(blockpos.add(-10, -10, -10), blockpos.add(10, 10, 10))) {
            final IBlockState iblockstate = world.getBlockState(blockpos2);
            if (iblockstate.getBlock() == Blocks.WATER || iblockstate.getBlock() == Blocks.FLOWING_WATER) {
                final double d0 = BlockLiquid.getLiquidHeight(iblockstate, world, blockpos2);
                RenderGlobal.renderFilledBox(new AxisAlignedBB(blockpos2.getX() + 0.01f, blockpos2.getY() + 0.01f, blockpos2.getZ() + 0.01f, blockpos2.getX() + 0.99f, d0, blockpos2.getZ() + 0.99f).offset(-this.xo, -this.yo, -this.zo), 1.0f, 1.0f, 1.0f, 0.2f);
            }
        }
        for (final BlockPos blockpos3 : BlockPos.getAllInBox(blockpos.add(-10, -10, -10), blockpos.add(10, 10, 10))) {
            final IBlockState iblockstate2 = world.getBlockState(blockpos3);
            if (iblockstate2.getBlock() == Blocks.WATER || iblockstate2.getBlock() == Blocks.FLOWING_WATER) {
                final Integer integer = iblockstate2.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
                final double d2 = (integer > 7) ? 0.9 : (1.0 - 0.11 * integer);
                final String s = (iblockstate2.getBlock() == Blocks.FLOWING_WATER) ? "f" : "s";
                DebugRenderer.renderDebugText(s + " " + integer, blockpos3.getX() + 0.5, blockpos3.getY() + d2, blockpos3.getZ() + 0.5, partialTicks, -16777216);
            }
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
