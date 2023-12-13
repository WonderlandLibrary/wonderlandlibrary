/*
 * Decompiled with CFR 0.152.
 */
package oshi.hardware;

public interface Processor {
    public String getVendor();

    public void setVendor(String var1);

    public String getName();

    public void setName(String var1);

    public String getIdentifier();

    public void setIdentifier(String var1);

    public boolean isCpu64bit();

    public void setCpu64(boolean var1);

    public String getStepping();

    public void setStepping(String var1);

    public String getModel();

    public void setModel(String var1);

    public String getFamily();

    public void setFamily(String var1);

    public float getLoad();
}

