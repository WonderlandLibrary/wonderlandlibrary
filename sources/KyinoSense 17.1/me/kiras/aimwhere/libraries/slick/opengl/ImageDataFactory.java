/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.opengl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import me.kiras.aimwhere.libraries.slick.opengl.CompositeImageData;
import me.kiras.aimwhere.libraries.slick.opengl.ImageIOImageData;
import me.kiras.aimwhere.libraries.slick.opengl.LoadableImageData;
import me.kiras.aimwhere.libraries.slick.opengl.PNGImageData;
import me.kiras.aimwhere.libraries.slick.opengl.TGAImageData;
import me.kiras.aimwhere.libraries.slick.util.Log;

public class ImageDataFactory {
    private static boolean usePngLoader = true;
    private static boolean pngLoaderPropertyChecked = false;
    private static final String PNG_LOADER = "me.kiras.aimwhere.libraries.slick.pngloader";

    private static void checkProperty() {
        if (!pngLoaderPropertyChecked) {
            pngLoaderPropertyChecked = true;
            try {
                AccessController.doPrivileged(new PrivilegedAction(){

                    public Object run() {
                        String val = System.getProperty(ImageDataFactory.PNG_LOADER);
                        if ("false".equalsIgnoreCase(val)) {
                            usePngLoader = false;
                        }
                        Log.info("Use Java PNG Loader = " + usePngLoader);
                        return null;
                    }
                });
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static LoadableImageData getImageDataFor(String ref) {
        ImageDataFactory.checkProperty();
        ref = ref.toLowerCase();
        if (ref.endsWith(".tga")) {
            return new TGAImageData();
        }
        if (ref.endsWith(".png")) {
            CompositeImageData data = new CompositeImageData();
            if (usePngLoader) {
                data.add(new PNGImageData());
            }
            data.add(new ImageIOImageData());
            return data;
        }
        return new ImageIOImageData();
    }
}

