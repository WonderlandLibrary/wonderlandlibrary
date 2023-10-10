// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import java.awt.Rectangle;
import com.sun.jna.platform.win32.WinDef;

public class DesktopWindow
{
    private WinDef.HWND hwnd;
    private String title;
    private String filePath;
    private Rectangle locAndSize;
    
    public DesktopWindow(final WinDef.HWND hwnd, final String title, final String filePath, final Rectangle locAndSize) {
        this.hwnd = hwnd;
        this.title = title;
        this.filePath = filePath;
        this.locAndSize = locAndSize;
    }
    
    public WinDef.HWND getHWND() {
        return this.hwnd;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getFilePath() {
        return this.filePath;
    }
    
    public Rectangle getLocAndSize() {
        return this.locAndSize;
    }
}
