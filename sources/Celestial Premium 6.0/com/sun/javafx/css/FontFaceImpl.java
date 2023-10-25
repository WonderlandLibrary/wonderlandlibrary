/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.FontFace
 *  javafx.css.StyleConverter$StringStore
 */
package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.css.FontFace;
import javafx.css.StyleConverter;

public final class FontFaceImpl
extends FontFace {
    private final Map<String, String> descriptors;
    private final List<FontFaceSrc> sources;

    public FontFaceImpl(Map<String, String> map, List<FontFaceSrc> list) {
        this.descriptors = map;
        this.sources = list;
    }

    public Map<String, String> getDescriptors() {
        return this.descriptors;
    }

    public List<FontFaceSrc> getSources() {
        return this.sources;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("@font-face { ");
        for (Map.Entry<String, String> object : this.descriptors.entrySet()) {
            stringBuilder.append(object.getKey());
            stringBuilder.append(" : ");
            stringBuilder.append(object.getValue());
            stringBuilder.append("; ");
        }
        stringBuilder.append("src : ");
        for (FontFaceSrc fontFaceSrc : this.sources) {
            stringBuilder.append((Object)fontFaceSrc.getType());
            stringBuilder.append(" \"");
            stringBuilder.append(fontFaceSrc.getSrc());
            stringBuilder.append("\", ");
        }
        stringBuilder.append("; ");
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public final void writeBinary(DataOutputStream dataOutputStream, StyleConverter.StringStore stringStore) throws IOException {
        Object object;
        Set<Map.Entry<String, String>> set = this.getDescriptors() != null ? this.getDescriptors().entrySet() : null;
        int n = set != null ? set.size() : 0;
        dataOutputStream.writeShort(n);
        if (set != null) {
            object = set.iterator();
            while (object.hasNext()) {
                Map.Entry entry = (Map.Entry)object.next();
                int n2 = stringStore.addString((String)entry.getKey());
                dataOutputStream.writeInt(n2);
                n2 = stringStore.addString((String)entry.getValue());
                dataOutputStream.writeInt(n2);
            }
        }
        n = (object = this.getSources()) != null ? object.size() : 0;
        dataOutputStream.writeShort(n);
        for (int i = 0; i < n; ++i) {
            FontFaceSrc fontFaceSrc = (FontFaceSrc)object.get(i);
            fontFaceSrc.writeBinary(dataOutputStream, stringStore);
        }
    }

    public static final FontFaceImpl readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        Object object;
        int n2;
        int n3 = dataInputStream.readShort();
        HashMap<String, String> hashMap = new HashMap<String, String>(n3);
        for (int i = 0; i < n3; ++i) {
            n2 = dataInputStream.readInt();
            object = arrstring[n2];
            n2 = dataInputStream.readInt();
            String string = arrstring[n2];
            hashMap.put((String)object, string);
        }
        n3 = dataInputStream.readShort();
        ArrayList<FontFaceSrc> arrayList = new ArrayList<FontFaceSrc>(n3);
        for (n2 = 0; n2 < n3; ++n2) {
            object = FontFaceSrc.readBinary(n, dataInputStream, arrstring);
            arrayList.add((FontFaceSrc)object);
        }
        return new FontFaceImpl(hashMap, arrayList);
    }

    public static class FontFaceSrc {
        private final FontFaceSrcType type;
        private final String src;
        private final String format;

        public FontFaceSrc(FontFaceSrcType fontFaceSrcType, String string, String string2) {
            this.type = fontFaceSrcType;
            this.src = string;
            this.format = string2;
        }

        public FontFaceSrc(FontFaceSrcType fontFaceSrcType, String string) {
            this.type = fontFaceSrcType;
            this.src = string;
            this.format = null;
        }

        public FontFaceSrcType getType() {
            return this.type;
        }

        public String getSrc() {
            return this.src;
        }

        public String getFormat() {
            return this.format;
        }

        final void writeBinary(DataOutputStream dataOutputStream, StyleConverter.StringStore stringStore) throws IOException {
            dataOutputStream.writeInt(stringStore.addString(this.type.name()));
            dataOutputStream.writeInt(stringStore.addString(this.src));
            dataOutputStream.writeInt(stringStore.addString(this.format));
        }

        static final FontFaceSrc readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
            int n2 = dataInputStream.readInt();
            FontFaceSrcType fontFaceSrcType = arrstring[n2] != null ? FontFaceSrcType.valueOf(arrstring[n2]) : null;
            n2 = dataInputStream.readInt();
            String string = arrstring[n2];
            n2 = dataInputStream.readInt();
            String string2 = arrstring[n2];
            return new FontFaceSrc(fontFaceSrcType, string, string2);
        }
    }

    public static final class FontFaceSrcType
    extends Enum<FontFaceSrcType> {
        public static final /* enum */ FontFaceSrcType URL = new FontFaceSrcType();
        public static final /* enum */ FontFaceSrcType LOCAL = new FontFaceSrcType();
        public static final /* enum */ FontFaceSrcType REFERENCE = new FontFaceSrcType();
        private static final /* synthetic */ FontFaceSrcType[] $VALUES;

        public static FontFaceSrcType[] values() {
            return (FontFaceSrcType[])$VALUES.clone();
        }

        public static FontFaceSrcType valueOf(String string) {
            return Enum.valueOf(FontFaceSrcType.class, string);
        }

        private static /* synthetic */ FontFaceSrcType[] $values() {
            return new FontFaceSrcType[]{URL, LOCAL, REFERENCE};
        }

        static {
            $VALUES = FontFaceSrcType.$values();
        }
    }
}

