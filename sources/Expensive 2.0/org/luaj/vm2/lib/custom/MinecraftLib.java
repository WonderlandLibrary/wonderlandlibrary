package org.luaj.vm2.lib.custom;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.*;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.GCDUtil;
import wtf.expensive.util.misc.HudUtil;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;

public class MinecraftLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("getFPS", new fps());
        library.set("getPing", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(HudUtil.calculatePing());
            }
        });
        library.set("getServerIP", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(HudUtil.serverIP());
            }
        });
        library.set("getBPS", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(HudUtil.calculateBPS());
            }
        });
        library.set("getFixSensivity", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(GCDUtil.getSensitivity(arg.tofloat()));
            }
        });
        env.set("minecraft", library);
        return library;
    }

    static class fps extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(Minecraft.debugFPS);
        }
    }

}
