/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.opencl.CLObject;

interface InfoUtil<T extends CLObject> {
    public int getInfoInt(T var1, int var2);

    public long getInfoSize(T var1, int var2);

    public long[] getInfoSizeArray(T var1, int var2);

    public long getInfoLong(T var1, int var2);

    public String getInfoString(T var1, int var2);
}

