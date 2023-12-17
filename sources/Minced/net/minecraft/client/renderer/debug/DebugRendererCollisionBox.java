// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private EntityPlayer player;
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;
    
    public DebugRendererCollisionBox(final Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        final Minecraft minecraft = this.minecraft;
        this.player = Minecraft.player;
        this.renderPosX = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * partialTicks;
        this.renderPosY = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * partialTicks;
        this.renderPosZ = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * partialTicks;
        final Minecraft minecraft2 = this.minecraft;
        final World world = Minecraft.player.world;
        final List<AxisAlignedBB> list = world.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().grow(6.0));
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        for (final AxisAlignedBB axisalignedbb : list) {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.grow(0.002).offset(-this.renderPosX, -this.renderPosY, -this.renderPosZ), 1.0f, 1.0f, 1.0f, 1.0f);
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
