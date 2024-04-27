package me.aquavit.liquidsense.script;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {

    private final List<Script> scripts = new ArrayList<>();
    private final File scriptsFolder = new File(LiquidSense.fileManager.dir, "scripts");
    private final String scriptFileExtension = ".js";

    public File getScriptsFolder() {
        return scriptsFolder;
    }

    /**
     * Loads all scripts inside the scripts folder.
     */
    public void loadScripts() {
        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdir();
        }

        File[] scriptFiles = scriptsFolder.listFiles(file -> file.getName().endsWith(scriptFileExtension));

        if (scriptFiles != null) {
            for (File scriptFile : scriptFiles) {
                loadScript(scriptFile);
            }
        }
    }

    /**
     * Unloads all scripts.
     */
    public void unloadScripts() {
        scripts.clear();
    }

    /**
     * Loads a script from a file.
     */
    public void loadScript(File scriptFile) {
        try {
            Script script = new Script(scriptFile);
            script.initScript();
            scripts.add(script);
        } catch (Throwable t) {
            ClientUtils.getLogger().error("[ScriptAPI] Failed to load script '" + scriptFile.getName() + "'.", t);
        }
    }

    /**
     * Enables all scripts.
     */
    public void enableScripts() {
        for (Script script : scripts) {
            script.onEnable();
        }
    }

    /**
     * Disables all scripts.
     */
    public void disableScripts() {
        for (Script script : scripts) {
            script.onDisable();
        }
    }


    public void importScript(final File file) {
        File scriptFile = new File(scriptsFolder, file.getName());
        try {
            FileUtils.copyFile(file,scriptFile);
        }catch (Exception e){
            ClientUtils.getLogger().info("[ScriptAPI] Failed to copy script '" + scriptFile.getName() + "'.");
        }
        loadScript(scriptFile);
        ClientUtils.getLogger().info("[ScriptAPI] Successfully imported script '" + scriptFile.getName() + "'.");

        /*
        File scriptFile = new File(scriptsFolder, file.getName());

        try {
            Files.copy(file.toPath(), scriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            loadScript(scriptFile);
            ClientUtils.getLogger().info("[ScriptAPI] Successfully imported script '" + scriptFile.getName() + "'.");
        } catch (IOException e) {
            ClientUtils.getLogger().error("[ScriptAPI] Failed to import script '" + scriptFile.getName() + "'.", e);
        }
         */
    }

    /**
     * Deletes a script.
     * @param script Script to be deleted.
     */
    public void deleteScript(Script script) {
        script.onDisable();
        scripts.remove(script);
        script.scriptFile.delete();

        ClientUtils.getLogger().info("[ScriptAPI] Successfully deleted script '" + script.scriptFile.getName() + "'.");
    }

    /**
     * Reloads all scripts.
     */
    public void reloadScripts() {
        disableScripts();
        unloadScripts();
        loadScripts();
        enableScripts();

        ClientUtils.getLogger().info("[ScriptAPI] Successfully reloaded scripts.");
    }

}