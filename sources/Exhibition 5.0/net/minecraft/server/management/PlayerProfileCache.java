// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.text.ParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.common.collect.Iterators;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.io.Reader;
import java.util.List;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.GameProfile;
import com.google.gson.GsonBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.ParameterizedType;
import java.io.File;
import com.google.gson.Gson;
import net.minecraft.server.MinecraftServer;
import java.util.LinkedList;
import java.util.Map;
import java.text.SimpleDateFormat;

public class PlayerProfileCache
{
    public static final SimpleDateFormat dateFormat;
    private final Map field_152661_c;
    private final Map field_152662_d;
    private final LinkedList field_152663_e;
    private final MinecraftServer field_152664_f;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType field_152666_h;
    private static final String __OBFID = "CL_00001888";
    
    public PlayerProfileCache(final MinecraftServer p_i1171_1_, final File p_i1171_2_) {
        this.field_152661_c = Maps.newHashMap();
        this.field_152662_d = Maps.newHashMap();
        this.field_152663_e = Lists.newLinkedList();
        this.field_152664_f = p_i1171_1_;
        this.usercacheFile = p_i1171_2_;
        final GsonBuilder var3 = new GsonBuilder();
        var3.registerTypeHierarchyAdapter((Class)ProfileEntry.class, (Object)new Serializer(null));
        this.gson = var3.create();
        this.func_152657_b();
    }
    
    private static GameProfile func_152650_a(final MinecraftServer p_152650_0_, final String p_152650_1_) {
        final GameProfile[] var2 = { null };
        final ProfileLookupCallback var3 = (ProfileLookupCallback)new ProfileLookupCallback() {
            private static final String __OBFID = "CL_00001887";
            
            public void onProfileLookupSucceeded(final GameProfile p_onProfileLookupSucceeded_1_) {
                var2[0] = p_onProfileLookupSucceeded_1_;
            }
            
            public void onProfileLookupFailed(final GameProfile p_onProfileLookupFailed_1_, final Exception p_onProfileLookupFailed_2_) {
                var2[0] = null;
            }
        };
        p_152650_0_.getGameProfileRepository().findProfilesByNames(new String[] { p_152650_1_ }, Agent.MINECRAFT, var3);
        if (!p_152650_0_.isServerInOnlineMode() && var2[0] == null) {
            final UUID var4 = EntityPlayer.getUUID(new GameProfile((UUID)null, p_152650_1_));
            final GameProfile var5 = new GameProfile(var4, p_152650_1_);
            var3.onProfileLookupSucceeded(var5);
        }
        return var2[0];
    }
    
    public void func_152649_a(final GameProfile p_152649_1_) {
        this.func_152651_a(p_152649_1_, null);
    }
    
    private void func_152651_a(final GameProfile p_152651_1_, Date p_152651_2_) {
        final UUID var3 = p_152651_1_.getId();
        if (p_152651_2_ == null) {
            final Calendar var4 = Calendar.getInstance();
            var4.setTime(new Date());
            var4.add(2, 1);
            p_152651_2_ = var4.getTime();
        }
        final String var5 = p_152651_1_.getName().toLowerCase(Locale.ROOT);
        final ProfileEntry var6 = new ProfileEntry(p_152651_1_, p_152651_2_, null);
        if (this.field_152662_d.containsKey(var3)) {
            final ProfileEntry var7 = this.field_152662_d.get(var3);
            this.field_152661_c.remove(var7.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152661_c.put(p_152651_1_.getName().toLowerCase(Locale.ROOT), var6);
            this.field_152663_e.remove(p_152651_1_);
        }
        else {
            this.field_152662_d.put(var3, var6);
            this.field_152661_c.put(var5, var6);
        }
        this.field_152663_e.addFirst(p_152651_1_);
    }
    
    public GameProfile getGameProfileForUsername(final String p_152655_1_) {
        final String var2 = p_152655_1_.toLowerCase(Locale.ROOT);
        ProfileEntry var3 = this.field_152661_c.get(var2);
        if (var3 != null && new Date().getTime() >= var3.field_152673_c.getTime()) {
            this.field_152662_d.remove(var3.func_152668_a().getId());
            this.field_152661_c.remove(var3.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152663_e.remove(var3.func_152668_a());
            var3 = null;
        }
        if (var3 != null) {
            final GameProfile var4 = var3.func_152668_a();
            this.field_152663_e.remove(var4);
            this.field_152663_e.addFirst(var4);
        }
        else {
            final GameProfile var4 = func_152650_a(this.field_152664_f, var2);
            if (var4 != null) {
                this.func_152649_a(var4);
                var3 = this.field_152661_c.get(var2);
            }
        }
        this.func_152658_c();
        return (var3 == null) ? null : var3.func_152668_a();
    }
    
    public String[] func_152654_a() {
        final ArrayList var1 = Lists.newArrayList((Iterable)this.field_152661_c.keySet());
        return var1.toArray(new String[var1.size()]);
    }
    
    public GameProfile func_152652_a(final UUID p_152652_1_) {
        final ProfileEntry var2 = this.field_152662_d.get(p_152652_1_);
        return (var2 == null) ? null : var2.func_152668_a();
    }
    
    private ProfileEntry func_152653_b(final UUID p_152653_1_) {
        final ProfileEntry var2 = this.field_152662_d.get(p_152653_1_);
        if (var2 != null) {
            final GameProfile var3 = var2.func_152668_a();
            this.field_152663_e.remove(var3);
            this.field_152663_e.addFirst(var3);
        }
        return var2;
    }
    
    public void func_152657_b() {
        List var1 = null;
        BufferedReader var2 = null;
        Label_0055: {
            try {
                var2 = Files.newReader(this.usercacheFile, Charsets.UTF_8);
                var1 = (List)this.gson.fromJson((Reader)var2, (Type)PlayerProfileCache.field_152666_h);
                break Label_0055;
            }
            catch (FileNotFoundException ex) {}
            finally {
                IOUtils.closeQuietly((Reader)var2);
            }
            return;
        }
        if (var1 != null) {
            this.field_152661_c.clear();
            this.field_152662_d.clear();
            this.field_152663_e.clear();
            var1 = Lists.reverse(var1);
            for (final ProfileEntry var4 : var1) {
                if (var4 != null) {
                    this.func_152651_a(var4.func_152668_a(), var4.func_152670_b());
                }
            }
        }
    }
    
    public void func_152658_c() {
        final String var1 = this.gson.toJson((Object)this.func_152656_a(1000));
        BufferedWriter var2 = null;
        try {
            var2 = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
            var2.write(var1);
        }
        catch (FileNotFoundException ex) {}
        catch (IOException var3) {}
        finally {
            IOUtils.closeQuietly((Writer)var2);
        }
    }
    
    private List func_152656_a(final int p_152656_1_) {
        final ArrayList var2 = Lists.newArrayList();
        final ArrayList var3 = Lists.newArrayList(Iterators.limit((Iterator)this.field_152663_e.iterator(), p_152656_1_));
        for (final GameProfile var5 : var3) {
            final ProfileEntry var6 = this.func_152653_b(var5.getId());
            if (var6 != null) {
                var2.add(var6);
            }
        }
        return var2;
    }
    
    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        field_152666_h = new ParameterizedType() {
            private static final String __OBFID = "CL_00001886";
            
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { ProfileEntry.class };
            }
            
            @Override
            public Type getRawType() {
                return List.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
    
    class ProfileEntry
    {
        private final GameProfile field_152672_b;
        private final Date field_152673_c;
        private static final String __OBFID = "CL_00001885";
        
        private ProfileEntry(final GameProfile p_i46333_2_, final Date p_i46333_3_) {
            this.field_152672_b = p_i46333_2_;
            this.field_152673_c = p_i46333_3_;
        }
        
        public GameProfile func_152668_a() {
            return this.field_152672_b;
        }
        
        public Date func_152670_b() {
            return this.field_152673_c;
        }
        
        ProfileEntry(final PlayerProfileCache this$0, final GameProfile p_i1166_2_, final Date p_i1166_3_, final Object p_i1166_4_) {
            this(this$0, p_i1166_2_, p_i1166_3_);
        }
    }
    
    class Serializer implements JsonDeserializer, JsonSerializer
    {
        private static final String __OBFID = "CL_00001884";
        
        private Serializer() {
        }
        
        public JsonElement func_152676_a(final ProfileEntry p_152676_1_, final Type p_152676_2_, final JsonSerializationContext p_152676_3_) {
            final JsonObject var4 = new JsonObject();
            var4.addProperty("name", p_152676_1_.func_152668_a().getName());
            final UUID var5 = p_152676_1_.func_152668_a().getId();
            var4.addProperty("uuid", (var5 == null) ? "" : var5.toString());
            var4.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_152676_1_.func_152670_b()));
            return (JsonElement)var4;
        }
        
        public ProfileEntry func_152675_a(final JsonElement p_152675_1_, final Type p_152675_2_, final JsonDeserializationContext p_152675_3_) {
            if (!p_152675_1_.isJsonObject()) {
                return null;
            }
            final JsonObject var4 = p_152675_1_.getAsJsonObject();
            final JsonElement var5 = var4.get("name");
            final JsonElement var6 = var4.get("uuid");
            final JsonElement var7 = var4.get("expiresOn");
            if (var5 == null || var6 == null) {
                return null;
            }
            final String var8 = var6.getAsString();
            final String var9 = var5.getAsString();
            Date var10 = null;
            if (var7 != null) {
                try {
                    var10 = PlayerProfileCache.dateFormat.parse(var7.getAsString());
                }
                catch (ParseException var13) {
                    var10 = null;
                }
            }
            if (var9 != null && var8 != null) {
                UUID var11;
                try {
                    var11 = UUID.fromString(var8);
                }
                catch (Throwable var14) {
                    return null;
                }
                final PlayerProfileCache this$0 = PlayerProfileCache.this;
                this$0.getClass();
                final ProfileEntry var12 = this$0.new ProfileEntry(new GameProfile(var11, var9), var10, null);
                return var12;
            }
            return null;
        }
        
        public JsonElement serialize(final Object p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            return this.func_152676_a((ProfileEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.func_152675_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
        
        Serializer(final PlayerProfileCache this$0, final Object p_i46332_2_) {
            this(this$0);
        }
    }
}
