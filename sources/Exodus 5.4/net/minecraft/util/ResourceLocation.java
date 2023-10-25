/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class ResourceLocation {
    protected final String resourceDomain;
    protected final String resourcePath;

    public ResourceLocation(String string) {
        this(0, ResourceLocation.splitObjectName(string));
    }

    public String getResourceDomain() {
        return this.resourceDomain;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ResourceLocation)) {
            return false;
        }
        ResourceLocation resourceLocation = (ResourceLocation)object;
        return this.resourceDomain.equals(resourceLocation.resourceDomain) && this.resourcePath.equals(resourceLocation.resourcePath);
    }

    public String toString() {
        return String.valueOf(this.resourceDomain) + ':' + this.resourcePath;
    }

    public int hashCode() {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }

    protected static String[] splitObjectName(String string) {
        String[] stringArray = new String[2];
        stringArray[1] = string;
        String[] stringArray2 = stringArray;
        int n = string.indexOf(58);
        if (n >= 0) {
            stringArray2[1] = string.substring(n + 1, string.length());
            if (n > 1) {
                stringArray2[0] = string.substring(0, n);
            }
        }
        return stringArray2;
    }

    protected ResourceLocation(int n, String ... stringArray) {
        this.resourceDomain = StringUtils.isEmpty((CharSequence)stringArray[0]) ? "minecraft" : stringArray[0].toLowerCase();
        this.resourcePath = stringArray[1];
        Validate.notNull((Object)this.resourcePath);
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public ResourceLocation(String string, String string2) {
        this(0, string, string2);
    }
}

