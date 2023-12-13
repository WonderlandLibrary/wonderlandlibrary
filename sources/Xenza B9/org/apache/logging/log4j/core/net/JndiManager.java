// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.util.Hashtable;
import org.apache.logging.log4j.Logger;
import javax.naming.NamingException;
import java.net.URISyntaxException;
import java.net.URI;
import javax.naming.Context;
import org.apache.logging.log4j.core.util.JndiCloser;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.PropertiesUtil;
import javax.naming.InitialContext;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class JndiManager extends AbstractManager
{
    private static final JndiManagerFactory FACTORY;
    private static final String PREFIX = "log4j2.enableJndi";
    private static final String JAVA_SCHEME = "java";
    private final InitialContext context;
    
    private static boolean isJndiEnabled(final String subKey) {
        return PropertiesUtil.getProperties().getBooleanProperty("log4j2.enableJndi" + subKey, false);
    }
    
    public static boolean isJndiEnabled() {
        return isJndiContextSelectorEnabled() || isJndiJdbcEnabled() || isJndiJmsEnabled() || isJndiLookupEnabled();
    }
    
    public static boolean isJndiContextSelectorEnabled() {
        return isJndiEnabled("ContextSelector");
    }
    
    public static boolean isJndiJdbcEnabled() {
        return isJndiEnabled("Jdbc");
    }
    
    public static boolean isJndiJmsEnabled() {
        return isJndiEnabled("Jms");
    }
    
    public static boolean isJndiLookupEnabled() {
        return isJndiEnabled("Lookup");
    }
    
    private JndiManager(final String name, final InitialContext context) {
        super(null, name);
        this.context = context;
    }
    
    public static JndiManager getDefaultManager() {
        return AbstractManager.getManager(JndiManager.class.getName(), (ManagerFactory<JndiManager, Object>)JndiManager.FACTORY, null);
    }
    
    public static JndiManager getDefaultManager(final String name) {
        return AbstractManager.getManager(name, (ManagerFactory<JndiManager, Object>)JndiManager.FACTORY, null);
    }
    
    public static JndiManager getJndiManager(final String initialContextFactoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipal, final String securityCredentials, final Properties additionalProperties) {
        final Properties properties = createProperties(initialContextFactoryName, providerURL, urlPkgPrefixes, securityPrincipal, securityCredentials, additionalProperties);
        return AbstractManager.getManager(createManagerName(), (ManagerFactory<JndiManager, Properties>)JndiManager.FACTORY, properties);
    }
    
    public static JndiManager getJndiManager(final Properties properties) {
        return AbstractManager.getManager(createManagerName(), (ManagerFactory<JndiManager, Properties>)JndiManager.FACTORY, properties);
    }
    
    private static String createManagerName() {
        return JndiManager.class.getName() + '@' + JndiManager.class.hashCode();
    }
    
    public static Properties createProperties(final String initialContextFactoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipal, final String securityCredentials, final Properties additionalProperties) {
        if (initialContextFactoryName == null) {
            return null;
        }
        final Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", initialContextFactoryName);
        if (providerURL != null) {
            properties.setProperty("java.naming.provider.url", providerURL);
        }
        else {
            JndiManager.LOGGER.warn("The JNDI InitialContextFactory class name [{}] was provided, but there was no associated provider URL. This is likely to cause problems.", initialContextFactoryName);
        }
        if (urlPkgPrefixes != null) {
            properties.setProperty("java.naming.factory.url.pkgs", urlPkgPrefixes);
        }
        if (securityPrincipal != null) {
            properties.setProperty("java.naming.security.principal", securityPrincipal);
            if (securityCredentials != null) {
                properties.setProperty("java.naming.security.credentials", securityCredentials);
            }
            else {
                JndiManager.LOGGER.warn("A security principal [{}] was provided, but with no corresponding security credentials.", securityPrincipal);
            }
        }
        if (additionalProperties != null) {
            properties.putAll(additionalProperties);
        }
        return properties;
    }
    
    @Override
    protected boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        return JndiCloser.closeSilently(this.context);
    }
    
    public <T> T lookup(final String name) throws NamingException {
        if (this.context == null) {
            return null;
        }
        try {
            final URI uri = new URI(name);
            if (uri.getScheme() == null || uri.getScheme().equals("java")) {
                return (T)this.context.lookup(name);
            }
            JndiManager.LOGGER.warn("Unsupported JNDI URI - {}", name);
        }
        catch (final URISyntaxException ex) {
            JndiManager.LOGGER.warn("Invalid JNDI URI - {}", name);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "JndiManager [context=" + this.context + ", count=" + this.count + "]";
    }
    
    static {
        FACTORY = new JndiManagerFactory();
    }
    
    private static class JndiManagerFactory implements ManagerFactory<JndiManager, Properties>
    {
        @Override
        public JndiManager createManager(final String name, final Properties data) {
            if (!JndiManager.isJndiEnabled()) {
                throw new IllegalStateException(String.format("JNDI must be enabled by setting one of the %s* properties to true", "log4j2.enableJndi"));
            }
            try {
                return new JndiManager(name, new InitialContext(data), null);
            }
            catch (final NamingException e) {
                JndiManager.LOGGER.error("Error creating JNDI InitialContext for '{}'.", name, e);
                return null;
            }
        }
    }
}
