// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.security.Key;
import javax.crypto.Mac;

final class MacHashFunction extends AbstractStreamingHashFunction
{
    private final Mac prototype;
    private final Key key;
    private final String toString;
    private final int bits;
    private final boolean supportsClone;
    
    MacHashFunction(final String algorithmName, final Key key, final String toString) {
        this.prototype = getMac(algorithmName, key);
        this.key = Preconditions.checkNotNull(key);
        this.toString = Preconditions.checkNotNull(toString);
        this.bits = this.prototype.getMacLength() * 8;
        this.supportsClone = supportsClone(this.prototype);
    }
    
    @Override
    public int bits() {
        return this.bits;
    }
    
    private static boolean supportsClone(final Mac mac) {
        try {
            mac.clone();
            return true;
        }
        catch (CloneNotSupportedException e) {
            return false;
        }
    }
    
    private static Mac getMac(final String algorithmName, final Key key) {
        try {
            final Mac mac = Mac.getInstance(algorithmName);
            mac.init(key);
            return mac;
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
    
    @Override
    public Hasher newHasher() {
        if (this.supportsClone) {
            try {
                return new MacHasher((Mac)this.prototype.clone());
            }
            catch (CloneNotSupportedException ex) {}
        }
        return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key));
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    private static final class MacHasher extends AbstractByteHasher
    {
        private final Mac mac;
        private boolean done;
        
        private MacHasher(final Mac mac) {
            this.mac = mac;
        }
        
        @Override
        protected void update(final byte b) {
            this.checkNotDone();
            this.mac.update(b);
        }
        
        @Override
        protected void update(final byte[] b) {
            this.checkNotDone();
            this.mac.update(b);
        }
        
        @Override
        protected void update(final byte[] b, final int off, final int len) {
            this.checkNotDone();
            this.mac.update(b, off, len);
        }
        
        private void checkNotDone() {
            Preconditions.checkState(!this.done, (Object)"Cannot re-use a Hasher after calling hash() on it");
        }
        
        @Override
        public HashCode hash() {
            this.checkNotDone();
            this.done = true;
            return HashCode.fromBytesNoCopy(this.mac.doFinal());
        }
    }
}
