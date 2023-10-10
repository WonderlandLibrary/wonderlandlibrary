// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import java.lang.reflect.Member;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.lang.annotation.Annotation;

public interface PluginVisitor<A extends Annotation>
{
    PluginVisitor<A> setAnnotation(final Annotation annotation);
    
    PluginVisitor<A> setAliases(final String... aliases);
    
    PluginVisitor<A> setConversionType(final Class<?> conversionType);
    
    PluginVisitor<A> setStrSubstitutor(final StrSubstitutor substitutor);
    
    PluginVisitor<A> setMember(final Member member);
    
    Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log);
}
