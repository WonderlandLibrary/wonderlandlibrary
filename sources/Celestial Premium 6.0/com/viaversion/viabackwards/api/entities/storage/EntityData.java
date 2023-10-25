/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EntityData {
    private final int id;
    private final int replacementId;
    private Object mobName;
    private MetaCreator defaultMeta;

    public EntityData(int id, int replacementId) {
        this.id = id;
        this.replacementId = replacementId;
    }

    public EntityData jsonName(String name) {
        this.mobName = ChatRewriter.legacyTextToJson(name);
        return this;
    }

    public EntityData mobName(String name) {
        this.mobName = name;
        return this;
    }

    public EntityData spawnMetadata(MetaCreator handler) {
        this.defaultMeta = handler;
        return this;
    }

    public boolean hasBaseMeta() {
        return this.defaultMeta != null;
    }

    public int typeId() {
        return this.id;
    }

    public @Nullable Object mobName() {
        return this.mobName;
    }

    public int replacementId() {
        return this.replacementId;
    }

    public @Nullable MetaCreator defaultMeta() {
        return this.defaultMeta;
    }

    public boolean isObjectType() {
        return false;
    }

    public int objectData() {
        return -1;
    }

    public String toString() {
        return "EntityData{id=" + this.id + ", mobName='" + this.mobName + '\'' + ", replacementId=" + this.replacementId + ", defaultMeta=" + this.defaultMeta + '}';
    }

    @FunctionalInterface
    public static interface MetaCreator {
        public void createMeta(WrappedMetadata var1);
    }
}

