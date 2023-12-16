package wtf.expensive.scripts;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.jse.CoerceJavaToLua;
import org.luaj.vm2.customs.EntityHook;
import org.luaj.vm2.customs.EventHook;
import org.luaj.vm2.customs.events.*;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.game.EventMouseTick;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventJump;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {

    public List<DefaultScript> scripts = new ArrayList<>();

    public void parseAllScripts() {
        File file = new File(Minecraft.getInstance().gameDir, "scripts");
        file.mkdirs();


        for (File file1 : file.listFiles()) {
            if (file1.getName().endsWith(".lua") || file1.getName().endsWith(".out")) {
                scripts.add(new DefaultScript(file1.getName()));
            }
        }
    }

    public void reload() {
        for (DefaultScript script : scripts) {
            script.load();
            script.globals.set("mod", LuaValue.userdataOf(script.mod));
            script.mod.init();
        }
    }

    public void init() {
        for (DefaultScript script : scripts) {
            script.load();
            System.out.println("Loaded script: " + script.scriptName);
            Function luaFunc = new Function(script.moduleName, script.category) {
                @Override
                public void init() {
                    super.init();
                    script.mod = this;
                    script.globals.set("mod", LuaValue.userdataOf(script.mod));
                    if (script.globals.get("init") != LuaValue.NIL) {
                        script.globals.get("init").call();
                    }
                }

                @Override
                protected void onEnable() {
                    super.onEnable();
                    if (script.globals.get("onEnable") != LuaValue.NIL) {
                        script.globals.get("onEnable").call();
                    }
                }

                @Override
                protected void onDisable() {
                    super.onDisable();
                    if (script.globals.get("onDisable") != LuaValue.NIL) {
                        script.globals.get("onDisable").call();
                    }
                }

                @Override
                public void onEvent(Event event) {

                    try {
                        if (script.globals.get("onEvent") != LuaValue.NIL) {
                            if (event instanceof EventMotion)
                                script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventMotionHook(event)));
                            if (event instanceof EventJump)
                                script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventJumpHook(event)));
                            if (event instanceof EventUpdate)
                                script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventUpdateHook(event)));
                            if (event instanceof EventRender)
                                script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventRenderHook(event)));
                            if (event instanceof EventPacket)
                                script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventPacketHook(event)));
                        }
                    } catch (LuaError ex) {
                        Managment.NOTIFICATION_MANAGER.add("Ошибка в скрипте: " + name, "Script", 5);
                        Managment.NOTIFICATION_MANAGER.add(ex.getMessageObject().toString(), "Debug", 3);
                        setState(false);
                    }

//                    if (event instanceof EventUpdate e) {
//                        if (script.globals.get("onPlayerUpdate") != LuaValue.NIL) {
//                            script.globals.get("onPlayerUpdate").call();
//                        }
//                    }
//                    if (event instanceof EventRender e) {
//                        if (e.isRender2D()) {
//                            if (script.globals.get("onDisplayDrawing") != LuaValue.NIL) {
//                                script.globals.get("onDisplayDrawing").call(LuaValue.userdataOf(e.matrixStack));
//                            }
//                        }
//                        if (e.isRender3D()) {
//                            if (script.globals.get("onWorldDrawing") != LuaValue.NIL) {
//                                script.globals.get("onWorldDrawing").call(LuaValue.userdataOf(e.matrixStack));
//                            }
//                        }
//                    }
//                    if (event instanceof EventMouseTick e) {
//                        if (script.globals.get("onMouseTick") != LuaValue.NIL) {
//                            script.globals.get("onMouseTick").call(LuaValue.valueOf(e.getButton()));
//                        }
//                    }
//                    if (event instanceof EventMotion e) {
//                        if (script.globals.get("onWalkingPlayerUpdate") != LuaValue.NIL) {
//
//                            LuaValue yaw = LuaValue.valueOf(e.getYaw());
//                            LuaValue pitch = LuaValue.valueOf(e.getPitch());
//                            LuaValue v = script.globals.get("onWalkingPlayerUpdate").call(LuaValue.listOf(new LuaValue[]{
//                                    yaw, pitch
//                            }));
//
//                            e.setYaw(v.checktable(1).get(1).tofloat());
//                            e.setPitch(v.checktable(1).get(2).tofloat());
//
//                        }
//                    }
                }
            };
            Managment.FUNCTION_MANAGER.getFunctions().add(luaFunc);
        }
    }

}
