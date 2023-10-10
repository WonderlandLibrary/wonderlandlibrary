// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StoredObject;

public class CompressionSendStorage extends StoredObject
{
    private boolean removeCompression;
    
    public CompressionSendStorage(final UserConnection user) {
        super(user);
        this.removeCompression = false;
    }
    
    public boolean isRemoveCompression() {
        return this.removeCompression;
    }
    
    public void setRemoveCompression(final boolean removeCompression) {
        this.removeCompression = removeCompression;
    }
}
