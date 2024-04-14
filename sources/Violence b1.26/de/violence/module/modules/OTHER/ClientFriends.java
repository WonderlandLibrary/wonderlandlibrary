package de.violence.module.modules.OTHER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class ClientFriends extends Module {
   private VSetting ircFriends = new VSetting("Irc-Friends", this, false);

   public ClientFriends() {
      super("ClientFriends", Category.OTHER);
   }
}
