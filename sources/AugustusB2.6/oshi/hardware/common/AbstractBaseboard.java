// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.annotation.concurrent.Immutable;
import oshi.hardware.Baseboard;

@Immutable
public abstract class AbstractBaseboard implements Baseboard
{
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("manufacturer=").append(this.getManufacturer()).append(", ");
        sb.append("model=").append(this.getModel()).append(", ");
        sb.append("version=").append(this.getVersion()).append(", ");
        sb.append("serial number=").append(this.getSerialNumber());
        return sb.toString();
    }
}
