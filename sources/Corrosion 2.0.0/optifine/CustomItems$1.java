package optifine;

import java.util.Comparator;

final class CustomItems$1 implements Comparator {
    public int compare(Object p_compare_1_, Object p_compare_2_) {
        CustomItemProperties customitemproperties = (CustomItemProperties)p_compare_1_;
        CustomItemProperties customitemproperties1 = (CustomItemProperties)p_compare_2_;
        return customitemproperties.layer != customitemproperties1.layer ? customitemproperties.layer - customitemproperties1.layer : (customitemproperties.weight != customitemproperties1.weight ? customitemproperties1.weight - customitemproperties.weight : (!customitemproperties.basePath.equals(customitemproperties1.basePath) ? customitemproperties.basePath.compareTo(customitemproperties1.basePath) : customitemproperties.name.compareTo(customitemproperties1.name)));
    }
}
