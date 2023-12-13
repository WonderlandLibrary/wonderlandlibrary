/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.dnd;

import com.sun.jna.platform.WindowUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GhostedDragImage {
    private static final float DEFAULT_ALPHA = 0.5f;
    private Window dragImage;
    private Point origin;
    private static final int SLIDE_INTERVAL = 33;

    public GhostedDragImage(Component dragSource, final Icon icon, Point initialScreenLoc, final Point cursorOffset) {
        Window parent = dragSource instanceof Window ? (Window)dragSource : SwingUtilities.getWindowAncestor(dragSource);
        GraphicsConfiguration gc = parent.getGraphicsConfiguration();
        this.dragImage = new Window(JOptionPane.getRootFrame(), gc){
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g2) {
                icon.paintIcon(this, g2, 0, 0);
            }

            public Dimension getPreferredSize() {
                return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }

            public Dimension getMinimumSize() {
                return this.getPreferredSize();
            }

            public Dimension getMaximumSize() {
                return this.getPreferredSize();
            }
        };
        this.dragImage.setFocusableWindowState(false);
        this.dragImage.setName("###overrideRedirect###");
        Icon dragIcon = new Icon(){

            public int getIconHeight() {
                return icon.getIconHeight();
            }

            public int getIconWidth() {
                return icon.getIconWidth();
            }

            public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
                g2 = g2.create();
                Area area = new Area(new Rectangle(x2, y2, this.getIconWidth(), this.getIconHeight()));
                area.subtract(new Area(new Rectangle(x2 + cursorOffset.x - 1, y2 + cursorOffset.y - 1, 3, 3)));
                g2.setClip(area);
                icon.paintIcon(c2, g2, x2, y2);
                g2.dispose();
            }
        };
        this.dragImage.pack();
        WindowUtils.setWindowMask(this.dragImage, dragIcon);
        WindowUtils.setWindowAlpha(this.dragImage, 0.5f);
        this.move(initialScreenLoc);
        this.dragImage.setVisible(true);
    }

    public void setAlpha(float alpha) {
        WindowUtils.setWindowAlpha(this.dragImage, alpha);
    }

    public void dispose() {
        this.dragImage.dispose();
        this.dragImage = null;
    }

    public void move(Point screenLocation) {
        if (this.origin == null) {
            this.origin = screenLocation;
        }
        this.dragImage.setLocation(screenLocation.x, screenLocation.y);
    }

    public void returnToOrigin() {
        final Timer timer = new Timer(33, null);
        timer.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e2) {
                Point location = GhostedDragImage.this.dragImage.getLocationOnScreen();
                Point dst = new Point(GhostedDragImage.this.origin);
                int dx = (dst.x - location.x) / 2;
                int dy = (dst.y - location.y) / 2;
                if (dx != 0 || dy != 0) {
                    location.translate(dx, dy);
                    GhostedDragImage.this.move(location);
                } else {
                    timer.stop();
                    GhostedDragImage.this.dispose();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}

