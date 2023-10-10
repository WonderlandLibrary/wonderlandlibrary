/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.DisplayMode;

public final class Display {
    private static final boolean DEBUG = false;

    public static DisplayMode[] getAvailableDisplayModes(int minWidth, int minHeight, int maxWidth, int maxHeight, int minBPP, int maxBPP, int minFreq, int maxFreq) throws LWJGLException {
        DisplayMode[] modes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
        if (LWJGLUtil.DEBUG) {
            System.out.println("Available screen modes:");
            for (DisplayMode mode : modes) {
                System.out.println(mode);
            }
        }
        ArrayList<DisplayMode> matches = new ArrayList<DisplayMode>(modes.length);
        for (int i2 = 0; i2 < modes.length; ++i2) {
            assert (modes[i2] != null) : "" + i2 + " " + modes.length;
            if (minWidth != -1 && modes[i2].getWidth() < minWidth || maxWidth != -1 && modes[i2].getWidth() > maxWidth || minHeight != -1 && modes[i2].getHeight() < minHeight || maxHeight != -1 && modes[i2].getHeight() > maxHeight || minBPP != -1 && modes[i2].getBitsPerPixel() < minBPP || maxBPP != -1 && modes[i2].getBitsPerPixel() > maxBPP || modes[i2].getFrequency() != 0 && (minFreq != -1 && modes[i2].getFrequency() < minFreq || maxFreq != -1 && modes[i2].getFrequency() > maxFreq)) continue;
            matches.add(modes[i2]);
        }
        DisplayMode[] ret = new DisplayMode[matches.size()];
        matches.toArray(ret);
        if (LWJGLUtil.DEBUG) {
            // empty if block
        }
        return ret;
    }

    public static DisplayMode setDisplayMode(DisplayMode[] dm, String[] param) throws Exception {
        /*
         * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
         */
        class Sorter
        implements Comparator<DisplayMode> {
            final 1FieldAccessor[] accessors;
            final /* synthetic */ String[] val$param;

            Sorter(String[] stringArray) {
                this.val$param = stringArray;
                class FieldAccessor {
                    final String fieldName;
                    final int order;
                    final int preferred;
                    final boolean usePreferred;

                    FieldAccessor(String fieldName, int order, int preferred, boolean usePreferred) {
                        this.fieldName = fieldName;
                        this.order = order;
                        this.preferred = preferred;
                        this.usePreferred = usePreferred;
                    }

                    int getInt(DisplayMode mode) {
                        if ("width".equals(this.fieldName)) {
                            return mode.getWidth();
                        }
                        if ("height".equals(this.fieldName)) {
                            return mode.getHeight();
                        }
                        if ("freq".equals(this.fieldName)) {
                            return mode.getFrequency();
                        }
                        if ("bpp".equals(this.fieldName)) {
                            return mode.getBitsPerPixel();
                        }
                        throw new IllegalArgumentException("Unknown field " + this.fieldName);
                    }
                }
                this.accessors = new FieldAccessor[this.val$param.length];
                for (int i2 = 0; i2 < this.accessors.length; ++i2) {
                    int idx = this.val$param[i2].indexOf(61);
                    this.accessors[i2] = idx > 0 ? new FieldAccessor(this.val$param[i2].substring(0, idx), 0, Integer.parseInt(this.val$param[i2].substring(idx + 1, this.val$param[i2].length())), true) : (this.val$param[i2].charAt(0) == '-' ? new FieldAccessor(this.val$param[i2].substring(1), -1, 0, false) : new FieldAccessor(this.val$param[i2], 1, 0, false));
                }
            }

            @Override
            public int compare(DisplayMode dm1, DisplayMode dm2) {
                for (FieldAccessor accessor : this.accessors) {
                    int f1 = accessor.getInt(dm1);
                    int f2 = accessor.getInt(dm2);
                    if (accessor.usePreferred && f1 != f2) {
                        int absf2;
                        if (f1 == accessor.preferred) {
                            return -1;
                        }
                        if (f2 == accessor.preferred) {
                            return 1;
                        }
                        int absf1 = Math.abs(f1 - accessor.preferred);
                        if (absf1 < (absf2 = Math.abs(f2 - accessor.preferred))) {
                            return -1;
                        }
                        if (absf1 <= absf2) continue;
                        return 1;
                    }
                    if (f1 < f2) {
                        return accessor.order;
                    }
                    if (f1 == f2) continue;
                    return -accessor.order;
                }
                return 0;
            }
        }
        Arrays.sort(dm, new Sorter(param));
        if (LWJGLUtil.DEBUG) {
            System.out.println("Sorted display modes:");
            for (DisplayMode aDm : dm) {
                System.out.println(aDm);
            }
        }
        for (DisplayMode aDm : dm) {
            try {
                if (LWJGLUtil.DEBUG) {
                    System.out.println("Attempting to set displaymode: " + aDm);
                }
                org.lwjgl.opengl.Display.setDisplayMode(aDm);
                return aDm;
            }
            catch (Exception e2) {
                if (!LWJGLUtil.DEBUG) continue;
                System.out.println("Failed to set display mode to " + aDm);
                e2.printStackTrace();
            }
        }
        throw new Exception("Failed to set display mode.");
    }
}

