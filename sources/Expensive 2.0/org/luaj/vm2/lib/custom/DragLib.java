package org.luaj.vm2.lib.custom;

import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.FourArgFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import wtf.expensive.Initilization;
import wtf.expensive.modules.Function;
import wtf.expensive.util.drag.DragManager;
import wtf.expensive.util.drag.Dragging;
import wtf.expensive.util.render.ColorUtil;

public class DragLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("create", new create());
        library.set("setSize", new setSize());
        library.set("getPosition", new getPosition());
        env.set("drag", library);
        return library;
    }

    static class create extends FourArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
            Function function = (Function) arg1.checkuserdata();
            return DragLib.userdataOf(Initilization.createDrag(function, arg2.toString(), arg3.tofloat(), arg4.tofloat()));
        }
    }

    static class setSize extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            Dragging function = (Dragging) arg1.checkuserdata();
            function.setWidth(arg2.tofloat());

            function.setHeight(arg3.tofloat());
            return LuaValue.valueOf(0);
        }
    }

    static class getPosition extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg1) {
            Dragging function = (Dragging) arg1.checkuserdata();
            return LuaValue.listOf(new LuaValue[]{
                    LuaValue.valueOf(function.getX()),
                    LuaValue.valueOf(function.getY())
            });
        }
    }

}
