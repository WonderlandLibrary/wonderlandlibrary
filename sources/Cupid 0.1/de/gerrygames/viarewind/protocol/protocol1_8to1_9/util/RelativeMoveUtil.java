package de.gerrygames.viarewind.protocol.protocol1_8to1_9.util;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Vector;

public class RelativeMoveUtil {
  public static Vector[] calculateRelativeMoves(UserConnection user, int entityId, int relX, int relY, int relZ) {
    int sentRelX, sentRelY, sentRelZ;
    Vector[] moves;
    EntityTracker tracker = (EntityTracker)user.get(EntityTracker.class);
    Vector offset = tracker.getEntityOffset(entityId);
    relX += offset.getBlockX();
    relY += offset.getBlockY();
    relZ += offset.getBlockZ();
    if (relX > 32767) {
      offset.setBlockX(relX - 32767);
      relX = 32767;
    } else if (relX < -32768) {
      offset.setBlockX(relX - -32768);
      relX = -32768;
    } else {
      offset.setBlockX(0);
    } 
    if (relY > 32767) {
      offset.setBlockY(relY - 32767);
      relY = 32767;
    } else if (relY < -32768) {
      offset.setBlockY(relY - -32768);
      relY = -32768;
    } else {
      offset.setBlockY(0);
    } 
    if (relZ > 32767) {
      offset.setBlockZ(relZ - 32767);
      relZ = 32767;
    } else if (relZ < -32768) {
      offset.setBlockZ(relZ - -32768);
      relZ = -32768;
    } else {
      offset.setBlockZ(0);
    } 
    if (relX > 16256 || relX < -16384 || relY > 16256 || relY < -16384 || relZ > 16256 || relZ < -16384) {
      byte relX1 = (byte)(relX / 256);
      byte relX2 = (byte)Math.round((relX - relX1 * 128) / 128.0F);
      byte relY1 = (byte)(relY / 256);
      byte relY2 = (byte)Math.round((relY - relY1 * 128) / 128.0F);
      byte relZ1 = (byte)(relZ / 256);
      byte relZ2 = (byte)Math.round((relZ - relZ1 * 128) / 128.0F);
      sentRelX = relX1 + relX2;
      sentRelY = relY1 + relY2;
      sentRelZ = relZ1 + relZ2;
      moves = new Vector[] { new Vector(relX1, relY1, relZ1), new Vector(relX2, relY2, relZ2) };
    } else {
      sentRelX = Math.round(relX / 128.0F);
      sentRelY = Math.round(relY / 128.0F);
      sentRelZ = Math.round(relZ / 128.0F);
      moves = new Vector[] { new Vector(sentRelX, sentRelY, sentRelZ) };
    } 
    offset.setBlockX(offset.getBlockX() + relX - sentRelX * 128);
    offset.setBlockY(offset.getBlockY() + relY - sentRelY * 128);
    offset.setBlockZ(offset.getBlockZ() + relZ - sentRelZ * 128);
    tracker.setEntityOffset(entityId, offset);
    return moves;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_\\util\RelativeMoveUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */