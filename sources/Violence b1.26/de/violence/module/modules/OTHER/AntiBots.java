package de.violence.module.modules.OTHER;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;

public class AntiBots extends Module {
   static List arrayList = new ArrayList();

   public AntiBots() {
      super("AntiBots", Category.OTHER);
   }

   public static List getArrayList() {
      return arrayList;
   }

   public static boolean canHit(Entity e) {
      return arrayList.contains(e);
   }
}
