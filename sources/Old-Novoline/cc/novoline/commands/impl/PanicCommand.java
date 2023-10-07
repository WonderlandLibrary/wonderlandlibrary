package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.combat.AntiBot;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PanicCommand extends NovoCommand {

    /* constructors */
    public PanicCommand(@NonNull Novoline novoline) {
        super(novoline, "p", "Disable modules", "panic");
    }

    /* methods */
    @Override
    public void process(String[] args) {
        if (args.length > 1) {
            notifyError("Use .panic or .panic (module type)");
            return;
        }

        if (args.length == 0) {
            for (ModuleHolder<?> moduleHolder : this.novoline.getModuleManager().getModuleManager().values()) {
                final AbstractModule module = moduleHolder.getModule();
                if (module.isEnabled() && !(module instanceof AntiBot)) {
                    module.toggle();
                }
            }
        } else {
            for (ModuleHolder<?> moduleHolder : this.novoline.getModuleManager().getModuleManager().values()) {
                final AbstractModule module = moduleHolder.getModule();
                if (module.isEnabled() && !(module instanceof AntiBot) && module.getType().toString().equalsIgnoreCase(args[0])) {
                    module.toggle();
                }
            }
        }
    }
}
