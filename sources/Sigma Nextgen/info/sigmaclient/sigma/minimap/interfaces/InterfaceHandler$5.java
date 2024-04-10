package info.sigmaclient.sigma.minimap.interfaces;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.minimap.*;
import info.sigmaclient.sigma.minimap.minimap.*;
import info.sigmaclient.sigma.minimap.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

final class InterfaceHandler$5 extends Interface {
    public InterfaceHandler$5(String name, int w, int h, ModOptions option) {
		super(name, w, h, option);
		// TODO Auto-generated constructor stub
	}

	long lastFBOTry = 0L;
    int lastMinimapSize = 0;
    private ArrayList<String> underText = new ArrayList<String>();
    
    @Override
    public int getW(final int scale) {
        return this.getSize() / scale;
    }
    
    @Override
    public int getH(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getWC(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getHC(final int scale) {
        return this.getH(scale);
    }
    
    @Override
    public int getW0(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getH0(final int scale) {
        return this.getH(scale);
    }
    
    @Override
    public int getSize() {
        return InterfaceHandler.minimap.getMinimapWidth() + 36 + 2;
    }
    
    public void translatePosition(final int specW, final int specH, final double ps, final double pc, final double offx, final double offy) {
        final double Y = (pc * offx + ps * offy) * Minimap.zoom;
        double borderedX;
        final double X = borderedX = (ps * offx - pc * offy) * Minimap.zoom;
        double borderedY = Y;
        if (borderedX > specW) {
            borderedX = specW;
            borderedY = Y * specW / X;
        }
        else if (borderedX < -specW) {
            borderedX = -specW;
            borderedY = -Y * specW / X;
        }
        if (borderedY > specH) {
            borderedY = specH;
            borderedX = X * specH / Y;
        }
        else if (borderedY < -specH) {
            borderedY = -specH;
            borderedX = -X * specH / Y;
        }
        GL11.glPushMatrix();
        GlStateManager.translate(borderedX, borderedY, 0.0);
    }
    
    @Override
    public void drawInterface(final int width, final int height, final int scale, final float partial) {
//        if (Minimap.loadedFBO && !OpenGlHelper.isFramebufferEnabled()) {
//            Minimap.loadedFBO = false;
//            Minimap.scalingFrameBuffer.deleteFramebuffer();
//            Minimap.rotationFrameBuffer.deleteFramebuffer();
//            Minimap.resetImage();
//        }
        if (!Minimap.loadedFBO && !XaeroMinimap.getSettings().mapSafeMode && System.currentTimeMillis() - this.lastFBOTry > 1000L) {
            this.lastFBOTry = System.currentTimeMillis();
            Minimap.loadFrameBuffer();
        }
        if (XaeroMinimap.getSettings().getMinimapSize() != this.lastMinimapSize) {
            this.lastMinimapSize = XaeroMinimap.getSettings().getMinimapSize();
            Minimap.resetImage();
            Minimap.frameUpdateNeeded = Minimap.usingFBO();
        }
        int bufferSize;
        bufferSize = InterfaceHandler.minimap.getBufferSize();
        final float mapScale = scale / 2.0f;
        final Minimap minimap = InterfaceHandler.minimap;
        final int minimapWidth = InterfaceHandler.minimap.getMinimapWidth();
        minimap.minimapWidth = minimapWidth;
        final int mapW = minimapWidth;
        final Minimap minimap2 = InterfaceHandler.minimap;
        final int minimapHeight = mapW;
        minimap2.minimapHeight = minimapHeight;
        final int mapH = minimapHeight;
        Minimap.frameUpdatePartialTicks = partial;
        InterfaceHandler.minimap.updateZoom();
        //Minimap.zoom = InterfaceHandler.minimap.getZoom();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float sizeFix = bufferSize / 512.0f;
            InterfaceHandler.minimap.renderFrameToFBO(bufferSize, mapW, sizeFix, partial, true);
            Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
            Minimap.rotationFrameBuffer.bindFramebufferTexture();
            sizeFix = 1.0f;
        GL11.glEnable(3008);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glScalef(1.0f / mapScale, 1.0f / mapScale, 1.0f);
        AbstractGui.drawModalRectWithCustomSizedTexture(5, 130, 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix), (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
    }
}
