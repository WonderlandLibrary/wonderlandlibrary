/*
 * Decompiled with CFR 0.152.
 */
package org.yaml.snakeyaml.introspector;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.yaml.snakeyaml.introspector.Property;

public abstract class GenericProperty
extends Property {
    private Type genType;
    private boolean actualClassesChecked;
    private Class<?>[] actualClasses;

    public GenericProperty(String name, Class<?> aClass, Type aType) {
        super(name, aClass);
        this.genType = aType;
        this.actualClassesChecked = aType == null;
    }

    @Override
    public Class<?>[] getActualTypeArguments() {
        if (!this.actualClassesChecked) {
            Class classType;
            if (this.genType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)this.genType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    this.actualClasses = new Class[actualTypeArguments.length];
                    for (int i2 = 0; i2 < actualTypeArguments.length; ++i2) {
                        if (actualTypeArguments[i2] instanceof Class) {
                            this.actualClasses[i2] = (Class)actualTypeArguments[i2];
                            continue;
                        }
                        if (actualTypeArguments[i2] instanceof ParameterizedType) {
                            this.actualClasses[i2] = (Class)((ParameterizedType)actualTypeArguments[i2]).getRawType();
                            continue;
                        }
                        if (actualTypeArguments[i2] instanceof GenericArrayType) {
                            Type componentType = ((GenericArrayType)actualTypeArguments[i2]).getGenericComponentType();
                            if (componentType instanceof Class) {
                                this.actualClasses[i2] = Array.newInstance((Class)componentType, 0).getClass();
                                continue;
                            }
                            this.actualClasses = null;
                        } else {
                            this.actualClasses = null;
                        }
                        break;
                    }
                }
            } else if (this.genType instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType)this.genType).getGenericComponentType();
                if (componentType instanceof Class) {
                    this.actualClasses = new Class[]{(Class)componentType};
                }
            } else if (this.genType instanceof Class && (classType = (Class)this.genType).isArray()) {
                this.actualClasses = new Class[1];
                this.actualClasses[0] = this.getType().getComponentType();
            }
            this.actualClassesChecked = true;
        }
        return this.actualClasses;
    }
}

