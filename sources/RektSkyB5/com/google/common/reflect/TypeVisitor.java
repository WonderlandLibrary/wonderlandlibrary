/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.NotThreadSafe
 */
package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
abstract class TypeVisitor {
    private final Set<Type> visited = Sets.newHashSet();

    TypeVisitor() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void visit(Type ... types) {
        for (Type type : types) {
            if (type == null || !this.visited.add(type)) continue;
            boolean succeeded = false;
            try {
                if (type instanceof TypeVariable) {
                    this.visitTypeVariable((TypeVariable)type);
                } else if (type instanceof WildcardType) {
                    this.visitWildcardType((WildcardType)type);
                } else if (type instanceof ParameterizedType) {
                    this.visitParameterizedType((ParameterizedType)type);
                } else if (type instanceof Class) {
                    this.visitClass((Class)type);
                } else if (type instanceof GenericArrayType) {
                    this.visitGenericArrayType((GenericArrayType)type);
                } else {
                    throw new AssertionError((Object)("Unknown type: " + type));
                }
                succeeded = true;
            }
            finally {
                if (!succeeded) {
                    this.visited.remove(type);
                }
            }
        }
    }

    void visitClass(Class<?> t2) {
    }

    void visitGenericArrayType(GenericArrayType t2) {
    }

    void visitParameterizedType(ParameterizedType t2) {
    }

    void visitTypeVariable(TypeVariable<?> t2) {
    }

    void visitWildcardType(WildcardType t2) {
    }
}

