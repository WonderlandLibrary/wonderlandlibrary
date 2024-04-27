/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class SharedPropertyMap
extends PropertyMap {
    private SwitchPoint switchPoint = new SwitchPoint();
    private static final long serialVersionUID = 2166297719721778876L;

    public SharedPropertyMap(PropertyMap map) {
        super(map);
    }

    @Override
    public void propertyAdded(Property property, boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyAdded(property, isSelf);
    }

    @Override
    public void propertyDeleted(Property property, boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyDeleted(property, isSelf);
    }

    @Override
    public void propertyModified(Property oldProperty, Property newProperty, boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyModified(oldProperty, newProperty, isSelf);
    }

    @Override
    synchronized boolean isValidSharedProtoMap() {
        return this.switchPoint != null;
    }

    @Override
    synchronized SwitchPoint getSharedProtoSwitchPoint() {
        return this.switchPoint;
    }

    synchronized void invalidateSwitchPoint() {
        if (this.switchPoint != null) {
            assert (!this.switchPoint.hasBeenInvalidated());
            SwitchPoint.invalidateAll(new SwitchPoint[]{this.switchPoint});
            this.switchPoint = null;
        }
    }
}

