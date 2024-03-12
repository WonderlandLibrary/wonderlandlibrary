// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import java.util.Comparator;

public class CustomItemsComparator implements Comparator
{
    @Override
    public int compare(final Object o1, final Object o2) {
        final CustomItemProperties customitemproperties = (CustomItemProperties)o1;
        final CustomItemProperties customitemproperties2 = (CustomItemProperties)o2;
        return (customitemproperties.weight != customitemproperties2.weight) ? (customitemproperties2.weight - customitemproperties.weight) : (Config.equals(customitemproperties.basePath, customitemproperties2.basePath) ? customitemproperties.name.compareTo(customitemproperties2.name) : customitemproperties.basePath.compareTo(customitemproperties2.basePath));
    }
}
