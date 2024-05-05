package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Map;
import java.util.UUID;

public class Session
{
  private String username;
  private final String playerID;
  private final String token;
  private final Type sessionType;
  private static final String __OBFID = "CL_00000659";
  
  public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_, String p_i1098_4_)
  {
    this.username = p_i1098_1_;
    this.playerID = p_i1098_2_;
    this.token = p_i1098_3_;
    this.sessionType = Type.setSessionType(p_i1098_4_);
  }
  
  public String getSessionID()
  {
    return "token:" + this.token + ":" + this.playerID;
  }
  
  public String getPlayerID()
  {
    return this.playerID;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getToken()
  {
    return this.token;
  }
  
  public GameProfile getProfile()
  {
    try
    {
      UUID var1 = UUIDTypeAdapter.fromString(getPlayerID());
      return new GameProfile(var1, getUsername());
    }
    catch (IllegalArgumentException var2) {}
    return new GameProfile((UUID)null, getUsername());
  }
  
  public Type getSessionType()
  {
    return this.sessionType;
  }
  
  public static enum Type
  {
    private static final Map field_152425_c;
    private final String sessionType;
    private static final Type[] $VALUES;
    private static final String __OBFID = "CL_00001851";
    
    private Type(String p_i1096_1_, int p_i1096_2_, String p_i1096_3_)
    {
      this.sessionType = p_i1096_3_;
    }
    
    public static Type setSessionType(String p_152421_0_)
    {
      return (Type)field_152425_c.get(p_152421_0_.toLowerCase());
    }
    
    static
    {
      field_152425_c = Maps.newHashMap();
      
      $VALUES = new Type[] { LEGACY, MOJANG };
      
      Type[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        Type var3 = var0[var2];
        field_152425_c.put(var3.sessionType, var3);
      }
    }
  }
}
