/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;
import org.apache.logging.log4j.core.net.ssl.PasswordProvider;

class EnvironmentPasswordProvider
implements PasswordProvider {
    private final String passwordEnvironmentVariable;

    public EnvironmentPasswordProvider(String passwordEnvironmentVariable) {
        this.passwordEnvironmentVariable = Objects.requireNonNull(passwordEnvironmentVariable, "passwordEnvironmentVariable");
    }

    @Override
    public char[] getPassword() {
        String password = System.getenv(this.passwordEnvironmentVariable);
        return password == null ? null : password.toCharArray();
    }
}

