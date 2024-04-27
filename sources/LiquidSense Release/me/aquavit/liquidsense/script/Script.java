package me.aquavit.liquidsense.script;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.JSObject;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.script.api.ScriptCommand;
import me.aquavit.liquidsense.script.api.ScriptTab;
import me.aquavit.liquidsense.script.api.global.Chat;
import me.aquavit.liquidsense.script.api.global.Item;
import me.aquavit.liquidsense.script.api.ScriptModule;
import me.aquavit.liquidsense.script.api.global.Setting;
import org.apache.commons.io.FileUtils;

public class Script extends MinecraftInstance {

    private final ScriptEngine scriptEngine;
    private final String scriptText;

    // Script information
    public String scriptName;
    public String scriptVersion;
    public String[] scriptAuthors;

    private boolean state = false;

    private final HashMap<String, JSObject> events = new HashMap<>();

    private final List<Module> registeredModules = new ArrayList<>();
    private final List<Command> registeredCommands = new ArrayList<>();

    public File scriptFile;

    public Script(File scriptFile) {
        this.scriptFile = scriptFile;
        try {
            this.scriptText = FileUtils.readFileToString(scriptFile, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read script file", e);
        }

        String engineFlagsString = getMagicComment("engine_flags");
        String[] engineFlags = engineFlagsString != null ? engineFlagsString.split(",") : new String[0];
        scriptEngine = new NashornScriptEngineFactory().getScriptEngine(engineFlags);


        // Global classes
        scriptEngine.put("Chat", StaticClass.forClass(Chat.class));
        scriptEngine.put("Setting", StaticClass.forClass(Setting.class));
        scriptEngine.put("Item", StaticClass.forClass(Item.class));

        // Global instances
        scriptEngine.put("mc", mc);

        scriptEngine.put("moduleManager", LiquidSense.moduleManager);
        scriptEngine.put("commandManager", LiquidSense.commandManager);
        scriptEngine.put("scriptManager", LiquidSense.scriptManager);

        // Global functions
        scriptEngine.put("registerScript", new RegisterScript());

        supportLegacyScripts();
    }

    public void initScript() throws ScriptException {
        scriptEngine.eval(scriptText);

        callEvent("load");

        ClientUtils.getLogger().info("[ScriptAPI] Successfully loaded script '" + scriptFile.getName() + "'.");
    }

    private class RegisterScript implements Function<JSObject, Script> {
        /**
         * Global function 'registerScript' which is called to register a script.
         * @param scriptObject JavaScript object containing information about the script.
         * @return The instance of this script.
         */
        @Override
        public Script apply(JSObject scriptObject) {
            scriptName = (String) scriptObject.getMember("name");
            scriptVersion = (String) scriptObject.getMember("version");
            scriptAuthors = (String[]) ScriptUtils.convert(scriptObject.getMember("authors"), String[].class);

            return Script.this;
        }
    }

    /**
     * Registers a new script module.
     * @param moduleObject JavaScript object containing information about the module.
     * @param callback JavaScript function to which the corresponding instance of [ScriptModule] is passed.
     * @see ScriptModule
     */
    @SuppressWarnings("unused")
    public void registerModule(JSObject moduleObject, JSObject callback) {
        ScriptModule module = new ScriptModule(moduleObject);
        LiquidSense.moduleManager.registerModule(module);
        registeredModules.add(module);
        callback.call(moduleObject, module);
    }

    /**
     * Registers a new script command.
     * @param commandObject JavaScript object containing information about the command.
     * @param callback JavaScript function to which the corresponding instance of [ScriptCommand] is passed.
     * @see ScriptCommand
     */
    @SuppressWarnings("unused")
    public void registerCommand(JSObject commandObject, JSObject callback) {
        ScriptCommand command = new ScriptCommand(commandObject);
        LiquidSense.commandManager.registerCommand(command);
        registeredCommands.add(command);
        callback.call(commandObject, command);
    }

    /**
     * Registers a new creative inventory tab.
     * @param tabObject JavaScript object containing information about the tab.
     * @see ScriptTab
     */
    @SuppressWarnings("unused")
    public void registerTab(JSObject tabObject) {
        new ScriptTab(tabObject);
    }

    /**
     * Gets the value of a magic comment from the script. Used for specifying additional information about the script.
     * @param name Name of the comment.
     * @return Value of the comment.
     */
    private String getMagicComment(String name) {
        String magicPrefix = "///";

        for (String line : scriptText.split("\\r?\\n")) {
            if (!line.startsWith(magicPrefix))
                return null;

            String[] commentData = line.substring(magicPrefix.length()).split("=", 2);

            if (commentData.length == 2 && commentData[0].trim().equals(name)) {
                return commentData[1].trim();
            }
        }

        return null;
    }

    /**
     * Adds support for scripts made for LiquidBounce's original script API.
     */
    private void supportLegacyScripts() {
        if (!"2".equals(getMagicComment("api_version"))) {
            ClientUtils.getLogger().info("[ScriptAPI] Running script '" + scriptFile.getName() + "' with legacy support.");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(LiquidSense.class.getResourceAsStream("/assets/minecraft/liquidsense/scriptapi/legacy.js"))))) {
                StringBuilder scriptBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    scriptBuilder.append(line).append("\n");
                }
                String legacyScript = scriptBuilder.toString();
                scriptEngine.eval(legacyScript);
            } catch (IOException | ScriptException e) {
                ClientUtils.getLogger().error("[ScriptAPI] Failed to evaluate legacy script.", e);
            }
        }
    }

    /**
     * Called from inside the script to register a new event handler.
     * @param eventName Name of the event.
     * @param handler JavaScript function used to handle the event.
     */
    public void on(String eventName, JSObject handler) {
        events.put(eventName, handler);
    }

    /**
     * Called when the client enables the script.
     */
    public void onEnable() {
        if (state) return;

        callEvent("enable");
        state = true;
    }

    /**
     * Called when the client disables the script. Handles unregistering all modules and commands
     * created with this script.
     */
    public void onDisable() {
        if (!state) return;

        for (Module module : registeredModules) {
            LiquidSense.moduleManager.unregisterModule(module);
        }

        for (Command command : registeredCommands) {
            LiquidSense.commandManager.unregisterCommand(command);
        }

        callEvent("disable");
        state = false;
    }
    public void importScript(String scriptFile) {
        File file = new File(LiquidSense.scriptManager.getScriptsFolder(), scriptFile);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Failed to load script file: " + file.getAbsolutePath());
            return;
        }

        try {
            String scriptText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            scriptEngine.eval(scriptText);
        } catch (IOException | ScriptException e) {
            ClientUtils.getLogger().error("[ScriptAPI] Failed to import script: " + scriptFile, e);
        }
    }

    /**
     * Calls the handler of a registered event.
     * @param eventName Name of the event to be called.
     */
    private void callEvent(String eventName) {
        try {
            JSObject handler = events.get(eventName);
            if (handler != null) {
                handler.call(null);
            }
        } catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in script '" + scriptName + "'!", throwable);
        }
    }
}
