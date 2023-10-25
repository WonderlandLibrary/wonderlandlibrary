/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

public abstract class Paint {
    private final Type type;
    private final boolean proportional;
    private final boolean isMutable;

    Paint(Type type, boolean bl, boolean bl2) {
        this.type = type;
        this.proportional = bl;
        this.isMutable = bl2;
    }

    public final Type getType() {
        return this.type;
    }

    public boolean isProportional() {
        return this.proportional;
    }

    public abstract boolean isOpaque();

    public boolean isMutable() {
        return this.isMutable;
    }

    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type COLOR = new Type("Color", false, false);
        public static final /* enum */ Type LINEAR_GRADIENT = new Type("LinearGradient", true, false);
        public static final /* enum */ Type RADIAL_GRADIENT = new Type("RadialGradient", true, false);
        public static final /* enum */ Type IMAGE_PATTERN = new Type("ImagePattern", false, true);
        private String name;
        private boolean isGradient;
        private boolean isImagePattern;
        private static final /* synthetic */ Type[] $VALUES;

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public static Type valueOf(String string) {
            return Enum.valueOf(Type.class, string);
        }

        private Type(String string2, boolean bl, boolean bl2) {
            this.name = string2;
            this.isGradient = bl;
            this.isImagePattern = bl2;
        }

        public String getName() {
            return this.name;
        }

        public boolean isGradient() {
            return this.isGradient;
        }

        public boolean isImagePattern() {
            return this.isImagePattern;
        }

        private static /* synthetic */ Type[] $values() {
            return new Type[]{COLOR, LINEAR_GRADIENT, RADIAL_GRADIENT, IMAGE_PATTERN};
        }

        static {
            $VALUES = Type.$values();
        }
    }
}

