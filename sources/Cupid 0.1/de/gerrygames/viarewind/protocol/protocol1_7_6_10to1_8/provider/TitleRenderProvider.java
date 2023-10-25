package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import us.myles.ViaVersion.api.platform.providers.Provider;

public abstract class TitleRenderProvider implements Provider {
  protected Map<UUID, Integer> fadeIn = new HashMap<>();
  
  protected Map<UUID, Integer> stay = new HashMap<>();
  
  protected Map<UUID, Integer> fadeOut = new HashMap<>();
  
  protected Map<UUID, String> titles = new HashMap<>();
  
  protected Map<UUID, String> subTitles = new HashMap<>();
  
  protected Map<UUID, AtomicInteger> times = new HashMap<>();
  
  public void setTimings(UUID uuid, int fadeIn, int stay, int fadeOut) {
    setFadeIn(uuid, fadeIn);
    setStay(uuid, stay);
    setFadeOut(uuid, fadeOut);
    AtomicInteger time = getTime(uuid);
    if (time.get() > 0)
      time.set(getFadeIn(uuid) + getStay(uuid) + getFadeOut(uuid)); 
  }
  
  public void reset(UUID uuid) {
    this.titles.remove(uuid);
    this.subTitles.remove(uuid);
    getTime(uuid).set(0);
    this.fadeIn.remove(uuid);
    this.stay.remove(uuid);
    this.fadeOut.remove(uuid);
  }
  
  public void setTitle(UUID uuid, String title) {
    this.titles.put(uuid, title);
    getTime(uuid).set(getFadeIn(uuid) + getStay(uuid) + getFadeOut(uuid));
  }
  
  public void setSubTitle(UUID uuid, String subTitle) {
    this.subTitles.put(uuid, subTitle);
  }
  
  public void clear(UUID uuid) {
    this.titles.remove(uuid);
    this.subTitles.remove(uuid);
    getTime(uuid).set(0);
  }
  
  public AtomicInteger getTime(UUID uuid) {
    return this.times.computeIfAbsent(uuid, key -> new AtomicInteger(0));
  }
  
  public int getFadeIn(UUID uuid) {
    return ((Integer)this.fadeIn.getOrDefault(uuid, Integer.valueOf(10))).intValue();
  }
  
  public int getStay(UUID uuid) {
    return ((Integer)this.stay.getOrDefault(uuid, Integer.valueOf(70))).intValue();
  }
  
  public int getFadeOut(UUID uuid) {
    return ((Integer)this.fadeOut.getOrDefault(uuid, Integer.valueOf(20))).intValue();
  }
  
  public void setFadeIn(UUID uuid, int fadeIn) {
    if (fadeIn >= 0) {
      this.fadeIn.put(uuid, Integer.valueOf(fadeIn));
    } else {
      this.fadeIn.remove(uuid);
    } 
  }
  
  public void setStay(UUID uuid, int stay) {
    if (stay >= 0) {
      this.stay.put(uuid, Integer.valueOf(stay));
    } else {
      this.stay.remove(uuid);
    } 
  }
  
  public void setFadeOut(UUID uuid, int fadeOut) {
    if (fadeOut >= 0) {
      this.fadeOut.put(uuid, Integer.valueOf(fadeOut));
    } else {
      this.fadeOut.remove(uuid);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\provider\TitleRenderProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */