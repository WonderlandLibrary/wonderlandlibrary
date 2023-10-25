package tv.twitch.chat;

import java.util.HashSet;
import tv.twitch.ErrorCode;

public class Chat {
  private static Chat s_Instance = null;
  
  public static Chat getInstance() {
    return s_Instance;
  }
  
  private ChatAPI m_ChatAPI = null;
  
  public Chat(ChatAPI paramChatAPI) {
    this.m_ChatAPI = paramChatAPI;
    if (s_Instance == null)
      s_Instance = this; 
  }
  
  public ErrorCode initialize(HashSet<ChatTokenizationOption> paramHashSet, IChatAPIListener paramIChatAPIListener) {
    return this.m_ChatAPI.initialize(paramHashSet, paramIChatAPIListener);
  }
  
  public ErrorCode shutdown() {
    return this.m_ChatAPI.shutdown();
  }
  
  public ErrorCode connect(String paramString1, String paramString2, String paramString3, IChatChannelListener paramIChatChannelListener) {
    return this.m_ChatAPI.connect(paramString1, paramString2, paramString3, paramIChatChannelListener);
  }
  
  public ErrorCode connectAnonymous(String paramString, IChatChannelListener paramIChatChannelListener) {
    return this.m_ChatAPI.connectAnonymous(paramString, paramIChatChannelListener);
  }
  
  public ErrorCode disconnect(String paramString) {
    return this.m_ChatAPI.disconnect(paramString);
  }
  
  public ErrorCode sendMessage(String paramString1, String paramString2) {
    return this.m_ChatAPI.sendMessage(paramString1, paramString2);
  }
  
  public ErrorCode flushEvents() {
    return this.m_ChatAPI.flushEvents();
  }
  
  public ErrorCode downloadEmoticonData() {
    return this.m_ChatAPI.downloadEmoticonData();
  }
  
  public ErrorCode getEmoticonData(ChatEmoticonData paramChatEmoticonData) {
    return this.m_ChatAPI.getEmoticonData(paramChatEmoticonData);
  }
  
  public ErrorCode clearEmoticonData() {
    return this.m_ChatAPI.clearEmoticonData();
  }
  
  public ErrorCode downloadBadgeData(String paramString) {
    return this.m_ChatAPI.downloadBadgeData(paramString);
  }
  
  public ErrorCode getBadgeData(String paramString, ChatBadgeData paramChatBadgeData) {
    return this.m_ChatAPI.getBadgeData(paramString, paramChatBadgeData);
  }
  
  public ErrorCode clearBadgeData(String paramString) {
    return this.m_ChatAPI.clearBadgeData(paramString);
  }
  
  public int getMessageFlushInterval() {
    return this.m_ChatAPI.getMessageFlushInterval();
  }
  
  public ErrorCode setMessageFlushInterval(int paramInt) {
    return this.m_ChatAPI.setMessageFlushInterval(paramInt);
  }
  
  public int getUserChangeEventInterval() {
    return this.m_ChatAPI.getUserChangeEventInterval();
  }
  
  public ErrorCode setUserChangeEventInterval(int paramInt) {
    return this.m_ChatAPI.setUserChangeEventInterval(paramInt);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\Chat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */