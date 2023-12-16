package org.luaj.vm2.lib.custom;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector2d;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.*;
import org.lwjgl.opengl.GL11;
import wtf.expensive.util.render.ProjectionUtils;
import wtf.expensive.util.render.RenderUtil;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class GLLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        LuaValue library = tableOf();
        library.set("pushMatrix", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                GlStateManager.pushMatrix();
                return LuaValue.valueOf(0);
            }
        });

        library.set("start2D", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                GlStateManager.enableBlend();
                RenderSystem.disableAlphaTest();
                GL11.glDisable(GL_TEXTURE_2D);
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.shadeModel(7425);
                return LuaValue.valueOf(0);
            }
        });


        library.set("end2D", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                RenderSystem.enableAlphaTest();
                RenderSystem.shadeModel(7424);
                GL11.glEnable(GL_TEXTURE_2D);
                GlStateManager.disableBlend();
                return LuaValue.valueOf(0);
            }
        });

        library.set("color", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1) {
                float[] vals = RenderUtil.IntColor.rgb(arg1.toint());
                GlStateManager.color4f(vals[0], vals[1], vals[2], vals[3]);
                return LuaValue.valueOf(0);
            }
        });

        library.set("lineWidth", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1) {
                GlStateManager.lineWidth(arg1.tofloat());
                return LuaValue.valueOf(0);
            }
        });

        library.set("enable", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                GL11.glEnable(arg.toint());
                return LuaValue.valueOf(0);
            }
        });

        library.set("begin", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                GL11.glBegin(arg.toint());
                return LuaValue.valueOf(0);
            }
        });

        library.set("glEnd", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                GL11.glEnd();
                return LuaValue.valueOf(0);
            }
        });

        library.set("vertex2d", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue x, LuaValue y) {
                GL11.glVertex2f(x.tofloat(), y.tofloat());
                return LuaValue.valueOf(0);
            }
        });

        library.set("vertex3d", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue x, LuaValue y,LuaValue z) {
                GL11.glVertex3f(x.tofloat(), y.tofloat(),z.tofloat());
                return LuaValue.valueOf(0);
            }
        });

        library.set("rotate", new FourArgFunction() {
            @Override
            public LuaValue call(LuaValue x, LuaValue y,LuaValue z, LuaValue w) {
                GL11.glRotatef(x.tofloat(), y.tofloat(),z.tofloat(),w.tofloat());
                return LuaValue.valueOf(0);
            }
        });

        library.set("position", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue x, LuaValue y,LuaValue z) {
                GL11.glTranslatef(x.tofloat(), y.tofloat(),z.tofloat());
                return LuaValue.valueOf(0);
            }
        });


        library.set("disable", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                GL11.glDisable(arg.toint());
                return LuaValue.valueOf(0);
            }
        });

        library.set("popMatrix", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                GlStateManager.popMatrix();
                return LuaValue.valueOf(0);
            }
        });
        env.set("gl11", library);
        return library;
    }
}
