package org.luaj.vm2.lib.custom;

import net.minecraft.entity.LivingEntity;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.jse.CoerceJavaToLua;
import org.luaj.vm2.customs.EntityHook;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import wtf.expensive.managment.Managment;

public class AuraTargetLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("isActiveTarget", new AuraTargetLib.isActiveTarget());
        library.set("getTarget", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return CoerceJavaToLua.coerce(new EntityHook(Managment.FUNCTION_MANAGER.auraFunction.getTarget()));
            }
        });
        library.set("getName", new AuraTargetLib.getName());
        library.set("getDistance", new AuraTargetLib.getDistance());
        library.set("getHealth", new AuraTargetLib.getHealth());
        library.set("getMaxHealth", new AuraTargetLib.getMaxHealth());
        library.set("getPosition", new AuraTargetLib.getPosition());
        env.set("auraTarget", library);
        return library;
    }

    static class isActiveTarget extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            return LuaValue.valueOf(target != null);
        }
    }
    static class getName extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            return LuaValue.valueOf(target != null ? target.getName().getString() : "");
        }
    }

    static class getDistance extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            return LuaValue.valueOf(target != null ? mc.player.getDistance(target) : -1.0);
        }
    }
    static class getHealth extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            return LuaValue.valueOf(target != null ? target.getHealth() : -1.0);
        }
    }

    static class getMaxHealth extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            return LuaValue.valueOf(target != null ? target.getMaxHealth() : -1.0);
        }
    }

    static class getPosition extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            LivingEntity target = Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            if (target != null) {
                return LuaValue.listOf(new LuaValue[] {
                        LuaValue.valueOf(target.getPosX()),
                        LuaValue.valueOf(target.getPosY()),
                        LuaValue.valueOf(target.getPosZ())
                });
            } else {
                return LuaValue.listOf(new LuaValue[] {
                        LuaValue.valueOf(0),
                        LuaValue.valueOf(0),
                        LuaValue.valueOf(0)
                });
            }
        }
    }
}