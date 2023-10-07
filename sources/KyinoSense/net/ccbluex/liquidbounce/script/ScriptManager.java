/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007J\u0006\u0010\r\u001a\u00020\u000bJ\u0006\u0010\u000e\u001a\u00020\u000bJ\u000e\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u000bJ\u0006\u0010\u0013\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/script/ScriptManager;", "", "()V", "scriptFileExtension", "", "scripts", "", "Lnet/ccbluex/liquidbounce/script/Script;", "getScripts", "()Ljava/util/List;", "deleteScript", "", "script", "disableScripts", "enableScripts", "loadScript", "scriptFile", "Ljava/io/File;", "reloadScripts", "unloadScripts", "KyinoClient"})
public final class ScriptManager {
    @NotNull
    private final List<Script> scripts;
    private final String scriptFileExtension;

    @NotNull
    public final List<Script> getScripts() {
        return this.scripts;
    }

    public final void unloadScripts() {
        this.scripts.clear();
    }

    public final void loadScript(@NotNull File scriptFile) {
        Intrinsics.checkParameterIsNotNull(scriptFile, "scriptFile");
        try {
            this.scripts.add(new Script(scriptFile));
            ClientUtils.getLogger().info("[ScriptAPI] Successfully loaded script '" + scriptFile.getName() + "'.");
        }
        catch (Throwable t) {
            ClientUtils.getLogger().error("[ScriptAPI] Failed to load script '" + scriptFile.getName() + "'.", t);
        }
    }

    public final void enableScripts() {
        Iterable $this$forEach$iv = this.scripts;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Script it = (Script)element$iv;
            boolean bl = false;
            it.onEnable();
        }
    }

    public final void disableScripts() {
        Iterable $this$forEach$iv = this.scripts;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Script it = (Script)element$iv;
            boolean bl = false;
            it.onDisable();
        }
    }

    public final void deleteScript(@NotNull Script script) {
        Intrinsics.checkParameterIsNotNull(script, "script");
        script.onDisable();
        this.scripts.remove(script);
        script.getScriptFile().delete();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully deleted script '" + script.getScriptFile().getName() + "'.");
    }

    public final void reloadScripts() {
        this.disableScripts();
        this.unloadScripts();
        this.enableScripts();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully reloaded scripts.");
    }

    public ScriptManager() {
        List list;
        ScriptManager scriptManager = this;
        boolean bl = false;
        scriptManager.scripts = list = (List)new ArrayList();
        this.scriptFileExtension = ".js";
    }
}

