// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.CommandBlockStorage;
import java.util.Optional;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public class CommandBlockProvider implements Provider
{
    public void addOrUpdateBlock(final UserConnection user, final Position position, final CompoundTag tag) throws Exception {
        this.checkPermission(user);
        if (this.isEnabled()) {
            this.getStorage(user).addOrUpdateBlock(position, tag);
        }
    }
    
    public Optional<CompoundTag> get(final UserConnection user, final Position position) throws Exception {
        this.checkPermission(user);
        if (this.isEnabled()) {
            return this.getStorage(user).getCommandBlock(position);
        }
        return Optional.empty();
    }
    
    public void unloadChunk(final UserConnection user, final int x, final int z) throws Exception {
        this.checkPermission(user);
        if (this.isEnabled()) {
            this.getStorage(user).unloadChunk(x, z);
        }
    }
    
    private CommandBlockStorage getStorage(final UserConnection connection) {
        return connection.get(CommandBlockStorage.class);
    }
    
    public void sendPermission(final UserConnection user) throws Exception {
        if (!this.isEnabled()) {
            return;
        }
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_STATUS, null, user);
        final EntityTracker1_9 tracker = user.getEntityTracker(Protocol1_9To1_8.class);
        wrapper.write(Type.INT, tracker.getProvidedEntityId());
        wrapper.write(Type.BYTE, (Byte)26);
        wrapper.scheduleSend(Protocol1_9To1_8.class);
        user.get(CommandBlockStorage.class).setPermissions(true);
    }
    
    private void checkPermission(final UserConnection user) throws Exception {
        if (!this.isEnabled()) {
            return;
        }
        final CommandBlockStorage storage = this.getStorage(user);
        if (!storage.isPermissions()) {
            this.sendPermission(user);
        }
    }
    
    public boolean isEnabled() {
        return true;
    }
    
    public void unloadChunks(final UserConnection userConnection) {
        if (this.isEnabled()) {
            this.getStorage(userConnection).unloadChunks();
        }
    }
}
