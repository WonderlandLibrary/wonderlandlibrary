/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration.impl;

interface RecordWriter {
    public boolean open(String var1);

    public boolean close();

    public void bool(String var1, boolean var2);

    public void boolArray(String var1, boolean[] var2);

    public void character(String var1, char var2);

    public void characterArray(String var1, char[] var2);

    public void namedIndex(String var1, String[] var2, int var3);

    public void namedIndexArray(String var1, String[] var2, byte[] var3);

    public void string(String var1, String var2);

    public void stringArray(String var1, String[] var2);

    public void stringTable(String var1, String[][] var2);
}

