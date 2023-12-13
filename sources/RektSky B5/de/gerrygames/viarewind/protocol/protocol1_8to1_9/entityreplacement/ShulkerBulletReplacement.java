/*
 * Decompiled with CFR 0.152.
 */
package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;

public class ShulkerBulletReplacement
implements EntityReplacement {
    private int entityId;
    private List<Metadata> datawatcher = new ArrayList<Metadata>();
    private double locX;
    private double locY;
    private double locZ;
    private float yaw;
    private float pitch;
    private float headYaw;
    private UserConnection user;

    public ShulkerBulletReplacement(int entityId, UserConnection user) {
        this.entityId = entityId;
        this.user = user;
        this.spawn();
    }

    @Override
    public void setLocation(double x2, double y2, double z2) {
        if (x2 != this.locX || y2 != this.locY || z2 != this.locZ) {
            this.locX = x2;
            this.locY = y2;
            this.locZ = z2;
            this.updateLocation();
        }
    }

    @Override
    public void relMove(double x2, double y2, double z2) {
        if (x2 == 0.0 && y2 == 0.0 && z2 == 0.0) {
            return;
        }
        this.locX += x2;
        this.locY += y2;
        this.locZ += z2;
        this.updateLocation();
    }

    @Override
    public void setYawPitch(float yaw, float pitch) {
        if (this.yaw != yaw && this.pitch != pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.updateLocation();
        }
    }

    @Override
    public void setHeadYaw(float yaw) {
        this.headYaw = yaw;
    }

    @Override
    public void updateMetadata(List<Metadata> metadataList) {
    }

    public void updateLocation() {
        PacketWrapper teleport = PacketWrapper.create(24, null, this.user);
        teleport.write(Type.VAR_INT, this.entityId);
        teleport.write(Type.INT, (int)(this.locX * 32.0));
        teleport.write(Type.INT, (int)(this.locY * 32.0));
        teleport.write(Type.INT, (int)(this.locZ * 32.0));
        teleport.write(Type.BYTE, (byte)(this.yaw / 360.0f * 256.0f));
        teleport.write(Type.BYTE, (byte)(this.pitch / 360.0f * 256.0f));
        teleport.write(Type.BOOLEAN, true);
        PacketWrapper head = PacketWrapper.create(25, null, this.user);
        head.write(Type.VAR_INT, this.entityId);
        head.write(Type.BYTE, (byte)(this.headYaw / 360.0f * 256.0f));
        PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
        PacketUtil.sendPacket(head, Protocol1_8TO1_9.class, true, true);
    }

    @Override
    public void spawn() {
        PacketWrapper spawn = PacketWrapper.create(14, null, this.user);
        spawn.write(Type.VAR_INT, this.entityId);
        spawn.write(Type.BYTE, (byte)66);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.BYTE, (byte)0);
        spawn.write(Type.BYTE, (byte)0);
        spawn.write(Type.INT, 0);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }

    @Override
    public void despawn() {
        PacketWrapper despawn = PacketWrapper.create(19, null, this.user);
        despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[]{this.entityId});
        PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }
}

