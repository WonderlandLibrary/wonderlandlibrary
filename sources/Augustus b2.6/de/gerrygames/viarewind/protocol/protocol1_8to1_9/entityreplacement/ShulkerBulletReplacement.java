// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.ArrayList;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public class ShulkerBulletReplacement implements EntityReplacement
{
    private int entityId;
    private List<Metadata> datawatcher;
    private double locX;
    private double locY;
    private double locZ;
    private float yaw;
    private float pitch;
    private float headYaw;
    private UserConnection user;
    
    public ShulkerBulletReplacement(final int entityId, final UserConnection user) {
        this.datawatcher = new ArrayList<Metadata>();
        this.entityId = entityId;
        this.user = user;
        this.spawn();
    }
    
    @Override
    public void setLocation(final double x, final double y, final double z) {
        if (x != this.locX || y != this.locY || z != this.locZ) {
            this.locX = x;
            this.locY = y;
            this.locZ = z;
            this.updateLocation();
        }
    }
    
    @Override
    public void relMove(final double x, final double y, final double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return;
        }
        this.locX += x;
        this.locY += y;
        this.locZ += z;
        this.updateLocation();
    }
    
    @Override
    public void setYawPitch(final float yaw, final float pitch) {
        if (this.yaw != yaw && this.pitch != pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.updateLocation();
        }
    }
    
    @Override
    public void setHeadYaw(final float yaw) {
        this.headYaw = yaw;
    }
    
    @Override
    public void updateMetadata(final List<Metadata> metadataList) {
    }
    
    public void updateLocation() {
        final PacketWrapper teleport = PacketWrapper.create(24, null, this.user);
        teleport.write(Type.VAR_INT, this.entityId);
        teleport.write(Type.INT, (int)(this.locX * 32.0));
        teleport.write(Type.INT, (int)(this.locY * 32.0));
        teleport.write(Type.INT, (int)(this.locZ * 32.0));
        teleport.write(Type.BYTE, (byte)(this.yaw / 360.0f * 256.0f));
        teleport.write(Type.BYTE, (byte)(this.pitch / 360.0f * 256.0f));
        teleport.write(Type.BOOLEAN, true);
        final PacketWrapper head = PacketWrapper.create(25, null, this.user);
        head.write(Type.VAR_INT, this.entityId);
        head.write(Type.BYTE, (byte)(this.headYaw / 360.0f * 256.0f));
        PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
        PacketUtil.sendPacket(head, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public void spawn() {
        final PacketWrapper spawn = PacketWrapper.create(14, null, this.user);
        spawn.write(Type.VAR_INT, this.entityId);
        spawn.write(Type.BYTE, (Byte)66);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.INT, 0);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public void despawn() {
        final PacketWrapper despawn = PacketWrapper.create(19, null, this.user);
        despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
        PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
