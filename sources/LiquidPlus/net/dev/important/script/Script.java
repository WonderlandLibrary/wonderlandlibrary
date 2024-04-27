/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.script;

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
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.module.Module;
import net.dev.important.script.api.ScriptCommand;
import net.dev.important.script.api.ScriptModule;
import net.dev.important.script.api.ScriptTab;
import net.dev.important.script.api.global.Chat;
import net.dev.important.script.api.global.Item;
import net.dev.important.script.api.global.Setting;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0012\u0018\u00002\u00020\u0001:\u00016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0007H\u0002J\u0012\u0010'\u001a\u0004\u0018\u00010\u00072\u0006\u0010(\u001a\u00020\u0007H\u0002J\u000e\u0010)\u001a\u00020%2\u0006\u0010\u0002\u001a\u00020\u0007J\u0016\u0010*\u001a\u00020%2\u0006\u0010&\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\bJ\u0006\u0010,\u001a\u00020%J\u0006\u0010-\u001a\u00020%J\u0016\u0010.\u001a\u00020%2\u0006\u0010/\u001a\u00020\b2\u0006\u00100\u001a\u00020\bJ\u0016\u00101\u001a\u00020%2\u0006\u00102\u001a\u00020\b2\u0006\u00100\u001a\u00020\bJ\u000e\u00103\u001a\u00020%2\u0006\u00104\u001a\u00020\bJ\b\u00105\u001a\u00020%H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000fX\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u0014\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u0007X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u00020\u0007X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001b\"\u0004\b!\u0010\u001dR\u000e\u0010\"\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2={"Lnet/dev/important/script/Script;", "", "scriptFile", "Ljava/io/File;", "(Ljava/io/File;)V", "events", "Ljava/util/HashMap;", "", "Ljdk/nashorn/api/scripting/JSObject;", "registeredCommands", "", "Lnet/dev/important/modules/command/Command;", "registeredModules", "Lnet/dev/important/modules/module/Module;", "scriptAuthors", "", "getScriptAuthors", "()[Ljava/lang/String;", "setScriptAuthors", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "scriptEngine", "Ljavax/script/ScriptEngine;", "getScriptFile", "()Ljava/io/File;", "scriptName", "getScriptName", "()Ljava/lang/String;", "setScriptName", "(Ljava/lang/String;)V", "scriptText", "scriptVersion", "getScriptVersion", "setScriptVersion", "state", "", "callEvent", "", "eventName", "getMagicComment", "name", "import", "on", "handler", "onDisable", "onEnable", "registerCommand", "commandObject", "callback", "registerModule", "moduleObject", "registerTab", "tabObject", "supportLegacyScripts", "RegisterScript", "LiquidBounce"})
public final class Script {
    @NotNull
    private final File scriptFile;
    @NotNull
    private final ScriptEngine scriptEngine;
    @NotNull
    private final String scriptText;
    public String scriptName;
    public String scriptVersion;
    public String[] scriptAuthors;
    private boolean state;
    @NotNull
    private final HashMap<String, JSObject> events;
    @NotNull
    private final List<Module> registeredModules;
    @NotNull
    private final List<Command> registeredCommands;

    public Script(@NotNull File scriptFile) {
        String[] stringArray;
        Object object;
        String[] stringArray2;
        Intrinsics.checkNotNullParameter(scriptFile, "scriptFile");
        this.scriptFile = scriptFile;
        this.scriptText = FilesKt.readText$default(this.scriptFile, null, 1, null);
        this.events = new HashMap();
        this.registeredModules = new ArrayList();
        this.registeredCommands = new ArrayList();
        String string = this.getMagicComment("engine_flags");
        if (string == null) {
            stringArray2 = null;
        } else {
            String[] stringArray3 = new String[]{","};
            List list = StringsKt.split$default((CharSequence)string, stringArray3, false, 0, 6, null);
            if (list == null) {
                stringArray2 = null;
            } else {
                Collection $this$toTypedArray$iv = list;
                boolean $i$f$toTypedArray = false;
                Collection thisCollection$iv = $this$toTypedArray$iv;
                String[] stringArray4 = thisCollection$iv.toArray(new String[0]);
                if (stringArray4 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                }
                stringArray2 = object = stringArray4;
            }
        }
        if (object == null) {
            boolean $i$f$emptyArray = false;
            stringArray = new String[]{};
        } else {
            stringArray = object;
        }
        String[] engineFlags = stringArray;
        object = new NashornScriptEngineFactory().getScriptEngine(Arrays.copyOf(engineFlags, engineFlags.length));
        Intrinsics.checkNotNullExpressionValue(object, "NashornScriptEngineFacto\u2026criptEngine(*engineFlags)");
        this.scriptEngine = object;
        this.scriptEngine.put("Chat", StaticClass.forClass(Chat.class));
        this.scriptEngine.put("Setting", StaticClass.forClass(Setting.class));
        this.scriptEngine.put("Item", StaticClass.forClass(Item.class));
        this.scriptEngine.put("mc", Minecraft.func_71410_x());
        this.scriptEngine.put("moduleManager", Client.INSTANCE.getModuleManager());
        this.scriptEngine.put("commandManager", Client.INSTANCE.getCommandManager());
        this.scriptEngine.put("scriptManager", Client.INSTANCE.getScriptManager());
        this.scriptEngine.put("registerScript", new RegisterScript());
        this.supportLegacyScripts();
        this.scriptEngine.eval(this.scriptText);
        this.callEvent("load");
    }

    @NotNull
    public final File getScriptFile() {
        return this.scriptFile;
    }

    @NotNull
    public final String getScriptName() {
        String string = this.scriptName;
        if (string != null) {
            return string;
        }
        Intrinsics.throwUninitializedPropertyAccessException("scriptName");
        return null;
    }

    public final void setScriptName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.scriptName = string;
    }

    @NotNull
    public final String getScriptVersion() {
        String string = this.scriptVersion;
        if (string != null) {
            return string;
        }
        Intrinsics.throwUninitializedPropertyAccessException("scriptVersion");
        return null;
    }

    public final void setScriptVersion(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.scriptVersion = string;
    }

    @NotNull
    public final String[] getScriptAuthors() {
        String[] stringArray = this.scriptAuthors;
        if (stringArray != null) {
            return stringArray;
        }
        Intrinsics.throwUninitializedPropertyAccessException("scriptAuthors");
        return null;
    }

    public final void setScriptAuthors(@NotNull String[] stringArray) {
        Intrinsics.checkNotNullParameter(stringArray, "<set-?>");
        this.scriptAuthors = stringArray;
    }

    public final void registerModule(@NotNull JSObject moduleObject, @NotNull JSObject callback) {
        Intrinsics.checkNotNullParameter(moduleObject, "moduleObject");
        Intrinsics.checkNotNullParameter(callback, "callback");
        ScriptModule module2 = new ScriptModule(moduleObject);
        Client.INSTANCE.getModuleManager().registerModule(module2);
        ((Collection)this.registeredModules).add(module2);
        Object[] objectArray = new Object[]{module2};
        callback.call(moduleObject, objectArray);
    }

    public final void registerCommand(@NotNull JSObject commandObject, @NotNull JSObject callback) {
        Intrinsics.checkNotNullParameter(commandObject, "commandObject");
        Intrinsics.checkNotNullParameter(callback, "callback");
        ScriptCommand command = new ScriptCommand(commandObject);
        Client.INSTANCE.getCommandManager().registerCommand(command);
        ((Collection)this.registeredCommands).add(command);
        Object[] objectArray = new Object[]{command};
        callback.call(commandObject, objectArray);
    }

    public final void registerTab(@NotNull JSObject tabObject) {
        Intrinsics.checkNotNullParameter(tabObject, "tabObject");
        new ScriptTab(tabObject);
    }

    private final String getMagicComment(String name) {
        String magicPrefix = "///";
        Iterable $this$forEach$iv = StringsKt.lines(this.scriptText);
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            String it = (String)element$iv;
            boolean bl = false;
            if (!StringsKt.startsWith$default(it, magicPrefix, false, 2, null)) {
                return null;
            }
            String string = it.substring(magicPrefix.length());
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
            String[] stringArray = new String[]{"="};
            List commentData = StringsKt.split$default((CharSequence)string, stringArray, false, 2, 2, null);
            if (!Intrinsics.areEqual(((Object)StringsKt.trim((CharSequence)((String)CollectionsKt.first(commentData)))).toString(), name)) continue;
            return ((Object)StringsKt.trim((CharSequence)((String)CollectionsKt.last(commentData)))).toString();
        }
        return null;
    }

    private final void supportLegacyScripts() {
        if (!Intrinsics.areEqual(this.getMagicComment("api_version"), "2")) {
            ClientUtils.getLogger().info("[ScriptAPI] Running script '" + this.scriptFile.getName() + "' with legacy support.");
            URL uRL = Client.class.getResource("/assets/minecraft/liquidplus/scriptapi/legacy.js");
            Intrinsics.checkNotNullExpressionValue(uRL, "Client::class.java.getRe\u2026lus/scriptapi/legacy.js\")");
            Charset charset = Charsets.UTF_8;
            byte[] byArray = TextStreamsKt.readBytes(uRL);
            String legacyScript = new String(byArray, charset);
            this.scriptEngine.eval(legacyScript);
        }
    }

    public final void on(@NotNull String eventName, @NotNull JSObject handler) {
        Intrinsics.checkNotNullParameter(eventName, "eventName");
        Intrinsics.checkNotNullParameter(handler, "handler");
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
            Client.INSTANCE.getModuleManager().unregisterModule((Module)it);
        }
        $this$forEach$iv = this.registeredCommands;
        $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            it = (Command)element$iv;
            boolean bl = false;
            Client.INSTANCE.getCommandManager().unregisterCommand((Command)it);
        }
        this.callEvent("disable");
        this.state = false;
    }

    public final void import(@NotNull String scriptFile) {
        Intrinsics.checkNotNullParameter(scriptFile, "scriptFile");
        this.scriptEngine.eval(FilesKt.readText$default(new File(Client.INSTANCE.getScriptManager().getScriptsFolder(), scriptFile), null, 1, null));
    }

    private final void callEvent(String eventName) {
        try {
            JSObject jSObject = this.events.get(eventName);
            if (jSObject != null) {
                jSObject.call(null, new Object[0]);
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in script '" + this.getScriptName() + "'!", throwable);
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0002H\u0016\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/script/Script$RegisterScript;", "Ljava/util/function/Function;", "Ljdk/nashorn/api/scripting/JSObject;", "Lnet/dev/important/script/Script;", "(Lnet/dev/important/script/Script;)V", "apply", "scriptObject", "LiquidBounce"})
    public final class RegisterScript
    implements Function<JSObject, Script> {
        public RegisterScript() {
            Intrinsics.checkNotNullParameter(Script.this, "this$0");
        }

        @Override
        @NotNull
        public Script apply(@NotNull JSObject scriptObject) {
            Intrinsics.checkNotNullParameter(scriptObject, "scriptObject");
            Object object = scriptObject.getMember("name");
            if (object == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            Script.this.setScriptName((String)object);
            Object object2 = scriptObject.getMember("version");
            if (object2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            Script.this.setScriptVersion((String)object2);
            Object object3 = ScriptUtils.convert(scriptObject.getMember("authors"), String[].class);
            if (object3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            Script.this.setScriptAuthors((String[])object3);
            return Script.this;
        }
    }
}

