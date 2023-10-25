/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.audio.SoundCategory;

public class SoundList {
    private final List<SoundEntry> soundList = Lists.newArrayList();
    private SoundCategory category;
    private boolean replaceExisting;

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    public List<SoundEntry> getSoundList() {
        return this.soundList;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    public void setReplaceExisting(boolean bl) {
        this.replaceExisting = bl;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        this.category = soundCategory;
    }

    public static class SoundEntry {
        private boolean streaming = false;
        private float volume = 1.0f;
        private int weight = 1;
        private float pitch = 1.0f;
        private Type type = Type.FILE;
        private String name;

        public float getSoundEntryVolume() {
            return this.volume;
        }

        public String getSoundEntryName() {
            return this.name;
        }

        public void setStreaming(boolean bl) {
            this.streaming = bl;
        }

        public float getSoundEntryPitch() {
            return this.pitch;
        }

        public void setSoundEntryName(String string) {
            this.name = string;
        }

        public void setSoundEntryWeight(int n) {
            this.weight = n;
        }

        public int getSoundEntryWeight() {
            return this.weight;
        }

        public void setSoundEntryPitch(float f) {
            this.pitch = f;
        }

        public Type getSoundEntryType() {
            return this.type;
        }

        public boolean isStreaming() {
            return this.streaming;
        }

        public void setSoundEntryType(Type type) {
            this.type = type;
        }

        public void setSoundEntryVolume(float f) {
            this.volume = f;
        }

        public static enum Type {
            FILE("file"),
            SOUND_EVENT("event");

            private final String field_148583_c;

            public static Type getType(String string) {
                Type[] typeArray = Type.values();
                int n = typeArray.length;
                int n2 = 0;
                while (n2 < n) {
                    Type type = typeArray[n2];
                    if (type.field_148583_c.equals(string)) {
                        return type;
                    }
                    ++n2;
                }
                return null;
            }

            private Type(String string2) {
                this.field_148583_c = string2;
            }
        }
    }
}

