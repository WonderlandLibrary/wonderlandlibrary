package space.lunaclient.luna.api.friend;

import com.google.gson.annotations.SerializedName;

public class Friend
{
  @SerializedName("name")
  private String name;
  @SerializedName("alias")
  private String alias;
  
  public Friend(String name, String alias)
  {
    this.name = name;
    this.alias = alias;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getAlias()
  {
    return this.alias;
  }
}
