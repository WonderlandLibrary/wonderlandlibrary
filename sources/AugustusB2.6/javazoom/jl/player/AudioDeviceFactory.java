// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;

public abstract class AudioDeviceFactory
{
    public abstract AudioDevice createAudioDevice() throws JavaLayerException;
    
    protected AudioDevice instantiate(final ClassLoader loader, final String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        AudioDevice dev = null;
        Class cls = null;
        if (loader == null) {
            cls = Class.forName(name);
        }
        else {
            cls = loader.loadClass(name);
        }
        final Object o = cls.newInstance();
        dev = (AudioDevice)o;
        return dev;
    }
}
