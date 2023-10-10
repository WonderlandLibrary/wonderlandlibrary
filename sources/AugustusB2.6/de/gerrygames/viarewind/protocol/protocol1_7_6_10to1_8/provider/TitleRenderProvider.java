// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.provider;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.platform.providers.Provider;

public abstract class TitleRenderProvider implements Provider
{
    protected Map<UUID, Integer> fadeIn;
    protected Map<UUID, Integer> stay;
    protected Map<UUID, Integer> fadeOut;
    protected Map<UUID, String> titles;
    protected Map<UUID, String> subTitles;
    protected Map<UUID, AtomicInteger> times;
    
    public TitleRenderProvider() {
        this.fadeIn = new HashMap<UUID, Integer>();
        this.stay = new HashMap<UUID, Integer>();
        this.fadeOut = new HashMap<UUID, Integer>();
        this.titles = new HashMap<UUID, String>();
        this.subTitles = new HashMap<UUID, String>();
        this.times = new HashMap<UUID, AtomicInteger>();
    }
    
    public void setTimings(final UUID uuid, final int fadeIn, final int stay, final int fadeOut) {
        this.setFadeIn(uuid, fadeIn);
        this.setStay(uuid, stay);
        this.setFadeOut(uuid, fadeOut);
        final AtomicInteger time = this.getTime(uuid);
        if (time.get() > 0) {
            time.set(this.getFadeIn(uuid) + this.getStay(uuid) + this.getFadeOut(uuid));
        }
    }
    
    public void reset(final UUID uuid) {
        this.titles.remove(uuid);
        this.subTitles.remove(uuid);
        this.getTime(uuid).set(0);
        this.fadeIn.remove(uuid);
        this.stay.remove(uuid);
        this.fadeOut.remove(uuid);
    }
    
    public void setTitle(final UUID uuid, final String title) {
        this.titles.put(uuid, title);
        this.getTime(uuid).set(this.getFadeIn(uuid) + this.getStay(uuid) + this.getFadeOut(uuid));
    }
    
    public void setSubTitle(final UUID uuid, final String subTitle) {
        this.subTitles.put(uuid, subTitle);
    }
    
    public void clear(final UUID uuid) {
        this.titles.remove(uuid);
        this.subTitles.remove(uuid);
        this.getTime(uuid).set(0);
    }
    
    public AtomicInteger getTime(final UUID uuid) {
        return this.times.computeIfAbsent(uuid, key -> new AtomicInteger(0));
    }
    
    public int getFadeIn(final UUID uuid) {
        return this.fadeIn.getOrDefault(uuid, 10);
    }
    
    public int getStay(final UUID uuid) {
        return this.stay.getOrDefault(uuid, 70);
    }
    
    public int getFadeOut(final UUID uuid) {
        return this.fadeOut.getOrDefault(uuid, 20);
    }
    
    public void setFadeIn(final UUID uuid, final int fadeIn) {
        if (fadeIn >= 0) {
            this.fadeIn.put(uuid, fadeIn);
        }
        else {
            this.fadeIn.remove(uuid);
        }
    }
    
    public void setStay(final UUID uuid, final int stay) {
        if (stay >= 0) {
            this.stay.put(uuid, stay);
        }
        else {
            this.stay.remove(uuid);
        }
    }
    
    public void setFadeOut(final UUID uuid, final int fadeOut) {
        if (fadeOut >= 0) {
            this.fadeOut.put(uuid, fadeOut);
        }
        else {
            this.fadeOut.remove(uuid);
        }
    }
}
