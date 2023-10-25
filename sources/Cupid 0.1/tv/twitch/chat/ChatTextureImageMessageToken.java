package tv.twitch.chat;

public class ChatTextureImageMessageToken extends ChatMessageToken {
  public int sheetIndex = -1;
  
  public short x1;
  
  public short y1;
  
  public short x2;
  
  public short y2;
  
  public ChatTextureImageMessageToken() {
    this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatTextureImageMessageToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */