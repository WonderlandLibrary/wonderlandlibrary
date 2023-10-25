package tv.twitch.chat;

import tv.twitch.ErrorCode;

public interface IChatChannelListener {
  void chatStatusCallback(String paramString, ErrorCode paramErrorCode);
  
  void chatChannelMembershipCallback(String paramString, ChatEvent paramChatEvent, ChatChannelInfo paramChatChannelInfo);
  
  void chatChannelUserChangeCallback(String paramString, ChatUserInfo[] paramArrayOfChatUserInfo1, ChatUserInfo[] paramArrayOfChatUserInfo2, ChatUserInfo[] paramArrayOfChatUserInfo3);
  
  void chatChannelRawMessageCallback(String paramString, ChatRawMessage[] paramArrayOfChatRawMessage);
  
  void chatChannelTokenizedMessageCallback(String paramString, ChatTokenizedMessage[] paramArrayOfChatTokenizedMessage);
  
  void chatClearCallback(String paramString1, String paramString2);
  
  void chatBadgeDataDownloadCallback(String paramString, ErrorCode paramErrorCode);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\IChatChannelListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */