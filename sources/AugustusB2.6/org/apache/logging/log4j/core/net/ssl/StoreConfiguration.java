// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration<T>
{
    protected static final StatusLogger LOGGER;
    private String location;
    private PasswordProvider passwordProvider;
    
    public StoreConfiguration(final String location, final PasswordProvider passwordProvider) {
        this.location = location;
        this.passwordProvider = Objects.requireNonNull(passwordProvider, "passwordProvider");
    }
    
    @Deprecated
    public StoreConfiguration(final String location, final char[] password) {
        this(location, new MemoryPasswordProvider(password));
    }
    
    @Deprecated
    public StoreConfiguration(final String location, final String password) {
        this(location, new MemoryPasswordProvider((char[])((password == null) ? null : password.toCharArray())));
    }
    
    public void clearSecrets() {
        this.location = null;
        this.passwordProvider = null;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    @Deprecated
    public String getPassword() {
        return String.valueOf(this.passwordProvider.getPassword());
    }
    
    public char[] getPasswordAsCharArray() {
        return this.passwordProvider.getPassword();
    }
    
    public void setPassword(final char[] password) {
        this.passwordProvider = new MemoryPasswordProvider(password);
    }
    
    @Deprecated
    public void setPassword(final String password) {
        this.passwordProvider = new MemoryPasswordProvider((char[])((password == null) ? null : password.toCharArray()));
    }
    
    protected T load() throws StoreConfigurationException {
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
        result = 31 * result + Arrays.hashCode(this.passwordProvider.getPassword());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final StoreConfiguration<?> other = (StoreConfiguration<?>)obj;
        return Objects.equals(this.location, other.location) && Arrays.equals(this.passwordProvider.getPassword(), other.passwordProvider.getPassword());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
