/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
public interface SpnegoTokenGenerator {
    public byte[] generateSpnegoDERObject(byte[] var1) throws IOException;
}

