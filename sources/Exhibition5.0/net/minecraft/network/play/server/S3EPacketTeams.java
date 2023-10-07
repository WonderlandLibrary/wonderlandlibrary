// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Team;
import java.util.Collection;
import net.minecraft.network.Packet;

public class S3EPacketTeams implements Packet
{
    private String field_149320_a;
    private String field_149318_b;
    private String field_149319_c;
    private String field_149316_d;
    private String field_179816_e;
    private int field_179815_f;
    private Collection field_149317_e;
    private int field_149314_f;
    private int field_149315_g;
    private static final String __OBFID = "CL_00001334";
    
    public S3EPacketTeams() {
        this.field_149320_a = "";
        this.field_149318_b = "";
        this.field_149319_c = "";
        this.field_149316_d = "";
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
    }
    
    public S3EPacketTeams(final ScorePlayerTeam p_i45225_1_, final int p_i45225_2_) {
        this.field_149320_a = "";
        this.field_149318_b = "";
        this.field_149319_c = "";
        this.field_149316_d = "";
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        this.field_149320_a = p_i45225_1_.getRegisteredName();
        this.field_149314_f = p_i45225_2_;
        if (p_i45225_2_ == 0 || p_i45225_2_ == 2) {
            this.field_149318_b = p_i45225_1_.func_96669_c();
            this.field_149319_c = p_i45225_1_.getColorPrefix();
            this.field_149316_d = p_i45225_1_.getColorSuffix();
            this.field_149315_g = p_i45225_1_.func_98299_i();
            this.field_179816_e = p_i45225_1_.func_178770_i().field_178830_e;
            this.field_179815_f = p_i45225_1_.func_178775_l().func_175746_b();
        }
        if (p_i45225_2_ == 0) {
            this.field_149317_e.addAll(p_i45225_1_.getMembershipCollection());
        }
    }
    
    public S3EPacketTeams(final ScorePlayerTeam p_i45226_1_, final Collection p_i45226_2_, final int p_i45226_3_) {
        this.field_149320_a = "";
        this.field_149318_b = "";
        this.field_149319_c = "";
        this.field_149316_d = "";
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        if (p_i45226_3_ != 3 && p_i45226_3_ != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (p_i45226_2_ != null && !p_i45226_2_.isEmpty()) {
            this.field_149314_f = p_i45226_3_;
            this.field_149320_a = p_i45226_1_.getRegisteredName();
            this.field_149317_e.addAll(p_i45226_2_);
            return;
        }
        throw new IllegalArgumentException("Players cannot be null/empty");
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149320_a = data.readStringFromBuffer(16);
        this.field_149314_f = data.readByte();
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            this.field_149318_b = data.readStringFromBuffer(32);
            this.field_149319_c = data.readStringFromBuffer(16);
            this.field_149316_d = data.readStringFromBuffer(16);
            this.field_149315_g = data.readByte();
            this.field_179816_e = data.readStringFromBuffer(32);
            this.field_179815_f = data.readByte();
        }
        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            for (int var2 = data.readVarIntFromBuffer(), var3 = 0; var3 < var2; ++var3) {
                this.field_149317_e.add(data.readStringFromBuffer(40));
            }
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.field_149320_a);
        data.writeByte(this.field_149314_f);
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            data.writeString(this.field_149318_b);
            data.writeString(this.field_149319_c);
            data.writeString(this.field_149316_d);
            data.writeByte(this.field_149315_g);
            data.writeString(this.field_179816_e);
            data.writeByte(this.field_179815_f);
        }
        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            data.writeVarIntToBuffer(this.field_149317_e.size());
            for (final String var3 : this.field_149317_e) {
                data.writeString(var3);
            }
        }
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleTeams(this);
    }
    
    public String func_149312_c() {
        return this.field_149320_a;
    }
    
    public String func_149306_d() {
        return this.field_149318_b;
    }
    
    public String func_149311_e() {
        return this.field_149319_c;
    }
    
    public String func_149309_f() {
        return this.field_149316_d;
    }
    
    public Collection func_149310_g() {
        return this.field_149317_e;
    }
    
    public int func_149307_h() {
        return this.field_149314_f;
    }
    
    public int func_149308_i() {
        return this.field_149315_g;
    }
    
    public int func_179813_h() {
        return this.field_179815_f;
    }
    
    public String func_179814_i() {
        return this.field_179816_e;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
