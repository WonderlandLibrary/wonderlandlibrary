// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;

public class JavaSoundAudioDeviceFactory extends AudioDeviceFactory
{
    private boolean tested;
    private static final String DEVICE_CLASS_NAME = "javazoom.jl.player.JavaSoundAudioDevice";
    
    public JavaSoundAudioDeviceFactory() {
        this.tested = false;
    }
    
    @Override
    public synchronized AudioDevice createAudioDevice() throws JavaLayerException {
        if (!this.tested) {
            this.testAudioDevice();
            this.tested = true;
        }
        try {
            return this.createAudioDeviceImpl();
        }
        catch (Exception ex) {
            throw new JavaLayerException("unable to create JavaSound device: " + ex);
        }
        catch (LinkageError ex2) {
            throw new JavaLayerException("unable to create JavaSound device: " + ex2);
        }
    }
    
    protected JavaSoundAudioDevice createAudioDeviceImpl() throws JavaLayerException {
        final ClassLoader loader = this.getClass().getClassLoader();
        try {
            final JavaSoundAudioDevice dev = (JavaSoundAudioDevice)this.instantiate(loader, "javazoom.jl.player.JavaSoundAudioDevice");
            return dev;
        }
        catch (Exception ex) {
            throw new JavaLayerException("Cannot create JavaSound device", ex);
        }
        catch (LinkageError ex2) {
            throw new JavaLayerException("Cannot create JavaSound device", ex2);
        }
    }
    
    public void testAudioDevice() throws JavaLayerException {
        final JavaSoundAudioDevice dev = this.createAudioDeviceImpl();
        dev.test();
    }
}
