// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventClickGui;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.sound.RadioPlayer;
import net.augustus.modules.Module;

public class Radio extends Module
{
    private final RadioPlayer radioPlayer;
    public DoubleValue volume;
    public StringValue radios;
    
    public Radio() {
        super("Radio", Color.orange, Categorys.MISC);
        this.radioPlayer = new RadioPlayer();
        this.volume = new DoubleValue(1, "Volume", this, 50.0, 0.0, 100.0, 0);
        this.radios = new StringValue(2, "RadioChannels", this, "I love Radio", new String[] { "I love Radio", "I love 2 Dance", "I love ChillHop", "I l Deutschrap", "I l Greatest Hits", "I love Hardstyle", "I love Hip Hop", "I love Mashup", "I love The Club" });
    }
    
    @Override
    public void onEnable() {
        this.radioPlayer.setCurrent("");
        this.playMusic();
    }
    
    @EventTarget
    public void onEventClickGui(final EventClickGui eventClick) {
        this.setDisplayName(this.getName() + " §8" + this.radios.getSelected());
        this.radioPlayer.setVolume();
        this.playMusic();
    }
    
    @Override
    public void onDisable() {
        if (this.radioPlayer != null) {
            this.radioPlayer.stop();
            this.radioPlayer.setCurrent("");
        }
    }
    
    private void playMusic() {
        if (this.radioPlayer != null && this.radios.getSelected().equals(this.radioPlayer.getCurrent())) {
            return;
        }
        if (this.radioPlayer != null) {
            this.radioPlayer.stop();
        }
        final String selected = this.radios.getSelected();
        switch (selected) {
            case "I love Radio": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio1.mp3", this.radios.getSelected());
                break;
            }
            case "I love 2 Dance": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio2.mp3", this.radios.getSelected());
                break;
            }
            case "I love ChillHop": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio17.mp3", this.radios.getSelected());
                break;
            }
            case "I l Deutschrap": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio6.mp3", this.radios.getSelected());
                break;
            }
            case "I l Greatest Hits": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio16.mp3", this.radios.getSelected());
                break;
            }
            case "I love Hardstyle": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio21.mp3", this.radios.getSelected());
                break;
            }
            case "I love Hip Hop": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio3.mp3", this.radios.getSelected());
                break;
            }
            case "I love Mashup": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio5.mp3", this.radios.getSelected());
                break;
            }
            case "I love The Club": {
                this.radioPlayer.start("https://streams.ilovemusic.de/iloveradio20.mp3", this.radios.getSelected());
                break;
            }
        }
    }
}
