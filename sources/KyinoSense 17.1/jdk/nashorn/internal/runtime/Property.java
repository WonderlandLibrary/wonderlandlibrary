/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.SwitchPoint;
import java.util.Objects;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class Property
implements Serializable {
    public static final int WRITABLE_ENUMERABLE_CONFIGURABLE = 0;
    public static final int NOT_WRITABLE = 1;
    public static final int NOT_ENUMERABLE = 2;
    public static final int NOT_CONFIGURABLE = 4;
    private static final int MODIFY_MASK = 7;
    public static final int IS_PARAMETER = 8;
    public static final int HAS_ARGUMENTS = 16;
    public static final int IS_FUNCTION_DECLARATION = 32;
    public static final int IS_NASGEN_PRIMITIVE = 64;
    public static final int IS_BUILTIN = 128;
    public static final int IS_BOUND = 256;
    public static final int NEEDS_DECLARATION = 512;
    public static final int IS_LEXICAL_BINDING = 1024;
    public static final int DUAL_FIELDS = 2048;
    private final String key;
    private int flags;
    private final int slot;
    private Class<?> type;
    protected transient SwitchPoint builtinSwitchPoint;
    private static final long serialVersionUID = 2099814273074501176L;

    Property(String key, int flags, int slot) {
        assert (key != null);
        this.key = key;
        this.flags = flags;
        this.slot = slot;
    }

    Property(Property property, int flags) {
        this.key = property.key;
        this.slot = property.slot;
        this.builtinSwitchPoint = property.builtinSwitchPoint;
        this.flags = flags;
    }

    public abstract Property copy();

    public abstract Property copy(Class<?> var1);

    static int mergeFlags(PropertyDescriptor oldDesc, PropertyDescriptor newDesc) {
        boolean value;
        int propFlags = 0;
        boolean bl = value = newDesc.has("configurable") ? newDesc.isConfigurable() : oldDesc.isConfigurable();
        if (!value) {
            propFlags |= 4;
        }
        boolean bl2 = value = newDesc.has("enumerable") ? newDesc.isEnumerable() : oldDesc.isEnumerable();
        if (!value) {
            propFlags |= 2;
        }
        boolean bl3 = value = newDesc.has("writable") ? newDesc.isWritable() : oldDesc.isWritable();
        if (!value) {
            propFlags |= 1;
        }
        return propFlags;
    }

    public final void setBuiltinSwitchPoint(SwitchPoint sp) {
        this.builtinSwitchPoint = sp;
    }

    public final SwitchPoint getBuiltinSwitchPoint() {
        return this.builtinSwitchPoint;
    }

    public boolean isBuiltin() {
        return this.builtinSwitchPoint != null && !this.builtinSwitchPoint.hasBeenInvalidated();
    }

    static int toFlags(PropertyDescriptor desc) {
        int propFlags = 0;
        if (!desc.isConfigurable()) {
            propFlags |= 4;
        }
        if (!desc.isEnumerable()) {
            propFlags |= 2;
        }
        if (!desc.isWritable()) {
            propFlags |= 1;
        }
        return propFlags;
    }

    public boolean hasGetterFunction(ScriptObject obj) {
        return false;
    }

    public boolean hasSetterFunction(ScriptObject obj) {
        return false;
    }

    public boolean isWritable() {
        return (this.flags & 1) == 0;
    }

    public boolean isConfigurable() {
        return (this.flags & 4) == 0;
    }

    public boolean isEnumerable() {
        return (this.flags & 2) == 0;
    }

    public boolean isParameter() {
        return (this.flags & 8) != 0;
    }

    public boolean hasArguments() {
        return (this.flags & 0x10) != 0;
    }

    public boolean isSpill() {
        return false;
    }

    public boolean isBound() {
        return (this.flags & 0x100) != 0;
    }

    public boolean needsDeclaration() {
        return (this.flags & 0x200) != 0;
    }

    public Property addFlags(int propertyFlags) {
        if ((this.flags & propertyFlags) != propertyFlags) {
            Property cloned = this.copy();
            cloned.flags |= propertyFlags;
            return cloned;
        }
        return this;
    }

    public int getFlags() {
        return this.flags;
    }

    public Property removeFlags(int propertyFlags) {
        if ((this.flags & propertyFlags) != 0) {
            Property cloned = this.copy();
            cloned.flags &= ~propertyFlags;
            return cloned;
        }
        return this;
    }

    public Property setFlags(int propertyFlags) {
        if (this.flags != propertyFlags) {
            Property cloned = this.copy();
            cloned.flags &= 0xFFFFFFF8;
            cloned.flags |= propertyFlags & 7;
            return cloned;
        }
        return this;
    }

    public abstract MethodHandle getGetter(Class<?> var1);

    public abstract MethodHandle getOptimisticGetter(Class<?> var1, int var2);

    abstract void initMethodHandles(Class<?> var1);

    public String getKey() {
        return this.key;
    }

    public int getSlot() {
        return this.slot;
    }

    public abstract int getIntValue(ScriptObject var1, ScriptObject var2);

    public abstract double getDoubleValue(ScriptObject var1, ScriptObject var2);

    public abstract Object getObjectValue(ScriptObject var1, ScriptObject var2);

    public abstract void setValue(ScriptObject var1, ScriptObject var2, int var3, boolean var4);

    public abstract void setValue(ScriptObject var1, ScriptObject var2, double var3, boolean var5);

    public abstract void setValue(ScriptObject var1, ScriptObject var2, Object var3, boolean var4);

    public abstract MethodHandle getSetter(Class<?> var1, PropertyMap var2);

    public ScriptFunction getGetterFunction(ScriptObject obj) {
        return null;
    }

    public ScriptFunction getSetterFunction(ScriptObject obj) {
        return null;
    }

    public int hashCode() {
        Class<?> t = this.getLocalType();
        return Objects.hashCode(this.key) ^ this.flags ^ this.getSlot() ^ (t == null ? 0 : t.hashCode());
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Property otherProperty = (Property)other;
        return this.equalsWithoutType(otherProperty) && this.getLocalType() == otherProperty.getLocalType();
    }

    boolean equalsWithoutType(Property otherProperty) {
        return this.getFlags() == otherProperty.getFlags() && this.getSlot() == otherProperty.getSlot() && this.getKey().equals(otherProperty.getKey());
    }

    private static String type(Class<?> type) {
        if (type == null) {
            return "undef";
        }
        if (type == Integer.TYPE) {
            return "i";
        }
        if (type == Double.TYPE) {
            return "d";
        }
        return "o";
    }

    public final String toStringShort() {
        StringBuilder sb = new StringBuilder();
        Class<?> t = this.getLocalType();
        sb.append(this.getKey()).append(" (").append(Property.type(t)).append(')');
        return sb.toString();
    }

    private static String indent(String str, int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < indent - str.length(); ++i) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<?> t = this.getLocalType();
        sb.append(Property.indent(this.getKey(), 20)).append(" id=").append(Debug.id(this)).append(" (0x").append(Property.indent(Integer.toHexString(this.flags), 4)).append(") ").append(this.getClass().getSimpleName()).append(" {").append(Property.indent(Property.type(t), 5)).append('}');
        if (this.slot != -1) {
            sb.append(" [").append("slot=").append(this.slot).append(']');
        }
        return sb.toString();
    }

    public final Class<?> getType() {
        return this.type;
    }

    public final void setType(Class<?> type) {
        assert (type != Boolean.TYPE) : "no boolean storage support yet - fix this";
        this.type = type == null ? null : (type.isPrimitive() ? type : Object.class);
    }

    protected Class<?> getLocalType() {
        return this.getType();
    }

    public boolean canChangeType() {
        return false;
    }

    public boolean isFunctionDeclaration() {
        return (this.flags & 0x20) != 0;
    }

    public boolean isLexicalBinding() {
        return (this.flags & 0x400) != 0;
    }

    public boolean hasDualFields() {
        return (this.flags & 0x800) != 0;
    }
}

