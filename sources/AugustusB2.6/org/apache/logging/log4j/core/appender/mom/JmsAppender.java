// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.net.JndiManager;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import javax.jms.JMSException;
import org.apache.logging.log4j.core.config.Property;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "JMS", category = "Core", elementType = "appender", printObject = true)
@PluginAliases({ "JMSQueue", "JMSTopic" })
public class JmsAppender extends AbstractAppender
{
    private volatile JmsManager manager;
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    protected JmsAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final Property[] properties, final JmsManager manager) throws JMSException {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = manager;
    }
    
    @Deprecated
    protected JmsAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final JmsManager manager) throws JMSException {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.manager = manager;
    }
    
    @Override
    public void append(final LogEvent event) {
        this.manager.send(event, this.toSerializable(event));
    }
    
    public JmsManager getManager() {
        return this.manager;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= this.manager.stop(timeout, timeUnit);
        this.setStopped();
        return stopped;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JmsAppender>
    {
        public static final int DEFAULT_RECONNECT_INTERVAL_MILLIS = 5000;
        @PluginBuilderAttribute
        private String factoryName;
        @PluginBuilderAttribute
        private String providerUrl;
        @PluginBuilderAttribute
        private String urlPkgPrefixes;
        @PluginBuilderAttribute
        private String securityPrincipalName;
        @PluginBuilderAttribute(sensitive = true)
        private String securityCredentials;
        @PluginBuilderAttribute
        @Required(message = "A javax.jms.ConnectionFactory JNDI name must be specified")
        private String factoryBindingName;
        @PluginBuilderAttribute
        @PluginAliases({ "queueBindingName", "topicBindingName" })
        @Required(message = "A javax.jms.Destination JNDI name must be specified")
        private String destinationBindingName;
        @PluginBuilderAttribute
        private String userName;
        @PluginBuilderAttribute(sensitive = true)
        private char[] password;
        @PluginBuilderAttribute
        private long reconnectIntervalMillis;
        @PluginBuilderAttribute
        private boolean immediateFail;
        @PluginBuilderAttribute
        private String allowedLdapClasses;
        @PluginBuilderAttribute
        private String allowedLdapHosts;
        @PluginBuilderAttribute
        private String allowedJndiProtocols;
        private JmsManager jmsManager;
        
        private Builder() {
            this.reconnectIntervalMillis = 5000L;
        }
        
        @Override
        public JmsAppender build() {
            JmsManager actualJmsManager = this.jmsManager;
            JmsManager.JmsManagerConfiguration configuration = null;
            if (actualJmsManager == null) {
                Properties additionalProperties = null;
                if (this.allowedLdapClasses != null || this.allowedLdapHosts != null) {
                    additionalProperties = new Properties();
                    if (this.allowedLdapHosts != null) {
                        additionalProperties.put("allowedLdapHosts", this.allowedLdapHosts);
                    }
                    if (this.allowedLdapClasses != null) {
                        additionalProperties.put("allowedLdapClasses", this.allowedLdapClasses);
                    }
                    if (this.allowedJndiProtocols != null) {
                        additionalProperties.put("allowedJndiProtocols", this.allowedJndiProtocols);
                    }
                }
                final Properties jndiProperties = JndiManager.createProperties(this.factoryName, this.providerUrl, this.urlPkgPrefixes, this.securityPrincipalName, this.securityCredentials, additionalProperties);
                configuration = new JmsManager.JmsManagerConfiguration(jndiProperties, this.factoryBindingName, this.destinationBindingName, this.userName, this.password, false, this.reconnectIntervalMillis);
                actualJmsManager = AbstractManager.getManager(this.getName(), (ManagerFactory<JmsManager, JmsManager.JmsManagerConfiguration>)JmsManager.FACTORY, configuration);
            }
            if (actualJmsManager == null) {
                return null;
            }
            final Layout<? extends Serializable> layout = this.getLayout();
            if (layout == null) {
                JmsAppender.LOGGER.error("No layout provided for JmsAppender");
                return null;
            }
            try {
                return new JmsAppender(this.getName(), this.getFilter(), layout, this.isIgnoreExceptions(), this.getPropertyArray(), actualJmsManager);
            }
            catch (JMSException e) {
                throw new IllegalStateException((Throwable)e);
            }
        }
        
        public Builder setDestinationBindingName(final String destinationBindingName) {
            this.destinationBindingName = destinationBindingName;
            return this;
        }
        
        public Builder setFactoryBindingName(final String factoryBindingName) {
            this.factoryBindingName = factoryBindingName;
            return this;
        }
        
        public Builder setFactoryName(final String factoryName) {
            this.factoryName = factoryName;
            return this;
        }
        
        public Builder setImmediateFail(final boolean immediateFail) {
            this.immediateFail = immediateFail;
            return this;
        }
        
        public Builder setJmsManager(final JmsManager jmsManager) {
            this.jmsManager = jmsManager;
            return this;
        }
        
        public Builder setPassword(final char[] password) {
            this.password = password;
            return this;
        }
        
        @Deprecated
        public Builder setPassword(final String password) {
            this.password = (char[])((password == null) ? null : password.toCharArray());
            return this;
        }
        
        public Builder setProviderUrl(final String providerUrl) {
            this.providerUrl = providerUrl;
            return this;
        }
        
        public Builder setReconnectIntervalMillis(final long reconnectIntervalMillis) {
            this.reconnectIntervalMillis = reconnectIntervalMillis;
            return this;
        }
        
        public Builder setSecurityCredentials(final String securityCredentials) {
            this.securityCredentials = securityCredentials;
            return this;
        }
        
        public Builder setSecurityPrincipalName(final String securityPrincipalName) {
            this.securityPrincipalName = securityPrincipalName;
            return this;
        }
        
        public Builder setUrlPkgPrefixes(final String urlPkgPrefixes) {
            this.urlPkgPrefixes = urlPkgPrefixes;
            return this;
        }
        
        @Deprecated
        public Builder setUsername(final String username) {
            this.userName = username;
            return this;
        }
        
        public Builder setUserName(final String userName) {
            this.userName = userName;
            return this;
        }
        
        public Builder setAllowedLdapClasses(final String allowedLdapClasses) {
            this.allowedLdapClasses = allowedLdapClasses;
            return this;
        }
        
        public Builder setAllowedLdapHosts(final String allowedLdapHosts) {
            this.allowedLdapHosts = allowedLdapHosts;
            return this;
        }
        
        public Builder setAllowedJndiProtocols(final String allowedJndiProtocols) {
            this.allowedJndiProtocols = allowedJndiProtocols;
            return this;
        }
        
        @Override
        public String toString() {
            return "Builder [name=" + this.getName() + ", factoryName=" + this.factoryName + ", providerUrl=" + this.providerUrl + ", urlPkgPrefixes=" + this.urlPkgPrefixes + ", securityPrincipalName=" + this.securityPrincipalName + ", securityCredentials=" + this.securityCredentials + ", factoryBindingName=" + this.factoryBindingName + ", destinationBindingName=" + this.destinationBindingName + ", username=" + this.userName + ", layout=" + this.getLayout() + ", filter=" + this.getFilter() + ", ignoreExceptions=" + this.isIgnoreExceptions() + ", jmsManager=" + this.jmsManager + ", allowedLdapClasses=" + this.allowedLdapClasses + ", allowedLdapHosts=" + this.allowedLdapHosts + ", allowedJndiProtocols=" + this.allowedJndiProtocols + "]";
        }
    }
}
