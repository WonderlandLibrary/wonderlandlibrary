/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdySettingsFrame;
import io.netty.util.internal.StringUtil;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DefaultSpdySettingsFrame
implements SpdySettingsFrame {
    private final Map<Integer, Setting> settingsMap = new TreeMap<Integer, Setting>();
    private boolean clear;

    @Override
    public boolean isSet(int n) {
        Integer n2 = n;
        return this.settingsMap.containsKey(n2);
    }

    @Override
    public Set<Integer> ids() {
        return this.settingsMap.keySet();
    }

    @Override
    public SpdySettingsFrame setPersisted(int n, boolean bl) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            this.settingsMap.get(n2).setPersisted(bl);
        }
        return this;
    }

    @Override
    public boolean clearPreviouslyPersistedSettings() {
        return this.clear;
    }

    @Override
    public boolean isPersistValue(int n) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            return this.settingsMap.get(n2).isPersist();
        }
        return false;
    }

    @Override
    public SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean bl) {
        this.clear = bl;
        return this;
    }

    private void appendSettings(StringBuilder stringBuilder) {
        for (Map.Entry<Integer, Setting> entry : this.getSettings()) {
            Setting setting = entry.getValue();
            stringBuilder.append("--> ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append(':');
            stringBuilder.append(setting.getValue());
            stringBuilder.append(" (persist value: ");
            stringBuilder.append(setting.isPersist());
            stringBuilder.append("; persisted: ");
            stringBuilder.append(setting.isPersisted());
            stringBuilder.append(')');
            stringBuilder.append(StringUtil.NEWLINE);
        }
    }

    private Set<Map.Entry<Integer, Setting>> getSettings() {
        return this.settingsMap.entrySet();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendSettings(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }

    @Override
    public boolean isPersisted(int n) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            return this.settingsMap.get(n2).isPersisted();
        }
        return false;
    }

    @Override
    public SpdySettingsFrame setPersistValue(int n, boolean bl) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            this.settingsMap.get(n2).setPersist(bl);
        }
        return this;
    }

    @Override
    public SpdySettingsFrame setValue(int n, int n2, boolean bl, boolean bl2) {
        if (n < 0 || n > 0xFFFFFF) {
            throw new IllegalArgumentException("Setting ID is not valid: " + n);
        }
        Integer n3 = n;
        if (this.settingsMap.containsKey(n3)) {
            Setting setting = this.settingsMap.get(n3);
            setting.setValue(n2);
            setting.setPersist(bl);
            setting.setPersisted(bl2);
        } else {
            this.settingsMap.put(n3, new Setting(n2, bl, bl2));
        }
        return this;
    }

    @Override
    public int getValue(int n) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            return this.settingsMap.get(n2).getValue();
        }
        return -1;
    }

    @Override
    public SpdySettingsFrame removeValue(int n) {
        Integer n2 = n;
        if (this.settingsMap.containsKey(n2)) {
            this.settingsMap.remove(n2);
        }
        return this;
    }

    @Override
    public SpdySettingsFrame setValue(int n, int n2) {
        return this.setValue(n, n2, false, false);
    }

    private static final class Setting {
        private int value;
        private boolean persisted;
        private boolean persist;

        void setPersist(boolean bl) {
            this.persist = bl;
        }

        int getValue() {
            return this.value;
        }

        void setPersisted(boolean bl) {
            this.persisted = bl;
        }

        Setting(int n, boolean bl, boolean bl2) {
            this.value = n;
            this.persist = bl;
            this.persisted = bl2;
        }

        boolean isPersist() {
            return this.persist;
        }

        void setValue(int n) {
            this.value = n;
        }

        boolean isPersisted() {
            return this.persisted;
        }
    }
}

