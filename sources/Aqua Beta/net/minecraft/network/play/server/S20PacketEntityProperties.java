package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S20PacketEntityProperties;

public class S20PacketEntityProperties
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private final List<Snapshot> field_149444_b = Lists.newArrayList();

    public S20PacketEntityProperties() {
    }

    public S20PacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> p_i45236_2_) {
        this.entityId = entityIdIn;
        for (IAttributeInstance iattributeinstance : p_i45236_2_) {
            this.field_149444_b.add((Object)new Snapshot(this, iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.func_111122_c()));
        }
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        int i = buf.readInt();
        for (int j = 0; j < i; ++j) {
            String s = buf.readStringFromBuffer(64);
            double d0 = buf.readDouble();
            ArrayList list = Lists.newArrayList();
            int k = buf.readVarIntFromBuffer();
            for (int l = 0; l < k; ++l) {
                UUID uuid = buf.readUuid();
                list.add((Object)new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), (int)buf.readByte()));
            }
            this.field_149444_b.add((Object)new Snapshot(this, s, d0, (Collection)list));
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeInt(this.field_149444_b.size());
        for (Snapshot s20packetentityproperties$snapshot : this.field_149444_b) {
            buf.writeString(s20packetentityproperties$snapshot.func_151409_a());
            buf.writeDouble(s20packetentityproperties$snapshot.func_151410_b());
            buf.writeVarIntToBuffer(s20packetentityproperties$snapshot.func_151408_c().size());
            for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
                buf.writeUuid(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityProperties(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<Snapshot> func_149441_d() {
        return this.field_149444_b;
    }
}
