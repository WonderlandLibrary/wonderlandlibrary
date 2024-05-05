/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util;

import java.io.InputStream;
import java.net.URL;
import me.kiras.aimwhere.libraries.slick.util.ResourceLoader;
import me.kiras.aimwhere.libraries.slick.util.ResourceLocation;

public class ClasspathLocation
implements ResourceLocation {
    @Override
    public URL getResource(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResource(cpRef);
    }

    @Override
    public InputStream getResourceAsStream(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResourceAsStream(cpRef);
    }
}

