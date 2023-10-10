package org.alphacentauri.modules;

import org.alphacentauri.management.modules.Module;

public class ModuleNoScoreboard extends Module {
   public ModuleNoScoreboard() {
      super("NoScoreboard", "Hides the scoreboard", new String[]{"noscoreboard"}, Module.Category.Render, 11110156);
   }
}
