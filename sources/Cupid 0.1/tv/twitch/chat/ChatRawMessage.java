package tv.twitch.chat;

import java.util.HashSet;

public class ChatRawMessage {
  public String userName = null;
  
  public String message = null;
  
  public HashSet<ChatUserMode> modes = new HashSet<ChatUserMode>();
  
  public HashSet<ChatUserSubscription> subscriptions = new HashSet<ChatUserSubscription>();
  
  public int nameColorARGB = 0;
  
  public boolean action = false;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatRawMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */