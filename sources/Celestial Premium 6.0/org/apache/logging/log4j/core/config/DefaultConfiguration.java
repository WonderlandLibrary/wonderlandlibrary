/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.BaseConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;

public class DefaultConfiguration
extends BaseConfiguration {
    public static final String DEFAULT_NAME = "Default";
    public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";

    public DefaultConfiguration() {
        this.setName(DEFAULT_NAME);
        PatternLayout layout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", null, null, null, null);
        ConsoleAppender appender = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "false", "true");
        appender.start();
        this.addAppender(appender);
        LoggerConfig root = this.getRootLogger();
        root.addAppender(appender, null, null);
        String levelName = PropertiesUtil.getProperties().getStringProperty(DEFAULT_LEVEL);
        Level level = levelName != null && Level.valueOf(levelName) != null ? Level.valueOf(levelName) : Level.ERROR;
        root.setLevel(level);
    }

    @Override
    protected void doConfigure() {
    }
}

