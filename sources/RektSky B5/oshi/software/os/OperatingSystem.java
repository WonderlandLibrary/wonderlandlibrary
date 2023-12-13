/*
 * Decompiled with CFR 0.152.
 */
package oshi.software.os;

import oshi.software.os.OperatingSystemVersion;

public interface OperatingSystem {
    public String getFamily();

    public String getManufacturer();

    public OperatingSystemVersion getVersion();
}

