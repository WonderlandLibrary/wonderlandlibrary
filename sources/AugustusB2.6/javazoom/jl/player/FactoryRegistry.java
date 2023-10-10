// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import java.util.Enumeration;
import javazoom.jl.decoder.JavaLayerException;
import java.util.Hashtable;

public class FactoryRegistry extends AudioDeviceFactory
{
    private static FactoryRegistry instance;
    protected Hashtable factories;
    
    public FactoryRegistry() {
        this.factories = new Hashtable();
    }
    
    public static synchronized FactoryRegistry systemRegistry() {
        if (FactoryRegistry.instance == null) {
            (FactoryRegistry.instance = new FactoryRegistry()).registerDefaultFactories();
        }
        return FactoryRegistry.instance;
    }
    
    public void addFactory(final AudioDeviceFactory factory) {
        this.factories.put(factory.getClass(), factory);
    }
    
    public void removeFactoryType(final Class cls) {
        this.factories.remove(cls);
    }
    
    public void removeFactory(final AudioDeviceFactory factory) {
        this.factories.remove(factory.getClass());
    }
    
    @Override
    public AudioDevice createAudioDevice() throws JavaLayerException {
        AudioDevice device = null;
        final AudioDeviceFactory[] factories = this.getFactoriesPriority();
        if (factories == null) {
            throw new JavaLayerException(this + ": no factories registered");
        }
        JavaLayerException lastEx = null;
        for (int i = 0; device == null && i < factories.length; ++i) {
            try {
                device = factories[i].createAudioDevice();
            }
            catch (JavaLayerException ex) {
                lastEx = ex;
            }
        }
        if (device == null && lastEx != null) {
            throw new JavaLayerException("Cannot create AudioDevice", lastEx);
        }
        return device;
    }
    
    protected AudioDeviceFactory[] getFactoriesPriority() {
        AudioDeviceFactory[] fa = null;
        synchronized (this.factories) {
            final int size = this.factories.size();
            if (size != 0) {
                fa = new AudioDeviceFactory[size];
                int idx = 0;
                final Enumeration e = this.factories.elements();
                while (e.hasMoreElements()) {
                    final AudioDeviceFactory factory = e.nextElement();
                    fa[idx++] = factory;
                }
            }
        }
        return fa;
    }
    
    protected void registerDefaultFactories() {
        this.addFactory(new JavaSoundAudioDeviceFactory());
    }
    
    static {
        FactoryRegistry.instance = null;
    }
}
