package optifine;

import java.util.Comparator;

public class CustomItemsComparator implements Comparator {
   public int compare(Object var1, Object var2) {
      CustomItemProperties var3 = (CustomItemProperties)var1;
      CustomItemProperties var4 = (CustomItemProperties)var2;
      return var3.weight != var4.weight ? var4.weight - var3.weight : (!Config.equals(var3.basePath, var4.basePath) ? var3.basePath.compareTo(var4.basePath) : var3.name.compareTo(var4.name));
   }
}
