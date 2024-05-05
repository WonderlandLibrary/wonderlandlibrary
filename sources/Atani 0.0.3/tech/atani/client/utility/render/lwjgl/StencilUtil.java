package tech.atani.client.utility.render.lwjgl;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import tech.atani.client.utility.interfaces.Methods;

import static org.lwjgl.opengl.GL11.*;

@Native
public class StencilUtil implements Methods {

    private static void setupFramebuffer(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        int stencil_texture_buffer_ID = EXTFramebufferObject.glGenFramebuffersEXT();
        {
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.displayWidth, mc.displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_texture_buffer_ID, 0);
        }
    }

    public static void checkAndSetupFramebuffer(Framebuffer framebuffer) {
        if (framebuffer == null) return;
        if (framebuffer.depthBuffer > -1) {
            setupFramebuffer(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }

    public static void start() {
        mc.getFramebuffer().bindFramebuffer(false);
        checkAndSetupFramebuffer(mc.getFramebuffer());
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1,1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }

    public static void end() {
        glDisable(GL_STENCIL_TEST);
        GlStateManager.bindTexture(0);
    }

    public static void readBuffer(int ref) {
        glColorMask(true,true,true,true);
        glStencilFunc(GL_EQUAL, ref, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }
}
