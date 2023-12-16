package org.luaj.vm2.lib.custom;

import net.minecraft.entity.Entity;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.jse.CoerceJavaToLua;
import org.luaj.vm2.customs.EntityHook;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.ArrayList;
import java.util.List;

public class WorldLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("setTime", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                mc.world.setDayTime(arg.toint());
                return LuaValue.valueOf(0);
            }
        });
        library.set("getEntities", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                List<LuaValue> values = new ArrayList<>();
                for (Entity entity : mc.world.getAllEntities()) {
                    values.add(CoerceJavaToLua.coerce(new EntityHook(entity)));
                }
                return LuaValue.listOf(values.toArray(LuaValue[]::new));
            }
        });
        env.set("world", library);
        return library;
    }

}
