/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import java.util.Arrays;
import java.util.List;

public interface ImageFormatDescription {
    public String getFormatName();

    public List<String> getExtensions();

    public List<Signature> getSignatures();

    public static final class Signature {
        private final byte[] bytes;

        public Signature(byte ... arrby) {
            this.bytes = arrby;
        }

        public int getLength() {
            return this.bytes.length;
        }

        public boolean matches(byte[] arrby) {
            if (arrby.length < this.bytes.length) {
                return false;
            }
            for (int i = 0; i < this.bytes.length; ++i) {
                if (arrby[i] == this.bytes[i]) continue;
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Arrays.hashCode(this.bytes);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Signature)) {
                return false;
            }
            return Arrays.equals(this.bytes, ((Signature)object).bytes);
        }
    }
}

