// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Map;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Collection;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidators;
import java.lang.reflect.Member;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitors;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import java.lang.reflect.Type;
import org.apache.logging.log4j.core.util.TypeUtil;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.Objects;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Builder;

public class PluginBuilder implements Builder<Object>
{
    private static final Logger LOGGER;
    private final PluginType<?> pluginType;
    private final Class<?> clazz;
    private Configuration configuration;
    private Node node;
    private LogEvent event;
    
    public PluginBuilder(final PluginType<?> pluginType) {
        this.pluginType = pluginType;
        this.clazz = pluginType.getPluginClass();
    }
    
    public PluginBuilder withConfiguration(final Configuration configuration) {
        this.configuration = configuration;
        return this;
    }
    
    public PluginBuilder withConfigurationNode(final Node node) {
        this.node = node;
        return this;
    }
    
    public PluginBuilder forLogEvent(final LogEvent event) {
        this.event = event;
        return this;
    }
    
    @Override
    public Object build() {
        this.verify();
        try {
            PluginBuilder.LOGGER.debug("Building Plugin[name={}, class={}].", this.pluginType.getElementName(), this.pluginType.getPluginClass().getName());
            final Builder<?> builder = createBuilder(this.clazz);
            if (builder != null) {
                this.injectFields(builder);
                return builder.build();
            }
        }
        catch (ConfigurationException e) {
            PluginBuilder.LOGGER.error("Could not create plugin of type {} for element {}", this.clazz, this.node.getName(), e);
            return null;
        }
        catch (Exception e2) {
            PluginBuilder.LOGGER.error("Could not create plugin of type {} for element {}: {}", this.clazz, this.node.getName(), ((e2 instanceof InvocationTargetException) ? ((InvocationTargetException)e2).getCause() : e2).toString(), e2);
        }
        try {
            final Method factory = findFactoryMethod(this.clazz);
            final Object[] params = this.generateParameters(factory);
            return factory.invoke(null, params);
        }
        catch (Exception e2) {
            PluginBuilder.LOGGER.error("Unable to invoke factory method in {} for element {}: {}", this.clazz, this.node.getName(), ((e2 instanceof InvocationTargetException) ? ((InvocationTargetException)e2).getCause() : e2).toString(), e2);
            return null;
        }
    }
    
    private void verify() {
        Objects.requireNonNull(this.configuration, "No Configuration object was set.");
        Objects.requireNonNull(this.node, "No Node object was set.");
    }
    
    private static Builder<?> createBuilder(final Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PluginBuilderFactory.class) && Modifier.isStatic(method.getModifiers()) && TypeUtil.isAssignable(Builder.class, method.getReturnType())) {
                ReflectionUtil.makeAccessible(method);
                return (Builder<?>)method.invoke(null, new Object[0]);
            }
        }
        return null;
    }
    
    private void injectFields(final Builder<?> builder) throws IllegalAccessException {
        final List<Field> fields = TypeUtil.getAllDeclaredFields(builder.getClass());
        AccessibleObject.setAccessible(fields.toArray(new Field[0]), true);
        final StringBuilder log = new StringBuilder();
        boolean invalid = false;
        String reason = "";
        for (final Field field : fields) {
            log.append((log.length() == 0) ? (simpleName(builder) + "(") : ", ");
            final Annotation[] annotations = field.getDeclaredAnnotations();
            final String[] aliases = extractPluginAliases(annotations);
            for (final Annotation a : annotations) {
                if (!(a instanceof PluginAliases)) {
                    final PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
                    if (visitor != null) {
                        final Object value = visitor.setAliases(aliases).setAnnotation(a).setConversionType(field.getType()).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(field).visit(this.configuration, this.node, this.event, log);
                        if (value != null) {
                            field.set(builder, value);
                        }
                    }
                }
            }
            final Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations);
            final Object value2 = field.get(builder);
            for (final ConstraintValidator<?> validator : validators) {
                if (!validator.isValid(field.getName(), value2)) {
                    invalid = true;
                    if (!reason.isEmpty()) {
                        reason += ", ";
                    }
                    reason = reason + "field '" + field.getName() + "' has invalid value '" + value2 + "'";
                }
            }
        }
        log.append((log.length() == 0) ? (builder.getClass().getSimpleName() + "()") : ")");
        PluginBuilder.LOGGER.debug(log.toString());
        if (invalid) {
            throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid: " + reason);
        }
        this.checkForRemainingAttributes();
        this.verifyNodeChildrenUsed();
    }
    
    private static String simpleName(final Object object) {
        if (object == null) {
            return "null";
        }
        final String cls = object.getClass().getName();
        final int index = cls.lastIndexOf(46);
        return (index < 0) ? cls : cls.substring(index + 1);
    }
    
    private static Method findFactoryMethod(final Class<?> clazz) {
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PluginFactory.class) && Modifier.isStatic(method.getModifiers())) {
                ReflectionUtil.makeAccessible(method);
                return method;
            }
        }
        throw new IllegalStateException("No factory method found for class " + clazz.getName());
    }
    
    private Object[] generateParameters(final Method factory) {
        final StringBuilder log = new StringBuilder();
        final Class<?>[] types = factory.getParameterTypes();
        final Annotation[][] annotations = factory.getParameterAnnotations();
        final Object[] args = new Object[annotations.length];
        boolean invalid = false;
        for (int i = 0; i < annotations.length; ++i) {
            log.append((log.length() == 0) ? (factory.getName() + "(") : ", ");
            final String[] aliases = extractPluginAliases(annotations[i]);
            for (final Annotation a : annotations[i]) {
                if (!(a instanceof PluginAliases)) {
                    final PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
                    if (visitor != null) {
                        final Object value = visitor.setAliases(aliases).setAnnotation(a).setConversionType(types[i]).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(factory).visit(this.configuration, this.node, this.event, log);
                        if (value != null) {
                            args[i] = value;
                        }
                    }
                }
            }
            final Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations[i]);
            final Object value2 = args[i];
            final String argName = "arg[" + i + "](" + simpleName(value2) + ")";
            for (final ConstraintValidator<?> validator : validators) {
                if (!validator.isValid(argName, value2)) {
                    invalid = true;
                }
            }
        }
        log.append((log.length() == 0) ? (factory.getName() + "()") : ")");
        this.checkForRemainingAttributes();
        this.verifyNodeChildrenUsed();
        PluginBuilder.LOGGER.debug(log.toString());
        if (invalid) {
            throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid");
        }
        return args;
    }
    
    private static String[] extractPluginAliases(final Annotation... parmTypes) {
        String[] aliases = null;
        for (final Annotation a : parmTypes) {
            if (a instanceof PluginAliases) {
                aliases = ((PluginAliases)a).value();
            }
        }
        return aliases;
    }
    
    private void checkForRemainingAttributes() {
        final Map<String, String> attrs = this.node.getAttributes();
        if (!attrs.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (final String key : attrs.keySet()) {
                if (sb.length() == 0) {
                    sb.append(this.node.getName());
                    sb.append(" contains ");
                    if (attrs.size() == 1) {
                        sb.append("an invalid element or attribute ");
                    }
                    else {
                        sb.append("invalid attributes ");
                    }
                }
                else {
                    sb.append(", ");
                }
                StringBuilders.appendDqValue(sb, key);
            }
            PluginBuilder.LOGGER.error(sb.toString());
        }
    }
    
    private void verifyNodeChildrenUsed() {
        final List<Node> children = this.node.getChildren();
        if (!this.pluginType.isDeferChildren() && !children.isEmpty()) {
            for (final Node child : children) {
                final String nodeType = this.node.getType().getElementName();
                final String start = nodeType.equals(this.node.getName()) ? this.node.getName() : (nodeType + ' ' + this.node.getName());
                PluginBuilder.LOGGER.error("{} has no parameter that matches element {}", start, child.getName());
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
