/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
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
    public void renderTileEntityAt(TileEntitySign tileEntitySign, double d, double d2, double d3, float f, int n) {
        float f2;
        Block block = tileEntitySign.getBlockType();
        GlStateManager.pushMatrix();
        float f3 = 0.6666667f;
        if (block == Blocks.standing_sign) {
            GlStateManager.translate((float)d + 0.5f, (float)d2 + 0.75f * f3, (float)d3 + 0.5f);
            float f4 = (float)(tileEntitySign.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate(-f4, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        } else {
            int n2 = tileEntitySign.getBlockMetadata();
            f2 = 0.0f;
            if (n2 == 2) {
                f2 = 180.0f;
            }
            if (n2 == 4) {
                f2 = 90.0f;
            }
            if (n2 == 5) {
                f2 = -90.0f;
            }
            GlStateManager.translate((float)d + 0.5f, (float)d2 + 0.75f * f3, (float)d3 + 0.5f);
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (n >= 0) {
            this.bindTexture(DESTROY_STAGES[n]);
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
        GlStateManager.scale(f3, -f3, -f3);
        this.model.renderSign();
        GlStateManager.popMatrix();
        FontRenderer fontRenderer = this.getFontRenderer();
        f2 = 0.015625f * f3;
        GlStateManager.translate(0.0f, 0.5f * f3, 0.07f * f3);
        GlStateManager.scale(f2, -f2, f2);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)(-1.0f * f2));
        GlStateManager.depthMask(false);
        int n3 = 0;
        if (n < 0) {
            int n4 = 0;
            while (n4 < tileEntitySign.signText.length) {
                if (tileEntitySign.signText[n4] != null) {
                    String string;
                    IChatComponent iChatComponent = tileEntitySign.signText[n4];
                    List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(iChatComponent, 90, fontRenderer, false, true);
                    String string2 = string = list != null && list.size() > 0 ? list.get(0).getFormattedText() : "";
                    if (n4 == tileEntitySign.lineBeingEdited) {
                        string = "> " + string + " <";
                        fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n4 * 10 - tileEntitySign.signText.length * 5, n3);
                    } else {
                        fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n4 * 10 - tileEntitySign.signText.length * 5, n3);
                    }
                }
                ++n4;
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (n >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

