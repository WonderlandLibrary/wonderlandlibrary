package com.sun.javafx.scene;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.scene.traversal.Direction;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;

public final class KeyboardShortcutsHandler extends BasicEventDispatcher {
   private ObservableMap accelerators;
   private CopyOnWriteMap acceleratorsBackingMap;
   private ObservableMap mnemonics;
   private boolean mnemonicsDisplayEnabled = false;

   public void addMnemonic(Mnemonic var1) {
      Object var2 = (ObservableList)this.getMnemonics().get(var1.getKeyCombination());
      if (var2 == null) {
         var2 = new ObservableListWrapper(new ArrayList());
         this.getMnemonics().put(var1.getKeyCombination(), var2);
      }

      boolean var3 = false;

      for(int var4 = 0; var4 < ((ObservableList)var2).size(); ++var4) {
         if (((ObservableList)var2).get(var4) == var1) {
            var3 = true;
         }
      }

      if (!var3) {
         ((ObservableList)var2).add(var1);
      }

   }

   public void removeMnemonic(Mnemonic var1) {
      ObservableList var2 = (ObservableList)this.getMnemonics().get(var1.getKeyCombination());
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            if (((Mnemonic)var2.get(var3)).getNode() == var1.getNode()) {
               var2.remove(var3);
            }
         }
      }

   }

   public ObservableMap getMnemonics() {
      if (this.mnemonics == null) {
         this.mnemonics = new ObservableMapWrapper(new HashMap());
      }

      return this.mnemonics;
   }

   public ObservableMap getAccelerators() {
      if (this.accelerators == null) {
         this.acceleratorsBackingMap = new CopyOnWriteMap();
         this.accelerators = new ObservableMapWrapper(this.acceleratorsBackingMap);
      }

      return this.accelerators;
   }

   private void traverse(Event var1, Node var2, Direction var3) {
      if (var2.impl_traverse(var3)) {
         var1.consume();
      }

   }

   public void processTraversal(Event var1) {
      if (var1 instanceof KeyEvent && var1.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent)var1).isMetaDown() && !((KeyEvent)var1).isControlDown() && !((KeyEvent)var1).isAltDown()) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof Node) {
            switch (((KeyEvent)var1).getCode()) {
               case TAB:
                  if (((KeyEvent)var1).isShiftDown()) {
                     this.traverse(var1, (Node)var2, Direction.PREVIOUS);
                  } else {
                     this.traverse(var1, (Node)var2, Direction.NEXT);
                  }
                  break;
               case UP:
                  this.traverse(var1, (Node)var2, Direction.UP);
                  break;
               case DOWN:
                  this.traverse(var1, (Node)var2, Direction.DOWN);
                  break;
               case LEFT:
                  this.traverse(var1, (Node)var2, Direction.LEFT);
                  break;
               case RIGHT:
                  this.traverse(var1, (Node)var2, Direction.RIGHT);
            }
         }
      }

   }

   public Event dispatchBubblingEvent(Event var1) {
      if (var1.getEventType() == KeyEvent.KEY_PRESSED) {
         if (PlatformUtil.isMac()) {
            if (((KeyEvent)var1).isMetaDown()) {
               this.processMnemonics((KeyEvent)var1);
            }
         } else if (((KeyEvent)var1).isAltDown() || this.isMnemonicsDisplayEnabled()) {
            this.processMnemonics((KeyEvent)var1);
         }

         if (!var1.isConsumed()) {
            this.processAccelerators((KeyEvent)var1);
         }

         if (!var1.isConsumed()) {
            this.processTraversal(var1);
         }
      }

      if (!PlatformUtil.isMac()) {
         if (var1.getEventType() == KeyEvent.KEY_PRESSED && ((KeyEvent)var1).isAltDown() && !var1.isConsumed()) {
            if (!this.isMnemonicsDisplayEnabled()) {
               this.setMnemonicsDisplayEnabled(true);
            } else if (PlatformUtil.isWindows()) {
               this.setMnemonicsDisplayEnabled(!this.isMnemonicsDisplayEnabled());
            }
         }

         if (var1.getEventType() == KeyEvent.KEY_RELEASED && !((KeyEvent)var1).isAltDown() && !PlatformUtil.isWindows()) {
            this.setMnemonicsDisplayEnabled(false);
         }
      }

      return var1;
   }

   private void processMnemonics(KeyEvent var1) {
      if (this.mnemonics != null) {
         ObservableList var2 = null;
         Iterator var3 = this.mnemonics.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (!this.isMnemonicsDisplayEnabled()) {
               if (((KeyCombination)var4.getKey()).match(var1)) {
                  var2 = (ObservableList)var4.getValue();
                  break;
               }
            } else {
               KeyEvent var5 = new KeyEvent((Object)null, var1.getTarget(), KeyEvent.KEY_PRESSED, var1.getCharacter(), var1.getText(), var1.getCode(), var1.isShiftDown(), var1.isControlDown(), true, var1.isMetaDown());
               if (((KeyCombination)var4.getKey()).match(var5)) {
                  var2 = (ObservableList)var4.getValue();
                  break;
               }
            }
         }

         if (var2 != null) {
            boolean var10 = false;
            Node var11 = null;
            Mnemonic var12 = null;
            int var6 = -1;
            int var7 = -1;

            for(int var8 = 0; var8 < var2.size(); ++var8) {
               if (var2.get(var8) instanceof Mnemonic) {
                  Node var9 = ((Mnemonic)var2.get(var8)).getNode();
                  if (var12 == null && var9.impl_isTreeVisible() && !var9.isDisabled()) {
                     var12 = (Mnemonic)var2.get(var8);
                  }

                  if (var9.impl_isTreeVisible() && var9.isFocusTraversable() && !var9.isDisabled()) {
                     if (var11 == null) {
                        var11 = var9;
                     } else {
                        var10 = true;
                        if (var6 != -1 && var7 == -1) {
                           var7 = var8;
                        }
                     }
                  }

                  if (var9.isFocused()) {
                     var6 = var8;
                  }
               }
            }

            if (var11 != null) {
               if (!var10) {
                  var11.requestFocus();
                  var1.consume();
               } else if (var6 == -1) {
                  var11.requestFocus();
                  var1.consume();
               } else if (var6 >= var2.size()) {
                  var11.requestFocus();
                  var1.consume();
               } else {
                  if (var7 != -1) {
                     ((Mnemonic)var2.get(var7)).getNode().requestFocus();
                  } else {
                     var11.requestFocus();
                  }

                  var1.consume();
               }
            }

            if (!var10 && var12 != null) {
               var12.fire();
            }
         }
      }

   }

   private void processAccelerators(KeyEvent var1) {
      if (this.acceleratorsBackingMap != null) {
         this.acceleratorsBackingMap.lock();

         try {
            Iterator var2 = this.acceleratorsBackingMap.backingMap.entrySet().iterator();

            while(var2.hasNext()) {
               Map.Entry var3 = (Map.Entry)var2.next();
               if (((KeyCombination)var3.getKey()).match(var1)) {
                  Runnable var4 = (Runnable)var3.getValue();
                  if (var4 != null) {
                     var4.run();
                     var1.consume();
                  }
               }
            }
         } finally {
            this.acceleratorsBackingMap.unlock();
         }
      }

   }

   private void processMnemonicsKeyDisplay() {
      ObservableList var1 = null;
      if (this.mnemonics != null) {
         Iterator var2 = this.mnemonics.entrySet().iterator();

         while(true) {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               Map.Entry var3 = (Map.Entry)var2.next();
               var1 = (ObservableList)var3.getValue();
            } while(var1 == null);

            for(int var4 = 0; var4 < var1.size(); ++var4) {
               Node var5 = ((Mnemonic)var1.get(var4)).getNode();
               var5.impl_setShowMnemonics(this.mnemonicsDisplayEnabled);
            }
         }
      }
   }

   public boolean isMnemonicsDisplayEnabled() {
      return this.mnemonicsDisplayEnabled;
   }

   public void setMnemonicsDisplayEnabled(boolean var1) {
      if (var1 != this.mnemonicsDisplayEnabled) {
         this.mnemonicsDisplayEnabled = var1;
         this.processMnemonicsKeyDisplay();
      }

   }

   public void clearNodeMnemonics(Node var1) {
      if (this.mnemonics != null) {
         Iterator var2 = this.mnemonics.values().iterator();

         while(var2.hasNext()) {
            ObservableList var3 = (ObservableList)var2.next();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               Mnemonic var5 = (Mnemonic)var4.next();
               if (var5.getNode() == var1) {
                  var4.remove();
               }
            }
         }
      }

   }

   private static class CopyOnWriteMap extends AbstractMap {
      private Map backingMap;
      private boolean lock;

      private CopyOnWriteMap() {
         this.backingMap = new HashMap();
      }

      public void lock() {
         this.lock = true;
      }

      public void unlock() {
         this.lock = false;
      }

      public Object put(Object var1, Object var2) {
         if (this.lock) {
            this.backingMap = new HashMap(this.backingMap);
            this.lock = false;
         }

         return this.backingMap.put(var1, var2);
      }

      public Set entrySet() {
         return new AbstractSet() {
            public Iterator iterator() {
               return new Iterator() {
                  private Iterator backingIt;
                  private Map backingMapAtCreation;
                  private Map.Entry lastNext;

                  {
                     this.backingIt = CopyOnWriteMap.this.backingMap.entrySet().iterator();
                     this.backingMapAtCreation = CopyOnWriteMap.this.backingMap;
                     this.lastNext = null;
                  }

                  public boolean hasNext() {
                     this.checkCoMod();
                     return this.backingIt.hasNext();
                  }

                  private void checkCoMod() {
                     if (CopyOnWriteMap.this.backingMap != this.backingMapAtCreation) {
                        throw new ConcurrentModificationException();
                     }
                  }

                  public Map.Entry next() {
                     this.checkCoMod();
                     return this.lastNext = (Map.Entry)this.backingIt.next();
                  }

                  public void remove() {
                     this.checkCoMod();
                     if (this.lastNext == null) {
                        throw new IllegalStateException();
                     } else {
                        if (CopyOnWriteMap.this.lock) {
                           CopyOnWriteMap.this.backingMap = new HashMap(CopyOnWriteMap.this.backingMap);
                           this.backingIt = CopyOnWriteMap.this.backingMap.entrySet().iterator();

                           while(true) {
                              if (this.lastNext.equals(this.backingIt.next())) {
                                 CopyOnWriteMap.this.lock = false;
                                 break;
                              }
                           }
                        }

                        this.backingIt.remove();
                        this.lastNext = null;
                     }
                  }
               };
            }

            public int size() {
               return CopyOnWriteMap.this.backingMap.size();
            }
         };
      }

      // $FF: synthetic method
      CopyOnWriteMap(Object var1) {
         this();
      }
   }
}
