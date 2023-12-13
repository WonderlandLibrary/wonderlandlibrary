package koks.manager.command.impl;

import koks.Koks;
import koks.manager.command.Command;
import koks.manager.command.CommandInfo;
import koks.manager.file.impl.Binds;
import koks.manager.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 03:52
 */

@CommandInfo(name = "bind")
public class Bind extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            Module module = Koks.getKoks().moduleManager.getModule(args[0]);
            if (module != null) {
                module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                Koks.getKoks().fileManager.writeFile(Binds.class);
                sendmsg("Bound §e" + module.getName() + " §7to Key §e" + args[1], true);
            } else {
                sendError("Module", args[0] + " doesn't exist!");
            }
        } else {
            sendError("Usage", ".bind [Module] [Key]");
        }
    }
}
