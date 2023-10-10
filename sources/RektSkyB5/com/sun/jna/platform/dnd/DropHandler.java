/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.dnd;

import com.sun.jna.platform.dnd.DragHandler;
import com.sun.jna.platform.dnd.DropTargetPainter;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class DropHandler
implements DropTargetListener {
    private int acceptedActions;
    private List<DataFlavor> acceptedFlavors;
    private DropTarget dropTarget;
    private boolean active = true;
    private DropTargetPainter painter;
    private String lastAction;

    public DropHandler(Component c2, int acceptedActions) {
        this(c2, acceptedActions, new DataFlavor[0]);
    }

    public DropHandler(Component c2, int acceptedActions, DataFlavor[] acceptedFlavors) {
        this(c2, acceptedActions, acceptedFlavors, null);
    }

    public DropHandler(Component c2, int acceptedActions, DataFlavor[] acceptedFlavors, DropTargetPainter painter) {
        this.acceptedActions = acceptedActions;
        this.acceptedFlavors = Arrays.asList(acceptedFlavors);
        this.painter = painter;
        this.dropTarget = new DropTarget(c2, acceptedActions, this, this.active);
    }

    protected DropTarget getDropTarget() {
        return this.dropTarget;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (this.dropTarget != null) {
            this.dropTarget.setActive(active);
        }
    }

    protected int getDropActionsForFlavors(DataFlavor[] dataFlavors) {
        return this.acceptedActions;
    }

    protected int getDropAction(DropTargetEvent e2) {
        int availableActions;
        DropTargetEvent ev;
        int currentAction = 0;
        int sourceActions = 0;
        Point location = null;
        DataFlavor[] flavors = new DataFlavor[]{};
        if (e2 instanceof DropTargetDragEvent) {
            ev = (DropTargetDragEvent)e2;
            currentAction = ((DropTargetDragEvent)ev).getDropAction();
            sourceActions = ((DropTargetDragEvent)ev).getSourceActions();
            flavors = ((DropTargetDragEvent)ev).getCurrentDataFlavors();
            location = ((DropTargetDragEvent)ev).getLocation();
        } else if (e2 instanceof DropTargetDropEvent) {
            ev = (DropTargetDropEvent)e2;
            currentAction = ((DropTargetDropEvent)ev).getDropAction();
            sourceActions = ((DropTargetDropEvent)ev).getSourceActions();
            flavors = ((DropTargetDropEvent)ev).getCurrentDataFlavors();
            location = ((DropTargetDropEvent)ev).getLocation();
        }
        if (this.isSupported(flavors) && (currentAction = this.getDropAction(e2, currentAction, sourceActions, availableActions = this.getDropActionsForFlavors(flavors))) != 0 && this.canDrop(e2, currentAction, location)) {
            return currentAction;
        }
        return 0;
    }

    protected int getDropAction(DropTargetEvent e2, int currentAction, int sourceActions, int acceptedActions) {
        int action;
        boolean modifiersActive = this.modifiersActive(currentAction);
        if ((currentAction & acceptedActions) == 0 && !modifiersActive) {
            int action2;
            currentAction = action2 = acceptedActions & sourceActions;
        } else if (modifiersActive && (action = currentAction & acceptedActions & sourceActions) != currentAction) {
            currentAction = action;
        }
        return currentAction;
    }

    protected boolean modifiersActive(int dropAction) {
        int mods = DragHandler.getModifiers();
        if (mods == -1) {
            return dropAction == 0x40000000 || dropAction == 1;
        }
        return mods != 0;
    }

    private void describe(String type, DropTargetEvent e2) {
    }

    protected int acceptOrReject(DropTargetDragEvent e2) {
        int action = this.getDropAction(e2);
        if (action != 0) {
            e2.acceptDrag(action);
        } else {
            e2.rejectDrag();
        }
        return action;
    }

    public void dragEnter(DropTargetDragEvent e2) {
        this.describe("enter(tgt)", e2);
        int action = this.acceptOrReject(e2);
        this.paintDropTarget(e2, action, e2.getLocation());
    }

    public void dragOver(DropTargetDragEvent e2) {
        this.describe("over(tgt)", e2);
        int action = this.acceptOrReject(e2);
        this.paintDropTarget(e2, action, e2.getLocation());
    }

    public void dragExit(DropTargetEvent e2) {
        this.describe("exit(tgt)", e2);
        this.paintDropTarget(e2, 0, null);
    }

    public void dropActionChanged(DropTargetDragEvent e2) {
        this.describe("change(tgt)", e2);
        int action = this.acceptOrReject(e2);
        this.paintDropTarget(e2, action, e2.getLocation());
    }

    public void drop(DropTargetDropEvent e2) {
        this.describe("drop(tgt)", e2);
        int action = this.getDropAction(e2);
        if (action != 0) {
            e2.acceptDrop(action);
            try {
                this.drop(e2, action);
                e2.dropComplete(true);
            }
            catch (Exception ex) {
                e2.dropComplete(false);
            }
        } else {
            e2.rejectDrop();
        }
        this.paintDropTarget(e2, 0, e2.getLocation());
    }

    protected boolean isSupported(DataFlavor[] flavors) {
        HashSet<DataFlavor> set = new HashSet<DataFlavor>(Arrays.asList(flavors));
        set.retainAll(this.acceptedFlavors);
        return !set.isEmpty();
    }

    protected void paintDropTarget(DropTargetEvent e2, int action, Point location) {
        if (this.painter != null) {
            this.painter.paintDropTarget(e2, action, location);
        }
    }

    protected boolean canDrop(DropTargetEvent e2, int action, Point location) {
        return true;
    }

    protected abstract void drop(DropTargetDropEvent var1, int var2) throws UnsupportedFlavorException, IOException;
}

