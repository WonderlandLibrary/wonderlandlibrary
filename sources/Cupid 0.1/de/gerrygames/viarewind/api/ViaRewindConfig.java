package de.gerrygames.viarewind.api;

public interface ViaRewindConfig {
  CooldownIndicator getCooldownIndicator();
  
  boolean isReplaceAdventureMode();
  
  boolean isReplaceParticles();
  
  public enum CooldownIndicator {
    TITLE, ACTION_BAR, BOSS_BAR, DISABLED;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\api\ViaRewindConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */