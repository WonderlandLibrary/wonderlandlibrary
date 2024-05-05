package tech.drainwalk.client.scripting;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumAction;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.openjdk.nashorn.api.scripting.JSObject;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.events.EventRender3D;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.minecraft.ChatUtility;
import tech.drainwalk.utility.render.RenderUtility;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Script {
    @Getter
    ScriptEngine engine;
    public String name;

    public Script(String fileName) {
        this.engine = ScriptManager.getEngineManager().getEngineByName("js");
        try {
            //module
            createJSVariableFromClass(Module[].class);
            createJSVariableFromClass(Option[].class);
            createJSVariableFromClass(BooleanOption.class);
            createJSVariableFromClass(ColorOption.class);
            createJSVariableFromClass(SelectOption.class);
            createJSVariableFromClass(SelectOptionValue.class);
            createJSVariableFromClass(MultiOption.class);
            createJSVariableFromClass(MultiOptionValue.class);
            createJSVariableFromClass(FloatOption.class);
            createJSVariableFromClass(ScriptModule.class);
            createJSVariableFromClass(Type.class);
            createJSVariableFromClass(Module.class);
            //utilities
            createJSVariableFromClass(RenderUtility.class);
            createJSVariableFromClass(ColorUtility.class);
            createJSVariableFromClass(ChatUtility.class);
            createJSVariableFromClass(MathHelper.class);
            //events
            createJSVariableFromClass(EventRender2D.class);
            createJSVariableFromClass(UpdateEvent.class);
            createJSVariableFromClass(EventRender3D.class);
            //minecraft
            createJSVariableFromClass(EnumAction.class);

            //opengl
            createJSVariableFromClass(GL11.class);
            createJSVariableFromClass(GlStateManager.class);
            createJSVariableFromClass(Keyboard.class);
            createJSVariableFromClass(Mouse.class);

            engine.put("__js__", this);
            engine.eval("function register() { __js__.registerModule(); }");
            engine.put("client", DrainWalk.getInstance());
            engine.put("mc", Minecraft.getMinecraft());
            engine.eval(new FileReader(fileName), engine.getContext());
        }
        catch (FileNotFoundException e) {
            System.out.println("Не можем найти скрипт, имя файла: " + fileName);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
    }


    public void registerModule() {
        DrainWalk.getModuleManager().registerModule(new ScriptModule(this));
    }

    public String getName() {
        try {
            return (String) engine.eval("script.name");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private void createJSVariableFromClass(Class<?> clazz) throws ScriptException {
        engine.eval(String.format("var %s = Java.type('%s');", clazz.getSimpleName().replace("[]", "s"), clazz.getCanonicalName()));
    }

    public void invokeMethod(String name, Object... args) {
        if(!((JSObject)engine.get("script")).hasMember(name)) {
            return;
        }
        try {
            ((Invocable)getEngine()).invokeMethod(getEngine().get("script"), name, args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
