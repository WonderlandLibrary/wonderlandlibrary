// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.command;

import java.util.UUID;

public interface ViaCommandSender
{
    boolean hasPermission(final String p0);
    
    void sendMessage(final String p0);
    
    UUID getUUID();
    
    String getName();
}
