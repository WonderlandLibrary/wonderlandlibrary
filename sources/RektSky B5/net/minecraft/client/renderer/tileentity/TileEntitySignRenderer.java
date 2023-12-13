/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();

    @Override
    public void renderTileEntityAt(TileEntitySign te, double x2, double y2, double z2, float partialTicks, int destroyStage) {
        Block block = te.getBlockType();
        GlStateManager.pushMatrix();
        float f2 = 0.6666667f;
        if (block == Blocks.standing_sign) {
            GlStateManager.translate((float)x2 + 0.5f, (float)y2 + 0.75f * f2, (float)z2 + 0.5f);
            float f1 = (float)(te.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate(-f1, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        } else {
            int k2 = te.getBlockMetadata();
            float f22 = 0.0f;
            if (k2 == 2) {
                f22 = 180.0f;
            }
            if (k2 == 4) {
                f22 = 90.0f;
            }
            if (k2 == 5) {
                f22 = -90.0f;
            }
            GlStateManager.translate((float)x2 + 0.5f, (float)y2 + 0.75f * f2, (float)z2 + 0.5f);
            GlStateManager.rotate(-f22, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(f2, -f2, -f2);
        this.model.renderSign();
        GlStateManager.popMatrix();
        FontRenderer fontrenderer = this.getFontRenderer();
        float f3 = 0.015625f * f2;
        GlStateManager.translate(0.0f, 0.5f * f2, 0.07f * f2);
        GlStateManager.scale(f3, -f3, f3);
        GL11.glNormal3f(0.0f, 0.0f, -1.0f * f3);
        GlStateManager.depthMask(false);
        int i2 = 0;
        if (destroyStage < 0) {
            for (int j2 = 0; j2 < te.signText.length; ++j2) {
                String s2;
                if (te.signText[j2] == null) continue;
                IChatComponent ichatcomponent = te.signText[j2];
                List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
                String string = s2 = list != null && list.size() > 0 ? list.get(0).getFormattedText() : "";
                if (j2 == te.lineBeingEdited) {
                    s2 = "> " + s2 + " <";
                    fontrenderer.drawString(s2, -fontrenderer.getStringWidth(s2) / 2, j2 * 10 - te.signText.length * 5, i2);
                    continue;
                }
                fontrenderer.drawString(s2, -fontrenderer.getStringWidth(s2) / 2, j2 * 10 - te.signText.length * 5, i2);
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

