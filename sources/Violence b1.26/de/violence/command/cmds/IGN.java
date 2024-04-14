package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.Minecraft;

public class IGN extends Command {
   public String getDescription() {
      return "Copy your ingame name.";
   }

   public String getName() {
      return "ign";
   }

   public String getUsage() {
      return ".ign";
   }

   public void onCommand(String[] args) {
      StringSelection selection = new StringSelection(Minecraft.getMinecraft().getSession().getUsername());
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, (ClipboardOwner)null);
      Violence.getViolence().sendChat("Ingame name copied.");
   }
}
