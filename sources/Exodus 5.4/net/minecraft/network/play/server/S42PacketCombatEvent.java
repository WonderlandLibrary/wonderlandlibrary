/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;

public class S42PacketCombatEvent
implements Packet<INetHandlerPlayClient> {
    public String deathMessage;
    public Event eventType;
    public int field_179772_d;
    public int field_179775_c;
    public int field_179774_b;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.eventType = packetBuffer.readEnumValue(Event.class);
        if (this.eventType == Event.END_COMBAT) {
            this.field_179772_d = packetBuffer.readVarIntFromBuffer();
            this.field_179775_c = packetBuffer.readInt();
        } else if (this.eventType == Event.ENTITY_DIED) {
            this.field_179774_b = packetBuffer.readVarIntFromBuffer();
            this.field_179775_c = packetBuffer.readInt();
            this.deathMessage = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
        }
    }

    public S42PacketCombatEvent() {
    }

    public S42PacketCombatEvent(CombatTracker combatTracker, Event event) {
        this.eventType = event;
        EntityLivingBase entityLivingBase = combatTracker.func_94550_c();
        switch (event) {
            case END_COMBAT: {
                this.field_179772_d = combatTracker.func_180134_f();
                this.field_179775_c = entityLivingBase == null ? -1 : entityLivingBase.getEntityId();
                break;
            }
            case ENTITY_DIED: {
                this.field_179774_b = combatTracker.getFighter().getEntityId();
                this.field_179775_c = entityLivingBase == null ? -1 : entityLivingBase.getEntityId();
                this.deathMessage = combatTracker.getDeathMessage().getUnformattedText();
            }
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleCombatEvent(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeEnumValue(this.eventType);
        if (this.eventType == Event.END_COMBAT) {
            packetBuffer.writeVarIntToBuffer(this.field_179772_d);
            packetBuffer.writeInt(this.field_179775_c);
        } else if (this.eventType == Event.ENTITY_DIED) {
            packetBuffer.writeVarIntToBuffer(this.field_179774_b);
            packetBuffer.writeInt(this.field_179775_c);
            packetBuffer.writeString(this.deathMessage);
        }
    }

    public static enum Event {
        ENTER_COMBAT,
        END_COMBAT,
        ENTITY_DIED;

    }
}

