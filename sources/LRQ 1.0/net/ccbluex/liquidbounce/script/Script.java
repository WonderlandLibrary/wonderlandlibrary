/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.internal.dynalink.beans.StaticClass
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.NashornScriptEngineFactory
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.io.FilesKt
 *  kotlin.io.TextStreamsKt
 *  kotlin.text.Charsets
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.script;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.script.ScriptEngine;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.script.api.ScriptCommand;
import net.ccbluex.liquidbounce.script.api.ScriptModule;
import net.ccbluex.liquidbounce.script.api.ScriptTab;
import net.ccbluex.liquidbounce.script.api.global.Chat;
import net.ccbluex.liquidbounce.script.api.global.Item;
import net.ccbluex.liquidbounce.script.api.global.Setting;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public final class Script
extends MinecraftInstance {
    private final ScriptEngine scriptEngine;
    private final String scriptText;
    public String scriptName;
    public String scriptVersion;
    public String[] scriptAuthors;
    private boolean state;
    private final HashMap<String, JSObject> events;
    private final List<Module> registeredModules;
    private final List<Command> registeredCommands;
    private final File scriptFile;

    public final String getScriptName() {
        return this.scriptName;
    }

    public final void setScriptName(String string) {
        this.scriptName = string;
    }

    public final String getScriptVersion() {
        return this.scriptVersion;
    }

    public final void setScriptVersion(String string) {
        this.scriptVersion = string;
    }

    public final String[] getScriptAuthors() {
        return this.scriptAuthors;
    }

    public final void setScriptAuthors(String[] stringArray) {
        this.scriptAuthors = stringArray;
    }

    public final void registerModule(JSObject moduleObject, JSObject callback) {
        ScriptModule module = new ScriptModule(moduleObject);
        LiquidBounce.INSTANCE.getModuleManager().registerModule(module);
        Collection collection = this.registeredModules;
        boolean bl = false;
        collection.add(module);
        callback.call((Object)moduleObject, new Object[]{module});
    }

    public final void registerCommand(JSObject commandObject, JSObject callback) {
        ScriptCommand command = new ScriptCommand(commandObject);
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(command);
        Collection collection = this.registeredCommands;
        boolean bl = false;
        collection.add(command);
        callback.call((Object)commandObject, new Object[]{command});
    }

    public final void registerTab(JSObject tabObject) {
        new ScriptTab(tabObject);
    }

    private final String getMagicComment(String name) {
        String magicPrefix = "///";
        Iterable $this$forEach$iv = StringsKt.lines((CharSequence)this.scriptText);
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            String[] it = (String[])element$iv;
            boolean bl = false;
            if (!StringsKt.startsWith$default((String)it, (String)magicPrefix, (boolean)false, (int)2, null)) {
                return null;
            }
            Object object = it;
            boolean bl2 = magicPrefix.length();
            int n = 0;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            CharSequence charSequence = object.substring(bl2 ? 1 : 0);
            object = new String[]{"="};
            bl2 = false;
            n = 2;
            List commentData = StringsKt.split$default((CharSequence)charSequence, (String[])object, (boolean)bl2, (int)n, (int)2, null);
            object = (String)CollectionsKt.first((List)commentData);
            bl2 = false;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            if (!((Object)StringsKt.trim((CharSequence)((CharSequence)object))).toString().equals(name)) continue;
            object = (String)CollectionsKt.last((List)commentData);
            boolean bl3 = false;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            return ((Object)StringsKt.trim((CharSequence)((CharSequence)object))).toString();
        }
        return null;
    }

    private final void supportLegacyScripts() {
        if (this.getMagicComment("api_version").equals("2") ^ true) {
            ClientUtils.getLogger().info("[ScriptAPI] Running script '" + this.scriptFile.getName() + "' with legacy support.");
            URL uRL = LiquidBounce.class.getResource("/assets/minecraft/liquidbounce/scriptapi/legacy.js");
            Charset charset = Charsets.UTF_8;
            boolean bl = false;
            byte[] byArray = TextStreamsKt.readBytes((URL)uRL);
            boolean bl2 = false;
            boolean bl3 = false;
            String legacyScript = new String(byArray, charset);
            this.scriptEngine.eval(legacyScript);
        }
    }

    public final void on(String eventName, JSObject handler) {
        ((Map)this.events).put(eventName, handler);
    }

    public final void onEnable() {
        if (this.state) {
            return;
        }
        this.callEvent("enable");
        this.state = true;
    }

    public final void onDisable() {
        MinecraftInstance it;
        if (!this.state) {
            return;
        }
        Iterable $this$forEach$iv = this.registeredModules;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            it = (Module)element$iv;
            boolean bl = false;
            LiquidBounce.INSTANCE.getModuleManager().unregisterModule((Module)it);
        }
        $this$forEach$iv = this.registeredCommands;
        $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            it = (Command)element$iv;
            boolean bl = false;
            LiquidBounce.INSTANCE.getCommandManager().unregisterCommand((Command)it);
        }
        this.callEvent("disable");
        this.state = false;
    }

    public final void import(String scriptFile) {
        this.scriptEngine.eval(FilesKt.readText$default((File)new File(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder(), scriptFile), null, (int)1, null));
    }

    private final void callEvent(String eventName) {
        try {
            JSObject jSObject = this.events.get(eventName);
            if (jSObject != null) {
                jSObject.call(null, new Object[0]);
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in script '" + this.scriptName + "'!", throwable);
        }
    }

    public final File getScriptFile() {
        return this.scriptFile;
    }

    public Script(File scriptFile) {
        Object object;
        block5: {
            block4: {
                List list;
                this.scriptFile = scriptFile;
                this.scriptText = FilesKt.readText$default((File)this.scriptFile, null, (int)1, null);
                this.events = new HashMap();
                Script script = this;
                boolean bl = false;
                script.registeredModules = list = (List)new ArrayList();
                script = this;
                bl = false;
                script.registeredCommands = list = (List)new ArrayList();
                object = this.getMagicComment("engine_flags");
                if (object == null || (object = StringsKt.split$default((CharSequence)((CharSequence)object), (String[])new String[]{","}, (boolean)false, (int)0, (int)6, null)) == null) break block4;
                Collection $this$toTypedArray$iv = (Collection)object;
                boolean $i$f$toTypedArray = false;
                Collection thisCollection$iv = $this$toTypedArray$iv;
                String[] stringArray = thisCollection$iv.toArray(new String[0]);
                if (stringArray == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                object = stringArray;
                if (stringArray != null) break block5;
            }
            object = new String[]{};
        }
        String[] engineFlags = object;
        this.scriptEngine = new NashornScriptEngineFactory().getScriptEngine(Arrays.copyOf(engineFlags, engineFlags.length));
        this.scriptEngine.put("Chat", StaticClass.forClass(Chat.class));
        this.scriptEngine.put("Setting", StaticClass.forClass(Setting.class));
        this.scriptEngine.put("Item", StaticClass.forClass(Item.class));
        this.scriptEngine.put("mc", MinecraftInstance.mc);
        this.scriptEngine.put("moduleManager", LiquidBounce.INSTANCE.getModuleManager());
        this.scriptEngine.put("commandManager", LiquidBounce.INSTANCE.getCommandManager());
        this.scriptEngine.put("scriptManager", LiquidBounce.INSTANCE.getScriptManager());
        this.scriptEngine.put("registerScript", new RegisterScript());
        this.supportLegacyScripts();
        this.scriptEngine.eval(this.scriptText);
        this.callEvent("load");
    }

    public final class RegisterScript
    implements Function<JSObject, Script> {
        @Override
        public Script apply(JSObject scriptObject) {
            Object object = scriptObject.getMember("name");
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
            Script.this.setScriptName((String)object);
            Object object2 = scriptObject.getMember("version");
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
            Script.this.setScriptVersion((String)object2);
            Object object3 = ScriptUtils.convert((Object)scriptObject.getMember("authors"), String[].class);
            if (object3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            Script.this.setScriptAuthors((String[])object3);
            return Script.this;
        }
    }
}

