package org.alphacentauri.modules;

import org.alphacentauri.management.modules.Module;

public class ModuleNoIRC extends Module {
   public ModuleNoIRC() {
      super("NoIRC", "Disables IRC", new String[]{"noirc"}, Module.Category.Misc, 4092764);
   }
}
