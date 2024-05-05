package intent.AquaDev.aqua.command.impl.commands;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.command.Command;
import intent.AquaDev.aqua.modules.Module;
import org.lwjgl.input.Keyboard;

public class bind
extends Command {
    public bind() {
        super("bind");
    }

    public void execute(String[] args) {
        try {
            Module mod = Aqua.moduleManager.getModuleByName(args[0]);
            if (args.length != 2) {
                return;
            }
            String key = args[1];
            Aqua.INSTANCE.fileUtil.saveKeys();
            Aqua.INSTANCE.fileUtil.saveModules();
            if (mod != null) {
                mod.setKeyBind(Keyboard.getKeyIndex((String)args[1].toUpperCase()));
            }
            assert (mod != null);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
