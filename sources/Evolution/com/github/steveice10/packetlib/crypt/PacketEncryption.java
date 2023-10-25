/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.crypt;

public interface PacketEncryption {
    public int getDecryptOutputSize(int var1);

    public int getEncryptOutputSize(int var1);

    public int decrypt(byte[] var1, int var2, int var3, byte[] var4, int var5) throws Exception;

    public int encrypt(byte[] var1, int var2, int var3, byte[] var4, int var5) throws Exception;
}

