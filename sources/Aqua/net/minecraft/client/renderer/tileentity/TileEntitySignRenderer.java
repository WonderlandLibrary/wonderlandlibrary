package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();
    private static double textRenderDistanceSq = 4096.0;

    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage) {
        Block block = te.getBlockType();
        GlStateManager.pushMatrix();
        float f = 0.6666667f;
        if (block == Blocks.standing_sign) {
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y + 0.75f * f), (float)((float)z + 0.5f));
            float f1 = (float)(te.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate((float)(-f1), (float)0.0f, (float)1.0f, (float)0.0f);
            this.model.signStick.showModel = true;
        } else {
            int k = te.getBlockMetadata();
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
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y + 0.75f * f), (float)((float)z + 0.5f));
            GlStateManager.rotate((float)(-f2), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-0.3125f, (float)-0.4375f);
            this.model.signStick.showModel = false;
        }
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)4.0f, (float)2.0f, (float)1.0f);
            GlStateManager.translate((float)0.0625f, (float)0.0625f, (float)0.0625f);
            GlStateManager.matrixMode((int)5888);
        } else {
            this.bindTexture(SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)f, (float)(-f), (float)(-f));
        this.model.renderSign();
        GlStateManager.popMatrix();
        if (TileEntitySignRenderer.isRenderText(te)) {
            FontRenderer fontrenderer = this.getFontRenderer();
            float f3 = 0.015625f * f;
            GlStateManager.translate((float)0.0f, (float)(0.5f * f), (float)(0.07f * f));
            GlStateManager.scale((float)f3, (float)(-f3), (float)f3);
            GL11.glNormal3f((float)0.0f, (float)0.0f, (float)(-1.0f * f3));
            GlStateManager.depthMask((boolean)false);
            int i = 0;
            if (Config.isCustomColors()) {
                i = CustomColors.getSignTextColor((int)i);
            }
            if (destroyStage < 0) {
                for (int j = 0; j < te.signText.length; ++j) {
                    String s;
                    if (te.signText[j] == null) continue;
                    IChatComponent ichatcomponent = te.signText[j];
                    List list = GuiUtilRenderComponents.splitText((IChatComponent)ichatcomponent, (int)90, (FontRenderer)fontrenderer, (boolean)false, (boolean)true);
                    String string = s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";
                    if (j == te.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                        continue;
                    }
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                }
            }
        }
        GlStateManager.depthMask((boolean)true);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode((int)5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
        }
    }

    private static boolean isRenderText(TileEntitySign p_isRenderText_0_) {
        if (Shaders.isShadowPass) {
            return false;
        }
        if (Config.getMinecraft().currentScreen instanceof GuiEditSign) {
            return true;
        }
        if (!Config.zoomMode && p_isRenderText_0_.lineBeingEdited < 0) {
            Entity entity = Config.getMinecraft().getRenderViewEntity();
            double d0 = p_isRenderText_0_.getDistanceSq(entity.posX, entity.posY, entity.posZ);
            if (d0 > textRenderDistanceSq) {
                return false;
            }
        }
        return true;
    }

    public static void updateTextRenderDistance() {
        Minecraft minecraft = Config.getMinecraft();
        double d0 = Config.limit((float)minecraft.gameSettings.fovSetting, (float)1.0f, (float)120.0f);
        double d1 = Math.max((double)(1.5 * (double)minecraft.displayHeight / d0), (double)16.0);
        textRenderDistanceSq = d1 * d1;
    }
}
