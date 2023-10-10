// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.util;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import com.viaversion.viaversion.api.minecraft.Vector;
import com.viaversion.viaversion.api.connection.UserConnection;

public class RelativeMoveUtil
{
    public static Vector[] calculateRelativeMoves(final UserConnection user, final int entityId, int relX, int relY, int relZ) {
        final EntityTracker tracker = user.get(EntityTracker.class);
        final Vector offset = tracker.getEntityOffset(entityId);
        relX += offset.getBlockX();
        relY += offset.getBlockY();
        relZ += offset.getBlockZ();
        if (relX > 32767) {
            offset.setBlockX(relX - 32767);
            relX = 32767;
        }
        else if (relX < -32768) {
            offset.setBlockX(relX + 32768);
            relX = -32768;
        }
        else {
            offset.setBlockX(0);
        }
        if (relY > 32767) {
            offset.setBlockY(relY - 32767);
            relY = 32767;
        }
        else if (relY < -32768) {
            offset.setBlockY(relY + 32768);
            relY = -32768;
        }
        else {
            offset.setBlockY(0);
        }
        if (relZ > 32767) {
            offset.setBlockZ(relZ - 32767);
            relZ = 32767;
        }
        else if (relZ < -32768) {
            offset.setBlockZ(relZ + 32768);
            relZ = -32768;
        }
        else {
            offset.setBlockZ(0);
        }
        int sentRelX;
        int sentRelY;
        int sentRelZ;
        Vector[] moves;
        if (relX > 16256 || relX < -16384 || relY > 16256 || relY < -16384 || relZ > 16256 || relZ < -16384) {
            final byte relX2 = (byte)(relX / 256);
            final byte relX3 = (byte)Math.round((relX - relX2 * 128) / 128.0f);
            final byte relY2 = (byte)(relY / 256);
            final byte relY3 = (byte)Math.round((relY - relY2 * 128) / 128.0f);
            final byte relZ2 = (byte)(relZ / 256);
            final byte relZ3 = (byte)Math.round((relZ - relZ2 * 128) / 128.0f);
            sentRelX = relX2 + relX3;
            sentRelY = relY2 + relY3;
            sentRelZ = relZ2 + relZ3;
            moves = new Vector[] { new Vector(relX2, relY2, relZ2), new Vector(relX3, relY3, relZ3) };
        }
        else {
            sentRelX = Math.round(relX / 128.0f);
            sentRelY = Math.round(relY / 128.0f);
            sentRelZ = Math.round(relZ / 128.0f);
            moves = new Vector[] { new Vector(sentRelX, sentRelY, sentRelZ) };
        }
        offset.setBlockX(offset.getBlockX() + relX - sentRelX * 128);
        offset.setBlockY(offset.getBlockY() + relY - sentRelY * 128);
        offset.setBlockZ(offset.getBlockZ() + relZ - sentRelZ * 128);
        tracker.setEntityOffset(entityId, offset);
        return moves;
    }
}
