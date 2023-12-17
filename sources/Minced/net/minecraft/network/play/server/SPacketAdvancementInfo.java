// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import com.google.common.collect.Sets;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Collection;
import net.minecraft.advancements.AdvancementProgress;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient>
{
    private boolean firstSync;
    private Map<ResourceLocation, Advancement.Builder> advancementsToAdd;
    private Set<ResourceLocation> advancementsToRemove;
    private Map<ResourceLocation, AdvancementProgress> progressUpdates;
    
    public SPacketAdvancementInfo() {
    }
    
    public SPacketAdvancementInfo(final boolean p_i47519_1_, final Collection<Advancement> p_i47519_2_, final Set<ResourceLocation> p_i47519_3_, final Map<ResourceLocation, AdvancementProgress> p_i47519_4_) {
        this.firstSync = p_i47519_1_;
        this.advancementsToAdd = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        for (final Advancement advancement : p_i47519_2_) {
            this.advancementsToAdd.put(advancement.getId(), advancement.copy());
        }
        this.advancementsToRemove = p_i47519_3_;
        this.progressUpdates = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap((Map)p_i47519_4_);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleAdvancementInfo(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.firstSync = buf.readBoolean();
        this.advancementsToAdd = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        this.advancementsToRemove = (Set<ResourceLocation>)Sets.newLinkedHashSet();
        this.progressUpdates = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (int i = buf.readVarInt(), j = 0; j < i; ++j) {
            final ResourceLocation resourcelocation = buf.readResourceLocation();
            final Advancement.Builder advancement$builder = Advancement.Builder.readFrom(buf);
            this.advancementsToAdd.put(resourcelocation, advancement$builder);
        }
        for (int i = buf.readVarInt(), k = 0; k < i; ++k) {
            final ResourceLocation resourcelocation2 = buf.readResourceLocation();
            this.advancementsToRemove.add(resourcelocation2);
        }
        for (int i = buf.readVarInt(), l = 0; l < i; ++l) {
            final ResourceLocation resourcelocation3 = buf.readResourceLocation();
            this.progressUpdates.put(resourcelocation3, AdvancementProgress.fromNetwork(buf));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.firstSync);
        buf.writeVarInt(this.advancementsToAdd.size());
        for (final Map.Entry<ResourceLocation, Advancement.Builder> entry : this.advancementsToAdd.entrySet()) {
            final ResourceLocation resourcelocation = entry.getKey();
            final Advancement.Builder advancement$builder = entry.getValue();
            buf.writeResourceLocation(resourcelocation);
            advancement$builder.writeTo(buf);
        }
        buf.writeVarInt(this.advancementsToRemove.size());
        for (final ResourceLocation resourcelocation2 : this.advancementsToRemove) {
            buf.writeResourceLocation(resourcelocation2);
        }
        buf.writeVarInt(this.progressUpdates.size());
        for (final Map.Entry<ResourceLocation, AdvancementProgress> entry2 : this.progressUpdates.entrySet()) {
            buf.writeResourceLocation(entry2.getKey());
            entry2.getValue().serializeToNetwork(buf);
        }
    }
    
    public Map<ResourceLocation, Advancement.Builder> getAdvancementsToAdd() {
        return this.advancementsToAdd;
    }
    
    public Set<ResourceLocation> getAdvancementsToRemove() {
        return this.advancementsToRemove;
    }
    
    public Map<ResourceLocation, AdvancementProgress> getProgressUpdates() {
        return this.progressUpdates;
    }
    
    public boolean isFirstSync() {
        return this.firstSync;
    }
}
