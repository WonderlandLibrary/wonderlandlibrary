// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.util.ResourceLocation;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147513_b;
    private final ModelSign model;
    private static final String __OBFID = "CL_00000970";
    
    public TileEntitySignRenderer() {
        this.model = new ModelSign();
    }
    
    public void func_180541_a(final TileEntitySign p_180541_1_, final double p_180541_2_, final double p_180541_4_, final double p_180541_6_, final float p_180541_8_, final int p_180541_9_) {
        final Block var10 = p_180541_1_.getBlockType();
        GlStateManager.pushMatrix();
        final float var11 = 0.6666667f;
        if (var10 == Blocks.standing_sign) {
            GlStateManager.translate((float)p_180541_2_ + 0.5f, (float)p_180541_4_ + 0.75f * var11, (float)p_180541_6_ + 0.5f);
            final float var12 = p_180541_1_.getBlockMetadata() * 360 / 16.0f;
            GlStateManager.rotate(-var12, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        }
        else {
            final int var13 = p_180541_1_.getBlockMetadata();
            float var14 = 0.0f;
            if (var13 == 2) {
                var14 = 180.0f;
            }
            if (var13 == 4) {
                var14 = 90.0f;
            }
            if (var13 == 5) {
                var14 = -90.0f;
            }
            GlStateManager.translate((float)p_180541_2_ + 0.5f, (float)p_180541_4_ + 0.75f * var11, (float)p_180541_6_ + 0.5f);
            GlStateManager.rotate(-var14, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (p_180541_9_ >= 0) {
            this.bindTexture(TileEntitySignRenderer.DESTROY_STAGES[p_180541_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntitySignRenderer.field_147513_b);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(var11, -var11, -var11);
        this.model.renderSign();
        GlStateManager.popMatrix();
        final FontRenderer var15 = this.getFontRenderer();
        float var14 = 0.015625f * var11;
        GlStateManager.translate(0.0f, 0.5f * var11, 0.07f * var11);
        GlStateManager.scale(var14, -var14, var14);
        GL11.glNormal3f(0.0f, 0.0f, -1.0f * var14);
        GlStateManager.depthMask(false);
        final byte var16 = 0;
        if (p_180541_9_ < 0) {
            for (int var17 = 0; var17 < p_180541_1_.signText.length; ++var17) {
                if (p_180541_1_.signText[var17] != null) {
                    final IChatComponent var18 = p_180541_1_.signText[var17];
                    final List var19 = GuiUtilRenderComponents.func_178908_a(var18, 90, var15, false, true);
                    String var20 = (var19 != null && var19.size() > 0) ? var19.get(0).getFormattedText() : "";
                    if (var17 == p_180541_1_.lineBeingEdited) {
                        var20 = "> " + var20 + " <";
                        var15.drawString(var20, -var15.getStringWidth(var20) / 2, var17 * 10 - p_180541_1_.signText.length * 5, var16);
                    }
                    else {
                        var15.drawString(var20, -var15.getStringWidth(var20) / 2, var17 * 10 - p_180541_1_.signText.length * 5, var16);
                    }
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (p_180541_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180541_a((TileEntitySign)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        field_147513_b = new ResourceLocation("textures/entity/sign.png");
    }
}
