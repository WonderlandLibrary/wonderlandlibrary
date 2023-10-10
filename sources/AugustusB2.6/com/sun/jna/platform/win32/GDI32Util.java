// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.awt.image.WritableRaster;
import java.awt.image.DataBuffer;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.DataBufferInt;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;

public class GDI32Util
{
    private static final DirectColorModel SCREENSHOT_COLOR_MODEL;
    private static final int[] SCREENSHOT_BAND_MASKS;
    
    public static BufferedImage getScreenshot(final WinDef.HWND target) {
        final WinDef.RECT rect = new WinDef.RECT();
        if (!User32.INSTANCE.GetWindowRect(target, rect)) {
            throw new Win32Exception(Native.getLastError());
        }
        final Rectangle jRectangle = rect.toRectangle();
        final int windowWidth = jRectangle.width;
        final int windowHeight = jRectangle.height;
        if (windowWidth == 0 || windowHeight == 0) {
            throw new IllegalStateException("Window width and/or height were 0 even though GetWindowRect did not appear to fail.");
        }
        final WinDef.HDC hdcTarget = User32.INSTANCE.GetDC(target);
        if (hdcTarget == null) {
            throw new Win32Exception(Native.getLastError());
        }
        Win32Exception we = null;
        WinDef.HDC hdcTargetMem = null;
        WinDef.HBITMAP hBitmap = null;
        WinNT.HANDLE hOriginal = null;
        BufferedImage image = null;
        try {
            hdcTargetMem = GDI32.INSTANCE.CreateCompatibleDC(hdcTarget);
            if (hdcTargetMem == null) {
                throw new Win32Exception(Native.getLastError());
            }
            hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcTarget, windowWidth, windowHeight);
            if (hBitmap == null) {
                throw new Win32Exception(Native.getLastError());
            }
            hOriginal = GDI32.INSTANCE.SelectObject(hdcTargetMem, hBitmap);
            if (hOriginal == null) {
                throw new Win32Exception(Native.getLastError());
            }
            if (!GDI32.INSTANCE.BitBlt(hdcTargetMem, 0, 0, windowWidth, windowHeight, hdcTarget, 0, 0, 13369376)) {
                throw new Win32Exception(Native.getLastError());
            }
            final WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
            bmi.bmiHeader.biWidth = windowWidth;
            bmi.bmiHeader.biHeight = -windowHeight;
            bmi.bmiHeader.biPlanes = 1;
            bmi.bmiHeader.biBitCount = 32;
            bmi.bmiHeader.biCompression = 0;
            final Memory buffer = new Memory(windowWidth * windowHeight * 4);
            final int resultOfDrawing = GDI32.INSTANCE.GetDIBits(hdcTarget, hBitmap, 0, windowHeight, buffer, bmi, 0);
            if (resultOfDrawing == 0 || resultOfDrawing == 87) {
                throw new Win32Exception(Native.getLastError());
            }
            final int bufferSize = windowWidth * windowHeight;
            final DataBuffer dataBuffer = new DataBufferInt(buffer.getIntArray(0L, bufferSize), bufferSize);
            final WritableRaster raster = Raster.createPackedRaster(dataBuffer, windowWidth, windowHeight, windowWidth, GDI32Util.SCREENSHOT_BAND_MASKS, null);
            image = new BufferedImage(GDI32Util.SCREENSHOT_COLOR_MODEL, raster, false, null);
        }
        catch (Win32Exception e) {
            we = e;
        }
        finally {
            if (hOriginal != null) {
                final WinNT.HANDLE result = GDI32.INSTANCE.SelectObject(hdcTargetMem, hOriginal);
                if (result == null || WinGDI.HGDI_ERROR.equals(result)) {
                    final Win32Exception ex = new Win32Exception(Native.getLastError());
                    if (we != null) {
                        ex.addSuppressedReflected(we);
                    }
                    we = ex;
                }
            }
            if (hBitmap != null && !GDI32.INSTANCE.DeleteObject(hBitmap)) {
                final Win32Exception ex2 = new Win32Exception(Native.getLastError());
                if (we != null) {
                    ex2.addSuppressedReflected(we);
                }
                we = ex2;
            }
            if (hdcTargetMem != null && !GDI32.INSTANCE.DeleteDC(hdcTargetMem)) {
                final Win32Exception ex2 = new Win32Exception(Native.getLastError());
                if (we != null) {
                    ex2.addSuppressedReflected(we);
                }
                we = ex2;
            }
            if (hdcTarget != null && 0 == User32.INSTANCE.ReleaseDC(target, hdcTarget)) {
                throw new IllegalStateException("Device context did not release properly.");
            }
        }
        if (we != null) {
            throw we;
        }
        return image;
    }
    
    static {
        SCREENSHOT_COLOR_MODEL = new DirectColorModel(24, 16711680, 65280, 255);
        SCREENSHOT_BAND_MASKS = new int[] { GDI32Util.SCREENSHOT_COLOR_MODEL.getRedMask(), GDI32Util.SCREENSHOT_COLOR_MODEL.getGreenMask(), GDI32Util.SCREENSHOT_COLOR_MODEL.getBlueMask() };
    }
}
