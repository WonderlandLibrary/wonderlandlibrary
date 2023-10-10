// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.Collection;
import java.util.Hashtable;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.Logger;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import java.net.URISyntaxException;
import javax.naming.directory.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.net.URI;
import javax.naming.Context;
import org.apache.logging.log4j.core.util.JndiCloser;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.LoggerContext;
import javax.naming.directory.DirContext;
import java.util.List;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class JndiManager extends AbstractManager
{
    public static final String ALLOWED_HOSTS = "allowedLdapHosts";
    public static final String ALLOWED_CLASSES = "allowedLdapClasses";
    public static final String ALLOWED_PROTOCOLS = "allowedJndiProtocols";
    private static final JndiManagerFactory FACTORY;
    private static final String PREFIX = "log4j2.";
    private static final String LDAP = "ldap";
    private static final String LDAPS = "ldaps";
    private static final String JAVA = "java";
    private static final List<String> permanentAllowedHosts;
    private static final List<String> permanentAllowedClasses;
    private static final List<String> permanentAllowedProtocols;
    private static final String SERIALIZED_DATA = "javaSerializedData";
    private static final String CLASS_NAME = "javaClassName";
    private static final String REFERENCE_ADDRESS = "javaReferenceAddress";
    private static final String OBJECT_FACTORY = "javaFactory";
    private final List<String> allowedHosts;
    private final List<String> allowedClasses;
    private final List<String> allowedProtocols;
    private final DirContext context;
    
    private JndiManager(final String name, final DirContext context, final List<String> allowedHosts, final List<String> allowedClasses, final List<String> allowedProtocols) {
        super(null, name);
        this.context = context;
        this.allowedHosts = allowedHosts;
        this.allowedClasses = allowedClasses;
        this.allowedProtocols = allowedProtocols;
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
    
    public synchronized <T> T lookup(final String name) throws NamingException {
        try {
            final URI uri = new URI(name);
            if (uri.getScheme() != null) {
                if (!this.allowedProtocols.contains(uri.getScheme().toLowerCase(Locale.ROOT))) {
                    JndiManager.LOGGER.warn("Log4j JNDI does not allow protocol {}", uri.getScheme());
                    return null;
                }
                if ("ldap".equalsIgnoreCase(uri.getScheme()) || "ldaps".equalsIgnoreCase(uri.getScheme())) {
                    if (!this.allowedHosts.contains(uri.getHost())) {
                        JndiManager.LOGGER.warn("Attempt to access ldap server not in allowed list");
                        return null;
                    }
                    final Attributes attributes = this.context.getAttributes(name);
                    if (attributes != null) {
                        final Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
                        final NamingEnumeration<? extends Attribute> enumeration = attributes.getAll();
                        while (enumeration.hasMore()) {
                            final Attribute attribute = (Attribute)enumeration.next();
                            attributeMap.put(attribute.getID(), attribute);
                        }
                        final Attribute classNameAttr = attributeMap.get("javaClassName");
                        if (attributeMap.get("javaSerializedData") != null) {
                            if (classNameAttr == null) {
                                JndiManager.LOGGER.warn("No class name provided for {}", name);
                                return null;
                            }
                            final String className = classNameAttr.get().toString();
                            if (!this.allowedClasses.contains(className)) {
                                JndiManager.LOGGER.warn("Deserialization of {} is not allowed", className);
                                return null;
                            }
                        }
                        else if (attributeMap.get("javaReferenceAddress") != null || attributeMap.get("javaFactory") != null) {
                            JndiManager.LOGGER.warn("Referenceable class is not allowed for {}", name);
                            return null;
                        }
                    }
                }
            }
        }
        catch (URISyntaxException ex) {
            JndiManager.LOGGER.warn("Invalid JNDI URI - {}", name);
            return null;
        }
        return (T)this.context.lookup(name);
    }
    
    @Override
    public String toString() {
        return "JndiManager [context=" + this.context + ", count=" + this.count + "]";
    }
    
    static {
        FACTORY = new JndiManagerFactory();
        permanentAllowedHosts = NetUtils.getLocalIps();
        permanentAllowedClasses = Arrays.asList(Boolean.class.getName(), Byte.class.getName(), Character.class.getName(), Double.class.getName(), Float.class.getName(), Integer.class.getName(), Long.class.getName(), Short.class.getName(), String.class.getName());
        permanentAllowedProtocols = Arrays.asList("java", "ldap", "ldaps");
    }
    
    private static class JndiManagerFactory implements ManagerFactory<JndiManager, Properties>
    {
        @Override
        public JndiManager createManager(final String name, final Properties data) {
            final String hosts = (data != null) ? data.getProperty("allowedLdapHosts") : null;
            final String classes = (data != null) ? data.getProperty("allowedLdapClasses") : null;
            final String protocols = (data != null) ? data.getProperty("allowedJndiProtocols") : null;
            final List<String> allowedHosts = new ArrayList<String>();
            final List<String> allowedClasses = new ArrayList<String>();
            final List<String> allowedProtocols = new ArrayList<String>();
            this.addAll(hosts, allowedHosts, JndiManager.permanentAllowedHosts, "allowedLdapHosts", data);
            this.addAll(classes, allowedClasses, JndiManager.permanentAllowedClasses, "allowedLdapClasses", data);
            this.addAll(protocols, allowedProtocols, JndiManager.permanentAllowedProtocols, "allowedJndiProtocols", data);
            try {
                return new JndiManager(name, new InitialDirContext(data), allowedHosts, allowedClasses, allowedProtocols, null);
            }
            catch (NamingException e) {
                JndiManager.LOGGER.error("Error creating JNDI InitialContext.", e);
                return null;
            }
        }
        
        private void addAll(String toSplit, final List<String> list, final List<String> permanentList, final String propertyName, final Properties data) {
            if (toSplit != null) {
                list.addAll(Arrays.asList(toSplit.split("\\s*,\\s*")));
                data.remove(propertyName);
            }
            toSplit = PropertiesUtil.getProperties().getStringProperty("log4j2." + propertyName);
            if (toSplit != null) {
                list.addAll(Arrays.asList(toSplit.split("\\s*,\\s*")));
            }
            list.addAll(permanentList);
        }
    }
}
