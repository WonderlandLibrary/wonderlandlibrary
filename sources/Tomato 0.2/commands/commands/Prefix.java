/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.files.files.Misc;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Logger;
import java.io.IOException;

public class Prefix
extends Command {
    public static Property<String> prefix;

    public Prefix() {
        super("Prefix", null, new String[]{"pf"}, "<prefix>", "Change the client's prefix for commands.");
        prefix = new Property<String>(null, "prefix", "-");
    }

    @EventTarget
    public void onTick(EventTick tick) {
        block9 : {
            try {
                String cmd2 = EventChatSend.getMessage().split(" ")[1];
                if (!cmd2.equalsIgnoreCase("reset")) {
                    try {
                        String newprefix = EventChatSend.getMessage().split(" ")[1];
                        if (!Prefix.isVowel(newprefix) && newprefix.length() < 2) {
                            Prefix.prefix.value = newprefix;
                            Logger.logChat("Prefix set to \u00a7e" + (String)Prefix.prefix.value);
                            break block9;
                        }
                        Logger.logChat("Can not make that a prefix!");
                    }
                    catch (Exception e) {
                        Logger.logChat("Invalid prefix!");
                    }
                    break block9;
                }
                if (cmd2.equalsIgnoreCase("reset")) {
                    Logger.logChat("Prefix set to default one. (-)");
                    Prefix.prefix.value = (String)Prefix.prefix.defaultvalue;
                }
            }
            catch (Exception e2) {
                Logger.logChat("Thats not a prefix!");
            }
        }
        try {
            Cheese.getInstance();
            Cheese.fileManager.getFile(Misc.class).saveFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.Toggle();
    }

    public static boolean isVowel(String c2) {
        if ("AEIOUaeiou".indexOf(c2) != -1) {
            return true;
        }
        return false;
    }
}

