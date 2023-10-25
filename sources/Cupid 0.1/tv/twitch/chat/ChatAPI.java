package tv.twitch.chat;

import java.util.HashSet;
import tv.twitch.ErrorCode;

public abstract class ChatAPI {
  public abstract ErrorCode initialize(HashSet<ChatTokenizationOption> paramHashSet, IChatAPIListener paramIChatAPIListener);
  
  public abstract ErrorCode shutdown();
  
  public abstract ErrorCode connect(String paramString1, String paramString2, String paramString3, IChatChannelListener paramIChatChannelListener);
  
  public abstract ErrorCode connectAnonymous(String paramString, IChatChannelListener paramIChatChannelListener);
  
  public abstract ErrorCode disconnect(String paramString);
  
  public abstract ErrorCode sendMessage(String paramString1, String paramString2);
  
  public abstract ErrorCode flushEvents();
  
  public abstract ErrorCode downloadEmoticonData();
  
  public abstract ErrorCode getEmoticonData(ChatEmoticonData paramChatEmoticonData);
  
  public abstract ErrorCode clearEmoticonData();
  
  public abstract ErrorCode downloadBadgeData(String paramString);
  
  public abstract ErrorCode getBadgeData(String paramString, ChatBadgeData paramChatBadgeData);
  
  public abstract ErrorCode clearBadgeData(String paramString);
  
  public abstract int getMessageFlushInterval();
  
  public abstract ErrorCode setMessageFlushInterval(int paramInt);
  
  public abstract int getUserChangeEventInterval();
  
  public abstract ErrorCode setUserChangeEventInterval(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */