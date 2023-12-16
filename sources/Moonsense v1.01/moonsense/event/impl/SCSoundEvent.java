// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.client.audio.ISound;
import moonsense.event.SCEventCancellable;

public class SCSoundEvent extends SCEventCancellable
{
    public final ISound sound;
    public final String soundLocation;
    
    public SCSoundEvent(final ISound sound, final String soundLocation) {
        this.sound = sound;
        this.soundLocation = soundLocation;
    }
}
