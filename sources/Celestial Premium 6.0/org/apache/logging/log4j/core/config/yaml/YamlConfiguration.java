/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonFactory
 *  com.fasterxml.jackson.core.JsonParser$Feature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.dataformat.yaml.YAMLFactory
 */
package org.apache.logging.log4j.core.config.yaml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;

public class YamlConfiguration
extends JsonConfiguration {
    public YamlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
        super(loggerContext, configSource);
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper((JsonFactory)new YAMLFactory()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    @Override
    public Configuration reconfigure() {
        try {
            ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            return new YamlConfiguration(this.getLoggerContext(), source);
        }
        catch (IOException ex) {
            LOGGER.error("Cannot locate file {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
}

