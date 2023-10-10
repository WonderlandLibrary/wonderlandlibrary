// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationAware;

public abstract class AbstractConfigurationAwareLookup extends AbstractLookup implements ConfigurationAware
{
    protected Configuration configuration;
    
    @Override
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
}
