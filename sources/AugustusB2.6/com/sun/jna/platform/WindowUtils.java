// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import java.awt.Dialog;
import java.awt.Frame;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.awt.Point;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Iterator;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.PsapiUtil;
import com.sun.jna.ptr.IntByReference;
import java.util.Arrays;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Kernel32;
import java.util.LinkedList;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import java.awt.event.ContainerEvent;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import java.awt.Container;
import javax.swing.JRootPane;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Area;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.Raster;
import java.awt.event.MouseEvent;
import java.awt.AWTEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.Graphics;
import java.awt.event.AWTEventListener;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.ComponentListener;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;
import java.awt.Rectangle;
import java.util.List;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import com.sun.jna.platform.win32.WinDef;
import java.awt.GraphicsConfiguration;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Window;
import java.awt.Shape;
import java.util.logging.Logger;

public class WindowUtils
{
    private static final Logger LOG;
    private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
    private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
    private static final String TRANSPARENT_ALPHA = "transparent-alpha";
    public static final Shape MASK_NONE;
    
    private static NativeWindowUtils getInstance() {
        return Holder.INSTANCE;
    }
    
    public static void setWindowMask(final Window w, final Shape mask) {
        getInstance().setWindowMask(w, mask);
    }
    
    public static void setComponentMask(final Component c, final Shape mask) {
        getInstance().setWindowMask(c, mask);
    }
    
    public static void setWindowMask(final Window w, final Icon mask) {
        getInstance().setWindowMask(w, mask);
    }
    
    public static boolean isWindowAlphaSupported() {
        return getInstance().isWindowAlphaSupported();
    }
    
    public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
        return getInstance().getAlphaCompatibleGraphicsConfiguration();
    }
    
    public static void setWindowAlpha(final Window w, final float alpha) {
        getInstance().setWindowAlpha(w, Math.max(0.0f, Math.min(alpha, 1.0f)));
    }
    
    public static void setWindowTransparent(final Window w, final boolean transparent) {
        getInstance().setWindowTransparent(w, transparent);
    }
    
    public static BufferedImage getWindowIcon(final WinDef.HWND hwnd) {
        return getInstance().getWindowIcon(hwnd);
    }
    
    public static Dimension getIconSize(final WinDef.HICON hIcon) {
        return getInstance().getIconSize(hIcon);
    }
    
    public static List<DesktopWindow> getAllWindows(final boolean onlyVisibleWindows) {
        return getInstance().getAllWindows(onlyVisibleWindows);
    }
    
    public static String getWindowTitle(final WinDef.HWND hwnd) {
        return getInstance().getWindowTitle(hwnd);
    }
    
    public static String getProcessFilePath(final WinDef.HWND hwnd) {
        return getInstance().getProcessFilePath(hwnd);
    }
    
    public static Rectangle getWindowLocationAndSize(final WinDef.HWND hwnd) {
        return getInstance().getWindowLocationAndSize(hwnd);
    }
    
    static {
        LOG = Logger.getLogger(WindowUtils.class.getName());
        MASK_NONE = null;
    }
    
    private static class HeavyweightForcer extends Window
    {
        private static final long serialVersionUID = 1L;
        private final boolean packed;
        
        public HeavyweightForcer(final Window parent) {
            super(parent);
            this.pack();
            this.packed = true;
        }
        
        @Override
        public boolean isVisible() {
            return this.packed;
        }
        
        @Override
        public Rectangle getBounds() {
            return this.getOwner().getBounds();
        }
    }
    
    protected static class RepaintTrigger extends JComponent
    {
        private static final long serialVersionUID = 1L;
        private final Listener listener;
        private final JComponent content;
        private Rectangle dirty;
        
        public RepaintTrigger(final JComponent content) {
            this.listener = this.createListener();
            this.content = content;
        }
        
        @Override
        public void addNotify() {
            super.addNotify();
            final Window w = SwingUtilities.getWindowAncestor(this);
            this.setSize(this.getParent().getSize());
            w.addComponentListener(this.listener);
            w.addWindowListener(this.listener);
            Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 48L);
        }
        
        @Override
        public void removeNotify() {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
            final Window w = SwingUtilities.getWindowAncestor(this);
            w.removeComponentListener(this.listener);
            w.removeWindowListener(this.listener);
            super.removeNotify();
        }
        
        @Override
        protected void paintComponent(final Graphics g) {
            final Rectangle bounds = g.getClipBounds();
            if (this.dirty == null || !this.dirty.contains(bounds)) {
                if (this.dirty == null) {
                    this.dirty = bounds;
                }
                else {
                    this.dirty = this.dirty.union(bounds);
                }
                this.content.repaint(this.dirty);
            }
            else {
                this.dirty = null;
            }
        }
        
        protected Listener createListener() {
            return new Listener();
        }
        
        protected class Listener extends WindowAdapter implements ComponentListener, HierarchyListener, AWTEventListener
        {
            @Override
            public void windowOpened(final WindowEvent e) {
                RepaintTrigger.this.repaint();
            }
            
            @Override
            public void componentHidden(final ComponentEvent e) {
            }
            
            @Override
            public void componentMoved(final ComponentEvent e) {
            }
            
            @Override
            public void componentResized(final ComponentEvent e) {
                RepaintTrigger.this.setSize(RepaintTrigger.this.getParent().getSize());
                RepaintTrigger.this.repaint();
            }
            
            @Override
            public void componentShown(final ComponentEvent e) {
                RepaintTrigger.this.repaint();
            }
            
            @Override
            public void hierarchyChanged(final HierarchyEvent e) {
                RepaintTrigger.this.repaint();
            }
            
            @Override
            public void eventDispatched(final AWTEvent e) {
                if (e instanceof MouseEvent) {
                    final Component src = ((MouseEvent)e).getComponent();
                    if (src != null && SwingUtilities.isDescendingFrom(src, RepaintTrigger.this.content)) {
                        final MouseEvent me = SwingUtilities.convertMouseEvent(src, (MouseEvent)e, RepaintTrigger.this.content);
                        final Component c = SwingUtilities.getDeepestComponentAt(RepaintTrigger.this.content, me.getX(), me.getY());
                        if (c != null) {
                            RepaintTrigger.this.setCursor(c.getCursor());
                        }
                    }
                }
            }
        }
    }
    
    public abstract static class NativeWindowUtils
    {
        protected Window getWindow(final Component c) {
            return (Window)((c instanceof Window) ? c : SwingUtilities.getWindowAncestor(c));
        }
        
        protected void whenDisplayable(final Component w, final Runnable action) {
            if (w.isDisplayable() && (!Holder.requiresVisible || w.isVisible())) {
                action.run();
            }
            else if (Holder.requiresVisible) {
                this.getWindow(w).addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(final WindowEvent e) {
                        e.getWindow().removeWindowListener(this);
                        action.run();
                    }
                    
                    @Override
                    public void windowClosed(final WindowEvent e) {
                        e.getWindow().removeWindowListener(this);
                    }
                });
            }
            else {
                w.addHierarchyListener(new HierarchyListener() {
                    @Override
                    public void hierarchyChanged(final HierarchyEvent e) {
                        if ((e.getChangeFlags() & 0x2L) != 0x0L && e.getComponent().isDisplayable()) {
                            e.getComponent().removeHierarchyListener(this);
                            action.run();
                        }
                    }
                });
            }
        }
        
        protected Raster toRaster(final Shape mask) {
            Raster raster = null;
            if (mask != WindowUtils.MASK_NONE) {
                final Rectangle bounds = mask.getBounds();
                if (bounds.width > 0 && bounds.height > 0) {
                    final BufferedImage clip = new BufferedImage(bounds.x + bounds.width, bounds.y + bounds.height, 12);
                    final Graphics2D g = clip.createGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0, 0, bounds.x + bounds.width, bounds.y + bounds.height);
                    g.setColor(Color.white);
                    g.fill(mask);
                    raster = clip.getRaster();
                }
            }
            return raster;
        }
        
        protected Raster toRaster(final Component c, final Icon mask) {
            Raster raster = null;
            if (mask != null) {
                final Rectangle bounds = new Rectangle(0, 0, mask.getIconWidth(), mask.getIconHeight());
                final BufferedImage clip = new BufferedImage(bounds.width, bounds.height, 2);
                final Graphics2D g = clip.createGraphics();
                g.setComposite(AlphaComposite.Clear);
                g.fillRect(0, 0, bounds.width, bounds.height);
                g.setComposite(AlphaComposite.SrcOver);
                mask.paintIcon(c, g, 0, 0);
                raster = clip.getAlphaRaster();
            }
            return raster;
        }
        
        protected Shape toShape(final Raster raster) {
            final Area area = new Area(new Rectangle(0, 0, 0, 0));
            RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
                @Override
                public boolean outputRange(final int x, final int y, final int w, final int h) {
                    area.add(new Area(new Rectangle(x, y, w, h)));
                    return true;
                }
            });
            return area;
        }
        
        public void setWindowAlpha(final Window w, final float alpha) {
        }
        
        public boolean isWindowAlphaSupported() {
            return false;
        }
        
        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice dev = env.getDefaultScreenDevice();
            return dev.getDefaultConfiguration();
        }
        
        public void setWindowTransparent(final Window w, final boolean transparent) {
        }
        
        protected void setDoubleBuffered(final Component root, final boolean buffered) {
            if (root instanceof JComponent) {
                ((JComponent)root).setDoubleBuffered(buffered);
            }
            if (root instanceof JRootPane && buffered) {
                ((JRootPane)root).setDoubleBuffered(true);
            }
            else if (root instanceof Container) {
                final Component[] kids = ((Container)root).getComponents();
                for (int i = 0; i < kids.length; ++i) {
                    this.setDoubleBuffered(kids[i], buffered);
                }
            }
        }
        
        protected void setLayersTransparent(final Window w, final boolean transparent) {
            Color bg = transparent ? new Color(0, 0, 0, 0) : null;
            if (w instanceof RootPaneContainer) {
                final RootPaneContainer rpc = (RootPaneContainer)w;
                final JRootPane root = rpc.getRootPane();
                final JLayeredPane lp = root.getLayeredPane();
                final Container c = root.getContentPane();
                final JComponent content = (c instanceof JComponent) ? ((JComponent)c) : null;
                if (transparent) {
                    lp.putClientProperty("transparent-old-opaque", lp.isOpaque());
                    lp.setOpaque(false);
                    root.putClientProperty("transparent-old-opaque", root.isOpaque());
                    root.setOpaque(false);
                    if (content != null) {
                        content.putClientProperty("transparent-old-opaque", content.isOpaque());
                        content.setOpaque(false);
                    }
                    root.putClientProperty("transparent-old-bg", root.getParent().getBackground());
                }
                else {
                    lp.setOpaque(Boolean.TRUE.equals(lp.getClientProperty("transparent-old-opaque")));
                    lp.putClientProperty("transparent-old-opaque", null);
                    root.setOpaque(Boolean.TRUE.equals(root.getClientProperty("transparent-old-opaque")));
                    root.putClientProperty("transparent-old-opaque", null);
                    if (content != null) {
                        content.setOpaque(Boolean.TRUE.equals(content.getClientProperty("transparent-old-opaque")));
                        content.putClientProperty("transparent-old-opaque", null);
                    }
                    bg = (Color)root.getClientProperty("transparent-old-bg");
                    root.putClientProperty("transparent-old-bg", null);
                }
            }
            w.setBackground(bg);
        }
        
        protected void setMask(final Component c, final Raster raster) {
            throw new UnsupportedOperationException("Window masking is not available");
        }
        
        protected void setWindowMask(final Component w, final Raster raster) {
            if (w.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight: " + w);
            }
            this.setMask(w, raster);
        }
        
        public void setWindowMask(final Component w, final Shape mask) {
            this.setWindowMask(w, this.toRaster(mask));
        }
        
        public void setWindowMask(final Component w, final Icon mask) {
            this.setWindowMask(w, this.toRaster(w, mask));
        }
        
        protected void setForceHeavyweightPopups(final Window w, final boolean force) {
            if (!(w instanceof HeavyweightForcer)) {
                final Window[] owned = w.getOwnedWindows();
                for (int i = 0; i < owned.length; ++i) {
                    if (owned[i] instanceof HeavyweightForcer) {
                        if (force) {
                            return;
                        }
                        owned[i].dispose();
                    }
                }
                final Boolean b = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
                if (force && b) {
                    new HeavyweightForcer(w);
                }
            }
        }
        
        protected BufferedImage getWindowIcon(final WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected Dimension getIconSize(final WinDef.HICON hIcon) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected List<DesktopWindow> getAllWindows(final boolean onlyVisibleWindows) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected String getWindowTitle(final WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected String getProcessFilePath(final WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected Rectangle getWindowLocationAndSize(final WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }
        
        protected abstract class TransparentContentPane extends JPanel implements AWTEventListener
        {
            private static final long serialVersionUID = 1L;
            private boolean transparent;
            
            public TransparentContentPane(final Container oldContent) {
                super(new BorderLayout());
                this.add(oldContent, "Center");
                this.setTransparent(true);
                if (oldContent instanceof JPanel) {
                    ((JComponent)oldContent).setOpaque(false);
                }
            }
            
            @Override
            public void addNotify() {
                super.addNotify();
                Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
            }
            
            @Override
            public void removeNotify() {
                Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                super.removeNotify();
            }
            
            public void setTransparent(final boolean transparent) {
                this.transparent = transparent;
                this.setOpaque(!transparent);
                this.setDoubleBuffered(!transparent);
                this.repaint();
            }
            
            @Override
            public void eventDispatched(final AWTEvent e) {
                if (e.getID() == 300 && SwingUtilities.isDescendingFrom(((ContainerEvent)e).getChild(), this)) {
                    final Component child = ((ContainerEvent)e).getChild();
                    NativeWindowUtils.this.setDoubleBuffered(child, false);
                }
            }
            
            @Override
            public void paint(final Graphics gr) {
                if (this.transparent) {
                    final Rectangle r = gr.getClipBounds();
                    final int w = r.width;
                    final int h = r.height;
                    if (this.getWidth() > 0 && this.getHeight() > 0) {
                        final BufferedImage buf = new BufferedImage(w, h, 3);
                        Graphics2D g = buf.createGraphics();
                        g.setComposite(AlphaComposite.Clear);
                        g.fillRect(0, 0, w, h);
                        g.dispose();
                        g = buf.createGraphics();
                        g.translate(-r.x, -r.y);
                        super.paint(g);
                        g.dispose();
                        this.paintDirect(buf, r);
                    }
                }
                else {
                    super.paint(gr);
                }
            }
            
            protected abstract void paintDirect(final BufferedImage p0, final Rectangle p1);
        }
    }
    
    private static class Holder
    {
        public static boolean requiresVisible;
        public static final NativeWindowUtils INSTANCE;
        
        static {
            if (Platform.isWindows()) {
                INSTANCE = new W32WindowUtils();
            }
            else if (Platform.isMac()) {
                INSTANCE = new MacWindowUtils();
            }
            else {
                if (!Platform.isX11()) {
                    final String os = System.getProperty("os.name");
                    throw new UnsupportedOperationException("No support for " + os);
                }
                INSTANCE = new X11WindowUtils();
                Holder.requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
            }
        }
    }
    
    private static class W32WindowUtils extends NativeWindowUtils
    {
        private WinDef.HWND getHWnd(final Component w) {
            final WinDef.HWND hwnd = new WinDef.HWND();
            hwnd.setPointer(Native.getComponentPointer(w));
            return hwnd;
        }
        
        @Override
        public boolean isWindowAlphaSupported() {
            return Boolean.getBoolean("sun.java2d.noddraw");
        }
        
        private boolean usingUpdateLayeredWindow(final Window w) {
            if (w instanceof RootPaneContainer) {
                final JRootPane root = ((RootPaneContainer)w).getRootPane();
                return root.getClientProperty("transparent-old-bg") != null;
            }
            return false;
        }
        
        private void storeAlpha(final Window w, final byte alpha) {
            if (w instanceof RootPaneContainer) {
                final JRootPane root = ((RootPaneContainer)w).getRootPane();
                final Byte b = (alpha == -1) ? null : Byte.valueOf(alpha);
                root.putClientProperty("transparent-alpha", b);
            }
        }
        
        private byte getAlpha(final Window w) {
            if (w instanceof RootPaneContainer) {
                final JRootPane root = ((RootPaneContainer)w).getRootPane();
                final Byte b = (Byte)root.getClientProperty("transparent-alpha");
                if (b != null) {
                    return b;
                }
            }
            return -1;
        }
        
        @Override
        public void setWindowAlpha(final Window w, final float alpha) {
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            this.whenDisplayable(w, new Runnable() {
                @Override
                public void run() {
                    final WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w);
                    final User32 user = User32.INSTANCE;
                    int flags = user.GetWindowLong(hWnd, -20);
                    final byte level = (byte)((int)(255.0f * alpha) & 0xFF);
                    if (W32WindowUtils.this.usingUpdateLayeredWindow(w)) {
                        final WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
                        blend.SourceConstantAlpha = level;
                        blend.AlphaFormat = 1;
                        user.UpdateLayeredWindow(hWnd, null, null, null, null, null, 0, blend, 2);
                    }
                    else if (alpha == 1.0f) {
                        flags &= 0xFFF7FFFF;
                        user.SetWindowLong(hWnd, -20, flags);
                    }
                    else {
                        flags |= 0x80000;
                        user.SetWindowLong(hWnd, -20, flags);
                        user.SetLayeredWindowAttributes(hWnd, 0, level, 2);
                    }
                    W32WindowUtils.this.setForceHeavyweightPopups(w, alpha != 1.0f);
                    W32WindowUtils.this.storeAlpha(w, level);
                }
            });
        }
        
        @Override
        public void setWindowTransparent(final Window w, final boolean transparent) {
            if (!(w instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            final boolean isTransparent = w.getBackground() != null && w.getBackground().getAlpha() == 0;
            if (transparent == isTransparent) {
                return;
            }
            this.whenDisplayable(w, new Runnable() {
                @Override
                public void run() {
                    final User32 user = User32.INSTANCE;
                    final WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w);
                    int flags = user.GetWindowLong(hWnd, -20);
                    final JRootPane root = ((RootPaneContainer)w).getRootPane();
                    final JLayeredPane lp = root.getLayeredPane();
                    final Container content = root.getContentPane();
                    if (content instanceof W32TransparentContentPane) {
                        ((W32TransparentContentPane)content).setTransparent(transparent);
                    }
                    else if (transparent) {
                        final W32TransparentContentPane w32content = new W32TransparentContentPane(content);
                        root.setContentPane(w32content);
                        lp.add(new RepaintTrigger(w32content), JLayeredPane.DRAG_LAYER);
                    }
                    if (transparent && !W32WindowUtils.this.usingUpdateLayeredWindow(w)) {
                        flags |= 0x80000;
                        user.SetWindowLong(hWnd, -20, flags);
                    }
                    else if (!transparent && W32WindowUtils.this.usingUpdateLayeredWindow(w)) {
                        flags &= 0xFFF7FFFF;
                        user.SetWindowLong(hWnd, -20, flags);
                    }
                    W32WindowUtils.this.setLayersTransparent(w, transparent);
                    W32WindowUtils.this.setForceHeavyweightPopups(w, transparent);
                    W32WindowUtils.this.setDoubleBuffered(w, !transparent);
                }
            });
        }
        
        @Override
        public void setWindowMask(final Component w, final Shape mask) {
            if (mask instanceof Area && ((Area)mask).isPolygonal()) {
                this.setMask(w, (Area)mask);
            }
            else {
                super.setWindowMask(w, mask);
            }
        }
        
        private void setWindowRegion(final Component w, final WinDef.HRGN hrgn) {
            this.whenDisplayable(w, new Runnable() {
                @Override
                public void run() {
                    final GDI32 gdi = GDI32.INSTANCE;
                    final User32 user = User32.INSTANCE;
                    final WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w);
                    try {
                        user.SetWindowRgn(hWnd, hrgn, true);
                        W32WindowUtils.this.setForceHeavyweightPopups(W32WindowUtils.this.getWindow(w), hrgn != null);
                    }
                    finally {
                        gdi.DeleteObject(hrgn);
                    }
                }
            });
        }
        
        private void setMask(final Component w, final Area area) {
            final GDI32 gdi = GDI32.INSTANCE;
            final PathIterator pi = area.getPathIterator(null);
            final int mode = (pi.getWindingRule() == 1) ? 2 : 1;
            final float[] coords = new float[6];
            final List<WinDef.POINT> points = new ArrayList<WinDef.POINT>();
            int size = 0;
            final List<Integer> sizes = new ArrayList<Integer>();
            while (!pi.isDone()) {
                final int type = pi.currentSegment(coords);
                if (type == 0) {
                    size = 1;
                    points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
                }
                else if (type == 1) {
                    ++size;
                    points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
                }
                else {
                    if (type != 4) {
                        throw new RuntimeException("Area is not polygonal: " + area);
                    }
                    sizes.add(size);
                }
                pi.next();
            }
            final WinDef.POINT[] lppt = (WinDef.POINT[])new WinDef.POINT().toArray(points.size());
            final WinDef.POINT[] pts = points.toArray(new WinDef.POINT[points.size()]);
            for (int i = 0; i < lppt.length; ++i) {
                lppt[i].x = pts[i].x;
                lppt[i].y = pts[i].y;
            }
            final int[] counts = new int[sizes.size()];
            for (int j = 0; j < counts.length; ++j) {
                counts[j] = sizes.get(j);
            }
            final WinDef.HRGN hrgn = gdi.CreatePolyPolygonRgn(lppt, counts, counts.length, mode);
            this.setWindowRegion(w, hrgn);
        }
        
        @Override
        protected void setMask(final Component w, final Raster raster) {
            final GDI32 gdi = GDI32.INSTANCE;
            final WinDef.HRGN region = (raster != null) ? gdi.CreateRectRgn(0, 0, 0, 0) : null;
            if (region != null) {
                final WinDef.HRGN tempRgn = gdi.CreateRectRgn(0, 0, 0, 0);
                try {
                    RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
                        @Override
                        public boolean outputRange(final int x, final int y, final int w, final int h) {
                            final GDI32 gdi = GDI32.INSTANCE;
                            gdi.SetRectRgn(tempRgn, x, y, x + w, y + h);
                            return gdi.CombineRgn(region, region, tempRgn, 2) != 0;
                        }
                    });
                }
                finally {
                    gdi.DeleteObject(tempRgn);
                }
            }
            this.setWindowRegion(w, region);
        }
        
        public BufferedImage getWindowIcon(final WinDef.HWND hwnd) {
            final WinDef.DWORDByReference hIconNumber = new WinDef.DWORDByReference();
            WinDef.LRESULT result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(1L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            if (result.intValue() == 0) {
                result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(0L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            }
            if (result.intValue() == 0) {
                result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(2L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            }
            if (result.intValue() == 0) {
                result = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr(hwnd, -14).intValue());
                hIconNumber.getValue().setValue(result.intValue());
            }
            if (result.intValue() == 0) {
                result = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr(hwnd, -34).intValue());
                hIconNumber.getValue().setValue(result.intValue());
            }
            if (result.intValue() == 0) {
                return null;
            }
            final WinDef.HICON hIcon = new WinDef.HICON(new Pointer(hIconNumber.getValue().longValue()));
            final Dimension iconSize = this.getIconSize(hIcon);
            if (iconSize.width == 0 || iconSize.height == 0) {
                return null;
            }
            final int width = iconSize.width;
            final int height = iconSize.height;
            final short depth = 24;
            final byte[] lpBitsColor = new byte[width * height * 24 / 8];
            final Pointer lpBitsColorPtr = new Memory(lpBitsColor.length);
            final byte[] lpBitsMask = new byte[width * height * 24 / 8];
            final Pointer lpBitsMaskPtr = new Memory(lpBitsMask.length);
            final WinGDI.BITMAPINFO bitmapInfo = new WinGDI.BITMAPINFO();
            final WinGDI.BITMAPINFOHEADER hdr = new WinGDI.BITMAPINFOHEADER();
            bitmapInfo.bmiHeader = hdr;
            hdr.biWidth = width;
            hdr.biHeight = height;
            hdr.biPlanes = 1;
            hdr.biBitCount = 24;
            hdr.biCompression = 0;
            hdr.write();
            bitmapInfo.write();
            final WinDef.HDC hDC = User32.INSTANCE.GetDC(null);
            final WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
            User32.INSTANCE.GetIconInfo(hIcon, iconInfo);
            iconInfo.read();
            GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmColor, 0, height, lpBitsColorPtr, bitmapInfo, 0);
            lpBitsColorPtr.read(0L, lpBitsColor, 0, lpBitsColor.length);
            GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmMask, 0, height, lpBitsMaskPtr, bitmapInfo, 0);
            lpBitsMaskPtr.read(0L, lpBitsMask, 0, lpBitsMask.length);
            final BufferedImage image = new BufferedImage(width, height, 2);
            int x = 0;
            int y = height - 1;
            for (int i = 0; i < lpBitsColor.length; i += 3) {
                final int b = lpBitsColor[i] & 0xFF;
                final int g = lpBitsColor[i + 1] & 0xFF;
                final int r = lpBitsColor[i + 2] & 0xFF;
                final int a = 255 - lpBitsMask[i] & 0xFF;
                final int argb = a << 24 | r << 16 | g << 8 | b;
                image.setRGB(x, y, argb);
                x = (x + 1) % width;
                if (x == 0) {
                    --y;
                }
            }
            User32.INSTANCE.ReleaseDC(null, hDC);
            return image;
        }
        
        public Dimension getIconSize(final WinDef.HICON hIcon) {
            final WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
            try {
                if (!User32.INSTANCE.GetIconInfo(hIcon, iconInfo)) {
                    return new Dimension();
                }
                iconInfo.read();
                final WinGDI.BITMAP bmp = new WinGDI.BITMAP();
                if (iconInfo.hbmColor != null && iconInfo.hbmColor.getPointer() != Pointer.NULL) {
                    final int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmColor, bmp.size(), bmp.getPointer());
                    bmp.read();
                    if (nWrittenBytes > 0) {
                        return new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue());
                    }
                }
                else if (iconInfo.hbmMask != null && iconInfo.hbmMask.getPointer() != Pointer.NULL) {
                    final int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmMask, bmp.size(), bmp.getPointer());
                    bmp.read();
                    if (nWrittenBytes > 0) {
                        return new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue() / 2);
                    }
                }
            }
            finally {
                if (iconInfo.hbmColor != null && iconInfo.hbmColor.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iconInfo.hbmColor);
                }
                if (iconInfo.hbmMask != null && iconInfo.hbmMask.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iconInfo.hbmMask);
                }
            }
            return new Dimension();
        }
        
        public List<DesktopWindow> getAllWindows(final boolean onlyVisibleWindows) {
            final List<DesktopWindow> result = new LinkedList<DesktopWindow>();
            final WinUser.WNDENUMPROC lpEnumFunc = new WinUser.WNDENUMPROC() {
                @Override
                public boolean callback(final WinDef.HWND hwnd, final Pointer arg1) {
                    try {
                        final boolean visible = !onlyVisibleWindows || User32.INSTANCE.IsWindowVisible(hwnd);
                        if (visible) {
                            final String title = W32WindowUtils.this.getWindowTitle(hwnd);
                            final String filePath = W32WindowUtils.this.getProcessFilePath(hwnd);
                            final Rectangle locAndSize = W32WindowUtils.this.getWindowLocationAndSize(hwnd);
                            result.add(new DesktopWindow(hwnd, title, filePath, locAndSize));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            };
            if (!User32.INSTANCE.EnumWindows(lpEnumFunc, null)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return result;
        }
        
        public String getWindowTitle(final WinDef.HWND hwnd) {
            final int requiredLength = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
            final char[] title = new char[requiredLength];
            final int length = User32.INSTANCE.GetWindowText(hwnd, title, title.length);
            return Native.toString(Arrays.copyOfRange(title, 0, length));
        }
        
        public String getProcessFilePath(final WinDef.HWND hwnd) {
            final IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);
            WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(1024, false, pid.getValue());
            if (process == null) {
                if (Kernel32.INSTANCE.GetLastError() != 5) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                process = Kernel32.INSTANCE.OpenProcess(4096, false, pid.getValue());
                if (process == null) {
                    if (Kernel32.INSTANCE.GetLastError() != 5) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                    return "";
                }
            }
            try {
                final String processImagePath = PsapiUtil.GetProcessImageFileName(process);
                if (processImagePath.startsWith("\\Device\\Mup\\")) {
                    return "\\" + processImagePath.substring(11);
                }
                final char[] volumeUUID = new char[50];
                final WinNT.HANDLE h = Kernel32.INSTANCE.FindFirstVolume(volumeUUID, 50);
                if (h == null || h.equals(WinBase.INVALID_HANDLE_VALUE)) {
                    throw new Win32Exception(Native.getLastError());
                }
                Label_0223: {
                    break Label_0223;
                    try {
                        do {
                            final String volumePath = Native.toString(volumeUUID);
                            for (final String s : Kernel32Util.getVolumePathNamesForVolumeName(volumePath)) {
                                if (s.matches("[a-zA-Z]:\\\\")) {
                                    for (final String path : Kernel32Util.queryDosDevice(s.substring(0, 2), 1024)) {
                                        if (processImagePath.startsWith(path)) {
                                            return s + processImagePath.substring(path.length() + 1);
                                        }
                                    }
                                }
                            }
                        } while (Kernel32.INSTANCE.FindNextVolume(h, volumeUUID, 50));
                        if (Native.getLastError() != 18) {
                            throw new Win32Exception(Native.getLastError());
                        }
                    }
                    finally {
                        Kernel32.INSTANCE.FindVolumeClose(h);
                    }
                }
                return processImagePath;
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(process);
            }
        }
        
        public Rectangle getWindowLocationAndSize(final WinDef.HWND hwnd) {
            final WinDef.RECT lpRect = new WinDef.RECT();
            if (!User32.INSTANCE.GetWindowRect(hwnd, lpRect)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return new Rectangle(lpRect.left, lpRect.top, Math.abs(lpRect.right - lpRect.left), Math.abs(lpRect.bottom - lpRect.top));
        }
        
        private class W32TransparentContentPane extends TransparentContentPane
        {
            private static final long serialVersionUID = 1L;
            private WinDef.HDC memDC;
            private WinDef.HBITMAP hBitmap;
            private Pointer pbits;
            private Dimension bitmapSize;
            
            public W32TransparentContentPane(final Container content) {
                super(content);
            }
            
            private void disposeBackingStore() {
                final GDI32 gdi = GDI32.INSTANCE;
                if (this.hBitmap != null) {
                    gdi.DeleteObject(this.hBitmap);
                    this.hBitmap = null;
                }
                if (this.memDC != null) {
                    gdi.DeleteDC(this.memDC);
                    this.memDC = null;
                }
            }
            
            @Override
            public void removeNotify() {
                super.removeNotify();
                this.disposeBackingStore();
            }
            
            @Override
            public void setTransparent(final boolean transparent) {
                super.setTransparent(transparent);
                if (!transparent) {
                    this.disposeBackingStore();
                }
            }
            
            @Override
            protected void paintDirect(final BufferedImage buf, final Rectangle bounds) {
                final Window win = SwingUtilities.getWindowAncestor(this);
                final GDI32 gdi = GDI32.INSTANCE;
                final User32 user = User32.INSTANCE;
                final int x = bounds.x;
                final int y = bounds.y;
                final Point origin = SwingUtilities.convertPoint(this, x, y, win);
                final int w = bounds.width;
                final int h = bounds.height;
                final int ww = win.getWidth();
                final int wh = win.getHeight();
                final WinDef.HDC screenDC = user.GetDC(null);
                WinNT.HANDLE oldBitmap = null;
                try {
                    if (this.memDC == null) {
                        this.memDC = gdi.CreateCompatibleDC(screenDC);
                    }
                    if (this.hBitmap == null || !win.getSize().equals(this.bitmapSize)) {
                        if (this.hBitmap != null) {
                            gdi.DeleteObject(this.hBitmap);
                            this.hBitmap = null;
                        }
                        final WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
                        bmi.bmiHeader.biWidth = ww;
                        bmi.bmiHeader.biHeight = wh;
                        bmi.bmiHeader.biPlanes = 1;
                        bmi.bmiHeader.biBitCount = 32;
                        bmi.bmiHeader.biCompression = 0;
                        bmi.bmiHeader.biSizeImage = ww * wh * 4;
                        final PointerByReference ppbits = new PointerByReference();
                        this.hBitmap = gdi.CreateDIBSection(this.memDC, bmi, 0, ppbits, null, 0);
                        this.pbits = ppbits.getValue();
                        this.bitmapSize = new Dimension(ww, wh);
                    }
                    oldBitmap = gdi.SelectObject(this.memDC, this.hBitmap);
                    final Raster raster = buf.getData();
                    final int[] pixel = new int[4];
                    final int[] bits = new int[w];
                    for (int row = 0; row < h; ++row) {
                        for (int col = 0; col < w; ++col) {
                            raster.getPixel(col, row, pixel);
                            final int alpha = (pixel[3] & 0xFF) << 24;
                            final int red = pixel[2] & 0xFF;
                            final int green = (pixel[1] & 0xFF) << 8;
                            final int blue = (pixel[0] & 0xFF) << 16;
                            bits[col] = (alpha | red | green | blue);
                        }
                        final int v = wh - (origin.y + row) - 1;
                        this.pbits.write((v * ww + origin.x) * 4, bits, 0, bits.length);
                    }
                    final WinUser.SIZE winSize = new WinUser.SIZE();
                    winSize.cx = win.getWidth();
                    winSize.cy = win.getHeight();
                    final WinDef.POINT winLoc = new WinDef.POINT();
                    winLoc.x = win.getX();
                    winLoc.y = win.getY();
                    final WinDef.POINT srcLoc = new WinDef.POINT();
                    final WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
                    final WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(win);
                    final ByteByReference bref = new ByteByReference();
                    final IntByReference iref = new IntByReference();
                    byte level = W32WindowUtils.this.getAlpha(win);
                    try {
                        if (user.GetLayeredWindowAttributes(hWnd, null, bref, iref) && (iref.getValue() & 0x2) != 0x0) {
                            level = bref.getValue();
                        }
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                    blend.SourceConstantAlpha = level;
                    blend.AlphaFormat = 1;
                    user.UpdateLayeredWindow(hWnd, screenDC, winLoc, winSize, this.memDC, srcLoc, 0, blend, 2);
                }
                finally {
                    user.ReleaseDC(null, screenDC);
                    if (this.memDC != null && oldBitmap != null) {
                        gdi.SelectObject(this.memDC, oldBitmap);
                    }
                }
            }
        }
    }
    
    private static class MacWindowUtils extends NativeWindowUtils
    {
        private static final String WDRAG = "apple.awt.draggableWindowBackground";
        
        @Override
        public boolean isWindowAlphaSupported() {
            return true;
        }
        
        private OSXMaskingContentPane installMaskingPane(final Window w) {
            OSXMaskingContentPane content;
            if (w instanceof RootPaneContainer) {
                final RootPaneContainer rpc = (RootPaneContainer)w;
                final Container oldContent = rpc.getContentPane();
                if (oldContent instanceof OSXMaskingContentPane) {
                    content = (OSXMaskingContentPane)oldContent;
                }
                else {
                    content = new OSXMaskingContentPane(oldContent);
                    rpc.setContentPane(content);
                }
            }
            else {
                final Component oldContent2 = (w.getComponentCount() > 0) ? w.getComponent(0) : null;
                if (oldContent2 instanceof OSXMaskingContentPane) {
                    content = (OSXMaskingContentPane)oldContent2;
                }
                else {
                    content = new OSXMaskingContentPane(oldContent2);
                    w.add(content);
                }
            }
            return content;
        }
        
        @Override
        public void setWindowTransparent(final Window w, final boolean transparent) {
            final boolean isTransparent = w.getBackground() != null && w.getBackground().getAlpha() == 0;
            if (transparent != isTransparent) {
                this.setBackgroundTransparent(w, transparent, "setWindowTransparent");
            }
        }
        
        private void fixWindowDragging(final Window w, final String context) {
            if (w instanceof RootPaneContainer) {
                final JRootPane p = ((RootPaneContainer)w).getRootPane();
                final Boolean oldDraggable = (Boolean)p.getClientProperty("apple.awt.draggableWindowBackground");
                if (oldDraggable == null) {
                    p.putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
                    if (w.isDisplayable()) {
                        WindowUtils.LOG.log(Level.WARNING, "{0}(): To avoid content dragging, {1}() must be called before the window is realized, or apple.awt.draggableWindowBackground must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set apple.awt.draggableWindowBackground on the window''s root pane to Boolean.TRUE before calling {2}() to hide this message.", new Object[] { context, context, context });
                    }
                }
            }
        }
        
        @Override
        public void setWindowAlpha(final Window w, final float alpha) {
            if (w instanceof RootPaneContainer) {
                final JRootPane p = ((RootPaneContainer)w).getRootPane();
                p.putClientProperty("Window.alpha", alpha);
                this.fixWindowDragging(w, "setWindowAlpha");
            }
            this.whenDisplayable(w, new Runnable() {
                @Override
                public void run() {
                    try {
                        final Method getPeer = w.getClass().getMethod("getPeer", (Class<?>[])new Class[0]);
                        final Object peer = getPeer.invoke(w, new Object[0]);
                        final Method setAlpha = peer.getClass().getMethod("setAlpha", Float.TYPE);
                        setAlpha.invoke(peer, alpha);
                    }
                    catch (Exception ex) {}
                }
            });
        }
        
        @Override
        protected void setWindowMask(final Component w, final Raster raster) {
            if (raster != null) {
                this.setWindowMask(w, this.toShape(raster));
            }
            else {
                this.setWindowMask(w, new Rectangle(0, 0, w.getWidth(), w.getHeight()));
            }
        }
        
        @Override
        public void setWindowMask(final Component c, final Shape shape) {
            if (c instanceof Window) {
                final Window w = (Window)c;
                final OSXMaskingContentPane content = this.installMaskingPane(w);
                content.setMask(shape);
                this.setBackgroundTransparent(w, shape != WindowUtils.MASK_NONE, "setWindowMask");
            }
        }
        
        private void setBackgroundTransparent(final Window w, final boolean transparent, final String context) {
            final JRootPane rp = (w instanceof RootPaneContainer) ? ((RootPaneContainer)w).getRootPane() : null;
            if (transparent) {
                if (rp != null) {
                    rp.putClientProperty("transparent-old-bg", w.getBackground());
                }
                w.setBackground(new Color(0, 0, 0, 0));
            }
            else if (rp != null) {
                Color bg = (Color)rp.getClientProperty("transparent-old-bg");
                if (bg != null) {
                    bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
                }
                w.setBackground(bg);
                rp.putClientProperty("transparent-old-bg", null);
            }
            else {
                w.setBackground(null);
            }
            this.fixWindowDragging(w, context);
        }
        
        private static class OSXMaskingContentPane extends JPanel
        {
            private static final long serialVersionUID = 1L;
            private Shape shape;
            
            public OSXMaskingContentPane(final Component oldContent) {
                super(new BorderLayout());
                if (oldContent != null) {
                    this.add(oldContent, "Center");
                }
            }
            
            public void setMask(final Shape shape) {
                this.shape = shape;
                this.repaint();
            }
            
            @Override
            public void paint(final Graphics graphics) {
                Graphics2D g = (Graphics2D)graphics.create();
                g.setComposite(AlphaComposite.Clear);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.dispose();
                if (this.shape != null) {
                    g = (Graphics2D)graphics.create();
                    g.setClip(this.shape);
                    super.paint(g);
                    g.dispose();
                }
                else {
                    super.paint(graphics);
                }
            }
        }
    }
    
    private static class X11WindowUtils extends NativeWindowUtils
    {
        private boolean didCheck;
        private long[] alphaVisualIDs;
        private static final long OPAQUE = 4294967295L;
        private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";
        
        private X11WindowUtils() {
            this.alphaVisualIDs = new long[0];
        }
        
        private static X11.Pixmap createBitmap(final X11.Display dpy, final X11.Window win, final Raster raster) {
            final X11 x11 = X11.INSTANCE;
            final Rectangle bounds = raster.getBounds();
            final int width = bounds.x + bounds.width;
            final int height = bounds.y + bounds.height;
            final X11.Pixmap pm = x11.XCreatePixmap(dpy, win, width, height, 1);
            final X11.GC gc = x11.XCreateGC(dpy, pm, new NativeLong(0L), null);
            if (gc == null) {
                return null;
            }
            x11.XSetForeground(dpy, gc, new NativeLong(0L));
            x11.XFillRectangle(dpy, pm, gc, 0, 0, width, height);
            final List<Rectangle> rlist = new ArrayList<Rectangle>();
            try {
                RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
                    @Override
                    public boolean outputRange(final int x, final int y, final int w, final int h) {
                        rlist.add(new Rectangle(x, y, w, h));
                        return true;
                    }
                });
                final X11.XRectangle[] rects = (X11.XRectangle[])new X11.XRectangle().toArray(rlist.size());
                for (int i = 0; i < rects.length; ++i) {
                    final Rectangle r = rlist.get(i);
                    rects[i].x = (short)r.x;
                    rects[i].y = (short)r.y;
                    rects[i].width = (short)r.width;
                    rects[i].height = (short)r.height;
                    final Pointer p = rects[i].getPointer();
                    p.setShort(0L, (short)r.x);
                    p.setShort(2L, (short)r.y);
                    p.setShort(4L, (short)r.width);
                    p.setShort(6L, (short)r.height);
                    rects[i].setAutoSynch(false);
                }
                final int UNMASKED = 1;
                x11.XSetForeground(dpy, gc, new NativeLong(1L));
                x11.XFillRectangles(dpy, pm, gc, rects, rects.length);
            }
            finally {
                x11.XFreeGC(dpy, gc);
            }
            return pm;
        }
        
        @Override
        public boolean isWindowAlphaSupported() {
            return this.getAlphaVisualIDs().length > 0;
        }
        
        private static long getVisualID(final GraphicsConfiguration config) {
            try {
                final Object o = config.getClass().getMethod("getVisual", (Class<?>[])null).invoke(config, (Object[])null);
                return ((Number)o).longValue();
            }
            catch (Exception e) {
                e.printStackTrace();
                return -1L;
            }
        }
        
        @Override
        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            if (this.isWindowAlphaSupported()) {
                final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                final GraphicsDevice[] devices = env.getScreenDevices();
                for (int i = 0; i < devices.length; ++i) {
                    final GraphicsConfiguration[] configs = devices[i].getConfigurations();
                    for (int j = 0; j < configs.length; ++j) {
                        final long visualID = getVisualID(configs[j]);
                        final long[] ids = this.getAlphaVisualIDs();
                        for (int k = 0; k < ids.length; ++k) {
                            if (visualID == ids[k]) {
                                return configs[j];
                            }
                        }
                    }
                }
            }
            return super.getAlphaCompatibleGraphicsConfiguration();
        }
        
        private synchronized long[] getAlphaVisualIDs() {
            if (this.didCheck) {
                return this.alphaVisualIDs;
            }
            this.didCheck = true;
            final X11 x11 = X11.INSTANCE;
            final X11.Display dpy = x11.XOpenDisplay(null);
            if (dpy == null) {
                return this.alphaVisualIDs;
            }
            X11.XVisualInfo info = null;
            try {
                final int screen = x11.XDefaultScreen(dpy);
                final X11.XVisualInfo template = new X11.XVisualInfo();
                template.screen = screen;
                template.depth = 32;
                template.c_class = 4;
                final NativeLong mask = new NativeLong(14L);
                final IntByReference pcount = new IntByReference();
                info = x11.XGetVisualInfo(dpy, mask, template, pcount);
                if (info != null) {
                    final List<X11.VisualID> list = new ArrayList<X11.VisualID>();
                    final X11.XVisualInfo[] infos = (X11.XVisualInfo[])info.toArray(pcount.getValue());
                    for (int i = 0; i < infos.length; ++i) {
                        final X11.Xrender.XRenderPictFormat format = X11.Xrender.INSTANCE.XRenderFindVisualFormat(dpy, infos[i].visual);
                        if (format.type == 1 && format.direct.alphaMask != 0) {
                            list.add(infos[i].visualid);
                        }
                    }
                    this.alphaVisualIDs = new long[list.size()];
                    for (int i = 0; i < this.alphaVisualIDs.length; ++i) {
                        this.alphaVisualIDs[i] = list.get(i).longValue();
                    }
                    return this.alphaVisualIDs;
                }
            }
            finally {
                if (info != null) {
                    x11.XFree(info.getPointer());
                }
                x11.XCloseDisplay(dpy);
            }
            return this.alphaVisualIDs;
        }
        
        private static X11.Window getContentWindow(final Window w, final X11.Display dpy, X11.Window win, final Point offset) {
            if ((w instanceof Frame && !((Frame)w).isUndecorated()) || (w instanceof Dialog && !((Dialog)w).isUndecorated())) {
                final X11 x11 = X11.INSTANCE;
                final X11.WindowByReference rootp = new X11.WindowByReference();
                final X11.WindowByReference parentp = new X11.WindowByReference();
                final PointerByReference childrenp = new PointerByReference();
                final IntByReference countp = new IntByReference();
                x11.XQueryTree(dpy, win, rootp, parentp, childrenp, countp);
                final Pointer p = childrenp.getValue();
                final int[] intArray;
                final int[] ids = intArray = p.getIntArray(0L, countp.getValue());
                final int length = intArray.length;
                final int n = 0;
                if (n < length) {
                    final int id = intArray[n];
                    final X11.Window child = new X11.Window((long)id);
                    final X11.XWindowAttributes xwa = new X11.XWindowAttributes();
                    x11.XGetWindowAttributes(dpy, child, xwa);
                    offset.x = -xwa.x;
                    offset.y = -xwa.y;
                    win = child;
                }
                if (p != null) {
                    x11.XFree(p);
                }
            }
            return win;
        }
        
        private static X11.Window getDrawable(final Component w) {
            final int id = (int)Native.getComponentID(w);
            if (id == 0) {
                return null;
            }
            return new X11.Window((long)id);
        }
        
        @Override
        public void setWindowAlpha(final Window w, final float alpha) {
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            final Runnable action = new Runnable() {
                @Override
                public void run() {
                    final X11 x11 = X11.INSTANCE;
                    final X11.Display dpy = x11.XOpenDisplay(null);
                    if (dpy == null) {
                        return;
                    }
                    try {
                        final X11.Window win = getDrawable(w);
                        if (alpha == 1.0f) {
                            x11.XDeleteProperty(dpy, win, x11.XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false));
                        }
                        else {
                            final int opacity = (int)((long)(alpha * 4.2949673E9f) & -1L);
                            final IntByReference patom = new IntByReference(opacity);
                            x11.XChangeProperty(dpy, win, x11.XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false), X11.XA_CARDINAL, 32, 0, patom.getPointer(), 1);
                        }
                    }
                    finally {
                        x11.XCloseDisplay(dpy);
                    }
                }
            };
            this.whenDisplayable(w, action);
        }
        
        @Override
        public void setWindowTransparent(final Window w, final boolean transparent) {
            if (!(w instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            if (!w.getGraphicsConfiguration().equals(this.getAlphaCompatibleGraphicsConfiguration())) {
                throw new IllegalArgumentException("Window GraphicsConfiguration '" + w.getGraphicsConfiguration() + "' does not support transparency");
            }
            final boolean isTransparent = w.getBackground() != null && w.getBackground().getAlpha() == 0;
            if (transparent == isTransparent) {
                return;
            }
            this.whenDisplayable(w, new Runnable() {
                @Override
                public void run() {
                    final JRootPane root = ((RootPaneContainer)w).getRootPane();
                    final JLayeredPane lp = root.getLayeredPane();
                    final Container content = root.getContentPane();
                    if (content instanceof X11TransparentContentPane) {
                        ((X11TransparentContentPane)content).setTransparent(transparent);
                    }
                    else if (transparent) {
                        final X11TransparentContentPane x11content = new X11TransparentContentPane(content);
                        root.setContentPane(x11content);
                        lp.add(new RepaintTrigger(x11content), JLayeredPane.DRAG_LAYER);
                    }
                    X11WindowUtils.this.setLayersTransparent(w, transparent);
                    X11WindowUtils.this.setForceHeavyweightPopups(w, transparent);
                    X11WindowUtils.this.setDoubleBuffered(w, !transparent);
                }
            });
        }
        
        private void setWindowShape(final Window w, final PixmapSource src) {
            final Runnable action = new Runnable() {
                @Override
                public void run() {
                    final X11 x11 = X11.INSTANCE;
                    final X11.Display dpy = x11.XOpenDisplay(null);
                    if (dpy == null) {
                        return;
                    }
                    X11.Pixmap pm = null;
                    try {
                        final X11.Window win = getDrawable(w);
                        pm = src.getPixmap(dpy, win);
                        final X11.Xext ext = X11.Xext.INSTANCE;
                        ext.XShapeCombineMask(dpy, win, 0, 0, 0, (pm == null) ? X11.Pixmap.None : pm, 0);
                    }
                    finally {
                        if (pm != null) {
                            x11.XFreePixmap(dpy, pm);
                        }
                        x11.XCloseDisplay(dpy);
                    }
                    X11WindowUtils.this.setForceHeavyweightPopups(X11WindowUtils.this.getWindow(w), pm != null);
                }
            };
            this.whenDisplayable(w, action);
        }
        
        @Override
        protected void setMask(final Component w, final Raster raster) {
            this.setWindowShape(this.getWindow(w), new PixmapSource() {
                @Override
                public X11.Pixmap getPixmap(final X11.Display dpy, final X11.Window win) {
                    return (raster != null) ? createBitmap(dpy, win, raster) : null;
                }
            });
        }
        
        private class X11TransparentContentPane extends TransparentContentPane
        {
            private static final long serialVersionUID = 1L;
            private Memory buffer;
            private int[] pixels;
            private final int[] pixel;
            
            public X11TransparentContentPane(final Container oldContent) {
                super(oldContent);
                this.pixel = new int[4];
            }
            
            @Override
            protected void paintDirect(final BufferedImage buf, final Rectangle bounds) {
                final Window window = SwingUtilities.getWindowAncestor(this);
                final X11 x11 = X11.INSTANCE;
                final X11.Display dpy = x11.XOpenDisplay(null);
                X11.Window win = getDrawable(window);
                final Point offset = new Point();
                win = getContentWindow(window, dpy, win, offset);
                final X11.GC gc = x11.XCreateGC(dpy, win, new NativeLong(0L), null);
                final Raster raster = buf.getData();
                final int w = bounds.width;
                final int h = bounds.height;
                if (this.buffer == null || this.buffer.size() != w * h * 4) {
                    this.buffer = new Memory(w * h * 4);
                    this.pixels = new int[w * h];
                }
                for (int y = 0; y < h; ++y) {
                    for (int x12 = 0; x12 < w; ++x12) {
                        raster.getPixel(x12, y, this.pixel);
                        final int alpha = this.pixel[3] & 0xFF;
                        final int red = this.pixel[2] & 0xFF;
                        final int green = this.pixel[1] & 0xFF;
                        final int blue = this.pixel[0] & 0xFF;
                        this.pixels[y * w + x12] = (alpha << 24 | blue << 16 | green << 8 | red);
                    }
                }
                final X11.XWindowAttributes xwa = new X11.XWindowAttributes();
                x11.XGetWindowAttributes(dpy, win, xwa);
                final X11.XImage image = x11.XCreateImage(dpy, xwa.visual, 32, 2, 0, this.buffer, w, h, 32, w * 4);
                this.buffer.write(0L, this.pixels, 0, this.pixels.length);
                final Point point = offset;
                point.x += bounds.x;
                final Point point2 = offset;
                point2.y += bounds.y;
                x11.XPutImage(dpy, win, gc, image, 0, 0, offset.x, offset.y, w, h);
                x11.XFree(image.getPointer());
                x11.XFreeGC(dpy, gc);
                x11.XCloseDisplay(dpy);
            }
        }
        
        private interface PixmapSource
        {
            X11.Pixmap getPixmap(final X11.Display p0, final X11.Window p1);
        }
    }
}
