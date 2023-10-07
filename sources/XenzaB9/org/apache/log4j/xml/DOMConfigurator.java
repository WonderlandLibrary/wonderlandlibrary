// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.xml;

import java.net.URLConnection;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.Writer;
import org.apache.logging.log4j.core.util.IOUtils;
import java.io.StringWriter;
import java.io.Reader;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.config.PropertySetter;
import java.util.Properties;
import java.net.URL;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.InputStream;
import java.nio.file.Path;
import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import org.w3c.dom.Element;

public class DOMConfigurator
{
    public static void configure(final Element element) {
    }
    
    public static void configure(final String fileName) throws FactoryConfigurationError {
        final Path path = Paths.get(fileName, new String[0]);
        try (final InputStream inputStream = Files.newInputStream(path, new OpenOption[0])) {
            final ConfigurationSource source = new ConfigurationSource(inputStream, path);
            final LoggerContext context = (LoggerContext)LogManager.getContext(false);
            final Configuration configuration = new XmlConfigurationFactory().getConfiguration(context, source);
            org.apache.log4j.LogManager.getRootLogger().removeAllAppenders();
            Configurator.reconfigure(configuration);
        }
        catch (final IOException e) {
            throw new FactoryConfigurationError(e);
        }
    }
    
    public static void configure(final URL url) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(url, org.apache.log4j.LogManager.getLoggerRepository());
    }
    
    public static void configureAndWatch(final String fileName) {
        configure(fileName);
    }
    
    public static void configureAndWatch(final String fileName, final long delay) {
        final XMLWatchdog xdog = new XMLWatchdog(fileName);
        xdog.setDelay(delay);
        xdog.start();
    }
    
    public static Object parseElement(final Element element, final Properties props, final Class expectedClass) {
        return null;
    }
    
    public static void setParameter(final Element elem, final PropertySetter propSetter, final Properties props) {
    }
    
    public static String subst(final String value, final Properties props) {
        return OptionConverter.substVars(value, props);
    }
    
    private void doConfigure(final ConfigurationSource source) {
        final LoggerContext context = (LoggerContext)LogManager.getContext(false);
        final Configuration configuration = new XmlConfigurationFactory().getConfiguration(context, source);
        Configurator.reconfigure(configuration);
    }
    
    public void doConfigure(final Element element, final LoggerRepository repository) {
    }
    
    public void doConfigure(final InputStream inputStream, final LoggerRepository repository) throws FactoryConfigurationError {
        try {
            this.doConfigure(new ConfigurationSource(inputStream));
        }
        catch (final IOException e) {
            throw new FactoryConfigurationError(e);
        }
    }
    
    public void doConfigure(final Reader reader, final LoggerRepository repository) throws FactoryConfigurationError {
        try {
            final StringWriter sw = new StringWriter();
            IOUtils.copy(reader, sw);
            this.doConfigure(new ConfigurationSource(new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8))));
        }
        catch (final IOException e) {
            throw new FactoryConfigurationError(e);
        }
    }
    
    public void doConfigure(final String fileName, final LoggerRepository repository) {
        configure(fileName);
    }
    
    public void doConfigure(final URL url, final LoggerRepository repository) {
        try {
            final URLConnection connection = UrlConnectionFactory.createConnection(url);
            try (final InputStream inputStream = connection.getInputStream()) {
                this.doConfigure(new ConfigurationSource(inputStream, url));
            }
        }
        catch (final IOException e) {
            throw new FactoryConfigurationError(e);
        }
    }
}
