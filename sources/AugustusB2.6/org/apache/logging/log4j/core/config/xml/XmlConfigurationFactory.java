// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.xml;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

@Plugin(name = "XmlConfigurationFactory", category = "ConfigurationFactory")
@Order(5)
public class XmlConfigurationFactory extends ConfigurationFactory
{
    public static final String[] SUFFIXES;
    
    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return new XmlConfiguration(loggerContext, source);
    }
    
    public String[] getSupportedTypes() {
        return XmlConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".xml", "*" };
    }
}
