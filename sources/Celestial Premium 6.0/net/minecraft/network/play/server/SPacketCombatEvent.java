/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SPacketCombatEvent
implements Packet<INetHandlerPlayClient> {
    public Event eventType;
    public int playerId;
    public int entityId;
    public int duration;
    public ITextComponent deathMessage;

    public SPacketCombatEvent() {
    }

    public SPacketCombatEvent(CombatTracker tracker, Event eventIn) {
        this(tracker, eventIn, true);
    }

    public SPacketCombatEvent(CombatTracker tracker, Event eventIn, boolean p_i46932_3_) {
        this.eventType = eventIn;
        EntityLivingBase entitylivingbase = tracker.getBestAttacker();
        switch (eventIn) {
            case END_COMBAT: {
                this.duration = tracker.getCombatDuration();
                this.entityId = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();
                break;
            }
            case ENTITY_DIED: {
                this.playerId = tracker.getFighter().getEntityId();
                this.entityId = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();
                this.deathMessage = p_i46932_3_ ? tracker.getDeathMessage() : new TextComponentString("");
            }
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.eventType = buf.readEnumValue(Event.class);
        if (this.eventType == Event.END_COMBAT) {
            this.duration = buf.readVarIntFromBuffer();
            this.entityId = buf.readInt();
        } else if (this.eventType == Event.ENTITY_DIED) {
            this.playerId = buf.readVarIntFromBuffer();
            this.entityId = buf.readInt();
            this.deathMessage = buf.readTextComponent();
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.eventType);
        if (this.eventType == Event.END_COMBAT) {
            buf.writeVarIntToBuffer(this.duration);
            buf.writeInt(this.entityId);
        } else if (this.eventType == Event.ENTITY_DIED) {
            buf.writeVarIntToBuffer(this.playerId);
            buf.writeInt(this.entityId);
            buf.writeTextComponent(this.deathMessage);
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleCombatEvent(this);
    }

    public static enum Event {
        ENTER_COMBAT,
        END_COMBAT,
        ENTITY_DIED;

    }
}

