package koks.script.javascript;

import koks.api.Methods;
import koks.api.registry.module.Module;
import koks.script.javascript.toolkit.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import javax.script.*;
import java.util.function.Consumer;

/**
 * @author sumandora :)
 * @created on 01.02.2021 : 22:33
 */
public class JSApi implements Methods {

    public void load(String code) throws ScriptException, NoSuchMethodException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.put("log", (Consumer<String>)System.out::println);
        engine.put("sendMessage", (Consumer<String>)this::sendMessage);
        engine.put("sendPacket", (Consumer<Packet<?>>)this::sendPacket);
        engine.put("session", mc.session);
        engine.put("world", getWorld());
        engine.put("mc", mc);
        engine.put("player", getPlayer());
        engine.put("PI", Math.PI);
        engine.put("category", Module.Category.class);
        engine.put("script", new Script(engine));
        engine.eval(code);

        final Invocable invocable = (Invocable) engine;
        /*invocable.invokeFunction("main", new Toolkit());*/
    }
}
