/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.directwrite.DWFontFile;
import com.sun.javafx.font.directwrite.DWGlyphLayout;
import com.sun.javafx.font.directwrite.ID2D1Factory;
import com.sun.javafx.font.directwrite.IDWriteFactory;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFile;
import com.sun.javafx.font.directwrite.IWICImagingFactory;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.text.GlyphLayout;
import com.sun.prism.GraphicsPipeline;

public class DWFactory
extends PrismFontFactory {
    private static IDWriteFactory DWRITE_FACTORY = null;
    private static IDWriteFontCollection FONT_COLLECTION = null;
    private static IWICImagingFactory WIC_FACTORY = null;
    private static ID2D1Factory D2D_FACTORY = null;
    private static Thread d2dThread;

    public static PrismFontFactory getFactory() {
        if (DWFactory.getDWriteFactory() == null) {
            return null;
        }
        return new DWFactory();
    }

    private DWFactory() {
    }

    @Override
    protected PrismFontFile createFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        return new DWFontFile(string, string2, n, bl, bl2, bl3, bl4);
    }

    @Override
    public GlyphLayout createGlyphLayout() {
        return new DWGlyphLayout();
    }

    @Override
    protected boolean registerEmbeddedFont(String string) {
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteFontFile iDWriteFontFile = iDWriteFactory.CreateFontFileReference(string);
        if (iDWriteFontFile == null) {
            return false;
        }
        boolean[] arrbl = new boolean[1];
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        int n = iDWriteFontFile.Analyze(arrbl, arrn, arrn2, arrn3);
        iDWriteFontFile.Release();
        if (n != 0) {
            return false;
        }
        return arrbl[0];
    }

    static IDWriteFactory getDWriteFactory() {
        if (DWRITE_FACTORY == null) {
            DWRITE_FACTORY = OS.DWriteCreateFactory(0);
        }
        return DWRITE_FACTORY;
    }

    static IDWriteFontCollection getFontCollection() {
        if (FONT_COLLECTION == null) {
            FONT_COLLECTION = DWFactory.getDWriteFactory().GetSystemFontCollection(false);
        }
        return FONT_COLLECTION;
    }

    private static void checkThread() {
        Thread thread = Thread.currentThread();
        if (d2dThread == null) {
            d2dThread = thread;
        }
        if (d2dThread != thread) {
            throw new IllegalStateException("This operation is not permitted on the current thread [" + thread.getName() + "]");
        }
    }

    static synchronized IWICImagingFactory getWICFactory() {
        DWFactory.checkThread();
        if (WIC_FACTORY == null) {
            if (!OS.CoInitializeEx(6)) {
                return null;
            }
            WIC_FACTORY = OS.WICCreateImagingFactory();
            if (WIC_FACTORY == null) {
                return null;
            }
            GraphicsPipeline.getPipeline().addDisposeHook(() -> {
                DWFactory.checkThread();
                WIC_FACTORY.Release();
                OS.CoUninitialize();
                WIC_FACTORY = null;
            });
        }
        return WIC_FACTORY;
    }

    static synchronized ID2D1Factory getD2DFactory() {
        DWFactory.checkThread();
        if (D2D_FACTORY == null) {
            D2D_FACTORY = OS.D2D1CreateFactory(0);
        }
        return D2D_FACTORY;
    }
}

