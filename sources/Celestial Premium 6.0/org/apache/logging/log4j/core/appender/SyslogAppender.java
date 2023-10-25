/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.layout.RFC5424Layout;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(name="Syslog", category="Core", elementType="appender", printObject=true)
public class SyslogAppender
extends SocketAppender {
    protected static final String RFC5424 = "RFC5424";

    protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser);
    }

    @PluginFactory
    public static SyslogAppender createAppender(@PluginAttribute(value="host") String host, @PluginAttribute(value="port") String portNum, @PluginAttribute(value="protocol") String protocol, @PluginAttribute(value="reconnectionDelay") String delay, @PluginAttribute(value="immediateFail") String immediateFail, @PluginAttribute(value="name") String name, @PluginAttribute(value="immediateFlush") String immediateFlush, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginAttribute(value="facility") String facility, @PluginAttribute(value="id") String id, @PluginAttribute(value="enterpriseNumber") String ein, @PluginAttribute(value="includeMDC") String includeMDC, @PluginAttribute(value="mdcId") String mdcId, @PluginAttribute(value="mdcPrefix") String mdcPrefix, @PluginAttribute(value="eventPrefix") String eventPrefix, @PluginAttribute(value="newLine") String includeNL, @PluginAttribute(value="newLineEscape") String escapeNL, @PluginAttribute(value="appName") String appName, @PluginAttribute(value="messageId") String msgId, @PluginAttribute(value="mdcExcludes") String excludes, @PluginAttribute(value="mdcIncludes") String includes, @PluginAttribute(value="mdcRequired") String required, @PluginAttribute(value="format") String format, @PluginElement(value="Filters") Filter filter, @PluginConfiguration Configuration config, @PluginAttribute(value="charset") String charsetName, @PluginAttribute(value="exceptionPattern") String exceptionPattern, @PluginElement(value="LoggerFields") LoggerFields[] loggerFields, @PluginAttribute(value="advertise") String advertise) {
        SyslogLayout layout;
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        int reconnectDelay = AbstractAppender.parseInt(delay, 0);
        boolean fail = Booleans.parseBoolean(immediateFail, true);
        int port = AbstractAppender.parseInt(portNum, 0);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        AbstractStringLayout abstractStringLayout = layout = RFC5424.equalsIgnoreCase(format) ? RFC5424Layout.createLayout(facility, id, ein, includeMDC, mdcId, mdcPrefix, eventPrefix, includeNL, escapeNL, appName, msgId, excludes, includes, required, exceptionPattern, "false", loggerFields, config) : SyslogLayout.createLayout(facility, includeNL, escapeNL, charsetName);
        if (name == null) {
            LOGGER.error("No name provided for SyslogAppender");
            return null;
        }
        Protocol p = EnglishEnums.valueOf(Protocol.class, protocol);
        AbstractSocketManager manager = SyslogAppender.createSocketManager(p, host, port, reconnectDelay, fail, layout);
        if (manager == null) {
            return null;
        }
        return new SyslogAppender(name, (Layout<? extends Serializable>)layout, filter, ignoreExceptions, isFlush, manager, isAdvertise ? config.getAdvertiser() : null);
    }
}

