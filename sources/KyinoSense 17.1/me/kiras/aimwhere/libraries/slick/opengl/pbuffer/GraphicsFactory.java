/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.opengl.Pbuffer
 */
package me.kiras.aimwhere.libraries.slick.opengl.pbuffer;

import java.util.HashMap;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.pbuffer.FBOGraphics;
import me.kiras.aimwhere.libraries.slick.opengl.pbuffer.PBufferGraphics;
import me.kiras.aimwhere.libraries.slick.opengl.pbuffer.PBufferUniqueGraphics;
import me.kiras.aimwhere.libraries.slick.util.Log;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;

public class GraphicsFactory {
    private static HashMap graphics = new HashMap();
    private static boolean pbuffer = true;
    private static boolean pbufferRT = true;
    private static boolean fbo = true;
    private static boolean init = false;

    private static void init() throws SlickException {
        init = true;
        if (fbo) {
            fbo = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        }
        pbuffer = (Pbuffer.getCapabilities() & 1) != 0;
        boolean bl = pbufferRT = (Pbuffer.getCapabilities() & 2) != 0;
        if (!(fbo || pbuffer || pbufferRT)) {
            throw new SlickException("Your OpenGL card does not support offscreen buffers and hence can't handle the dynamic images required for this application.");
        }
        Log.info("Offscreen Buffers FBO=" + fbo + " PBUFFER=" + pbuffer + " PBUFFERRT=" + pbufferRT);
    }

    public static void setUseFBO(boolean useFBO) {
        fbo = useFBO;
    }

    public static boolean usingFBO() {
        return fbo;
    }

    public static boolean usingPBuffer() {
        return !fbo && pbuffer;
    }

    public static Graphics getGraphicsForImage(Image image2) throws SlickException {
        Graphics g = (Graphics)graphics.get(image2.getTexture());
        if (g == null) {
            g = GraphicsFactory.createGraphics(image2);
            graphics.put(image2.getTexture(), g);
        }
        return g;
    }

    public static void releaseGraphicsForImage(Image image2) throws SlickException {
        Graphics g = (Graphics)graphics.remove(image2.getTexture());
        if (g != null) {
            g.destroy();
        }
    }

    private static Graphics createGraphics(Image image2) throws SlickException {
        GraphicsFactory.init();
        if (fbo) {
            try {
                return new FBOGraphics(image2);
            }
            catch (Exception e) {
                fbo = false;
                Log.warn("FBO failed in use, falling back to PBuffer");
            }
        }
        if (pbuffer) {
            if (pbufferRT) {
                return new PBufferGraphics(image2);
            }
            return new PBufferUniqueGraphics(image2);
        }
        throw new SlickException("Failed to create offscreen buffer even though the card reports it's possible");
    }
}

