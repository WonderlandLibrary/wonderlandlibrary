package org.alphacentauri.commands;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class CommandExport implements ICommandHandler {
   public String getName() {
      return "Export";
   }

   public boolean execute(Command cmd) {
      StringBuilder scriptBuilder = new StringBuilder();

      for(Module module : AC.getModuleManager().all()) {
         String mod = module.getAliases()[0];
         scriptBuilder.append("exec bypass ").append(module.getBypass().name()).append(" ").append(mod).append("\n");

         for(Property property : AC.getPropertyManager().ofModule(module)) {
            if(property.isVisible()) {
               scriptBuilder.append("exec ").append(mod).append(" ").append(property.getName()).append(" ").append(property.value).append("\n");
            }
         }
      }

      scriptBuilder.append("exec clearchat\n");
      scriptBuilder.append("settitle Settings\n");
      scriptBuilder.append("print applied!");
      GuiScreen.setClipboardString(scriptBuilder.toString());
      AC.addChat(this.getName(), "Config has been saved as script to your clipboard.");
      return true;
   }

   public String[] getAliases() {
      return new String[]{"export"};
   }

   public String getDesc() {
      return "Export your configs";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
