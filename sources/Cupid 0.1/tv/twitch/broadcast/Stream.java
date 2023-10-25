package tv.twitch.broadcast;

import java.util.HashSet;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public class Stream {
  static Stream s_Instance = null;
  
  public static Stream getInstance() {
    return s_Instance;
  }
  
  StreamAPI m_StreamAPI = null;
  
  public Stream(StreamAPI paramStreamAPI) {
    this.m_StreamAPI = paramStreamAPI;
    if (s_Instance == null)
      s_Instance = this; 
  }
  
  protected void finalize() {
    if (s_Instance == this)
      s_Instance = null; 
  }
  
  public IStreamCallbacks getStreamCallbacks() {
    return this.m_StreamAPI.getStreamCallbacks();
  }
  
  public void setStreamCallbacks(IStreamCallbacks paramIStreamCallbacks) {
    this.m_StreamAPI.setStreamCallbacks(paramIStreamCallbacks);
  }
  
  public IStatCallbacks getStatCallbacks() {
    return this.m_StreamAPI.getStatCallbacks();
  }
  
  public void setStatCallbacks(IStatCallbacks paramIStatCallbacks) {
    this.m_StreamAPI.setStatCallbacks(paramIStatCallbacks);
  }
  
  public FrameBuffer allocateFrameBuffer(int paramInt) {
    return new FrameBuffer(this.m_StreamAPI, paramInt);
  }
  
  public ErrorCode memsetFrameBuffer(FrameBuffer paramFrameBuffer, int paramInt) {
    return this.m_StreamAPI.memsetFrameBuffer(paramFrameBuffer.getAddress(), paramFrameBuffer.getSize(), paramInt);
  }
  
  public ErrorCode randomizeFrameBuffer(FrameBuffer paramFrameBuffer) {
    return this.m_StreamAPI.randomizeFrameBuffer(paramFrameBuffer.getAddress(), paramFrameBuffer.getSize());
  }
  
  public ErrorCode requestAuthToken(AuthParams paramAuthParams, HashSet<AuthFlag> paramHashSet) {
    return this.m_StreamAPI.requestAuthToken(paramAuthParams, paramHashSet);
  }
  
  public ErrorCode login(AuthToken paramAuthToken) {
    return this.m_StreamAPI.login(paramAuthToken);
  }
  
  public ErrorCode getIngestServers(AuthToken paramAuthToken) {
    return this.m_StreamAPI.getIngestServers(paramAuthToken);
  }
  
  public ErrorCode getUserInfo(AuthToken paramAuthToken) {
    return this.m_StreamAPI.getUserInfo(paramAuthToken);
  }
  
  public ErrorCode getStreamInfo(AuthToken paramAuthToken, String paramString) {
    return this.m_StreamAPI.getStreamInfo(paramAuthToken, paramString);
  }
  
  public ErrorCode setStreamInfo(AuthToken paramAuthToken, String paramString, StreamInfoForSetting paramStreamInfoForSetting) {
    return this.m_StreamAPI.setStreamInfo(paramAuthToken, paramString, paramStreamInfoForSetting);
  }
  
  public ErrorCode getArchivingState(AuthToken paramAuthToken) {
    return this.m_StreamAPI.getArchivingState(paramAuthToken);
  }
  
  public ErrorCode runCommercial(AuthToken paramAuthToken) {
    return this.m_StreamAPI.runCommercial(paramAuthToken);
  }
  
  public ErrorCode setVolume(AudioDeviceType paramAudioDeviceType, float paramFloat) {
    return this.m_StreamAPI.setVolume(paramAudioDeviceType, paramFloat);
  }
  
  public float getVolume(AudioDeviceType paramAudioDeviceType) {
    return this.m_StreamAPI.getVolume(paramAudioDeviceType);
  }
  
  public ErrorCode getGameNameList(String paramString) {
    return this.m_StreamAPI.getGameNameList(paramString);
  }
  
  public ErrorCode getDefaultParams(VideoParams paramVideoParams) {
    return this.m_StreamAPI.getDefaultParams(paramVideoParams);
  }
  
  public int[] getMaxResolution(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    return this.m_StreamAPI.getMaxResolution(paramInt1, paramInt2, paramFloat1, paramFloat2);
  }
  
  public ErrorCode pollTasks() {
    return this.m_StreamAPI.pollTasks();
  }
  
  public ErrorCode pollStats() {
    return this.m_StreamAPI.pollStats();
  }
  
  public ErrorCode sendActionMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3) {
    return this.m_StreamAPI.sendActionMetaData(paramAuthToken, paramString1, paramLong, paramString2, paramString3);
  }
  
  public long sendStartSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong, String paramString2, String paramString3) {
    return this.m_StreamAPI.sendStartSpanMetaData(paramAuthToken, paramString1, paramLong, paramString2, paramString3);
  }
  
  public ErrorCode sendEndSpanMetaData(AuthToken paramAuthToken, String paramString1, long paramLong1, long paramLong2, String paramString2, String paramString3) {
    return this.m_StreamAPI.sendEndSpanMetaData(paramAuthToken, paramString1, paramLong1, paramLong2, paramString2, paramString3);
  }
  
  public ErrorCode submitVideoFrame(FrameBuffer paramFrameBuffer) {
    return this.m_StreamAPI.submitVideoFrame(paramFrameBuffer.getAddress());
  }
  
  public ErrorCode captureFrameBuffer_ReadPixels(FrameBuffer paramFrameBuffer) {
    return this.m_StreamAPI.captureFrameBuffer_ReadPixels(paramFrameBuffer.getAddress());
  }
  
  public ErrorCode start(VideoParams paramVideoParams, AudioParams paramAudioParams, IngestServer paramIngestServer, StartFlags paramStartFlags, boolean paramBoolean) {
    if (paramStartFlags == null)
      paramStartFlags = StartFlags.None; 
    return this.m_StreamAPI.start(paramVideoParams, paramAudioParams, paramIngestServer, paramStartFlags.getValue(), paramBoolean);
  }
  
  public ErrorCode stop(boolean paramBoolean) {
    return this.m_StreamAPI.stop(paramBoolean);
  }
  
  public ErrorCode pauseVideo() {
    return this.m_StreamAPI.pauseVideo();
  }
  
  public long getStreamTime() {
    return this.m_StreamAPI.getStreamTime();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\Stream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */