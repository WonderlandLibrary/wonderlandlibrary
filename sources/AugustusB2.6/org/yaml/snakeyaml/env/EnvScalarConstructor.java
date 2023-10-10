// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.env;

import java.util.regex.Matcher;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.constructor.Constructor;

public class EnvScalarConstructor extends Constructor
{
    public static final Tag ENV_TAG;
    public static final Pattern ENV_FORMAT;
    
    public EnvScalarConstructor() {
        this.yamlConstructors.put(EnvScalarConstructor.ENV_TAG, new ConstructEnv());
    }
    
    public String apply(final String name, final String separator, final String value, final String environment) {
        if (environment != null && !environment.isEmpty()) {
            return environment;
        }
        if (separator != null) {
            if (separator.equals("?") && environment == null) {
                throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value);
            }
            if (separator.equals(":?")) {
                if (environment == null) {
                    throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value);
                }
                if (environment.isEmpty()) {
                    throw new MissingEnvironmentVariableException("Empty mandatory variable " + name + ": " + value);
                }
            }
            if (separator.startsWith(":")) {
                if (environment == null || environment.isEmpty()) {
                    return value;
                }
            }
            else if (environment == null) {
                return value;
            }
        }
        return "";
    }
    
    public String getEnv(final String key) {
        return System.getenv(key);
    }
    
    static {
        ENV_TAG = new Tag("!ENV");
        ENV_FORMAT = Pattern.compile("^\\$\\{\\s*((?<name>\\w+)((?<separator>:?(-|\\?))(?<value>\\w+)?)?)\\s*\\}$");
    }
    
    private class ConstructEnv extends AbstractConstruct
    {
        @Override
        public Object construct(final Node node) {
            final String val = BaseConstructor.this.constructScalar((ScalarNode)node);
            final Matcher matcher = EnvScalarConstructor.ENV_FORMAT.matcher(val);
            matcher.matches();
            final String name = matcher.group("name");
            final String value = matcher.group("value");
            final String separator = matcher.group("separator");
            return EnvScalarConstructor.this.apply(name, separator, (value != null) ? value : "", EnvScalarConstructor.this.getEnv(name));
        }
    }
}
