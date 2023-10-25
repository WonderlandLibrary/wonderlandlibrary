/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class S3EPacketTeams
implements Packet<INetHandlerPlayClient> {
    private String field_149320_a = "";
    private int field_149314_f;
    private String field_149319_c = "";
    private String field_179816_e;
    private String field_149316_d = "";
    private String field_149318_b = "";
    private Collection<String> field_149317_e;
    private int field_149315_g;
    private int field_179815_f;

    public int func_149308_i() {
        return this.field_149315_g;
    }

    public S3EPacketTeams(ScorePlayerTeam scorePlayerTeam, Collection<String> collection, int n) {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        if (n != 3 && n != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
        this.field_149314_f = n;
        this.field_149320_a = scorePlayerTeam.getRegisteredName();
        this.field_149317_e.addAll(collection);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.field_149320_a);
        packetBuffer.writeByte(this.field_149314_f);
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            packetBuffer.writeString(this.field_149318_b);
            packetBuffer.writeString(this.field_149319_c);
            packetBuffer.writeString(this.field_149316_d);
            packetBuffer.writeByte(this.field_149315_g);
            packetBuffer.writeString(this.field_179816_e);
            packetBuffer.writeByte(this.field_179815_f);
        }
        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            packetBuffer.writeVarIntToBuffer(this.field_149317_e.size());
            for (String string : this.field_149317_e) {
                packetBuffer.writeString(string);
            }
        }
    }

    public String func_149306_d() {
        return this.field_149318_b;
    }

    public int func_149307_h() {
        return this.field_149314_f;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.field_149320_a = packetBuffer.readStringFromBuffer(16);
        this.field_149314_f = packetBuffer.readByte();
        if (this.field_149314_f == 0 || this.field_149314_f == 2) {
            this.field_149318_b = packetBuffer.readStringFromBuffer(32);
            this.field_149319_c = packetBuffer.readStringFromBuffer(16);
            this.field_149316_d = packetBuffer.readStringFromBuffer(16);
            this.field_149315_g = packetBuffer.readByte();
            this.field_179816_e = packetBuffer.readStringFromBuffer(32);
            this.field_179815_f = packetBuffer.readByte();
        }
        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4) {
            int n = packetBuffer.readVarIntFromBuffer();
            int n2 = 0;
            while (n2 < n) {
                this.field_149317_e.add(packetBuffer.readStringFromBuffer(40));
                ++n2;
            }
        }
    }

    public Collection<String> func_149310_g() {
        return this.field_149317_e;
    }

    public String func_149309_f() {
        return this.field_149316_d;
    }

    public String func_149311_e() {
        return this.field_149319_c;
    }

    public String func_179814_i() {
        return this.field_179816_e;
    }

    public S3EPacketTeams() {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
    }

    public S3EPacketTeams(ScorePlayerTeam scorePlayerTeam, int n) {
        this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
        this.field_179815_f = -1;
        this.field_149317_e = Lists.newArrayList();
        this.field_149320_a = scorePlayerTeam.getRegisteredName();
        this.field_149314_f = n;
        if (n == 0 || n == 2) {
            this.field_149318_b = scorePlayerTeam.getTeamName();
            this.field_149319_c = scorePlayerTeam.getColorPrefix();
            this.field_149316_d = scorePlayerTeam.getColorSuffix();
            this.field_149315_g = scorePlayerTeam.func_98299_i();
            this.field_179816_e = scorePlayerTeam.getNameTagVisibility().field_178830_e;
            this.field_179815_f = scorePlayerTeam.getChatFormat().getColorIndex();
        }
        if (n == 0) {
            this.field_149317_e.addAll(scorePlayerTeam.getMembershipCollection());
        }
    }

    public String func_149312_c() {
        return this.field_149320_a;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleTeams(this);
    }

    public int func_179813_h() {
        return this.field_179815_f;
    }
}

