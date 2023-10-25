package tv.twitch.chat;

import tv.twitch.ErrorCode;

public interface IChatAPIListener {
  void chatInitializationCallback(ErrorCode paramErrorCode);
  
  void chatShutdownCallback(ErrorCode paramErrorCode);
  
  void chatEmoticonDataDownloadCallback(ErrorCode paramErrorCode);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\IChatAPIListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */