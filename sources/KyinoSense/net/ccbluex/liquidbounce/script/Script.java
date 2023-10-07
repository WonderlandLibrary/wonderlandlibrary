/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.apache.logging.log4j.Logger
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.script.api.ScriptModule;
import net.ccbluex.liquidbounce.script.api.ScriptTab;
import net.ccbluex.liquidbounce.script.api.global.Chat;
import net.ccbluex.liquidbounce.script.api.global.Item;
import net.ccbluex.liquidbounce.script.api.global.Setting;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001:\u00016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0007H\u0002J\u000e\u0010)\u001a\u00020'2\u0006\u0010\u0002\u001a\u00020\u0007J\u0016\u0010*\u001a\u00020'2\u0006\u0010(\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\bJ\u0006\u0010,\u001a\u00020'J\u0006\u0010-\u001a\u00020'J\u0016\u0010.\u001a\u00020'2\u0006\u0010/\u001a\u00020\b2\u0006\u00100\u001a\u00020\bJ\u0016\u00101\u001a\u00020'2\u0006\u00102\u001a\u00020\b2\u0006\u00100\u001a\u00020\bJ\u000e\u00103\u001a\u00020'2\u0006\u00104\u001a\u00020\bJ\u0006\u00105\u001a\u00020'R*\u0010\u0005\u001a\u001e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006j\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b`\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0010X\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u0015\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0016\u0010\u0016\u001a\n \u0018*\u0004\u0018\u00010\u00170\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u0007X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u000e\u0010 \u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010!\u001a\u00020\u0007X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u001d\"\u0004\b#\u0010\u001fR\u000e\u0010$\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2={"Lnet/ccbluex/liquidbounce/script/Script;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "scriptFile", "Ljava/io/File;", "(Ljava/io/File;)V", "events", "Ljava/util/HashMap;", "", "Ljdk/nashorn/api/scripting/JSObject;", "Lkotlin/collections/HashMap;", "registeredCommands", "", "Lnet/ccbluex/liquidbounce/features/command/Command;", "registeredModules", "Lnet/ccbluex/liquidbounce/features/module/Module;", "scriptAuthors", "", "getScriptAuthors", "()[Ljava/lang/String;", "setScriptAuthors", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "scriptEngine", "Ljavax/script/ScriptEngine;", "kotlin.jvm.PlatformType", "getScriptFile", "()Ljava/io/File;", "scriptName", "getScriptName", "()Ljava/lang/String;", "setScriptName", "(Ljava/lang/String;)V", "scriptText", "scriptVersion", "getScriptVersion", "setScriptVersion", "state", "", "callEvent", "", "eventName", "import", "on", "handler", "onDisable", "onEnable", "registerCommand", "commandObject", "callback", "registerModule", "moduleObject", "registerTab", "tabObject", "supportLegacyScripts", "RegisterScript", "KyinoClient"})
public final class Script
extends MinecraftInstance {
    private ScriptEngine scriptEngine;
    private final String scriptText;
    @NotNull
    public String scriptName;
    @NotNull
    public String scriptVersion;
    @NotNull
    public String[] scriptAuthors;
    private boolean state;
    private final HashMap<String, JSObject> events;
    private final List<Module> registeredModules;
    private final List<Command> registeredCommands;
    @NotNull
    private final File scriptFile;

    @NotNull
    public final String getScriptName() {
        String string = this.scriptName;
        if (string == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptName");
        }
        return string;
    }

    public final void setScriptName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.scriptName = string;
    }

    @NotNull
    public final String getScriptVersion() {
        String string = this.scriptVersion;
        if (string == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptVersion");
        }
        return string;
    }

    public final void setScriptVersion(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.scriptVersion = string;
    }

    @NotNull
    public final String[] getScriptAuthors() {
        if (this.scriptAuthors == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptAuthors");
        }
        return this.scriptAuthors;
    }

    public final void setScriptAuthors(@NotNull String[] stringArray) {
        Intrinsics.checkParameterIsNotNull(stringArray, "<set-?>");
        this.scriptAuthors = stringArray;
    }

    public final void registerModule(@NotNull JSObject moduleObject, @NotNull JSObject callback) {
        Intrinsics.checkParameterIsNotNull(moduleObject, "moduleObject");
        Intrinsics.checkParameterIsNotNull(callback, "callback");
        ScriptModule module = new ScriptModule(moduleObject);
        LiquidBounce.INSTANCE.getModuleManager().registerModule(module);
        Collection collection = this.registeredModules;
        boolean bl = false;
        collection.add(module);
        callback.call(moduleObject, module);
    }

    public final void registerCommand(@NotNull JSObject commandObject, @NotNull JSObject callback) {
        Intrinsics.checkParameterIsNotNull(commandObject, "commandObject");
        Intrinsics.checkParameterIsNotNull(callback, "callback");
    }

    public final void registerTab(@NotNull JSObject tabObject) {
        Intrinsics.checkParameterIsNotNull(tabObject, "tabObject");
        new ScriptTab(tabObject);
    }

    public final void supportLegacyScripts() {
        if (!StringsKt.contains$default((CharSequence)CollectionsKt.first(StringsKt.lines(this.scriptText)), "api_version=2", false, 2, null)) {
            ClientUtils.getLogger().info("[ScriptAPI] Running script '" + this.scriptFile.getName() + "' with legacy support.");
            URL uRL = LiquidBounce.class.getResource("/assets/minecraft/liquidbounce/scriptapi/legacy.js");
            Intrinsics.checkExpressionValueIsNotNull(uRL, "LiquidBounce::class.java\u2026nce/scriptapi/legacy.js\")");
            URL uRL2 = uRL;
            Charset charset = Charsets.UTF_8;
            boolean bl = false;
            byte[] byArray = TextStreamsKt.readBytes(uRL2);
            boolean bl2 = false;
            boolean bl3 = false;
            String legacyScript = new String(byArray, charset);
            this.scriptEngine.eval(legacyScript);
        }
    }

    public final void on(@NotNull String eventName, @NotNull JSObject handler) {
        Intrinsics.checkParameterIsNotNull(eventName, "eventName");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
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

    public final void import(@NotNull String scriptFile) {
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
    }

    private final void callEvent(String eventName) {
        try {
            JSObject jSObject = this.events.get(eventName);
            if (jSObject != null) {
                jSObject.call(null, new Object[0]);
            }
        }
        catch (Throwable throwable) {
            Logger logger = ClientUtils.getLogger();
            StringBuilder stringBuilder = new StringBuilder().append("[ScriptAPI] Exception in script '");
            String string = this.scriptName;
            if (string == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scriptName");
            }
            logger.error(stringBuilder.append(string).append("'!").toString(), throwable);
        }
    }

    @NotNull
    public final File getScriptFile() {
        return this.scriptFile;
    }

    public Script(@NotNull File scriptFile) {
        List list;
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
        this.scriptFile = scriptFile;
        this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        this.scriptText = FilesKt.readText$default(this.scriptFile, null, 1, null);
        this.events = new HashMap();
        Script script = this;
        boolean bl = false;
        script.registeredModules = list = (List)new ArrayList();
        script = this;
        bl = false;
        script.registeredCommands = list = (List)new ArrayList();
        this.scriptEngine.put("Chat", StaticClass.forClass(Chat.class));
        this.scriptEngine.put("Setting", StaticClass.forClass(Setting.class));
        this.scriptEngine.put("Item", StaticClass.forClass(Item.class));
        this.scriptEngine.put("mc", Script.access$getMc$p$s1046033730());
        this.scriptEngine.put("moduleManager", LiquidBounce.INSTANCE.getModuleManager());
        this.scriptEngine.put("commandManager", LiquidBounce.INSTANCE.getCommandManager());
        this.scriptEngine.put("scriptManager", LiquidBounce.INSTANCE.getScriptManager());
        this.scriptEngine.put("registerScript", new RegisterScript());
        this.supportLegacyScripts();
        this.scriptEngine.eval(this.scriptText);
        this.callEvent("load");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0002H\u0016\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/script/Script$RegisterScript;", "Ljava/util/function/Function;", "Ljdk/nashorn/api/scripting/JSObject;", "Lnet/ccbluex/liquidbounce/script/Script;", "(Lnet/ccbluex/liquidbounce/script/Script;)V", "apply", "scriptObject", "KyinoClient"})
    public final class RegisterScript
    implements Function<JSObject, Script> {
        @Override
        @NotNull
        public Script apply(@NotNull JSObject scriptObject) {
            Intrinsics.checkParameterIsNotNull(scriptObject, "scriptObject");
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
            Object object3 = ScriptUtils.convert(scriptObject.getMember("authors"), String[].class);
            if (object3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            Script.this.setScriptAuthors((String[])object3);
            return Script.this;
        }
    }
}

