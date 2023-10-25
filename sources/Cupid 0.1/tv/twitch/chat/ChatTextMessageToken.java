package tv.twitch.chat;

public class ChatTextMessageToken extends ChatMessageToken {
  public String text = null;
  
  public ChatTextMessageToken() {
    this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_TEXT;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatTextMessageToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */