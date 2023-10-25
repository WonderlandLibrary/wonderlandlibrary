/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

public abstract class Light {
    private final Type type;
    private Color4f color;

    Light(Type type) {
        this(type, Color4f.WHITE);
    }

    Light(Type type, Color4f color4f) {
        if (type == null) {
            throw new InternalError("Light type must be non-null");
        }
        this.type = type;
        this.setColor(color4f);
    }

    public Type getType() {
        return this.type;
    }

    public Color4f getColor() {
        return this.color;
    }

    public void setColor(Color4f color4f) {
        if (color4f == null) {
            throw new IllegalArgumentException("Color must be non-null");
        }
        this.color = color4f;
    }

    public abstract float[] getNormalizedLightPosition();

    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type DISTANT = new Type();
        public static final /* enum */ Type POINT = new Type();
        public static final /* enum */ Type SPOT = new Type();
        private static final /* synthetic */ Type[] $VALUES;

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public static Type valueOf(String string) {
            return Enum.valueOf(Type.class, string);
        }

        private static /* synthetic */ Type[] $values() {
            return new Type[]{DISTANT, POINT, SPOT};
        }

        static {
            $VALUES = Type.$values();
        }
    }
}

