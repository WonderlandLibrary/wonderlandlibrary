package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;

public class PlayerProfileCache
{
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
  private final Map field_152661_c = Maps.newHashMap();
  private final Map field_152662_d = Maps.newHashMap();
  private final LinkedList field_152663_e = Lists.newLinkedList();
  private final MinecraftServer field_152664_f;
  protected final Gson gson;
  private final File usercacheFile;
  private static final ParameterizedType field_152666_h = new ParameterizedType()
  {
    private static final String __OBFID = "CL_00001886";
    
    public Type[] getActualTypeArguments()
    {
      return new Type[] { PlayerProfileCache.ProfileEntry.class };
    }
    
    public Type getRawType()
    {
      return List.class;
    }
    
    public Type getOwnerType()
    {
      return null;
    }
  };
  private static final String __OBFID = "CL_00001888";
  
  public PlayerProfileCache(MinecraftServer p_i1171_1_, File p_i1171_2_)
  {
    this.field_152664_f = p_i1171_1_;
    this.usercacheFile = p_i1171_2_;
    GsonBuilder var3 = new GsonBuilder();
    var3.registerTypeHierarchyAdapter(ProfileEntry.class, new Serializer(null));
    this.gson = var3.create();
    func_152657_b();
  }
  
  private static GameProfile func_152650_a(MinecraftServer p_152650_0_, String p_152650_1_)
  {
    GameProfile[] var2 = new GameProfile[1];
    ProfileLookupCallback var3 = new ProfileLookupCallback()
    {
      private static final String __OBFID = "CL_00001887";
      
      public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
      {
        this.val$var2[0] = p_onProfileLookupSucceeded_1_;
      }
      
      public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_)
      {
        this.val$var2[0] = null;
      }
    };
    p_152650_0_.getGameProfileRepository().findProfilesByNames(new String[] { p_152650_1_ }, Agent.MINECRAFT, var3);
    if ((!p_152650_0_.isServerInOnlineMode()) && (var2[0] == null))
    {
      UUID var4 = EntityPlayer.getUUID(new GameProfile((UUID)null, p_152650_1_));
      GameProfile var5 = new GameProfile(var4, p_152650_1_);
      var3.onProfileLookupSucceeded(var5);
    }
    return var2[0];
  }
  
  public void func_152649_a(GameProfile p_152649_1_)
  {
    func_152651_a(p_152649_1_, (Date)null);
  }
  
  private void func_152651_a(GameProfile p_152651_1_, Date p_152651_2_)
  {
    UUID var3 = p_152651_1_.getId();
    if (p_152651_2_ == null)
    {
      Calendar var4 = Calendar.getInstance();
      var4.setTime(new Date());
      var4.add(2, 1);
      p_152651_2_ = var4.getTime();
    }
    String var7 = p_152651_1_.getName().toLowerCase(Locale.ROOT);
    ProfileEntry var5 = new ProfileEntry(p_152651_1_, p_152651_2_, null);
    if (this.field_152662_d.containsKey(var3))
    {
      ProfileEntry var6 = (ProfileEntry)this.field_152662_d.get(var3);
      this.field_152661_c.remove(var6.func_152668_a().getName().toLowerCase(Locale.ROOT));
      this.field_152661_c.put(p_152651_1_.getName().toLowerCase(Locale.ROOT), var5);
      this.field_152663_e.remove(p_152651_1_);
    }
    else
    {
      this.field_152662_d.put(var3, var5);
      this.field_152661_c.put(var7, var5);
    }
    this.field_152663_e.addFirst(p_152651_1_);
  }
  
  public GameProfile getGameProfileForUsername(String p_152655_1_)
  {
    String var2 = p_152655_1_.toLowerCase(Locale.ROOT);
    ProfileEntry var3 = (ProfileEntry)this.field_152661_c.get(var2);
    if ((var3 != null) && (new Date().getTime() >= var3.field_152673_c.getTime()))
    {
      this.field_152662_d.remove(var3.func_152668_a().getId());
      this.field_152661_c.remove(var3.func_152668_a().getName().toLowerCase(Locale.ROOT));
      this.field_152663_e.remove(var3.func_152668_a());
      var3 = null;
    }
    if (var3 != null)
    {
      GameProfile var4 = var3.func_152668_a();
      this.field_152663_e.remove(var4);
      this.field_152663_e.addFirst(var4);
    }
    else
    {
      GameProfile var4 = func_152650_a(this.field_152664_f, var2);
      if (var4 != null)
      {
        func_152649_a(var4);
        var3 = (ProfileEntry)this.field_152661_c.get(var2);
      }
    }
    func_152658_c();
    return var3 == null ? null : var3.func_152668_a();
  }
  
  public String[] func_152654_a()
  {
    ArrayList var1 = Lists.newArrayList(this.field_152661_c.keySet());
    return (String[])var1.toArray(new String[var1.size()]);
  }
  
  public GameProfile func_152652_a(UUID p_152652_1_)
  {
    ProfileEntry var2 = (ProfileEntry)this.field_152662_d.get(p_152652_1_);
    return var2 == null ? null : var2.func_152668_a();
  }
  
  private ProfileEntry func_152653_b(UUID p_152653_1_)
  {
    ProfileEntry var2 = (ProfileEntry)this.field_152662_d.get(p_152653_1_);
    if (var2 != null)
    {
      GameProfile var3 = var2.func_152668_a();
      this.field_152663_e.remove(var3);
      this.field_152663_e.addFirst(var3);
    }
    return var2;
  }
  
  /* Error */
  public void func_152657_b()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield 8	net/minecraft/server/management/PlayerProfileCache:usercacheFile	Ljava/io/File;
    //   8: getstatic 59	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   11: invokestatic 60	com/google/common/io/Files:newReader	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;
    //   14: astore_2
    //   15: aload_0
    //   16: getfield 16	net/minecraft/server/management/PlayerProfileCache:gson	Lcom/google/gson/Gson;
    //   19: aload_2
    //   20: getstatic 61	net/minecraft/server/management/PlayerProfileCache:field_152666_h	Ljava/lang/reflect/ParameterizedType;
    //   23: invokevirtual 62	com/google/gson/Gson:fromJson	(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
    //   26: checkcast 63	java/util/List
    //   29: astore_1
    //   30: aload_2
    //   31: invokestatic 64	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   34: goto +21 -> 55
    //   37: astore_3
    //   38: aload_2
    //   39: invokestatic 64	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   42: goto +12 -> 54
    //   45: astore 4
    //   47: aload_2
    //   48: invokestatic 64	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   51: aload 4
    //   53: athrow
    //   54: return
    //   55: aload_1
    //   56: ifnull +82 -> 138
    //   59: aload_0
    //   60: getfield 3	net/minecraft/server/management/PlayerProfileCache:field_152661_c	Ljava/util/Map;
    //   63: invokeinterface 66 1 0
    //   68: aload_0
    //   69: getfield 4	net/minecraft/server/management/PlayerProfileCache:field_152662_d	Ljava/util/Map;
    //   72: invokeinterface 66 1 0
    //   77: aload_0
    //   78: getfield 6	net/minecraft/server/management/PlayerProfileCache:field_152663_e	Ljava/util/LinkedList;
    //   81: invokevirtual 67	java/util/LinkedList:clear	()V
    //   84: aload_1
    //   85: invokestatic 68	com/google/common/collect/Lists:reverse	(Ljava/util/List;)Ljava/util/List;
    //   88: astore_1
    //   89: aload_1
    //   90: invokeinterface 69 1 0
    //   95: astore_3
    //   96: aload_3
    //   97: invokeinterface 70 1 0
    //   102: ifeq +36 -> 138
    //   105: aload_3
    //   106: invokeinterface 71 1 0
    //   111: checkcast 11	net/minecraft/server/management/PlayerProfileCache$ProfileEntry
    //   114: astore 4
    //   116: aload 4
    //   118: ifnull +17 -> 135
    //   121: aload_0
    //   122: aload 4
    //   124: invokevirtual 44	net/minecraft/server/management/PlayerProfileCache$ProfileEntry:func_152668_a	()Lcom/mojang/authlib/GameProfile;
    //   127: aload 4
    //   129: invokevirtual 72	net/minecraft/server/management/PlayerProfileCache$ProfileEntry:func_152670_b	()Ljava/util/Date;
    //   132: invokespecial 31	net/minecraft/server/management/PlayerProfileCache:func_152651_a	(Lcom/mojang/authlib/GameProfile;Ljava/util/Date;)V
    //   135: goto -39 -> 96
    //   138: return
    // Line number table:
    //   Java source line #205	-> byte code offset #0
    //   Java source line #206	-> byte code offset #2
    //   Java source line #211	-> byte code offset #4
    //   Java source line #212	-> byte code offset #15
    //   Java source line #221	-> byte code offset #30
    //   Java source line #215	-> byte code offset #37
    //   Java source line #221	-> byte code offset #38
    //   Java source line #222	-> byte code offset #42
    //   Java source line #221	-> byte code offset #45
    //   Java source line #222	-> byte code offset #51
    //   Java source line #224	-> byte code offset #54
    //   Java source line #227	-> byte code offset #55
    //   Java source line #229	-> byte code offset #59
    //   Java source line #230	-> byte code offset #68
    //   Java source line #231	-> byte code offset #77
    //   Java source line #232	-> byte code offset #84
    //   Java source line #233	-> byte code offset #89
    //   Java source line #235	-> byte code offset #96
    //   Java source line #237	-> byte code offset #105
    //   Java source line #239	-> byte code offset #116
    //   Java source line #241	-> byte code offset #121
    //   Java source line #243	-> byte code offset #135
    //   Java source line #245	-> byte code offset #138
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	139	0	this	PlayerProfileCache
    //   1	89	1	var1	List
    //   3	45	2	var2	java.io.BufferedReader
    //   37	1	3	localFileNotFoundException	FileNotFoundException
    //   95	11	3	var3	Iterator
    //   45	7	4	localObject	Object
    //   114	14	4	var4	ProfileEntry
    // Exception table:
    //   from	to	target	type
    //   4	30	37	java/io/FileNotFoundException
    //   4	30	45	finally
    //   45	47	45	finally
  }
  
  public void func_152658_c()
  {
    String var1 = this.gson.toJson(func_152656_a(1000));
    BufferedWriter var2 = null;
    try
    {
      var2 = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
      var2.write(var1);
      return;
    }
    catch (FileNotFoundException var8) {}catch (IOException localIOException) {}finally
    {
      IOUtils.closeQuietly(var2);
    }
  }
  
  private List func_152656_a(int p_152656_1_)
  {
    ArrayList var2 = Lists.newArrayList();
    ArrayList var3 = Lists.newArrayList(Iterators.limit(this.field_152663_e.iterator(), p_152656_1_));
    Iterator var4 = var3.iterator();
    while (var4.hasNext())
    {
      GameProfile var5 = (GameProfile)var4.next();
      ProfileEntry var6 = func_152653_b(var5.getId());
      if (var6 != null) {
        var2.add(var6);
      }
    }
    return var2;
  }
  
  class ProfileEntry
  {
    private final GameProfile field_152672_b;
    private final Date field_152673_c;
    private static final String __OBFID = "CL_00001885";
    
    private ProfileEntry(GameProfile p_i46333_2_, Date p_i46333_3_)
    {
      this.field_152672_b = p_i46333_2_;
      this.field_152673_c = p_i46333_3_;
    }
    
    public GameProfile func_152668_a()
    {
      return this.field_152672_b;
    }
    
    public Date func_152670_b()
    {
      return this.field_152673_c;
    }
    
    ProfileEntry(GameProfile p_i1166_2_, Date p_i1166_3_, Object p_i1166_4_)
    {
      this(p_i1166_2_, p_i1166_3_);
    }
  }
  
  class Serializer
    implements JsonDeserializer, JsonSerializer
  {
    private static final String __OBFID = "CL_00001884";
    
    private Serializer() {}
    
    public JsonElement func_152676_a(PlayerProfileCache.ProfileEntry p_152676_1_, Type p_152676_2_, JsonSerializationContext p_152676_3_)
    {
      JsonObject var4 = new JsonObject();
      var4.addProperty("name", p_152676_1_.func_152668_a().getName());
      UUID var5 = p_152676_1_.func_152668_a().getId();
      var4.addProperty("uuid", var5 == null ? "" : var5.toString());
      var4.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_152676_1_.func_152670_b()));
      return var4;
    }
    
    public PlayerProfileCache.ProfileEntry func_152675_a(JsonElement p_152675_1_, Type p_152675_2_, JsonDeserializationContext p_152675_3_)
    {
      if (p_152675_1_.isJsonObject())
      {
        JsonObject var4 = p_152675_1_.getAsJsonObject();
        JsonElement var5 = var4.get("name");
        JsonElement var6 = var4.get("uuid");
        JsonElement var7 = var4.get("expiresOn");
        if ((var5 != null) && (var6 != null))
        {
          String var8 = var6.getAsString();
          String var9 = var5.getAsString();
          Date var10 = null;
          if (var7 != null) {
            try
            {
              var10 = PlayerProfileCache.dateFormat.parse(var7.getAsString());
            }
            catch (ParseException var14)
            {
              var10 = null;
            }
          }
          if ((var9 != null) && (var8 != null))
          {
            try
            {
              var11 = UUID.fromString(var8);
            }
            catch (Throwable var13)
            {
              UUID var11;
              return null;
            }
            UUID var11;
            PlayerProfileCache tmp125_122 = PlayerProfileCache.this;tmp125_122.getClass();PlayerProfileCache.ProfileEntry var12 = new PlayerProfileCache.ProfileEntry(tmp125_122, new GameProfile(var11, var9), var10, null);
            return var12;
          }
          return null;
        }
        return null;
      }
      return null;
    }
    
    public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
    {
      return func_152676_a((PlayerProfileCache.ProfileEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
    }
    
    public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
    {
      return func_152675_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
    }
    
    Serializer(Object p_i46332_2_)
    {
      this();
    }
  }
}
