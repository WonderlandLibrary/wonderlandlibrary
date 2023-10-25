/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Material;
import com.sun.prism.TextureMap;

public interface PhongMaterial
extends Material {
    public static final int DIFFUSE = MapType.DIFFUSE.ordinal();
    public static final int SPECULAR = MapType.SPECULAR.ordinal();
    public static final int BUMP = MapType.BUMP.ordinal();
    public static final int SELF_ILLUM = MapType.SELF_ILLUM.ordinal();
    public static final int MAX_MAP_TYPE = MapType.values().length;

    public void setDiffuseColor(float var1, float var2, float var3, float var4);

    public void setSpecularColor(boolean var1, float var2, float var3, float var4, float var5);

    public void setTextureMap(TextureMap var1);

    public void lockTextureMaps();

    public void unlockTextureMaps();

    public static final class MapType
    extends Enum<MapType> {
        public static final /* enum */ MapType DIFFUSE = new MapType();
        public static final /* enum */ MapType SPECULAR = new MapType();
        public static final /* enum */ MapType BUMP = new MapType();
        public static final /* enum */ MapType SELF_ILLUM = new MapType();
        private static final /* synthetic */ MapType[] $VALUES;

        public static MapType[] values() {
            return (MapType[])$VALUES.clone();
        }

        public static MapType valueOf(String string) {
            return Enum.valueOf(MapType.class, string);
        }

        private static /* synthetic */ MapType[] $values() {
            return new MapType[]{DIFFUSE, SPECULAR, BUMP, SELF_ILLUM};
        }

        static {
            $VALUES = MapType.$values();
        }
    }
}

