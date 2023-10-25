package tv.twitch.broadcast;

import java.util.HashSet;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public class DesktopStreamAPI extends StreamAPI {
  protected void finalize() {
    TTV_Java_SetStreamCallbacks(null);
    TTV_Java_SetStatCallbacks(null);
  }
  
  private static native void TTV_Java_SetStreamCallbacks(IStreamCallbacks paramIStreamCallbacks);
  
  private static native IStreamCallbacks TTV_Java_GetStreamCallbacks();
  
  private static native void TTV_Java_SetStatCallbacks(IStatCallbacks paramIStatCallbacks);
  
  private static native IStatCallbacks TTV_Java_GetStatCallbacks();
  
  private static native ErrorCode TTV_Java_RequestAuthToken(AuthParams paramAuthParams, int paramInt);
  
  private static native ErrorCode TTV_Java_Login(AuthToken paramAuthToken);
  
  private static native ErrorCode TTV_Java_GetIngestServers(AuthToken paramAuthToken);
  
  private static native ErrorCode TTV_Java_GetUserInfo(AuthToken paramAuthToken);
  
  private static native ErrorCode TTV_Java_GetStreamInfo(AuthToken paramAuthToken, String paramString);
  
  private static native ErrorCode TTV_Java_SetStreamInfo(AuthToken paramAuthToken, String paramString, StreamInfoForSetting paramStreamInfoForSetting);
  
  private static native ErrorCode TTV_Java_GetArchivingState(AuthToken paramAuthToken);
  
  private static native ErrorCode TTV_Java_RunCommercial(AuthToken paramAuthToken);
  
  private static native ErrorCode TTV_Java_SetVolume(AudioDeviceType paramAudioDeviceType, float paramFloat);
  
  private static native float TTV_Java_GetVolume(AudioDeviceType paramAudioDeviceType);
  
  private static native ErrorCode TTV_Java_GetGameNameList(String paramString);
  
  private static native ErrorCode TTV_Java_GetDefaultParams(VideoParams paramVideoParams);
  
  private static native ErrorCode TTV_GetMaxResolution(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int[] paramArrayOfint);
  
  private static native ErrorCode TTV_Java_PollTasks();
  
  private static native ErrorCode TTV_Java_PollStats();
  
  private static native ErrorCode TTV_Java_Init(int paramInt);
  
  private static native ErrorCode TTV_Java_Shutdown();
  
  private static native ErrorCode TTV_Java_SendActionMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3);
  
  private static native long TTV_Java_SendStartSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3);
  
  private static native ErrorCode TTV_Java_SendEndSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong1, long paramLong2, String paramString2, String paramString3);
  
  private static native ErrorCode TTV_Java_SubmitVideoFrame(long paramLong);
  
  private static native ErrorCode TTV_Java_Start(VideoParams paramVideoParams, AudioParams paramAudioParams, IngestServer paramIngestServer, int paramInt, boolean paramBoolean);
  
  private static native ErrorCode TTV_Java_Stop(boolean paramBoolean);
  
  private static native ErrorCode TTV_Java_PauseVideo();
  
  private static native long TTV_Java_AllocateFrameBuffer(int paramInt);
  
  private static native ErrorCode TTV_Java_FreeFrameBuffer(long paramLong);
  
  private static native ErrorCode TTV_Java_MemsetFrameBuffer(long paramLong, int paramInt1, int paramInt2);
  
  private static native ErrorCode TTV_Java_RandomizeFrameBuffer(long paramLong, int paramInt);
  
  private static native ErrorCode TTV_Java_CaptureFrameBuffer_ReadPixels(long paramLong);
  
  private static native long TTV_Java_GetStreamTime();
  
  public void setStreamCallbacks(IStreamCallbacks paramIStreamCallbacks) {
    TTV_Java_SetStreamCallbacks(paramIStreamCallbacks);
  }
  
  public IStreamCallbacks getStreamCallbacks() {
    return TTV_Java_GetStreamCallbacks();
  }
  
  public void setStatCallbacks(IStatCallbacks paramIStatCallbacks) {
    TTV_Java_SetStatCallbacks(paramIStatCallbacks);
  }
  
  public IStatCallbacks getStatCallbacks() {
    return TTV_Java_GetStatCallbacks();
  }
  
  public ErrorCode requestAuthToken(AuthParams paramAuthParams, HashSet<AuthFlag> paramHashSet) {
    if (paramHashSet == null)
      paramHashSet = new HashSet<AuthFlag>(); 
    int i = AuthFlag.getNativeValue(paramHashSet);
    return TTV_Java_RequestAuthToken(paramAuthParams, i);
  }
  
  public ErrorCode login(AuthToken paramAuthToken) {
    return TTV_Java_Login(paramAuthToken);
  }
  
  public ErrorCode getIngestServers(AuthToken paramAuthToken) {
    return TTV_Java_GetIngestServers(paramAuthToken);
  }
  
  public ErrorCode getUserInfo(AuthToken paramAuthToken) {
    return TTV_Java_GetUserInfo(paramAuthToken);
  }
  
  public ErrorCode getStreamInfo(AuthToken paramAuthToken, String paramString) {
    return TTV_Java_GetStreamInfo(paramAuthToken, paramString);
  }
  
  public ErrorCode setStreamInfo(AuthToken paramAuthToken, String paramString, StreamInfoForSetting paramStreamInfoForSetting) {
    return TTV_Java_SetStreamInfo(paramAuthToken, paramString, paramStreamInfoForSetting);
  }
  
  public ErrorCode getArchivingState(AuthToken paramAuthToken) {
    return TTV_Java_GetArchivingState(paramAuthToken);
  }
  
  public ErrorCode runCommercial(AuthToken paramAuthToken) {
    return TTV_Java_RunCommercial(paramAuthToken);
  }
  
  public ErrorCode setVolume(AudioDeviceType paramAudioDeviceType, float paramFloat) {
    return TTV_Java_SetVolume(paramAudioDeviceType, paramFloat);
  }
  
  public float getVolume(AudioDeviceType paramAudioDeviceType) {
    return TTV_Java_GetVolume(paramAudioDeviceType);
  }
  
  public ErrorCode getGameNameList(String paramString) {
    return TTV_Java_GetGameNameList(paramString);
  }
  
  public ErrorCode getDefaultParams(VideoParams paramVideoParams) {
    return TTV_Java_GetDefaultParams(paramVideoParams);
  }
  
  public int[] getMaxResolution(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    int[] arrayOfInt = { 0, 0 };
    TTV_GetMaxResolution(paramInt1, paramInt2, paramFloat1, paramFloat2, arrayOfInt);
    return arrayOfInt;
  }
  
  public ErrorCode pollTasks() {
    return TTV_Java_PollTasks();
  }
  
  public ErrorCode pollStats() {
    return TTV_Java_PollStats();
  }
  
  public ErrorCode sendActionMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3) {
    return TTV_Java_SendActionMetaData(paramAuthToken, paramString1, paramLong, paramString2, paramString3);
  }
  
  public long sendStartSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3) {
    return TTV_Java_SendStartSpanMetaData(paramAuthToken, paramString1, paramLong, paramString2, paramString3);
  }
  
  public ErrorCode sendEndSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong1, long paramLong2, String paramString2, String paramString3) {
    return TTV_Java_SendEndSpanMetaData(paramAuthToken, paramString1, paramLong1, paramLong2, paramString2, paramString3);
  }
  
  public ErrorCode submitVideoFrame(long paramLong) {
    return TTV_Java_SubmitVideoFrame(paramLong);
  }
  
  public ErrorCode start(VideoParams paramVideoParams, AudioParams paramAudioParams, IngestServer paramIngestServer, int paramInt, boolean paramBoolean) {
    return TTV_Java_Start(paramVideoParams, paramAudioParams, paramIngestServer, paramInt, paramBoolean);
  }
  
  public ErrorCode stop(boolean paramBoolean) {
    return TTV_Java_Stop(paramBoolean);
  }
  
  public ErrorCode pauseVideo() {
    return TTV_Java_PauseVideo();
  }
  
  public long allocateFrameBuffer(int paramInt) {
    return TTV_Java_AllocateFrameBuffer(paramInt);
  }
  
  public ErrorCode freeFrameBuffer(long paramLong) {
    return TTV_Java_FreeFrameBuffer(paramLong);
  }
  
  public ErrorCode memsetFrameBuffer(long paramLong, int paramInt1, int paramInt2) {
    return TTV_Java_MemsetFrameBuffer(paramLong, paramInt1, paramInt2);
  }
  
  public ErrorCode randomizeFrameBuffer(long paramLong, int paramInt) {
    return TTV_Java_RandomizeFrameBuffer(paramLong, paramInt);
  }
  
  public ErrorCode captureFrameBuffer_ReadPixels(long paramLong) {
    return TTV_Java_CaptureFrameBuffer_ReadPixels(paramLong);
  }
  
  public long getStreamTime() {
    return TTV_Java_GetStreamTime();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\DesktopStreamAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */