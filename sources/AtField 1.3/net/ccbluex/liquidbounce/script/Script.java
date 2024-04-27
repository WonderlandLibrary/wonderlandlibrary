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
    private final String scriptText;
    private final List registeredCommands;
    public String[] scriptAuthors;
    private final ScriptEngine scriptEngine;
    private final HashMap events;
    public String scriptName;
    private boolean state;
    public String scriptVersion;
    private final List registeredModules;
    private final File scriptFile;

    public final String getScriptVersion() {
        return this.scriptVersion;
    }

    private final void callEvent(String string) {
        try {
            JSObject jSObject = (JSObject)this.events.get(string);
            if (jSObject != null) {
                jSObject.call(null, new Object[0]);
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in script '" + this.scriptName + "'!", throwable);
        }
    }

    public final void registerTab(JSObject jSObject) {
        new ScriptTab(jSObject);
    }

    public final String getScriptName() {
        return this.scriptName;
    }

    public final void setScriptAuthors(String[] stringArray) {
        this.scriptAuthors = stringArray;
    }

    public final void registerCommand(JSObject jSObject, JSObject jSObject2) {
        ScriptCommand scriptCommand = new ScriptCommand(jSObject);
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(scriptCommand);
        Collection collection = this.registeredCommands;
        boolean bl = false;
        collection.add(scriptCommand);
        jSObject2.call((Object)jSObject, new Object[]{scriptCommand});
    }

    public final void onDisable() {
        boolean bl;
        MinecraftInstance minecraftInstance;
        if (!this.state) {
            return;
        }
        Iterable iterable = this.registeredModules;
        boolean bl2 = false;
        for (Object t : iterable) {
            minecraftInstance = (Module)t;
            bl = false;
            LiquidBounce.INSTANCE.getModuleManager().unregisterModule((Module)minecraftInstance);
        }
        iterable = this.registeredCommands;
        bl2 = false;
        for (Object t : iterable) {
            minecraftInstance = (Command)t;
            bl = false;
            LiquidBounce.INSTANCE.getCommandManager().unregisterCommand((Command)minecraftInstance);
        }
        this.callEvent("disable");
        this.state = false;
    }

    private final void supportLegacyScripts() {
        if (this.getMagicComment("api_version").equals("2") ^ true) {
            ClientUtils.getLogger().info("[ScriptAPI] Running script '" + this.scriptFile.getName() + "' with legacy support.");
            URL uRL = LiquidBounce.class.getResource("/assets/minecraft/More/scriptapi/legacy.js");
            Charset charset = Charsets.UTF_8;
            boolean bl = false;
            byte[] byArray = TextStreamsKt.readBytes((URL)uRL);
            boolean bl2 = false;
            boolean bl3 = false;
            String string = new String(byArray, charset);
            this.scriptEngine.eval(string);
        }
    }

    public final File getScriptFile() {
        return this.scriptFile;
    }

    private final String getMagicComment(String string) {
        String string2 = "///";
        Iterable iterable = StringsKt.lines((CharSequence)this.scriptText);
        boolean bl = false;
        for (Object t : iterable) {
            String[] stringArray = (String[])t;
            boolean bl2 = false;
            if (!StringsKt.startsWith$default((String)stringArray, (String)string2, (boolean)false, (int)2, null)) {
                return null;
            }
            Object object = stringArray;
            boolean bl3 = string2.length();
            int n = 0;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            CharSequence charSequence = object.substring(bl3 ? 1 : 0);
            object = new String[]{"="};
            bl3 = false;
            n = 2;
            List list = StringsKt.split$default((CharSequence)charSequence, (String[])object, (boolean)bl3, (int)n, (int)2, null);
            object = (String)CollectionsKt.first((List)list);
            bl3 = false;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            if (!((Object)StringsKt.trim((CharSequence)((CharSequence)object))).toString().equals(string)) continue;
            object = (String)CollectionsKt.last((List)list);
            boolean bl4 = false;
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            return ((Object)StringsKt.trim((CharSequence)((CharSequence)object))).toString();
        }
        return null;
    }

    public Script(File file) {
        Object object;
        block5: {
            block4: {
                List list;
                this.scriptFile = file;
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
                Collection collection = (Collection)object;
                boolean bl2 = false;
                Collection collection2 = collection;
                String[] stringArray = collection2.toArray(new String[0]);
                if (stringArray == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                object = stringArray;
                if (stringArray != null) break block5;
            }
            object = new String[]{};
        }
        String[] stringArray = object;
        this.scriptEngine = new NashornScriptEngineFactory().getScriptEngine(Arrays.copyOf(stringArray, stringArray.length));
        this.scriptEngine.put("Chat", StaticClass.forClass(Chat.class));
        this.scriptEngine.put("Setting", StaticClass.forClass(Setting.class));
        this.scriptEngine.put("Item", StaticClass.forClass(Item.class));
        this.scriptEngine.put("mc", MinecraftInstance.mc);
        this.scriptEngine.put("moduleManager", LiquidBounce.INSTANCE.getModuleManager());
        this.scriptEngine.put("commandManager", LiquidBounce.INSTANCE.getCommandManager());
        this.scriptEngine.put("scriptManager", LiquidBounce.INSTANCE.getScriptManager());
        this.scriptEngine.put("registerScript", new RegisterScript(this));
        this.supportLegacyScripts();
        this.scriptEngine.eval(this.scriptText);
        this.callEvent("load");
    }

    public final void registerModule(JSObject jSObject, JSObject jSObject2) {
        ScriptModule scriptModule = new ScriptModule(jSObject);
        LiquidBounce.INSTANCE.getModuleManager().registerModule(scriptModule);
        Collection collection = this.registeredModules;
        boolean bl = false;
        collection.add(scriptModule);
        jSObject2.call((Object)jSObject, new Object[]{scriptModule});
    }

    public final void setScriptName(String string) {
        this.scriptName = string;
    }

    public final void on(String string, JSObject jSObject) {
        ((Map)this.events).put(string, jSObject);
    }

    public final void import(String string) {
        this.scriptEngine.eval(FilesKt.readText$default((File)new File(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder(), string), null, (int)1, null));
    }

    public final void setScriptVersion(String string) {
        this.scriptVersion = string;
    }

    public final void onEnable() {
        if (this.state) {
            return;
        }
        this.callEvent("enable");
        this.state = true;
    }

    public final String[] getScriptAuthors() {
        return this.scriptAuthors;
    }

    public final class RegisterScript
    implements Function {
        final Script this$0;

        public RegisterScript(Script script) {
            this.this$0 = script;
        }

        public Object apply(Object object) {
            return this.apply((JSObject)object);
        }

        public Script apply(JSObject jSObject) {
            Object object = jSObject.getMember("name");
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
            this.this$0.setScriptName((String)object);
            Object object2 = jSObject.getMember("version");
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
            }
            this.this$0.setScriptVersion((String)object2);
            Object object3 = ScriptUtils.convert((Object)jSObject.getMember("authors"), String[].class);
            if (object3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            this.this$0.setScriptAuthors((String[])object3);
            return this.this$0;
        }
    }
}

