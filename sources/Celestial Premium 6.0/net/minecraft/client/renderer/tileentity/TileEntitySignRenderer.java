/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import optifine.Config;
import optifine.CustomColors;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.NoRender;

public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();

    @Override
    public void func_192841_a(TileEntitySign p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.sign.getCurrentValue()) {
            return;
        }
        Block block = p_192841_1_.getBlockType();
        GlStateManager.pushMatrix();
        float f = 0.6666667f;
        if (block == Blocks.STANDING_SIGN) {
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.5f, (float)p_192841_6_ + 0.5f);
            float f1 = (float)(p_192841_1_.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate(-f1, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        } else {
            int k = p_192841_1_.getBlockMetadata();
            float f2 = 0.0f;
            if (k == 2) {
                f2 = 180.0f;
            }
            if (k == 4) {
                f2 = 90.0f;
            }
            if (k == 5) {
                f2 = -90.0f;
            }
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.5f, (float)p_192841_6_ + 0.5f);
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (p_192841_9_ >= 0) {
            this.bindTexture(DESTROY_STAGES[p_192841_9_]);
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
        GlStateManager.scale(0.6666667f, -0.6666667f, -0.6666667f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        FontRenderer fontrenderer = this.getFontRenderer();
        float f3 = 0.010416667f;
        GlStateManager.translate(0.0f, 0.33333334f, 0.046666667f);
        GlStateManager.scale(0.010416667f, -0.010416667f, 0.010416667f);
        GlStateManager.glNormal3f(0.0f, 0.0f, -0.010416667f);
        GlStateManager.depthMask(false);
        int i = 0;
        if (Config.isCustomColors()) {
            i = CustomColors.getSignTextColor(i);
        }
        if (p_192841_9_ < 0) {
            for (int j = 0; j < p_192841_1_.signText.length; ++j) {
                String s;
                if (p_192841_1_.signText[j] == null) continue;
                ITextComponent itextcomponent = p_192841_1_.signText[j];
                List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer, false, true);
                String string = s = list != null && !list.isEmpty() ? list.get(0).getFormattedText() : "";
                if (j == p_192841_1_.lineBeingEdited) {
                    s = "> " + s + " <";
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - p_192841_1_.signText.length * 5, i);
                    continue;
                }
                fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - p_192841_1_.signText.length * 5, i);
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (p_192841_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

