package com.sun.jna.platform.dnd;

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
import java.util.Set;

public abstract class DropHandler implements DropTargetListener {
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
    if (this.dropTarget != null)
      this.dropTarget.setActive(active); 
  }
  
  protected int getDropActionsForFlavors(DataFlavor[] dataFlavors) {
    return this.acceptedActions;
  }
  
  protected int getDropAction(DropTargetEvent e) {
    int currentAction = 0;
    int sourceActions = 0;
    Point location = null;
    DataFlavor[] flavors = new DataFlavor[0];
    if (e instanceof DropTargetDragEvent) {
      DropTargetDragEvent ev = (DropTargetDragEvent)e;
      currentAction = ev.getDropAction();
      sourceActions = ev.getSourceActions();
      flavors = ev.getCurrentDataFlavors();
      location = ev.getLocation();
    } else if (e instanceof DropTargetDropEvent) {
      DropTargetDropEvent ev = (DropTargetDropEvent)e;
      currentAction = ev.getDropAction();
      sourceActions = ev.getSourceActions();
      flavors = ev.getCurrentDataFlavors();
      location = ev.getLocation();
    } 
    if (isSupported(flavors)) {
      int availableActions = getDropActionsForFlavors(flavors);
      currentAction = getDropAction(e, currentAction, sourceActions, availableActions);
      if (currentAction != 0 && 
        canDrop(e, currentAction, location))
        return currentAction; 
    } 
    return 0;
  }
  
  protected int getDropAction(DropTargetEvent e, int currentAction, int sourceActions, int acceptedActions) {
    boolean modifiersActive = modifiersActive(currentAction);
    if ((currentAction & acceptedActions) == 0 && !modifiersActive) {
      int action = acceptedActions & sourceActions;
      currentAction = action;
    } else if (modifiersActive) {
      int action = currentAction & acceptedActions & sourceActions;
      if (action != currentAction)
        currentAction = action; 
    } 
    return currentAction;
  }
  
  protected boolean modifiersActive(int dropAction) {
    int mods = DragHandler.getModifiers();
    if (mods == -1) {
      if (dropAction == 1073741824 || dropAction == 1)
        return true; 
      return false;
    } 
    return (mods != 0);
  }
  
  private void describe(String type, DropTargetEvent e) {}
  
  protected int acceptOrReject(DropTargetDragEvent e) {
    int action = getDropAction(e);
    if (action != 0) {
      e.acceptDrag(action);
    } else {
      e.rejectDrag();
    } 
    return action;
  }
  
  public void dragEnter(DropTargetDragEvent e) {
    describe("enter(tgt)", e);
    int action = acceptOrReject(e);
    paintDropTarget(e, action, e.getLocation());
  }
  
  public void dragOver(DropTargetDragEvent e) {
    describe("over(tgt)", e);
    int action = acceptOrReject(e);
    paintDropTarget(e, action, e.getLocation());
  }
  
  public void dragExit(DropTargetEvent e) {
    describe("exit(tgt)", e);
    paintDropTarget(e, 0, null);
  }
  
  public void dropActionChanged(DropTargetDragEvent e) {
    describe("change(tgt)", e);
    int action = acceptOrReject(e);
    paintDropTarget(e, action, e.getLocation());
  }
  
  public void drop(DropTargetDropEvent e) {
    describe("drop(tgt)", e);
    int action = getDropAction(e);
    if (action != 0) {
      e.acceptDrop(action);
      try {
        drop(e, action);
        e.dropComplete(true);
      } catch (Exception ex) {
        e.dropComplete(false);
      } 
    } else {
      e.rejectDrop();
    } 
    paintDropTarget(e, 0, e.getLocation());
  }
  
  protected boolean isSupported(DataFlavor[] flavors) {
    Set<DataFlavor> set = new HashSet<DataFlavor>(Arrays.asList(flavors));
    set.retainAll(this.acceptedFlavors);
    return !set.isEmpty();
  }
  
  protected void paintDropTarget(DropTargetEvent e, int action, Point location) {
    if (this.painter != null)
      this.painter.paintDropTarget(e, action, location); 
  }
  
  protected boolean canDrop(DropTargetEvent e, int action, Point location) {
    return true;
  }
  
  protected abstract void drop(DropTargetDropEvent paramDropTargetDropEvent, int paramInt) throws UnsupportedFlavorException, IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\dnd\DropHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */