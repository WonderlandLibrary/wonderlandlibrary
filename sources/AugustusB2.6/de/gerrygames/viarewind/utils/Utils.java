// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import java.util.UUID;
import com.viaversion.viaversion.api.connection.UserConnection;

public class Utils
{
    public static UUID getUUID(final UserConnection user) {
        return user.getProtocolInfo().getUuid();
    }
}
