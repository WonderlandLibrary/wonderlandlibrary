package io.netty.handler.codec.spdy;

import java.util.Set;

public interface SpdySettingsFrame extends SpdyFrame {
  public static final int SETTINGS_MINOR_VERSION = 0;
  
  public static final int SETTINGS_UPLOAD_BANDWIDTH = 1;
  
  public static final int SETTINGS_DOWNLOAD_BANDWIDTH = 2;
  
  public static final int SETTINGS_ROUND_TRIP_TIME = 3;
  
  public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 4;
  
  public static final int SETTINGS_CURRENT_CWND = 5;
  
  public static final int SETTINGS_DOWNLOAD_RETRANS_RATE = 6;
  
  public static final int SETTINGS_INITIAL_WINDOW_SIZE = 7;
  
  public static final int SETTINGS_CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
  
  Set<Integer> ids();
  
  boolean isSet(int paramInt);
  
  int getValue(int paramInt);
  
  SpdySettingsFrame setValue(int paramInt1, int paramInt2);
  
  SpdySettingsFrame setValue(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2);
  
  SpdySettingsFrame removeValue(int paramInt);
  
  boolean isPersistValue(int paramInt);
  
  SpdySettingsFrame setPersistValue(int paramInt, boolean paramBoolean);
  
  boolean isPersisted(int paramInt);
  
  SpdySettingsFrame setPersisted(int paramInt, boolean paramBoolean);
  
  boolean clearPreviouslyPersistedSettings();
  
  SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean paramBoolean);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdySettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */