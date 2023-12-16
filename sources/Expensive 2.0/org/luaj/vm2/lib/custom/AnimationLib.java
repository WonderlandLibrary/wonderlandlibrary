package org.luaj.vm2.lib.custom;

import net.minecraft.item.Item;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.util.render.animation.AnimationMath;
import wtf.expensive.util.world.InventoryUtil;

public class AnimationLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("fast", new AnimationLib.addFast());
        library.set("lerp", new AnimationLib.addLerp());
        env.set("animation", library);
        return library;
    }

    static class addFast extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            return LuaValue.valueOf(AnimationMath.fast(arg1.tofloat(), arg2.tofloat(), arg3.tofloat()));
        }
    }

    static class addLerp extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            return LuaValue.valueOf(AnimationMath.lerp(arg1.tofloat(), arg2.tofloat(), arg3.tofloat()));
        }
    }
}
