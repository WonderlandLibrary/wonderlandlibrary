package tv.twitch.chat;

import java.util.HashSet;

public class ChatTokenizedMessage {
  public String displayName;
  
  public HashSet<ChatUserMode> modes = new HashSet<ChatUserMode>();
  
  public HashSet<ChatUserSubscription> subscriptions = new HashSet<ChatUserSubscription>();
  
  public int nameColorARGB;
  
  public ChatMessageToken[] tokenList;
  
  public boolean action;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatTokenizedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */