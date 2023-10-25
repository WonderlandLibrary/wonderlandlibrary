package org.apache.logging.log4j.core.appender.mom;

import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.jms.JMSException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.JndiManager;

@Plugin(name = "JMS", category = "Core", elementType = "appender", printObject = true)
@PluginAliases({"JMSQueue", "JMSTopic"})
public class JmsAppender extends AbstractAppender {
  private volatile JmsManager manager;
  
  public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JmsAppender> {
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
    @PluginAliases({"queueBindingName", "topicBindingName"})
    @Required(message = "A javax.jms.Destination JNDI name must be specified")
    private String destinationBindingName;
    
    @PluginBuilderAttribute
    private String userName;
    
    @PluginBuilderAttribute(sensitive = true)
    private char[] password;
    
    @PluginBuilderAttribute
    private long reconnectIntervalMillis = 5000L;
    
    @PluginBuilderAttribute
    private boolean immediateFail;
    
    private JmsManager jmsManager;
    
    public JmsAppender build() {
      JmsManager actualJmsManager = this.jmsManager;
      JmsManager.JmsManagerConfiguration configuration = null;
      if (actualJmsManager == null) {
        Properties jndiProperties = JndiManager.createProperties(this.factoryName, this.providerUrl, this.urlPkgPrefixes, this.securityPrincipalName, this.securityCredentials, null);
        configuration = new JmsManager.JmsManagerConfiguration(jndiProperties, this.factoryBindingName, this.destinationBindingName, this.userName, this.password, false, this.reconnectIntervalMillis);
        actualJmsManager = (JmsManager)AbstractManager.getManager(getName(), JmsManager.FACTORY, configuration);
      } 
      if (actualJmsManager == null)
        return null; 
      Layout<? extends Serializable> layout = getLayout();
      if (layout == null) {
        JmsAppender.LOGGER.error("No layout provided for JmsAppender");
        return null;
      } 
      try {
        return new JmsAppender(getName(), getFilter(), layout, isIgnoreExceptions(), getPropertyArray(), actualJmsManager);
      } catch (JMSException e) {
        throw new IllegalStateException(e);
      } 
    }
    
    public Builder setDestinationBindingName(String destinationBindingName) {
      this.destinationBindingName = destinationBindingName;
      return this;
    }
    
    public Builder setFactoryBindingName(String factoryBindingName) {
      this.factoryBindingName = factoryBindingName;
      return this;
    }
    
    public Builder setFactoryName(String factoryName) {
      this.factoryName = factoryName;
      return this;
    }
    
    public Builder setImmediateFail(boolean immediateFail) {
      this.immediateFail = immediateFail;
      return this;
    }
    
    public Builder setJmsManager(JmsManager jmsManager) {
      this.jmsManager = jmsManager;
      return this;
    }
    
    public Builder setPassword(char[] password) {
      this.password = password;
      return this;
    }
    
    @Deprecated
    public Builder setPassword(String password) {
      this.password = (password == null) ? null : password.toCharArray();
      return this;
    }
    
    public Builder setProviderUrl(String providerUrl) {
      this.providerUrl = providerUrl;
      return this;
    }
    
    public Builder setReconnectIntervalMillis(long reconnectIntervalMillis) {
      this.reconnectIntervalMillis = reconnectIntervalMillis;
      return this;
    }
    
    public Builder setSecurityCredentials(String securityCredentials) {
      this.securityCredentials = securityCredentials;
      return this;
    }
    
    public Builder setSecurityPrincipalName(String securityPrincipalName) {
      this.securityPrincipalName = securityPrincipalName;
      return this;
    }
    
    public Builder setUrlPkgPrefixes(String urlPkgPrefixes) {
      this.urlPkgPrefixes = urlPkgPrefixes;
      return this;
    }
    
    @Deprecated
    public Builder setUsername(String username) {
      this.userName = username;
      return this;
    }
    
    public Builder setUserName(String userName) {
      this.userName = userName;
      return this;
    }
    
    public String toString() {
      return "Builder [name=" + getName() + ", factoryName=" + this.factoryName + ", providerUrl=" + this.providerUrl + ", urlPkgPrefixes=" + this.urlPkgPrefixes + ", securityPrincipalName=" + this.securityPrincipalName + ", securityCredentials=" + this.securityCredentials + ", factoryBindingName=" + this.factoryBindingName + ", destinationBindingName=" + this.destinationBindingName + ", username=" + this.userName + ", layout=" + 
        
        getLayout() + ", filter=" + getFilter() + ", ignoreExceptions=" + isIgnoreExceptions() + ", jmsManager=" + this.jmsManager + "]";
    }
    
    private Builder() {}
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder<>();
  }
  
  protected JmsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, JmsManager manager) throws JMSException {
    super(name, filter, layout, ignoreExceptions, properties);
    this.manager = manager;
  }
  
  @Deprecated
  protected JmsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, JmsManager manager) throws JMSException {
    super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
    this.manager = manager;
  }
  
  public void append(LogEvent event) {
    this.manager.send(event, toSerializable(event));
  }
  
  public JmsManager getManager() {
    return this.manager;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(timeout, timeUnit, false);
    stopped &= this.manager.stop(timeout, timeUnit);
    setStopped();
    return stopped;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\mom\JmsAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */