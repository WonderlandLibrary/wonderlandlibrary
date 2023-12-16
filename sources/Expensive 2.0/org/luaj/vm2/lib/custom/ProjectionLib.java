package org.luaj.vm2.lib.custom;

import org.joml.Vector2d;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import wtf.expensive.util.render.ProjectionUtils;

public class ProjectionLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("projection", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {

                Vector2d project = ProjectionUtils.project(arg1.tofloat(), arg2.tofloat(), arg3.tofloat());
                if (project != null) {
                    return LuaValue.listOf(new LuaValue[]{
                            LuaValue.valueOf(project.x),
                            LuaValue.valueOf(project.y)
                    });
                } else {
                    return LuaValue.listOf(new LuaValue[]{
                            LuaValue.valueOf(Integer.MAX_VALUE),
                            LuaValue.valueOf(Integer.MAX_VALUE)
                    });
                }
            }
        });
        library.set("getX", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.getRenderManager().info.getProjectedView().getX());
            }
        });
        library.set("getY", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.getRenderManager().info.getProjectedView().getY());
            }
        });
        library.set("getZ", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.getRenderManager().info.getProjectedView().getZ());
            }
        });
        library.set("renderPartialTicks", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.getRenderPartialTicks());
            }
        });
        env.set("project", library);
        return library;
    }
}
