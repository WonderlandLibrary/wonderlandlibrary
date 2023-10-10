// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;

class EnvironmentPasswordProvider implements PasswordProvider
{
    private final String passwordEnvironmentVariable;
    
    public EnvironmentPasswordProvider(final String passwordEnvironmentVariable) {
        this.passwordEnvironmentVariable = Objects.requireNonNull(passwordEnvironmentVariable, "passwordEnvironmentVariable");
    }
    
    @Override
    public char[] getPassword() {
        final String password = System.getenv(this.passwordEnvironmentVariable);
        return (char[])((password == null) ? null : password.toCharArray());
    }
}
