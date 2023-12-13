/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import java.util.UUID;

public class ObjectIdGenerators {

    public static final class StringIdGenerator
    extends Base<String> {
        private static final long serialVersionUID = 1L;

        public StringIdGenerator() {
            this(Object.class);
        }

        private StringIdGenerator(Class<?> scope) {
            super(Object.class);
        }

        @Override
        public ObjectIdGenerator<String> forScope(Class<?> scope) {
            return this;
        }

        @Override
        public ObjectIdGenerator<String> newForSerialization(Object context) {
            return this;
        }

        @Override
        public String generateId(Object forPojo) {
            return UUID.randomUUID().toString();
        }

        @Override
        public ObjectIdGenerator.IdKey key(Object key) {
            if (key == null) {
                return null;
            }
            return new ObjectIdGenerator.IdKey(this.getClass(), null, key);
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return gen instanceof StringIdGenerator;
        }
    }

    public static final class UUIDGenerator
    extends Base<UUID> {
        private static final long serialVersionUID = 1L;

        public UUIDGenerator() {
            this(Object.class);
        }

        private UUIDGenerator(Class<?> scope) {
            super(Object.class);
        }

        @Override
        public ObjectIdGenerator<UUID> forScope(Class<?> scope) {
            return this;
        }

        @Override
        public ObjectIdGenerator<UUID> newForSerialization(Object context) {
            return this;
        }

        @Override
        public UUID generateId(Object forPojo) {
            return UUID.randomUUID();
        }

        @Override
        public ObjectIdGenerator.IdKey key(Object key) {
            if (key == null) {
                return null;
            }
            return new ObjectIdGenerator.IdKey(this.getClass(), null, key);
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return gen.getClass() == this.getClass();
        }
    }

    public static final class IntSequenceGenerator
    extends Base<Integer> {
        private static final long serialVersionUID = 1L;
        protected transient int _nextValue;

        public IntSequenceGenerator() {
            this(Object.class, -1);
        }

        public IntSequenceGenerator(Class<?> scope, int fv) {
            super(scope);
            this._nextValue = fv;
        }

        protected int initialValue() {
            return 1;
        }

        @Override
        public ObjectIdGenerator<Integer> forScope(Class<?> scope) {
            return this._scope == scope ? this : new IntSequenceGenerator(scope, this._nextValue);
        }

        @Override
        public ObjectIdGenerator<Integer> newForSerialization(Object context) {
            return new IntSequenceGenerator(this._scope, this.initialValue());
        }

        @Override
        public ObjectIdGenerator.IdKey key(Object key) {
            if (key == null) {
                return null;
            }
            return new ObjectIdGenerator.IdKey(this.getClass(), this._scope, key);
        }

        @Override
        public Integer generateId(Object forPojo) {
            if (forPojo == null) {
                return null;
            }
            int id = this._nextValue++;
            return id;
        }
    }

    public static abstract class PropertyGenerator
    extends Base<Object> {
        private static final long serialVersionUID = 1L;

        protected PropertyGenerator(Class<?> scope) {
            super(scope);
        }
    }

    public static abstract class None
    extends ObjectIdGenerator<Object> {
    }

    private static abstract class Base<T>
    extends ObjectIdGenerator<T> {
        protected final Class<?> _scope;

        protected Base(Class<?> scope) {
            this._scope = scope;
        }

        @Override
        public final Class<?> getScope() {
            return this._scope;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return gen.getClass() == this.getClass() && gen.getScope() == this._scope;
        }

        @Override
        public abstract T generateId(Object var1);
    }
}

