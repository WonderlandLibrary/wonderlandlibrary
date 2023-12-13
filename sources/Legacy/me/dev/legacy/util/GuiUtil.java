package me.dev.legacy.util;

import me.dev.legacy.Legacy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiUtil {
    public static void drawString(String paramString, float paramFloat1, float paramFloat2, int paramInt) {
        if (Legacy.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            Legacy.fontRenderer.drawStringWithShadow(paramString, paramFloat1, paramFloat2, paramInt);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, paramFloat1, paramFloat2, paramInt);
        }
    }

    public static int getStringWidth(String paramString) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(paramString);
    }

    public static void drawHorizontalLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (paramInt2 < paramInt1) {
            int i = paramInt1;
            paramInt1 = paramInt2;
            paramInt2 = i;
        }
        drawRect(paramInt1, paramInt3, paramInt2 + 1, paramInt3 + 1, paramInt4);
    }

    public static void drawString(String paramString, int paramInt1, int paramInt2, int paramInt3) {
        if (Legacy.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            Legacy.fontRenderer.drawStringWithShadow(paramString, paramInt1, paramInt2, paramInt3);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, paramInt1, paramInt2, paramInt3);
        }
    }

    public static String getCFont() {
        return Legacy.fontRenderer.getFont().getFamily();
    }

    public static void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        if (paramInt1 < paramInt3) {
            int i = paramInt1;
            paramInt1 = paramInt3;
            paramInt3 = i;
        }
        if (paramInt2 < paramInt4) {
            int i = paramInt2;
            paramInt2 = paramInt4;
            paramInt4 = i;
        }
        float f1 = (paramInt5 >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt5 >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt5 >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt5 & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f2, f3, f4, f1);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(paramInt1, paramInt4, 0.0D).endVertex();
        bufferBuilder.pos(paramInt3, paramInt4, 0.0D).endVertex();
        bufferBuilder.pos(paramInt3, paramInt2, 0.0D).endVertex();
        bufferBuilder.pos(paramInt1, paramInt2, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static int getHeight() {
        return Legacy.moduleManager.getModuleByName("CustomFont").isEnabled() ? Legacy.fontRenderer.getHeight() : Legacy.fontRenderer.getHeight();
    }

    public static void drawVerticalLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (paramInt3 < paramInt2) {
            int i = paramInt2;
            paramInt2 = paramInt3;
            paramInt3 = i;
        }
        drawRect(paramInt1, paramInt2 + 1, paramInt1 + 1, paramInt3, paramInt4);
    }

    public static void drawCenteredString(String paramString, int paramInt1, int paramInt2, int paramInt3) {
        if (Legacy.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            Legacy.fontRenderer.drawStringWithShadow(paramString, (paramInt1 - Legacy.fontRenderer.getStringWidth(paramString) / 2), paramInt2, paramInt3);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, (paramInt1 - Legacy.fontRenderer.getStringWidth(paramString) / 2), paramInt2, paramInt3);
        }
    }
}
