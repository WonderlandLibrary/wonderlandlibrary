/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.JMSQueueManager;

@Plugin(name="JMSQueue", category="Core", elementType="appender", printObject=true)
public final class JMSQueueAppender
extends AbstractAppender {
    private final JMSQueueManager manager;

    private JMSQueueAppender(String name, Filter filter, Layout<? extends Serializable> layout, JMSQueueManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }

    @Override
    public void append(LogEvent event) {
        try {
            this.manager.send(this.getLayout().toSerializable(event));
        }
        catch (Exception ex) {
            throw new AppenderLoggingException(ex);
        }
    }

    @PluginFactory
    public static JMSQueueAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="factoryName") String factoryName, @PluginAttribute(value="providerURL") String providerURL, @PluginAttribute(value="urlPkgPrefixes") String urlPkgPrefixes, @PluginAttribute(value="securityPrincipalName") String securityPrincipalName, @PluginAttribute(value="securityCredentials") String securityCredentials, @PluginAttribute(value="factoryBindingName") String factoryBindingName, @PluginAttribute(value="queueBindingName") String queueBindingName, @PluginAttribute(value="userName") String userName, @PluginAttribute(value="password") String password, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filter") Filter filter, @PluginAttribute(value="ignoreExceptions") String ignore) {
        if (name == null) {
            LOGGER.error("No name provided for JMSQueueAppender");
            return null;
        }
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        JMSQueueManager manager = JMSQueueManager.getJMSQueueManager(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password);
        if (manager == null) {
            return null;
        }
        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }
        return new JMSQueueAppender(name, filter, layout, manager, ignoreExceptions);
    }
}

