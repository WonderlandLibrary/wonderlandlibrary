// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.network.Packet;

public class S3BPacketScoreboardObjective implements Packet
{
    private String field_149343_a;
    private String field_149341_b;
    private IScoreObjectiveCriteria.EnumRenderType field_179818_c;
    private int field_149342_c;
    private static final String __OBFID = "CL_00001333";
    
    public S3BPacketScoreboardObjective() {
    }
    
    public S3BPacketScoreboardObjective(final ScoreObjective p_i45224_1_, final int p_i45224_2_) {
        this.field_149343_a = p_i45224_1_.getName();
        this.field_149341_b = p_i45224_1_.getDisplayName();
        this.field_179818_c = p_i45224_1_.getCriteria().func_178790_c();
        this.field_149342_c = p_i45224_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149343_a = data.readStringFromBuffer(16);
        this.field_149342_c = data.readByte();
        if (this.field_149342_c == 0 || this.field_149342_c == 2) {
            this.field_149341_b = data.readStringFromBuffer(32);
            this.field_179818_c = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(data.readStringFromBuffer(16));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.field_149343_a);
        data.writeByte(this.field_149342_c);
        if (this.field_149342_c == 0 || this.field_149342_c == 2) {
            data.writeString(this.field_149341_b);
            data.writeString(this.field_179818_c.func_178796_a());
        }
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleScoreboardObjective(this);
    }
    
    public String func_149339_c() {
        return this.field_149343_a;
    }
    
    public String func_149337_d() {
        return this.field_149341_b;
    }
    
    public int func_149338_e() {
        return this.field_149342_c;
    }
    
    public IScoreObjectiveCriteria.EnumRenderType func_179817_d() {
        return this.field_179818_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
