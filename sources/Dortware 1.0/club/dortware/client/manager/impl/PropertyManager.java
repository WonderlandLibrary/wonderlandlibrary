package club.dortware.client.manager.impl;

import club.dortware.client.manager.Manager;
import club.dortware.client.property.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyManager extends Manager<Property<?, ?>> {

    public PropertyManager() {
        super(new ArrayList<>());
    }

    @Override
    public void onCreated() {

    }

    public Property<?, ?> getProperty(Object object, String name) {
        for (Property<?, ?> property : getList()) {
            if (property.getOwner() == object && property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }
        return null;
    }


    public List<Property<?, ?>> getSettingsByMod(Object owner) {
        List<Property<?, ?>> list = new ArrayList<>();
        for (Property<?, ?> property : getList()) {
            if (property.getOwner() == owner) {
                list.add(property);
            }
        }
        return list;
    }

}
