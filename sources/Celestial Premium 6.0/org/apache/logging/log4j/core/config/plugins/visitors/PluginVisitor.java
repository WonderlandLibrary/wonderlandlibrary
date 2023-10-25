/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

public interface PluginVisitor<A extends Annotation> {
    public PluginVisitor<A> setAnnotation(Annotation var1);

    public PluginVisitor<A> setAliases(String ... var1);

    public PluginVisitor<A> setConversionType(Class<?> var1);

    public PluginVisitor<A> setStrSubstitutor(StrSubstitutor var1);

    public PluginVisitor<A> setMember(Member var1);

    public Object visit(Configuration var1, Node var2, LogEvent var3, StringBuilder var4);
}

