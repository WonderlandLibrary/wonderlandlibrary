/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.ScriptObject;

public class SpillProperty
extends AccessorProperty {
    private static final long serialVersionUID = 3028496245198669460L;
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodHandle PARRAY_GETTER = Lookup.MH.asType(Lookup.MH.getter(LOOKUP, ScriptObject.class, "primitiveSpill", long[].class), Lookup.MH.type(long[].class, Object.class));
    private static final MethodHandle OARRAY_GETTER = Lookup.MH.asType(Lookup.MH.getter(LOOKUP, ScriptObject.class, "objectSpill", Object[].class), Lookup.MH.type(Object[].class, Object.class));
    private static final MethodHandle OBJECT_GETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementGetter(Object[].class), 0, OARRAY_GETTER);
    private static final MethodHandle PRIMITIVE_GETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementGetter(long[].class), 0, PARRAY_GETTER);
    private static final MethodHandle OBJECT_SETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementSetter(Object[].class), 0, OARRAY_GETTER);
    private static final MethodHandle PRIMITIVE_SETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementSetter(long[].class), 0, PARRAY_GETTER);

    private static MethodHandle primitiveGetter(int slot, int flags) {
        return (flags & 0x800) == 2048 ? Accessors.getCached(slot, true, true) : null;
    }

    private static MethodHandle primitiveSetter(int slot, int flags) {
        return (flags & 0x800) == 2048 ? Accessors.getCached(slot, true, false) : null;
    }

    private static MethodHandle objectGetter(int slot) {
        return Accessors.getCached(slot, false, true);
    }

    private static MethodHandle objectSetter(int slot) {
        return Accessors.getCached(slot, false, false);
    }

    public SpillProperty(String key, int flags, int slot) {
        super(key, flags, slot, SpillProperty.primitiveGetter(slot, flags), SpillProperty.primitiveSetter(slot, flags), SpillProperty.objectGetter(slot), SpillProperty.objectSetter(slot));
    }

    public SpillProperty(String key, int flags, int slot, Class<?> initialType) {
        this(key, flags, slot);
        this.setType(this.hasDualFields() ? initialType : Object.class);
    }

    SpillProperty(String key, int flags, int slot, ScriptObject owner, Object initialValue) {
        this(key, flags, slot);
        this.setInitialValue(owner, initialValue);
    }

    protected SpillProperty(SpillProperty property) {
        super(property);
    }

    protected SpillProperty(SpillProperty property, Class<?> newType) {
        super((AccessorProperty)property, newType);
    }

    @Override
    public Property copy() {
        return new SpillProperty(this);
    }

    @Override
    public Property copy(Class<?> newType) {
        return new SpillProperty(this, newType);
    }

    @Override
    public boolean isSpill() {
        return true;
    }

    @Override
    void initMethodHandles(Class<?> structure) {
        int slot = this.getSlot();
        this.primitiveGetter = SpillProperty.primitiveGetter(slot, this.getFlags());
        this.primitiveSetter = SpillProperty.primitiveSetter(slot, this.getFlags());
        this.objectGetter = SpillProperty.objectGetter(slot);
        this.objectSetter = SpillProperty.objectSetter(slot);
    }

    private static class Accessors {
        private MethodHandle objectGetter;
        private MethodHandle objectSetter;
        private MethodHandle primitiveGetter;
        private MethodHandle primitiveSetter;
        private final int slot;
        private final MethodHandle ensureSpillSize;
        private static Accessors[] ACCESSOR_CACHE = new Accessors[512];

        Accessors(int slot) {
            assert (slot >= 0);
            this.slot = slot;
            this.ensureSpillSize = Lookup.MH.asType(Lookup.MH.insertArguments(ScriptObject.ENSURE_SPILL_SIZE, 1, slot), Lookup.MH.type(Object.class, Object.class));
        }

        private static void ensure(int slot) {
            int len = ACCESSOR_CACHE.length;
            if (slot >= len) {
                while (slot >= (len *= 2)) {
                }
                Accessors[] newCache = new Accessors[len];
                System.arraycopy(ACCESSOR_CACHE, 0, newCache, 0, ACCESSOR_CACHE.length);
                ACCESSOR_CACHE = newCache;
            }
        }

        static MethodHandle getCached(int slot, boolean isPrimitive, boolean isGetter) {
            Accessors.ensure(slot);
            Accessors acc = ACCESSOR_CACHE[slot];
            if (acc == null) {
                Accessors.ACCESSOR_CACHE[slot] = acc = new Accessors(slot);
            }
            return acc.getOrCreate(isPrimitive, isGetter);
        }

        private static MethodHandle primordial(boolean isPrimitive, boolean isGetter) {
            if (isPrimitive) {
                return isGetter ? PRIMITIVE_GETTER : PRIMITIVE_SETTER;
            }
            return isGetter ? OBJECT_GETTER : OBJECT_SETTER;
        }

        MethodHandle getOrCreate(boolean isPrimitive, boolean isGetter) {
            MethodHandle accessor = this.getInner(isPrimitive, isGetter);
            if (accessor != null) {
                return accessor;
            }
            accessor = Accessors.primordial(isPrimitive, isGetter);
            accessor = Lookup.MH.insertArguments(accessor, 1, this.slot);
            if (!isGetter) {
                accessor = Lookup.MH.filterArguments(accessor, 0, this.ensureSpillSize);
            }
            this.setInner(isPrimitive, isGetter, accessor);
            return accessor;
        }

        void setInner(boolean isPrimitive, boolean isGetter, MethodHandle mh) {
            if (isPrimitive) {
                if (isGetter) {
                    this.primitiveGetter = mh;
                } else {
                    this.primitiveSetter = mh;
                }
            } else if (isGetter) {
                this.objectGetter = mh;
            } else {
                this.objectSetter = mh;
            }
        }

        MethodHandle getInner(boolean isPrimitive, boolean isGetter) {
            if (isPrimitive) {
                return isGetter ? this.primitiveGetter : this.primitiveSetter;
            }
            return isGetter ? this.objectGetter : this.objectSetter;
        }
    }
}

