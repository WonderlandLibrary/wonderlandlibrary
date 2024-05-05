package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class SimpleResource
  implements IResource
{
  private final Map mapMetadataSections = Maps.newHashMap();
  private final String field_177242_b;
  private final ResourceLocation srResourceLocation;
  private final InputStream resourceInputStream;
  private final InputStream mcmetaInputStream;
  private final IMetadataSerializer srMetadataSerializer;
  private boolean mcmetaJsonChecked;
  private JsonObject mcmetaJson;
  private static final String __OBFID = "CL_00001093";
  
  public SimpleResource(String p_i46090_1_, ResourceLocation p_i46090_2_, InputStream p_i46090_3_, InputStream p_i46090_4_, IMetadataSerializer p_i46090_5_)
  {
    this.field_177242_b = p_i46090_1_;
    this.srResourceLocation = p_i46090_2_;
    this.resourceInputStream = p_i46090_3_;
    this.mcmetaInputStream = p_i46090_4_;
    this.srMetadataSerializer = p_i46090_5_;
  }
  
  public ResourceLocation func_177241_a()
  {
    return this.srResourceLocation;
  }
  
  public InputStream getInputStream()
  {
    return this.resourceInputStream;
  }
  
  public boolean hasMetadata()
  {
    return this.mcmetaInputStream != null;
  }
  
  /* Error */
  public net.minecraft.client.resources.data.IMetadataSection getMetadata(String p_110526_1_)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 9	net/minecraft/client/resources/SimpleResource:hasMetadata	()Z
    //   4: ifne +5 -> 9
    //   7: aconst_null
    //   8: areturn
    //   9: aload_0
    //   10: getfield 10	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
    //   13: ifnonnull +68 -> 81
    //   16: aload_0
    //   17: getfield 11	net/minecraft/client/resources/SimpleResource:mcmetaJsonChecked	Z
    //   20: ifne +61 -> 81
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield 11	net/minecraft/client/resources/SimpleResource:mcmetaJsonChecked	Z
    //   28: aconst_null
    //   29: astore_2
    //   30: new 12	java/io/BufferedReader
    //   33: dup
    //   34: new 13	java/io/InputStreamReader
    //   37: dup
    //   38: aload_0
    //   39: getfield 7	net/minecraft/client/resources/SimpleResource:mcmetaInputStream	Ljava/io/InputStream;
    //   42: invokespecial 14	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   45: invokespecial 15	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   48: astore_2
    //   49: aload_0
    //   50: new 16	com/google/gson/JsonParser
    //   53: dup
    //   54: invokespecial 17	com/google/gson/JsonParser:<init>	()V
    //   57: aload_2
    //   58: invokevirtual 18	com/google/gson/JsonParser:parse	(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
    //   61: invokevirtual 19	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
    //   64: putfield 10	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
    //   67: aload_2
    //   68: invokestatic 20	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   71: goto +10 -> 81
    //   74: astore_3
    //   75: aload_2
    //   76: invokestatic 20	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   79: aload_3
    //   80: athrow
    //   81: aload_0
    //   82: getfield 3	net/minecraft/client/resources/SimpleResource:mapMetadataSections	Ljava/util/Map;
    //   85: aload_1
    //   86: invokeinterface 21 2 0
    //   91: checkcast 22	net/minecraft/client/resources/data/IMetadataSection
    //   94: astore_2
    //   95: aload_2
    //   96: ifnonnull +16 -> 112
    //   99: aload_0
    //   100: getfield 8	net/minecraft/client/resources/SimpleResource:srMetadataSerializer	Lnet/minecraft/client/resources/data/IMetadataSerializer;
    //   103: aload_1
    //   104: aload_0
    //   105: getfield 10	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
    //   108: invokevirtual 23	net/minecraft/client/resources/data/IMetadataSerializer:parseMetadataSection	(Ljava/lang/String;Lcom/google/gson/JsonObject;)Lnet/minecraft/client/resources/data/IMetadataSection;
    //   111: astore_2
    //   112: aload_2
    //   113: areturn
    // Line number table:
    //   Java source line #53	-> byte code offset #0
    //   Java source line #55	-> byte code offset #7
    //   Java source line #59	-> byte code offset #9
    //   Java source line #61	-> byte code offset #23
    //   Java source line #62	-> byte code offset #28
    //   Java source line #66	-> byte code offset #30
    //   Java source line #67	-> byte code offset #49
    //   Java source line #71	-> byte code offset #67
    //   Java source line #72	-> byte code offset #71
    //   Java source line #71	-> byte code offset #74
    //   Java source line #72	-> byte code offset #79
    //   Java source line #75	-> byte code offset #81
    //   Java source line #77	-> byte code offset #95
    //   Java source line #79	-> byte code offset #99
    //   Java source line #82	-> byte code offset #112
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	114	0	this	SimpleResource
    //   0	114	1	p_110526_1_	String
    //   29	47	2	var2	java.io.BufferedReader
    //   94	19	2	var6	net.minecraft.client.resources.data.IMetadataSection
    //   74	6	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   30	67	74	finally
  }
  
  public String func_177240_d()
  {
    return this.field_177242_b;
  }
  
  public boolean equals(Object p_equals_1_)
  {
    if (this == p_equals_1_) {
      return true;
    }
    if (!(p_equals_1_ instanceof SimpleResource)) {
      return false;
    }
    SimpleResource var2 = (SimpleResource)p_equals_1_;
    if (this.srResourceLocation != null)
    {
      if (!this.srResourceLocation.equals(var2.srResourceLocation)) {
        return false;
      }
    }
    else if (var2.srResourceLocation != null) {
      return false;
    }
    if (this.field_177242_b != null) {
      return this.field_177242_b.equals(var2.field_177242_b);
    }
    return var2.field_177242_b == null;
  }
  
  public int hashCode()
  {
    int var1 = this.field_177242_b != null ? this.field_177242_b.hashCode() : 0;
    var1 = 31 * var1 + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
    return var1;
  }
}
