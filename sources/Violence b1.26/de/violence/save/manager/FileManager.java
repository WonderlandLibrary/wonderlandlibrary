package de.violence.save.manager;

import de.violence.save.EasySaveGame;

public class FileManager {
   public static EasySaveGame toggledModules = new EasySaveGame("toggled_modules.cfg");
   public static EasySaveGame moduleKeybinds = new EasySaveGame("keybind_modules.cfg");
   public static EasySaveGame gui = new EasySaveGame("gui.cfg");
}
