// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.util.world.LocationUtils;

class Particles
{
    public int ticks;
    public LocationUtils location;
    public String text;
    
    public Particles(final LocationUtils location, final String text) {
        this.location = location;
        this.text = text;
        this.ticks = 0;
    }
}
