/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.scene.paint.Material
 */
package com.sun.javafx.scene.paint;

import com.sun.javafx.sg.prism.NGPhongMaterial;
import com.sun.javafx.util.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Material;

public class MaterialHelper {
    private static MaterialAccessor materialAccessor;

    private MaterialHelper() {
    }

    public static BooleanProperty dirtyProperty(Material material) {
        return materialAccessor.dirtyProperty(material);
    }

    public static void updatePG(Material material) {
        materialAccessor.updatePG(material);
    }

    public static NGPhongMaterial getNGMaterial(Material material) {
        return materialAccessor.getNGMaterial(material);
    }

    public static void setMaterialAccessor(MaterialAccessor materialAccessor) {
        if (MaterialHelper.materialAccessor != null) {
            throw new IllegalStateException();
        }
        MaterialHelper.materialAccessor = materialAccessor;
    }

    static {
        Utils.forceInit(Material.class);
    }

    public static interface MaterialAccessor {
        public BooleanProperty dirtyProperty(Material var1);

        public void updatePG(Material var1);

        public NGPhongMaterial getNGMaterial(Material var1);
    }
}

