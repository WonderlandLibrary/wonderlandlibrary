package best.azura.scripting;

import best.azura.client.impl.Client;
import best.azura.scripting.functions.RegisterModule;
import best.azura.scripting.tables.JavaTable;
import fr.ducouscous.csl.compiler.Compiler;
import fr.ducouscous.csl.running.Script;
import fr.ducouscous.mcscript.ScriptRegisterer;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptManager {
    private final ArrayList<Script> scripts = new ArrayList<>();
    private final ScriptRegisterer scriptRegisterer = new ScriptRegisterer();
    private final Compiler compiler = new Compiler();

    public ScriptManager() {
        loadScripts();
    }

    public void registerScript(Script script) {
        script.setGlobalValue("registerModule", new RegisterModule());
        script.setGlobalValue("Java", new JavaTable());
    }

    public void loadScripts() {
        scripts.clear();
        final File scriptsDir = Client.INSTANCE.getConfigManager().getScriptsDirectory();
        if (!scriptsDir.exists() && scriptsDir.mkdir()) Client.INSTANCE.getLogger().info("Created scripts directory!");
        try {
            for (final File file : Objects.requireNonNull(scriptsDir.listFiles())) {
                if (!file.getName().endsWith(".as")) continue;
                final Script script = compiler.compile(file);
                final AzuraScript azuraScript = new AzuraScript(script);
                scriptRegisterer.register(azuraScript);
                registerScript(azuraScript);
                azuraScript.load();
                azuraScript.main();
                scripts.add(azuraScript);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<Script> getScripts() {
        return scripts;
    }
}