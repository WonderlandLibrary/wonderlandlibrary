package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;

public interface MediaPlayer {
   void addMediaErrorListener(MediaErrorListener var1);

   void removeMediaErrorListener(MediaErrorListener var1);

   void addMediaPlayerListener(PlayerStateListener var1);

   void removeMediaPlayerListener(PlayerStateListener var1);

   void addMediaTimeListener(PlayerTimeListener var1);

   void removeMediaTimeListener(PlayerTimeListener var1);

   void addVideoTrackSizeListener(VideoTrackSizeListener var1);

   void removeVideoTrackSizeListener(VideoTrackSizeListener var1);

   void addMarkerListener(MarkerListener var1);

   void removeMarkerListener(MarkerListener var1);

   void addBufferListener(BufferListener var1);

   void removeBufferListener(BufferListener var1);

   void addAudioSpectrumListener(AudioSpectrumListener var1);

   void removeAudioSpectrumListener(AudioSpectrumListener var1);

   VideoRenderControl getVideoRenderControl();

   MediaPlayerOverlay getMediaPlayerOverlay();

   Media getMedia();

   void setAudioSyncDelay(long var1);

   long getAudioSyncDelay();

   void play();

   void stop();

   void pause();

   float getRate();

   void setRate(float var1);

   double getPresentationTime();

   float getVolume();

   void setVolume(float var1);

   boolean getMute();

   void setMute(boolean var1);

   float getBalance();

   void setBalance(float var1);

   AudioEqualizer getEqualizer();

   AudioSpectrum getAudioSpectrum();

   double getDuration();

   double getStartTime();

   void setStartTime(double var1);

   double getStopTime();

   void setStopTime(double var1);

   void seek(double var1);

   PlayerStateEvent.PlayerState getState();

   void dispose();

   boolean isErrorEventCached();
}
