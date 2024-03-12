// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api;

import javax.script.ScriptException;
import java.util.function.Function;
import java.util.Comparator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import java.util.Iterator;
import java.util.HashMap;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import dev.tenacity.utils.Utils;

public class ScriptManager implements Utils
{
    private final File scriptDirectory;
    private final List<Script> scripts;
    
    public ScriptManager() {
        this.scriptDirectory = new File(ScriptManager.mc.mcDataDir, "/Tenacity/Scripts");
        this.scripts = new ArrayList<Script>();
        if (!this.scriptDirectory.exists()) {
            this.scriptDirectory.mkdirs();
        }
    }
    
    public void reloadScripts() {
        final HashMap<Object, Module> moduleList = Tenacity.INSTANCE.getModuleCollection().getModuleMap();
        this.scripts.removeIf(Script::isReloadable);
        final File[] scriptFiles = this.scriptDirectory.listFiles((dir, name) -> name.endsWith(".js"));
        if (scriptFiles == null) {
            return;
        }
        for (final Module moduleValue : moduleList.values()) {
            if (moduleValue.getCategory().equals(Category.SCRIPTS) && moduleValue.isEnabled() && ((ScriptModule)moduleValue).isReloadable()) {
                moduleValue.toggleSilent();
            }
        }
        moduleList.values().removeIf(moduleClass -> moduleClass.getCategory().equals(Category.SCRIPTS) && moduleClass.isReloadable());
        for (final File scriptFile : scriptFiles) {
            if (!this.scripts.stream().anyMatch(script -> script.getFile().equals(scriptFile))) {
                try {
                    this.scripts.add(new Script(scriptFile));
                }
                catch (Exception e) {
                    ChatUtil.print(false, "");
                    ChatUtil.scriptError(scriptFile, "(Failed to load)");
                    ChatUtil.print(false, e.getMessage().replace("\r", "").replace("<eval>", "§l" + scriptFile.getName() + "§r") + "\n");
                    NotificationManager.post(NotificationType.WARNING, "Failed to load script §l" + scriptFile.getName(), "Check chat for more information.");
                }
            }
        }
        final ScriptModule scriptModule;
        this.scripts.forEach(script -> scriptModule = moduleList.put(script.getName() + script.getDescription(), script.getScriptModule()));
        this.scripts.stream().filter(script -> !script.isReloadable()).forEach(script -> {
            script.setReloadable(true);
            script.getScriptModule().setReloadable(true);
            return;
        });
        ModuleCollection.reloadModules = true;
        NotificationManager.post(NotificationType.SUCCESS, "Script Manager", "Local scripts loaded");
    }
    
    public String[] getScriptFileNameList() {
        final List<File> scriptFiles = Arrays.asList((File[])Objects.requireNonNull((T[])this.scriptDirectory.listFiles((dir, name) -> name.endsWith(".js"))));
        final HashMap<File, BasicFileAttributes> attributesHashMap = new HashMap<File, BasicFileAttributes>();
        try {
            for (final File scriptFile : scriptFiles) {
                attributesHashMap.put(scriptFile, Files.readAttributes(scriptFile.toPath(), BasicFileAttributes.class, new LinkOption[0]));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        scriptFiles.sort(Comparator.comparingLong(file -> attributesHashMap.get(file).lastModifiedTime().toMillis()).reversed());
        return scriptFiles.stream().map((Function<? super Object, ?>)File::getName).toArray(String[]::new);
    }
    
    public boolean processScriptData(final File scriptFile) {
        Script script;
        try {
            script = new Script(scriptFile);
        }
        catch (ScriptException e) {
            NotificationManager.post(NotificationType.WARNING, "Error", e.getMessage(), 10.0f);
            return false;
        }
        if (script.getEventHashMap().isEmpty()) {
            NotificationManager.post(NotificationType.WARNING, "Error", "You cannot upload empty scripts", 10.0f);
            return false;
        }
        return true;
    }
    
    public File getScriptDirectory() {
        return this.scriptDirectory;
    }
    
    public List<Script> getScripts() {
        return this.scripts;
    }
}
