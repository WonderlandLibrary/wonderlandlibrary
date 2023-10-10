// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.mac;

import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.Library;

public interface CoreGraphics extends Library
{
    public static final CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);
    public static final int kCGNullWindowID = 0;
    public static final int kCGWindowListOptionAll = 0;
    public static final int kCGWindowListOptionOnScreenOnly = 1;
    public static final int kCGWindowListOptionOnScreenAboveWindow = 2;
    public static final int kCGWindowListOptionOnScreenBelowWindow = 4;
    public static final int kCGWindowListOptionIncludingWindow = 8;
    public static final int kCGWindowListExcludeDesktopElements = 16;
    
    CoreFoundation.CFArrayRef CGWindowListCopyWindowInfo(final int p0, final int p1);
    
    boolean CGRectMakeWithDictionaryRepresentation(final CoreFoundation.CFDictionaryRef p0, final CGRect p1);
    
    @FieldOrder({ "x", "y" })
    public static class CGPoint extends Structure
    {
        public double x;
        public double y;
    }
    
    @FieldOrder({ "width", "height" })
    public static class CGSize extends Structure
    {
        public double width;
        public double height;
    }
    
    @FieldOrder({ "origin", "size" })
    public static class CGRect extends Structure
    {
        public CGPoint origin;
        public CGSize size;
    }
}
