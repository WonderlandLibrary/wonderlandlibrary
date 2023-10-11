package cow.milkgod.cheese.commands.commands;

import cow.milkgod.cheese.commands.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import cow.milkgod.cheese.*;
import cow.milkgod.cheese.module.*;
import java.util.*;
import com.darkmagician6.eventapi.*;

public class Legit extends Command
{
    private ArrayList<Module> toggledMods;
    
    public Legit() {
        super("Legit", "Invalid Option! ", new String[] { "lg" }, "<backon>", "Disables non-legit modules. [You can toggle back on]");
        this.toggledMods = new ArrayList<Module>();
    }
    
    @EventTarget
    public void onTick(final EventTick e) {
        if (this.getState()) {
            try {
                final String arg = EventChatSend.getMessage().split(" ")[1];
                if (arg.equalsIgnoreCase("backon") || arg.equalsIgnoreCase("bo")) {
                    for (final Module mod : this.toggledMods) {
                        if (!mod.getState() && !mod.getName().equalsIgnoreCase("Freecam") && !mod.getName().equalsIgnoreCase("Glide")) {
                            mod.toggleModule();
                        }
                    }
                    Logger.logChat("Toggled disabled mods back on.");
                }
                else {
                    Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                }
            }
            catch (Exception ex) {
                Cheese.getInstance();
                final ModuleManager moduleManager = Cheese.moduleManager;
                for (final Module m : ModuleManager.activeModules) {
                    if (m.getState() && m.getCategory() != Category.RENDER) {
                        this.toggledMods.add(m);
                        m.toggleModule();
                    }
                }
            }
            this.Toggle();
        }
    }
}
