package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiResourcePackSelected extends GuiResourcePackList {
   public GuiResourcePackSelected(Minecraft var1, int var2, int var3, List var4) {
      super(var1, var2, var3, var4);
   }

   protected String getListHeader() {
      return I18n.format("resourcePack.selected.title");
   }
}
