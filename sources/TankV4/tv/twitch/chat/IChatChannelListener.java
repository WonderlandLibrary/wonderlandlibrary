package tv.twitch.chat;

import tv.twitch.ErrorCode;

public interface IChatChannelListener {
   void chatStatusCallback(String var1, ErrorCode var2);

   void chatChannelMembershipCallback(String var1, ChatEvent var2, ChatChannelInfo var3);

   void chatChannelUserChangeCallback(String var1, ChatUserInfo[] var2, ChatUserInfo[] var3, ChatUserInfo[] var4);

   void chatChannelRawMessageCallback(String var1, ChatRawMessage[] var2);

   void chatChannelTokenizedMessageCallback(String var1, ChatTokenizedMessage[] var2);

   void chatClearCallback(String var1, String var2);

   void chatBadgeDataDownloadCallback(String var1, ErrorCode var2);
}
