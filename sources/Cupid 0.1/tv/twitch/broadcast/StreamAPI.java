package tv.twitch.broadcast;

import java.util.HashSet;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public abstract class StreamAPI {
  public abstract void setStreamCallbacks(IStreamCallbacks paramIStreamCallbacks);
  
  public abstract IStreamCallbacks getStreamCallbacks();
  
  public abstract void setStatCallbacks(IStatCallbacks paramIStatCallbacks);
  
  public abstract IStatCallbacks getStatCallbacks();
  
  public abstract ErrorCode requestAuthToken(AuthParams paramAuthParams, HashSet<AuthFlag> paramHashSet);
  
  public abstract ErrorCode login(AuthToken paramAuthToken);
  
  public abstract ErrorCode getIngestServers(AuthToken paramAuthToken);
  
  public abstract ErrorCode getUserInfo(AuthToken paramAuthToken);
  
  public abstract ErrorCode getStreamInfo(AuthToken paramAuthToken, String paramString);
  
  public abstract ErrorCode setStreamInfo(AuthToken paramAuthToken, String paramString, StreamInfoForSetting paramStreamInfoForSetting);
  
  public abstract ErrorCode getArchivingState(AuthToken paramAuthToken);
  
  public abstract ErrorCode runCommercial(AuthToken paramAuthToken);
  
  public abstract ErrorCode setVolume(AudioDeviceType paramAudioDeviceType, float paramFloat);
  
  public abstract float getVolume(AudioDeviceType paramAudioDeviceType);
  
  public abstract ErrorCode getGameNameList(String paramString);
  
  public abstract ErrorCode getDefaultParams(VideoParams paramVideoParams);
  
  public abstract int[] getMaxResolution(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2);
  
  public abstract ErrorCode pollTasks();
  
  public abstract ErrorCode pollStats();
  
  public abstract ErrorCode sendActionMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3);
  
  public abstract long sendStartSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3);
  
  public abstract ErrorCode sendEndSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong1, long paramLong2, String paramString2, String paramString3);
  
  public abstract ErrorCode submitVideoFrame(long paramLong);
  
  public abstract ErrorCode start(VideoParams paramVideoParams, AudioParams paramAudioParams, IngestServer paramIngestServer, int paramInt, boolean paramBoolean);
  
  public abstract ErrorCode stop(boolean paramBoolean);
  
  public abstract ErrorCode pauseVideo();
  
  public abstract long allocateFrameBuffer(int paramInt);
  
  public abstract ErrorCode freeFrameBuffer(long paramLong);
  
  public abstract ErrorCode memsetFrameBuffer(long paramLong, int paramInt1, int paramInt2);
  
  public abstract ErrorCode randomizeFrameBuffer(long paramLong, int paramInt);
  
  public abstract ErrorCode captureFrameBuffer_ReadPixels(long paramLong);
  
  public abstract long getStreamTime();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\StreamAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */