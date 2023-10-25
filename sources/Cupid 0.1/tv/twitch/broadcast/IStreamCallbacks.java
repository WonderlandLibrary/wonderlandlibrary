package tv.twitch.broadcast;

import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public interface IStreamCallbacks {
  void requestAuthTokenCallback(ErrorCode paramErrorCode, AuthToken paramAuthToken);
  
  void loginCallback(ErrorCode paramErrorCode, ChannelInfo paramChannelInfo);
  
  void getIngestServersCallback(ErrorCode paramErrorCode, IngestList paramIngestList);
  
  void getUserInfoCallback(ErrorCode paramErrorCode, UserInfo paramUserInfo);
  
  void getStreamInfoCallback(ErrorCode paramErrorCode, StreamInfo paramStreamInfo);
  
  void getArchivingStateCallback(ErrorCode paramErrorCode, ArchivingState paramArchivingState);
  
  void runCommercialCallback(ErrorCode paramErrorCode);
  
  void setStreamInfoCallback(ErrorCode paramErrorCode);
  
  void getGameNameListCallback(ErrorCode paramErrorCode, GameInfoList paramGameInfoList);
  
  void bufferUnlockCallback(long paramLong);
  
  void startCallback(ErrorCode paramErrorCode);
  
  void stopCallback(ErrorCode paramErrorCode);
  
  void sendActionMetaDataCallback(ErrorCode paramErrorCode);
  
  void sendStartSpanMetaDataCallback(ErrorCode paramErrorCode);
  
  void sendEndSpanMetaDataCallback(ErrorCode paramErrorCode);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\IStreamCallbacks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */