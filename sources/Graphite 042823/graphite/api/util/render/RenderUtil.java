package graphite.api.util.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
	protected static float zLevel;
	
    public static void drawRect(double startX, double startY, double endX, double endY, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(startX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(endX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(endX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(startX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawBorderedRect(double startX, double startY, double endX, double endY, double lineSize, int color, int borderColor) {
        drawRect(startX, startY, endX, endY, color);        
        drawRect(startX, startY, startX + lineSize, endY, borderColor);
        drawRect(endX - lineSize, startY, endX, endY, borderColor);
        drawRect(startX, endY - lineSize, endX, endY, borderColor);
        drawRect(startX, startY, endX, startY + lineSize, borderColor);
    }
    
    public static void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        if (sideways) {
            bufferbuilder.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(left, bottom, zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(right, top, zLevel).color(f5, f6, f7, f4).endVertex();
        } else {
            bufferbuilder.pos(right, top, zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(left, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawArrow(float x, float y, float size, boolean rotate, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, rotate ? y + .5F : y, 1.0F);
        GL11.glRotatef(rotate ? 180 : 0, x, 0, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_LINES);
        
        float offset = 0F;
        for(float i = 0; i < size; i++) {
        	GL11.glVertex2f(offset, offset);
        	GL11.glVertex2f(size - offset, offset);
        	offset += 0.5F;
        }
        
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);

        GL11.glVertex2f(0, -.5F);
        GL11.glVertex2f(size, -.5F);

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawAlphaGrid(double x, double y, double width, double height) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 1.0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_POINTS);

        // TODO :: ALPHA GRID RENDERER

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
