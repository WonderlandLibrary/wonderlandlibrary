// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;

public class S45PacketTitle implements Packet
{
    private Type field_179812_a;
    private IChatComponent field_179810_b;
    private int field_179811_c;
    private int field_179808_d;
    private int field_179809_e;
    private static final String __OBFID = "CL_00002287";
    
    public S45PacketTitle() {
    }
    
    public S45PacketTitle(final Type p_i45953_1_, final IChatComponent p_i45953_2_) {
        this(p_i45953_1_, p_i45953_2_, -1, -1, -1);
    }
    
    public S45PacketTitle(final int p_i45954_1_, final int p_i45954_2_, final int p_i45954_3_) {
        this(Type.TIMES, null, p_i45954_1_, p_i45954_2_, p_i45954_3_);
    }
    
    public S45PacketTitle(final Type p_i45955_1_, final IChatComponent p_i45955_2_, final int p_i45955_3_, final int p_i45955_4_, final int p_i45955_5_) {
        this.field_179812_a = p_i45955_1_;
        this.field_179810_b = p_i45955_2_;
        this.field_179811_c = p_i45955_3_;
        this.field_179808_d = p_i45955_4_;
        this.field_179809_e = p_i45955_5_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179812_a = (Type)data.readEnumValue(Type.class);
        if (this.field_179812_a == Type.TITLE || this.field_179812_a == Type.SUBTITLE) {
            this.field_179810_b = data.readChatComponent();
        }
        if (this.field_179812_a == Type.TIMES) {
            this.field_179811_c = data.readInt();
            this.field_179808_d = data.readInt();
            this.field_179809_e = data.readInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeEnumValue(this.field_179812_a);
        if (this.field_179812_a == Type.TITLE || this.field_179812_a == Type.SUBTITLE) {
            data.writeChatComponent(this.field_179810_b);
        }
        if (this.field_179812_a == Type.TIMES) {
            data.writeInt(this.field_179811_c);
            data.writeInt(this.field_179808_d);
            data.writeInt(this.field_179809_e);
        }
    }
    
    public void func_179802_a(final INetHandlerPlayClient p_179802_1_) {
        p_179802_1_.func_175099_a(this);
    }
    
    public Type func_179807_a() {
        return this.field_179812_a;
    }
    
    public IChatComponent func_179805_b() {
        return this.field_179810_b;
    }
    
    public int func_179806_c() {
        return this.field_179811_c;
    }
    
    public int func_179804_d() {
        return this.field_179808_d;
    }
    
    public int func_179803_e() {
        return this.field_179809_e;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179802_a((INetHandlerPlayClient)handler);
    }
    
    public enum Type
    {
        TITLE("TITLE", 0), 
        SUBTITLE("SUBTITLE", 1), 
        TIMES("TIMES", 2), 
        CLEAR("CLEAR", 3), 
        RESET("RESET", 4);
        
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00002286";
        
        private Type(final String p_i45952_1_, final int p_i45952_2_) {
        }
        
        public static Type func_179969_a(final String p_179969_0_) {
            for (final Type var4 : values()) {
                if (var4.name().equalsIgnoreCase(p_179969_0_)) {
                    return var4;
                }
            }
            return Type.TITLE;
        }
        
        public static String[] func_179971_a() {
            final String[] var0 = new String[values().length];
            int var2 = 0;
            for (final Type var6 : values()) {
                var0[var2++] = var6.name().toLowerCase();
            }
            return var0;
        }
        
        static {
            $VALUES = new Type[] { Type.TITLE, Type.SUBTITLE, Type.TIMES, Type.CLEAR, Type.RESET };
        }
    }
}
