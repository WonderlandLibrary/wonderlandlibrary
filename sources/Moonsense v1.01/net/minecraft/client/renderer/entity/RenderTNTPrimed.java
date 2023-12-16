// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import moonsense.config.ModuleConfig;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.SCModule;
import moonsense.features.modules.type.world.TNTTimerModule;
import java.awt.Color;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.client.Minecraft;
import java.text.DecimalFormat;

public class RenderTNTPrimed extends Render
{
    private static final String __OBFID = "CL_00001030";
    private final DecimalFormat timeFormatter;
    private final Minecraft mc;
    
    public RenderTNTPrimed(final RenderManager p_i46134_1_) {
        super(p_i46134_1_);
        this.timeFormatter = new DecimalFormat("0.00");
        this.mc = Minecraft.getMinecraft();
        this.shadowSize = 0.5f;
    }
    
    public void renderTag(final RenderTNTPrimed tntRenderer, final EntityTNTPrimed tntPrimed, final double x, final double y, final double z, final float partialTicks) {
        boolean isHypixel = false;
        if (Minecraft.getMinecraft().getCurrentServerData() != null && this.mc.theWorld != null && this.mc.thePlayer != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.equalsIgnoreCase("mc.hypixel.net")) {
            isHypixel = true;
        }
        final int fuseTimer = isHypixel ? (tntPrimed.fuse - 28) : tntPrimed.fuse;
        if (fuseTimer >= 1) {
            final double distance = tntPrimed.getDistanceSqToEntity(tntRenderer.renderManager.livingPlayer);
            if (distance <= 4096.0) {
                final float number = (fuseTimer - partialTicks) / 20.0f;
                final String time = this.timeFormatter.format(number);
                final FontRenderer fontrenderer = tntRenderer.getFontRendererFromRenderManager();
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)x + 0.0f, (float)y + tntPrimed.height + 0.5f, (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-tntRenderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                byte xMultiplier = 1;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    xMultiplier = -1;
                }
                final float scale = 0.02666667f;
                GlStateManager.rotate(tntRenderer.renderManager.playerViewX * xMultiplier, 1.0f, 0.0f, 0.0f);
                GlStateManager.scale(-scale, -scale, scale);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                final int stringWidth = fontrenderer.getStringWidth(time) >> 1;
                final float green = Math.min(fuseTimer / (isHypixel ? 52.0f : 80.0f), 1.0f);
                final Color color = new Color(1.0f - green, green, 0.0f);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.disableTexture2D();
                worldrenderer.startDrawing(7);
                worldrenderer.color(0.0f, 0.0f, 0.0f, 0.25f);
                worldrenderer.addVertex(-stringWidth - 1, -1.0, 0.0);
                worldrenderer.addVertex(-stringWidth - 1, 8.0, 0.0);
                worldrenderer.addVertex(stringWidth + 1, 8.0, 0.0);
                worldrenderer.addVertex(stringWidth + 1, -1.0, 0.0);
                tessellator.draw();
                GlStateManager.enableTexture2D();
                if (TNTTimerModule.INSTANCE.staticColor.getBoolean()) {
                    GuiUtils.drawString(time, -fontrenderer.getStringWidth(time) >> 1, 0, SCModule.getColor(TNTTimerModule.INSTANCE.timerColor.getColorObject()), TNTTimerModule.INSTANCE.textShadow.getBoolean());
                }
                else {
                    GuiUtils.drawString(time, -fontrenderer.getStringWidth(time) >> 1, 0, color.getRGB(), TNTTimerModule.INSTANCE.textShadow.getBoolean());
                }
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }
        }
    }
    
    public void doRender(final EntityTNTPrimed p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        if (ModuleConfig.INSTANCE.isEnabled(TNTTimerModule.INSTANCE)) {
            this.renderTag(this, p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_);
        }
        final BlockRendererDispatcher var10 = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_ + 0.5f, (float)p_76986_6_);
        if (p_76986_1_.fuse - p_76986_9_ + 1.0f < 10.0f) {
            float var11 = 1.0f - (p_76986_1_.fuse - p_76986_9_ + 1.0f) / 10.0f;
            var11 = MathHelper.clamp_float(var11, 0.0f, 1.0f);
            var11 *= var11;
            var11 *= var11;
            final float var12 = 1.0f + var11 * 0.3f;
            GlStateManager.scale(var12, var12, var12);
        }
        float var11 = (1.0f - (p_76986_1_.fuse - p_76986_9_ + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture(p_76986_1_);
        GlStateManager.translate(-0.5f, -0.5f, 0.5f);
        var10.func_175016_a(Blocks.tnt.getDefaultState(), p_76986_1_.getBrightness(p_76986_9_));
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (p_76986_1_.fuse / 5 % 2 == 0) {
            GlStateManager.func_179090_x();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, var11);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            var10.func_175016_a(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.func_179098_w();
        }
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation func_180563_a(final EntityTNTPrimed p_180563_1_) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180563_a((EntityTNTPrimed)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityTNTPrimed)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
