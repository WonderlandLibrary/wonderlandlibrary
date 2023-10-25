/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import java.util.Arrays;
import org.apache.logging.log4j.core.net.ssl.PasswordProvider;

class MemoryPasswordProvider
implements PasswordProvider {
    private final char[] password;

    public MemoryPasswordProvider(char[] chars) {
        this.password = (char[])(chars != null ? (char[])chars.clone() : null);
    }

    @Override
    public char[] getPassword() {
        if (this.password == null) {
            return null;
        }
        return (char[])this.password.clone();
    }

    public void clearSecrets() {
        Arrays.fill(this.password, '\u0000');
    }
}

