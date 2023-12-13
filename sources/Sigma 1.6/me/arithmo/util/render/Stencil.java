/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 */
package me.arithmo.util.render;

import java.io.PrintStream;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public final class Stencil {
    private static final Stencil INSTANCE = new Stencil();
    private final HashMap<Integer, StencilFunc> stencilFuncs = new HashMap();
    private int layers = 1;
    private boolean renderMask;

    public static Stencil getInstance() {
        return INSTANCE;
    }

    public void setRenderMask(boolean renderMask) {
        this.renderMask = renderMask;
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
            int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.getMinecraft().displayWidth, (int)Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencil_depth_buffer_ID);
            fbo.depthBuffer = -1;
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.getMinecraft().displayWidth, (int)Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencil_depth_buffer_ID);
    }

    public void startLayer() {
        if (this.layers == 1) {
            GL11.glClearStencil((int)0);
            GL11.glClear((int)1024);
        }
        GL11.glEnable((int)2960);
        ++this.layers;
        if (this.layers > this.getMaximumLayers()) {
            System.out.println("StencilUtil: Reached maximum amount of layers!");
            this.layers = 1;
        }
    }

    public void stopLayer() {
        if (this.layers == 1) {
            System.out.println("StencilUtil: No layers found!");
            return;
        }
        --this.layers;
        if (this.layers == 1) {
            GL11.glDisable((int)2960);
        } else {
            StencilFunc lastStencilFunc = this.stencilFuncs.remove(this.layers);
            if (lastStencilFunc != null) {
                lastStencilFunc.use();
            }
        }
    }

    public void clear() {
        GL11.glClearStencil((int)0);
        GL11.glClear((int)1024);
        this.stencilFuncs.clear();
        this.layers = 1;
    }

    public void setBuffer() {
        this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, this.getMaximumLayers(), 7681, 7680, 7680));
    }

    public void setBuffer(boolean set) {
        this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, set ? this.layers : this.layers - 1, this.getMaximumLayers(), 7681, 7681, 7681));
    }

    public void cropOutside() {
        this.setStencilFunc(new StencilFunc(this, 517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
    }

    public void cropInside() {
        this.setStencilFunc(new StencilFunc(this, 514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
    }

    public void setStencilFunc(StencilFunc stencilFunc) {
        GL11.glStencilFunc((int)StencilFunc.func_func, (int)StencilFunc.func_ref, (int)StencilFunc.func_mask);
        GL11.glStencilOp((int)StencilFunc.op_fail, (int)StencilFunc.op_zfail, (int)StencilFunc.op_zpass);
        this.stencilFuncs.put(this.layers, stencilFunc);
    }

    public StencilFunc getStencilFunc() {
        return this.stencilFuncs.get(this.layers);
    }

    public int getLayer() {
        return this.layers;
    }

    public int getStencilBufferSize() {
        return GL11.glGetInteger((int)3415);
    }

    public int getMaximumLayers() {
        return (int)(Math.pow(2.0, this.getStencilBufferSize()) - 1.0);
    }

    public void createCirlce(double x, double y, double radius) {
        GL11.glBegin((int)6);
        for (int i = 0; i <= 360; ++i) {
            double sin = Math.sin((double)i * 3.141592653589793 / 180.0) * radius;
            double cos = Math.cos((double)i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2d((double)(x + sin), (double)(y + cos));
        }
        GL11.glEnd();
    }

    public void createRect(double x, double y, double x2, double y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
    }

    public static class StencilFunc {
        public static int func_func;
        public static int func_ref;
        public static int func_mask;
        public static int op_fail;
        public static int op_zfail;
        public static int op_zpass;

        public StencilFunc(Stencil paramStencil, int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
            StencilFunc.func_func = func_func;
            StencilFunc.func_ref = func_ref;
            StencilFunc.func_mask = func_mask;
            StencilFunc.op_fail = op_fail;
            StencilFunc.op_zfail = op_zfail;
            StencilFunc.op_zpass = op_zpass;
        }

        public void use() {
            GL11.glStencilFunc((int)func_func, (int)func_ref, (int)func_mask);
            GL11.glStencilOp((int)op_fail, (int)op_zfail, (int)op_zpass);
        }
    }

}

