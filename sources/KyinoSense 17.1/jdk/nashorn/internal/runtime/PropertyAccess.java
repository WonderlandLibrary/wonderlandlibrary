/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

public interface PropertyAccess {
    public int getInt(Object var1, int var2);

    public int getInt(double var1, int var3);

    public int getInt(int var1, int var2);

    public double getDouble(Object var1, int var2);

    public double getDouble(double var1, int var3);

    public double getDouble(int var1, int var2);

    public Object get(Object var1);

    public Object get(double var1);

    public Object get(int var1);

    public void set(Object var1, int var2, int var3);

    public void set(Object var1, double var2, int var4);

    public void set(Object var1, Object var2, int var3);

    public void set(double var1, int var3, int var4);

    public void set(double var1, double var3, int var5);

    public void set(double var1, Object var3, int var4);

    public void set(int var1, int var2, int var3);

    public void set(int var1, double var2, int var4);

    public void set(int var1, Object var2, int var3);

    public boolean has(Object var1);

    public boolean has(int var1);

    public boolean has(double var1);

    public boolean hasOwnProperty(Object var1);

    public boolean hasOwnProperty(int var1);

    public boolean hasOwnProperty(double var1);

    public boolean delete(int var1, boolean var2);

    public boolean delete(double var1, boolean var3);

    public boolean delete(Object var1, boolean var2);
}

