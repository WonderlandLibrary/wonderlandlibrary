package wtf.expensive.command.impl;

import org.luaj.vm2.globals.Standarts;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.scripts.DefaultScript;

import java.io.File;
import java.nio.file.Files;

@CommandInfo(name = "obf", description = "Обфускация скрипта.")
public class ObfuscatorCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 1) {
            String scriptName = args[1];
            if (Files.exists(new File(mc.gameDir, "scripts/" + scriptName + ".lua").toPath())) {
                DefaultScript script = null;
                for (DefaultScript script1 : Managment.SCRIPT_MANAGER.scripts) {
                    if (script1.scriptName.equalsIgnoreCase(scriptName + ".lua")) {
                        script = script1;
                    }
                }
                if (script != null) {
                    script.processScript(Standarts.standardGlobals(), Files.newInputStream(new File(mc.gameDir, "scripts/" + scriptName + ".lua").toPath()),  "=stdin", Files.newOutputStream(new File(mc.gameDir, "scripts/" + scriptName + "-obf.lua").toPath()));
                    sendMessage("Скрипт сохранен с названием: " + scriptName + "-obf.lua");
                }
            } else {
                sendMessage("Скрипт не найден!");
            }
        }
    }

    @Override
    public void error() {

    }
}
