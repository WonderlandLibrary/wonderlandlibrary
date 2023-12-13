package tv.twitch.chat;

public class ChatTextMessageToken extends ChatMessageToken {
   public String text = null;

   public ChatTextMessageToken() {
      this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_TEXT;
   }
}
