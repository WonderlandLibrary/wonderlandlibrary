// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Arrays;

class MemoryPasswordProvider implements PasswordProvider
{
    private final char[] password;
    
    public MemoryPasswordProvider(final char[] chars) {
        if (chars != null) {
            this.password = chars.clone();
        }
        else {
            this.password = null;
        }
    }
    
    @Override
    public char[] getPassword() {
        if (this.password == null) {
            return null;
        }
        return this.password.clone();
    }
    
    public void clearSecrets() {
        Arrays.fill(this.password, '\0');
    }
}
