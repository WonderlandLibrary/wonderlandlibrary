package optifine;

import java.util.Comparator;

public class CustomItemsComparator
  implements Comparator
{
  public CustomItemsComparator() {}
  
  public int compare(Object o1, Object o2)
  {
    CustomItemProperties p1 = (CustomItemProperties)o1;
    CustomItemProperties p2 = (CustomItemProperties)o2;
    return !Config.equals(p1.basePath, p2.basePath) ? p1.basePath.compareTo(p2.basePath) : p1.weight != p2.weight ? p2.weight - p1.weight : p1.name.compareTo(p2.name);
  }
}
