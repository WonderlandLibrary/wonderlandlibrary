package tv.twitch.chat;

import java.util.HashSet;
import tv.twitch.ErrorCode;

public class StandardChatAPI extends ChatAPI {
  protected void finalize() {}
  
  private static native ErrorCode TTV_Java_Chat_Initialize(int paramInt, IChatAPIListener paramIChatAPIListener);
  
  private static native ErrorCode TTV_Java_Chat_Shutdown();
  
  private static native ErrorCode TTV_Java_Chat_Connect(String paramString1, String paramString2, String paramString3, IChatChannelListener paramIChatChannelListener);
  
  private static native ErrorCode TTV_Java_Chat_ConnectAnonymous(String paramString, IChatChannelListener paramIChatChannelListener);
  
  private static native ErrorCode TTV_Java_Chat_Disconnect(String paramString);
  
  private static native ErrorCode TTV_Java_Chat_SendMessage(String paramString1, String paramString2);
  
  private static native ErrorCode TTV_Java_Chat_FlushEvents();
  
  private static native ErrorCode TTV_Java_Chat_DownloadEmoticonData();
  
  private static native ErrorCode TTV_Java_Chat_GetEmoticonData(ChatEmoticonData paramChatEmoticonData);
  
  private static native ErrorCode TTV_Java_Chat_ClearEmoticonData();
  
  private static native ErrorCode TTV_Java_Chat_DownloadBadgeData(String paramString);
  
  private static native ErrorCode TTV_Java_Chat_GetBadgeData(String paramString, ChatBadgeData paramChatBadgeData);
  
  private static native ErrorCode TTV_Java_Chat_ClearBadgeData(String paramString);
  
  private static native long TTV_Java_Chat_GetMessageFlushInterval();
  
  private static native ErrorCode TTV_Java_Chat_SetMessageFlushInterval(long paramLong);
  
  private static native long TTV_Java_Chat_GetUserChangeEventInterval();
  
  private static native ErrorCode TTV_Java_Chat_SetUserChangeEventInterval(long paramLong);
  
  public ErrorCode initialize(HashSet<ChatTokenizationOption> paramHashSet, IChatAPIListener paramIChatAPIListener) {
    int i = ChatTokenizationOption.getNativeValue(paramHashSet);
    return TTV_Java_Chat_Initialize(i, paramIChatAPIListener);
  }
  
  public ErrorCode shutdown() {
    return TTV_Java_Chat_Shutdown();
  }
  
  public ErrorCode connect(String paramString1, String paramString2, String paramString3, IChatChannelListener paramIChatChannelListener) {
    return TTV_Java_Chat_Connect(paramString1, paramString2, paramString3, paramIChatChannelListener);
  }
  
  public ErrorCode connectAnonymous(String paramString, IChatChannelListener paramIChatChannelListener) {
    return TTV_Java_Chat_ConnectAnonymous(paramString, paramIChatChannelListener);
  }
  
  public ErrorCode disconnect(String paramString) {
    return TTV_Java_Chat_Disconnect(paramString);
  }
  
  public ErrorCode sendMessage(String paramString1, String paramString2) {
    return TTV_Java_Chat_SendMessage(paramString1, paramString2);
  }
  
  public ErrorCode flushEvents() {
    return TTV_Java_Chat_FlushEvents();
  }
  
  public ErrorCode downloadEmoticonData() {
    return TTV_Java_Chat_DownloadEmoticonData();
  }
  
  public ErrorCode getEmoticonData(ChatEmoticonData paramChatEmoticonData) {
    return TTV_Java_Chat_GetEmoticonData(paramChatEmoticonData);
  }
  
  public ErrorCode clearEmoticonData() {
    return TTV_Java_Chat_ClearEmoticonData();
  }
  
  public ErrorCode downloadBadgeData(String paramString) {
    return TTV_Java_Chat_DownloadBadgeData(paramString);
  }
  
  public ErrorCode getBadgeData(String paramString, ChatBadgeData paramChatBadgeData) {
    return TTV_Java_Chat_GetBadgeData(paramString, paramChatBadgeData);
  }
  
  public ErrorCode clearBadgeData(String paramString) {
    return TTV_Java_Chat_ClearBadgeData(paramString);
  }
  
  public int getMessageFlushInterval() {
    return (int)TTV_Java_Chat_GetMessageFlushInterval();
  }
  
  public ErrorCode setMessageFlushInterval(int paramInt) {
    return TTV_Java_Chat_SetMessageFlushInterval(paramInt);
  }
  
  public int getUserChangeEventInterval() {
    return (int)TTV_Java_Chat_GetUserChangeEventInterval();
  }
  
  public ErrorCode setUserChangeEventInterval(int paramInt) {
    return TTV_Java_Chat_SetUserChangeEventInterval(paramInt);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\StandardChatAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */