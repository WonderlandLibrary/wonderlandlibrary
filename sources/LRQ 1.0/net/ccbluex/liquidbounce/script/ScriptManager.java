/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.io.FilesKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.script;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import kotlin.io.FilesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class ScriptManager {
    private final List<Script> scripts;
    private final File scriptsFolder;
    private final String scriptFileExtension = ".js";

    public final List<Script> getScripts() {
        return this.scripts;
    }

    public final File getScriptsFolder() {
        return this.scriptsFolder;
    }

    public final void loadScripts() {
        if (!this.scriptsFolder.exists()) {
            this.scriptsFolder.mkdir();
        }
        File[] $this$forEach$iv = this.scriptsFolder.listFiles(new FileFilter(this){
            final /* synthetic */ ScriptManager this$0;

            public final boolean accept(File it) {
                return StringsKt.endsWith$default((String)it.getName(), (String)ScriptManager.access$getScriptFileExtension$p(this.this$0), (boolean)false, (int)2, null);
            }
            {
                this.this$0 = scriptManager;
            }
        });
        boolean $i$f$forEach = false;
        File[] fileArray = $this$forEach$iv;
        int n = fileArray.length;
        for (int i = 0; i < n; ++i) {
            File element$iv;
            File it = element$iv = fileArray[i];
            boolean bl = false;
            this.loadScript(it);
        }
    }

    public final void unloadScripts() {
        this.scripts.clear();
    }

    public final void loadScript(File scriptFile) {
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

    public final void importScript(File file) {
        File scriptFile = new File(this.scriptsFolder, file.getName());
        FilesKt.copyTo$default((File)file, (File)scriptFile, (boolean)false, (int)0, (int)6, null);
        this.loadScript(scriptFile);
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully imported script '" + scriptFile.getName() + "'.");
    }

    public final void deleteScript(Script script) {
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

    public ScriptManager() {
        List list;
        ScriptManager scriptManager = this;
        boolean bl = false;
        scriptManager.scripts = list = (List)new ArrayList();
        this.scriptsFolder = new File(LiquidBounce.INSTANCE.getFileManager().dir, "scripts");
        this.scriptFileExtension = ".js";
    }

    public static final /* synthetic */ String access$getScriptFileExtension$p(ScriptManager $this) {
        return $this.scriptFileExtension;
    }
}

