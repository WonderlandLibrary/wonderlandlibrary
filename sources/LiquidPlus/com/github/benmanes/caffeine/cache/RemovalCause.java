/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

public enum RemovalCause {
    EXPLICIT{

        @Override
        public boolean wasEvicted() {
            return false;
        }
    }
    ,
    REPLACED{

        @Override
        public boolean wasEvicted() {
            return false;
        }
    }
    ,
    COLLECTED{

        @Override
        public boolean wasEvicted() {
            return true;
        }
    }
    ,
    EXPIRED{

        @Override
        public boolean wasEvicted() {
            return true;
        }
    }
    ,
    SIZE{

        @Override
        public boolean wasEvicted() {
            return true;
        }
    };


    public abstract boolean wasEvicted();
}

