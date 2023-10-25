package tv.twitch.chat;

import java.util.HashSet;

public class ChatUserInfo {
  public String displayName = null;
  
  public HashSet<ChatUserMode> modes = new HashSet<ChatUserMode>();
  
  public HashSet<ChatUserSubscription> subscriptions = new HashSet<ChatUserSubscription>();
  
  public int nameColorARGB = 0;
  
  public boolean ignore = false;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatUserInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */