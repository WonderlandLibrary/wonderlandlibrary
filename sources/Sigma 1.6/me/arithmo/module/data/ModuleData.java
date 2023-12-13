/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package me.arithmo.module.data;

import com.google.gson.annotations.Expose;
import me.arithmo.management.keybinding.KeyMask;

public class ModuleData {
    public final Type type;
    public final String name;
    public final String description;
    @Expose
    public int key;
    @Expose
    public KeyMask mask;

    public ModuleData(Type type, String name, String description, int key, KeyMask mask) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.key = key;
        this.mask = mask;
    }

    public ModuleData(Type type, String name, String description) {
        this(type, name, description, 0, KeyMask.None);
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public KeyMask getMask() {
        return this.mask;
    }

    public void setMask(KeyMask mask) {
        this.mask = mask;
    }

    public void update(ModuleData data) {
        this.key = data.key;
        this.mask = data.mask;
    }

    public static enum Type {
        Combat,
        Player,
        Movement,
        Visuals,
        Other,
        MiniGames;
        

        private Type() {
        }
    }

}

