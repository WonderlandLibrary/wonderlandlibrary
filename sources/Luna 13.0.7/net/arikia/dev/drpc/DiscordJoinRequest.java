package net.arikia.dev.drpc;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class DiscordJoinRequest
  extends Structure
{
  public String userId;
  public String username;
  public int discriminator;
  public String avatar;
  
  public DiscordJoinRequest() {}
  
  public List<String> getFieldOrder()
  {
    return Arrays.asList(new String[] { "userId", "username", "discriminator", "avatar" });
  }
}
