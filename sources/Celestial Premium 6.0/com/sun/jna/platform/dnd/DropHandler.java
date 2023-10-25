/*
 * Decompiled with CFR 0.150.
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

    public DropHandler(Component c, int acceptedActions) {
        this(c, acceptedActions, new DataFlavor[0]);
    }

    public DropHandler(Component c, int acceptedActions, DataFlavor[] acceptedFlavors) {
        this(c, acceptedActions, acceptedFlavors, null);
    }

    public DropHandler(Component c, int acceptedActions, DataFlavor[] acceptedFlavors, DropTargetPainter painter) {
        this.acceptedActions = acceptedActions;
        this.acceptedFlavors = Arrays.asList(acceptedFlavors);
        this.painter = painter;
        this.dropTarget = new DropTarget(c, acceptedActions, this, this.active);
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

    protected int getDropAction(DropTargetEvent e) {
        int availableActions;
        DropTargetEvent ev;
        int currentAction = 0;
        int sourceActions = 0;
        Point location = null;
        DataFlavor[] flavors = new DataFlavor[]{};
        if (e instanceof DropTargetDragEvent) {
            ev = (DropTargetDragEvent)e;
            currentAction = ((DropTargetDragEvent)ev).getDropAction();
            sourceActions = ((DropTargetDragEvent)ev).getSourceActions();
            flavors = ((DropTargetDragEvent)ev).getCurrentDataFlavors();
            location = ((DropTargetDragEvent)ev).getLocation();
        } else if (e instanceof DropTargetDropEvent) {
            ev = (DropTargetDropEvent)e;
            currentAction = ((DropTargetDropEvent)ev).getDropAction();
            sourceActions = ((DropTargetDropEvent)ev).getSourceActions();
            flavors = ((DropTargetDropEvent)ev).getCurrentDataFlavors();
            location = ((DropTargetDropEvent)ev).getLocation();
        }
        if (this.isSupported(flavors) && (currentAction = this.getDropAction(e, currentAction, sourceActions, availableActions = this.getDropActionsForFlavors(flavors))) != 0 && this.canDrop(e, currentAction, location)) {
            return currentAction;
        }
        return 0;
    }

    protected int getDropAction(DropTargetEvent e, int currentAction, int sourceActions, int acceptedActions) {
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

    private void describe(String type, DropTargetEvent e) {
    }

    protected int acceptOrReject(DropTargetDragEvent e) {
        int action = this.getDropAction(e);
        if (action != 0) {
            e.acceptDrag(action);
        } else {
            e.rejectDrag();
        }
        return action;
    }

    public void dragEnter(DropTargetDragEvent e) {
        this.describe("enter(tgt)", e);
        int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }

    public void dragOver(DropTargetDragEvent e) {
        this.describe("over(tgt)", e);
        int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }

    public void dragExit(DropTargetEvent e) {
        this.describe("exit(tgt)", e);
        this.paintDropTarget(e, 0, null);
    }

    public void dropActionChanged(DropTargetDragEvent e) {
        this.describe("change(tgt)", e);
        int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }

    public void drop(DropTargetDropEvent e) {
        this.describe("drop(tgt)", e);
        int action = this.getDropAction(e);
        if (action != 0) {
            e.acceptDrop(action);
            try {
                this.drop(e, action);
                e.dropComplete(true);
            }
            catch (Exception ex) {
                e.dropComplete(false);
            }
        } else {
            e.rejectDrop();
        }
        this.paintDropTarget(e, 0, e.getLocation());
    }

    protected boolean isSupported(DataFlavor[] flavors) {
        HashSet<DataFlavor> set = new HashSet<DataFlavor>(Arrays.asList(flavors));
        set.retainAll(this.acceptedFlavors);
        return !set.isEmpty();
    }

    protected void paintDropTarget(DropTargetEvent e, int action, Point location) {
        if (this.painter != null) {
            this.painter.paintDropTarget(e, action, location);
        }
    }

    protected boolean canDrop(DropTargetEvent e, int action, Point location) {
        return true;
    }

    protected abstract void drop(DropTargetDropEvent var1, int var2) throws UnsupportedFlavorException, IOException;
}

