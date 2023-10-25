/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 */
package com.sun.scenario.effect.impl;

import com.sun.javafx.PlatformUtil;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.reflect.Method;
import java.security.AccessController;

class RendererFactory {
    private static String rootPkg = "com.sun.scenario.effect";
    private static boolean tryRSL = true;
    private static boolean trySIMD = false;
    private static boolean tryJOGL = PlatformUtil.isMac();
    private static boolean tryPrism = true;

    RendererFactory() {
    }

    private static boolean isRSLFriendly(Class class_) {
        if (class_.getName().equals("sun.java2d.pipe.hw.AccelGraphicsConfig")) {
            return true;
        }
        boolean bl = false;
        for (Class<?> class_2 : class_.getInterfaces()) {
            if (!RendererFactory.isRSLFriendly(class_2)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    private static boolean isRSLAvailable(FilterContext filterContext) {
        return RendererFactory.isRSLFriendly(filterContext.getReferent().getClass());
    }

    private static Renderer createRSLRenderer(FilterContext filterContext) {
        try {
            Class<?> class_ = Class.forName(rootPkg + ".impl.j2d.rsl.RSLRenderer");
            Method method = class_.getMethod("createRenderer", FilterContext.class);
            return (Renderer)method.invoke(null, filterContext);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Renderer createJOGLRenderer(FilterContext filterContext) {
        if (tryJOGL) {
            try {
                Class<?> class_ = Class.forName(rootPkg + ".impl.j2d.jogl.JOGLRenderer");
                Method method = class_.getMethod("createRenderer", FilterContext.class);
                return (Renderer)method.invoke(null, filterContext);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private static Renderer createPrismRenderer(FilterContext filterContext) {
        if (tryPrism) {
            try {
                Class<?> class_ = Class.forName(rootPkg + ".impl.prism.PrRenderer");
                Method method = class_.getMethod("createRenderer", FilterContext.class);
                return (Renderer)method.invoke(null, filterContext);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }

    private static Renderer getSSERenderer() {
        if (trySIMD) {
            try {
                Class<?> class_ = Class.forName(rootPkg + ".impl.j2d.J2DSWRenderer");
                Method method = class_.getMethod("getSSEInstance", null);
                Renderer renderer = (Renderer)method.invoke(null, null);
                if (renderer != null) {
                    return renderer;
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            trySIMD = false;
        }
        return null;
    }

    private static Renderer getJavaRenderer() {
        try {
            Class<?> class_ = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
            Class<?> class_2 = Class.forName("com.sun.glass.ui.Screen");
            Method method = class_.getMethod("createJSWInstance", class_2);
            Renderer renderer = (Renderer)method.invoke(null, new Object[]{null});
            if (renderer != null) {
                return renderer;
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private static Renderer getJavaRenderer(FilterContext filterContext) {
        try {
            Class<?> class_ = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
            Method method = class_.getMethod("createJSWInstance", FilterContext.class);
            Renderer renderer = (Renderer)method.invoke(null, filterContext);
            if (renderer != null) {
                return renderer;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return null;
    }

    static Renderer getSoftwareRenderer() {
        Renderer renderer = RendererFactory.getSSERenderer();
        if (renderer == null) {
            renderer = RendererFactory.getJavaRenderer();
        }
        return renderer;
    }

    static Renderer createRenderer(FilterContext filterContext) {
        return AccessController.doPrivileged(() -> {
            Renderer renderer = null;
            String string = filterContext.getClass().getName();
            String string2 = string.substring(string.lastIndexOf(".") + 1);
            if (string2.equals("PrFilterContext") && tryPrism) {
                renderer = RendererFactory.createPrismRenderer(filterContext);
            }
            if (renderer == null && tryRSL && RendererFactory.isRSLAvailable(filterContext)) {
                renderer = RendererFactory.createRSLRenderer(filterContext);
            }
            if (renderer == null && tryJOGL) {
                renderer = RendererFactory.createJOGLRenderer(filterContext);
            }
            if (renderer == null && trySIMD) {
                renderer = RendererFactory.getSSERenderer();
            }
            if (renderer == null) {
                renderer = RendererFactory.getJavaRenderer(filterContext);
            }
            return renderer;
        });
    }

    static {
        try {
            String string;
            if ("false".equals(System.getProperty("decora.rsl"))) {
                tryRSL = false;
            }
            if ("false".equals(System.getProperty("decora.simd"))) {
                trySIMD = false;
            }
            if ((string = System.getProperty("decora.jogl")) != null) {
                tryJOGL = Boolean.parseBoolean(string);
            }
            if ("false".equals(System.getProperty("decora.prism"))) {
                tryPrism = false;
            }
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
    }
}

