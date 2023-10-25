/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.ssl;

import java.net.Socket;
import java.util.Map;
import org.apache.http.conn.ssl.PrivateKeyDetails;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface PrivateKeyStrategy {
    public String chooseAlias(Map<String, PrivateKeyDetails> var1, Socket var2);
}

