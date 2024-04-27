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
    private final String scriptFileExtension = ".js";
    private final File scriptsFolder;
    private final List scripts;

    public static final String access$getScriptFileExtension$p(ScriptManager scriptManager) {
        return scriptManager.scriptFileExtension;
    }

    public final void loadScripts() {
        if (!this.scriptsFolder.exists()) {
            this.scriptsFolder.mkdir();
        }
        File[] fileArray = this.scriptsFolder.listFiles(new FileFilter(this){
            final ScriptManager this$0;

            static {
            }
            {
                this.this$0 = scriptManager;
            }

            public final boolean accept(File file) {
                return StringsKt.endsWith$default((String)file.getName(), (String)ScriptManager.access$getScriptFileExtension$p(this.this$0), (boolean)false, (int)2, null);
            }
        });
        boolean bl = false;
        File[] fileArray2 = fileArray;
        int n = fileArray2.length;
        for (int i = 0; i < n; ++i) {
            File file;
            File file2 = file = fileArray2[i];
            boolean bl2 = false;
            this.loadScript(file2);
        }
    }

    public final void importScript(File file) {
        File file2 = new File(this.scriptsFolder, file.getName());
        FilesKt.copyTo$default((File)file, (File)file2, (boolean)false, (int)0, (int)6, null);
        this.loadScript(file2);
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully imported script '" + file2.getName() + "'.");
    }

    public final void reloadScripts() {
        this.disableScripts();
        this.unloadScripts();
        this.loadScripts();
        this.enableScripts();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully reloaded scripts.");
    }

    public final void loadScript(File file) {
        try {
            this.scripts.add(new Script(file));
            ClientUtils.getLogger().info("[ScriptAPI] Successfully loaded script '" + file.getName() + "'.");
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Failed to load script '" + file.getName() + "'.", throwable);
        }
    }

    public final void enableScripts() {
        Iterable iterable = this.scripts;
        boolean bl = false;
        for (Object t : iterable) {
            Script script = (Script)t;
            boolean bl2 = false;
            script.onEnable();
        }
    }

    public final File getScriptsFolder() {
        return this.scriptsFolder;
    }

    public final void unloadScripts() {
        this.scripts.clear();
    }

    public final List getScripts() {
        return this.scripts;
    }

    public final void deleteScript(Script script) {
        script.onDisable();
        this.scripts.remove(script);
        script.getScriptFile().delete();
        ClientUtils.getLogger().info("[ScriptAPI]  Successfully deleted script '" + script.getScriptFile().getName() + "'.");
    }

    public ScriptManager() {
        List list;
        ScriptManager scriptManager = this;
        boolean bl = false;
        scriptManager.scripts = list = (List)new ArrayList();
        this.scriptsFolder = new File(LiquidBounce.INSTANCE.getFileManager().dir, "scripts");
        this.scriptFileExtension = ".js";
    }

    public final void disableScripts() {
        Iterable iterable = this.scripts;
        boolean bl = false;
        for (Object t : iterable) {
            Script script = (Script)t;
            boolean bl2 = false;
            script.onDisable();
        }
    }
}

