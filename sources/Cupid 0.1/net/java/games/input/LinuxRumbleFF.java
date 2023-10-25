package net.java.games.input;

import java.io.IOException;

final class LinuxRumbleFF extends LinuxForceFeedbackEffect {
  public LinuxRumbleFF(LinuxEventDevice device) throws IOException {
    super(device);
  }
  
  protected final int upload(int id, float intensity) throws IOException {
    int weak_magnitude;
    int strong_magnitude;
    if (intensity > 0.666666F) {
      strong_magnitude = (int)(32768.0F * intensity);
      weak_magnitude = (int)(49152.0F * intensity);
    } else if (intensity > 0.3333333F) {
      strong_magnitude = (int)(32768.0F * intensity);
      weak_magnitude = 0;
    } else {
      strong_magnitude = 0;
      weak_magnitude = (int)(49152.0F * intensity);
    } 
    return getDevice().uploadRumbleEffect(id, 0, 0, 0, -1, 0, strong_magnitude, weak_magnitude);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxRumbleFF.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */