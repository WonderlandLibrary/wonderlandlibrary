// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.world.WorldSettings;
import moonsense.event.SCEvent;

public class SCWorldLoadedEvent extends SCEvent
{
    public final String folderName;
    public final String worldName;
    public final WorldSettings worldSettings;
    
    public SCWorldLoadedEvent(final String folderName, final String worldName, final WorldSettings worldSettingsIn) {
        this.folderName = folderName;
        this.worldName = worldName;
        this.worldSettings = worldSettingsIn;
    }
}
