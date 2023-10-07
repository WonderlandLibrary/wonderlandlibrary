package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.modules.AbstractModule;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public final class HideCommand extends NovoCommand {

    public HideCommand(@NonNull Novoline novoline) {
        super(novoline, "Hide", "Hides modules from arraylist", Arrays.asList("hide", "h"));
    }

    @Override
    public void process(String[] args) {
        if (args.length == 1) {
            final String arg = args[0];
            final AbstractModule module = this.novoline.getModuleManager().getByNameIgnoreCase(arg);

            if (module == null) {
                notifyError("Module " + arg + " was not found!");
                return;
            }

            module.setHidden(!module.isHidden());
            notify((module.isHidden() ? "Hidden" : "Shown") + " " + module.getName());
        } else {
            notifyError("Use .hide <module> to hide a module!");
        }
    }

}
