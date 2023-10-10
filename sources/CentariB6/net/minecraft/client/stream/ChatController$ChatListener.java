package net.minecraft.client.stream;

import net.minecraft.client.stream.ChatController.ChatState;
import tv.twitch.ErrorCode;
import tv.twitch.chat.ChatRawMessage;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatUserInfo;

public interface ChatController$ChatListener {
   void func_176017_a(ChatState var1);

   void func_180605_a(String var1, ChatRawMessage[] var2);

   void func_176022_e(ErrorCode var1);

   void func_176018_a(String var1, ChatUserInfo[] var2, ChatUserInfo[] var3, ChatUserInfo[] var4);

   void func_176025_a(String var1, ChatTokenizedMessage[] var2);

   void func_176023_d(ErrorCode var1);

   void func_176016_c(String var1);

   void func_176019_a(String var1, String var2);

   void func_176020_d(String var1);

   void func_180606_a(String var1);

   void func_176024_e();

   void func_180607_b(String var1);

   void func_176021_d();
}
