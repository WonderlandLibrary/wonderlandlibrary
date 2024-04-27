/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.script.Script;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0007J\u0006\u0010\u0011\u001a\u00020\u000fJ\u0006\u0010\u0012\u001a\u00020\u000fJ\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000bJ\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u000bJ\u0006\u0010\u0017\u001a\u00020\u000fJ\u0006\u0010\u0018\u001a\u00020\u000fJ\u0006\u0010\u0019\u001a\u00020\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/script/ScriptManager;", "", "()V", "scriptFileExtension", "", "scripts", "", "Lnet/dev/important/script/Script;", "getScripts", "()Ljava/util/List;", "scriptsFolder", "Ljava/io/File;", "getScriptsFolder", "()Ljava/io/File;", "deleteScript", "", "script", "disableScripts", "enableScripts", "importScript", "file", "loadScript", "scriptFile", "loadScripts", "reloadScripts", "unloadScripts", "LiquidBounce"})
public final class ScriptManager {
    @NotNull
    private final List<Script> scripts = new ArrayList();
    @NotNull
    private final File scriptsFolder;
    @NotNull
    private final String scriptFileExtension;

    public ScriptManager() {
        this.scriptsFolder = new File(Client.INSTANCE.getFileManager().dir, "scripts");
        this.scriptFileExtension = ".js";
    }

    @NotNull
    public final List<Script> getScripts() {
        return this.scripts;
    }

    @NotNull
    public final File getScriptsFolder() {
        return this.scriptsFolder;
    }

    public final void loadScripts() {
        if (!this.scriptsFolder.exists()) {
            this.scriptsFolder.mkdir();
        }
        File[] fileArray = this.scriptsFolder.listFiles(arg_0 -> ScriptManager.loadScripts$lambda-0(this, arg_0));
        Intrinsics.checkNotNullExpressionValue(fileArray, "scriptsFolder.listFiles(\u2026h(scriptFileExtension) })");
        Object[] $this$forEach$iv = fileArray;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            File it = (File)element$iv;
            boolean bl = false;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            this.loadScript(it);
        }
    }

    public final void unloadScripts() {
        this.scripts.clear();
    }

    public final void loadScript(@NotNull File scriptFile) {
        Intrinsics.checkNotNullParameter(scriptFile, "scriptFile");
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

    public final void importScript(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        File scriptFile = new File(this.scriptsFolder, file.getName());
        FilesKt.copyTo$default(file, scriptFile, false, 0, 6, null);
        this.loadScript(scriptFile);
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully imported script '" + scriptFile.getName() + "'.");
    }

    public final void deleteScript(@NotNull Script script) {
        Intrinsics.checkNotNullParameter(script, "script");
        script.onDisable();
        this.scripts.remove(script);
        script.getScriptFile().delete();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully deleted script '" + script.getScriptFile().getName() + "'.");
    }

    public final void reloadScripts() {
        this.disableScripts();
        this.unloadScripts();
        this.loadScripts();
        this.enableScripts();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully reloaded scripts.");
    }

    private static final boolean loadScripts$lambda-0(ScriptManager this$0, File it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        String string = it.getName();
        Intrinsics.checkNotNullExpressionValue(string, "it.name");
        return StringsKt.endsWith$default(string, this$0.scriptFileExtension, false, 2, null);
    }
}

