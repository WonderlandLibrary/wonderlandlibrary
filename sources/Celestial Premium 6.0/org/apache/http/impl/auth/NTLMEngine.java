/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.auth;

import org.apache.http.impl.auth.NTLMEngineException;

public interface NTLMEngine {
    public String generateType1Msg(String var1, String var2) throws NTLMEngineException;

    public String generateType3Msg(String var1, String var2, String var3, String var4, String var5) throws NTLMEngineException;
}

