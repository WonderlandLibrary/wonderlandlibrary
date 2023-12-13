/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.properties;

import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import java.util.ArrayList;

public class PropertyManager {
    public static ArrayList<Property> properties = new ArrayList();

    public static ArrayList<Property> getProperties() {
        return properties;
    }

    public static Property getPropertybyName(String name) {
        for (Property s : PropertyManager.getProperties()) {
            if (!s.getName().equalsIgnoreCase(name)) continue;
            return s;
        }
        return null;
    }

    public ArrayList<Property> getPropertiesFromModule(Module module) {
        ArrayList<Property> array = new ArrayList<Property>();
        for (Property p2 : properties) {
            if (p2.getModule() != module) continue;
            array.add(p2);
        }
        return array;
    }
}

