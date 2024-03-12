package dev.tenacity.scripting.api;

import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.Utils;
import dev.tenacity.utils.player.ChatUtil;
import lombok.Getter;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Getter
public class ScriptManager implements Utils {

    private final File scriptDirectory = new File(mc.mcDataDir, "/Tenacity/Scripts");
    private final List<Script> scripts = new ArrayList<>();

    public ScriptManager() {
        if (!scriptDirectory.exists()) scriptDirectory.mkdirs();
    }

    public void reloadScripts() {
        HashMap<Object, Module> moduleList = Tenacity.INSTANCE.getModuleCollection().getModuleMap();

        scripts.removeIf(Script::isReloadable);

        File[] scriptFiles = scriptDirectory.listFiles(((dir, name) -> name.endsWith(".js")));

        if (scriptFiles == null) return;

        for (Module moduleValue : moduleList.values()) {
            if (moduleValue.getCategory().equals(Category.SCRIPTS) && moduleValue.isEnabled()) {
                if (((ScriptModule) moduleValue).isReloadable()) {
                    moduleValue.toggleSilent();
                }
            }
        }

        moduleList.values().removeIf(moduleClass ->
                moduleClass.getCategory().equals(Category.SCRIPTS) && ((ScriptModule) moduleClass).isReloadable());

        for (File scriptFile : scriptFiles) {
            if (scripts.stream().anyMatch(script -> script.getFile().equals(scriptFile))) continue;
            try {
                scripts.add(new Script(scriptFile));
            } catch (Exception e) {
                ChatUtil.print(false, "");
                ChatUtil.scriptError(scriptFile, "(Failed to load)");
                ChatUtil.print(false, e.getMessage().replace("\r", "").replace("<eval>", "§l" + scriptFile.getName() + "§r") + "\n");
                NotificationManager.post(NotificationType.WARNING, "Failed to load script §l" + scriptFile.getName(), "Check chat for more information.");
            }
        }

        scripts.forEach(script -> moduleList.put(script.getName() + script.getDescription(), script.getScriptModule()));

        scripts.stream().filter(script -> !script.isReloadable()).forEach(script -> {
            script.setReloadable(true);
            script.getScriptModule().setReloadable(true);
        });


        ModuleCollection.reloadModules = true;
        //ScriptPanel.reInit = true;
        NotificationManager.post(NotificationType.SUCCESS, "Script Manager", "Local scripts loaded");
    }


    public String[] getScriptFileNameList() {
        List<File> scriptFiles = Arrays.asList(Objects.requireNonNull(scriptDirectory.listFiles(((dir, name) -> name.endsWith(".js")))));
        HashMap<File, BasicFileAttributes> attributesHashMap = new HashMap<>();
        try {
            for (File scriptFile : scriptFiles) {
                attributesHashMap.put(scriptFile, Files.readAttributes(scriptFile.toPath(), BasicFileAttributes.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scriptFiles.sort(Comparator.<File>comparingLong(file -> attributesHashMap.get(file).lastModifiedTime().toMillis()).reversed());

        return scriptFiles.stream().map(File::getName).toArray(String[]::new);
    }


    public boolean processScriptData(File scriptFile) {
        Script script;
        try {
            script = new Script(scriptFile);
        } catch (ScriptException e) {
            NotificationManager.post(NotificationType.WARNING, "Error", e.getMessage(), 10);
            return false;
        }

        if (script.getEventHashMap().isEmpty()) {
            NotificationManager.post(NotificationType.WARNING, "Error", "You cannot upload empty scripts", 10);
            return false;
        }

        return true;
    }

}
