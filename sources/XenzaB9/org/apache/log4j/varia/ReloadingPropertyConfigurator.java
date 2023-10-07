// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.varia;

import java.net.URL;
import org.apache.log4j.spi.LoggerRepository;
import java.io.InputStream;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;

public class ReloadingPropertyConfigurator implements Configurator
{
    PropertyConfigurator delegate;
    
    public ReloadingPropertyConfigurator() {
        this.delegate = new PropertyConfigurator();
    }
    
    @Override
    public void doConfigure(final InputStream inputStream, final LoggerRepository repository) {
    }
    
    @Override
    public void doConfigure(final URL url, final LoggerRepository repository) {
    }
}
