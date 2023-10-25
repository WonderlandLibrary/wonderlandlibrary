/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.collections.ObservableListWrapper
 *  com.sun.javafx.collections.ObservableMapWrapper
 *  com.sun.javafx.event.BasicEventDispatcher
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.scene.Node
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.Mnemonic
 */
package com.sun.javafx.scene;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.scene.NodeHelper;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;

public final class KeyboardShortcutsHandler
extends BasicEventDispatcher {
    private ObservableMap<KeyCombination, Runnable> accelerators;
    private CopyOnWriteMap<KeyCombination, Runnable> acceleratorsBackingMap;
    private ObservableMap<KeyCombination, ObservableList<Mnemonic>> mnemonics;
    private boolean mnemonicsDisplayEnabled = false;

    public void addMnemonic(Mnemonic mnemonic) {
        ObservableList observableList = (ObservableList)this.getMnemonics().get((Object)mnemonic.getKeyCombination());
        if (observableList == null) {
            observableList = new ObservableListWrapper(new ArrayList());
            this.getMnemonics().put((Object)mnemonic.getKeyCombination(), (Object)observableList);
        }
        boolean bl = false;
        for (Mnemonic mnemonic2 : observableList) {
            if (mnemonic2 != mnemonic) continue;
            bl = true;
            break;
        }
        if (!bl) {
            observableList.add((Object)mnemonic);
        }
    }

    public void removeMnemonic(Mnemonic mnemonic) {
        ObservableList observableList = (ObservableList)this.getMnemonics().get((Object)mnemonic.getKeyCombination());
        if (observableList != null) {
            for (int i = 0; i < observableList.size(); ++i) {
                if (((Mnemonic)observableList.get(i)).getNode() != mnemonic.getNode()) continue;
                observableList.remove(i);
            }
        }
    }

    public ObservableMap<KeyCombination, ObservableList<Mnemonic>> getMnemonics() {
        if (this.mnemonics == null) {
            this.mnemonics = new ObservableMapWrapper(new HashMap());
        }
        return this.mnemonics;
    }

    public ObservableMap<KeyCombination, Runnable> getAccelerators() {
        if (this.accelerators == null) {
            this.acceleratorsBackingMap = new CopyOnWriteMap();
            this.accelerators = new ObservableMapWrapper(this.acceleratorsBackingMap);
        }
        return this.accelerators;
    }

    private void traverse(Event event, Node node, Direction direction) {
        if (NodeHelper.traverse(node, direction)) {
            event.consume();
        }
    }

    public void processTraversal(Event event) {
        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }
        if (!(event instanceof KeyEvent)) {
            return;
        }
        KeyEvent keyEvent = (KeyEvent)event;
        if (!(keyEvent.isMetaDown() || keyEvent.isControlDown() || keyEvent.isAltDown())) {
            EventTarget eventTarget = event.getTarget();
            if (!(eventTarget instanceof Node)) {
                return;
            }
            Node node = (Node)eventTarget;
            switch (keyEvent.getCode()) {
                case TAB: {
                    if (keyEvent.isShiftDown()) {
                        this.traverse(event, node, Direction.PREVIOUS);
                        break;
                    }
                    this.traverse(event, node, Direction.NEXT);
                    break;
                }
                case UP: {
                    this.traverse(event, node, Direction.UP);
                    break;
                }
                case DOWN: {
                    this.traverse(event, node, Direction.DOWN);
                    break;
                }
                case LEFT: {
                    this.traverse(event, node, Direction.LEFT);
                    break;
                }
                case RIGHT: {
                    this.traverse(event, node, Direction.RIGHT);
                    break;
                }
            }
        }
    }

    public Event dispatchBubblingEvent(Event event) {
        if (!(event instanceof KeyEvent)) {
            return event;
        }
        boolean bl = event.getEventType() == KeyEvent.KEY_PRESSED;
        KeyEvent keyEvent = (KeyEvent)event;
        if (bl) {
            if (!event.isConsumed()) {
                this.processAccelerators(keyEvent);
            }
            if (!event.isConsumed()) {
                this.processTraversal(event);
            }
        }
        return event;
    }

    public Event dispatchCapturingEvent(Event event) {
        if (!(event instanceof KeyEvent)) {
            return event;
        }
        boolean bl = event.getEventType() == KeyEvent.KEY_PRESSED;
        boolean bl2 = event.getEventType() == KeyEvent.KEY_TYPED;
        boolean bl3 = event.getEventType() == KeyEvent.KEY_RELEASED;
        KeyEvent keyEvent = (KeyEvent)event;
        if (bl || bl2) {
            if (PlatformUtil.isMac()) {
                if (keyEvent.isMetaDown()) {
                    this.processMnemonics(keyEvent);
                }
            } else if (keyEvent.isAltDown() || this.isMnemonicsDisplayEnabled()) {
                this.processMnemonics(keyEvent);
            }
        }
        if (!PlatformUtil.isMac() && !event.isConsumed()) {
            if (bl) {
                if (keyEvent.isAltDown()) {
                    if (!this.isMnemonicsDisplayEnabled()) {
                        this.setMnemonicsDisplayEnabled(true);
                    } else if (PlatformUtil.isWindows()) {
                        this.setMnemonicsDisplayEnabled(!this.isMnemonicsDisplayEnabled());
                    }
                } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    this.setMnemonicsDisplayEnabled(false);
                }
            }
            if (bl3 && !keyEvent.isAltDown() && !PlatformUtil.isWindows()) {
                this.setMnemonicsDisplayEnabled(false);
            }
        }
        return event;
    }

    private void processMnemonics(KeyEvent keyEvent) {
        Map.Entry entry2;
        if (this.mnemonics == null) {
            return;
        }
        KeyEvent keyEvent2 = keyEvent;
        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
            keyEvent2 = new KeyEvent(null, keyEvent.getTarget(), KeyEvent.KEY_PRESSED, " ", keyEvent.getCharacter(), KeyCode.getKeyCode((String)keyEvent.getCharacter()), keyEvent.isShiftDown(), keyEvent.isControlDown(), this.isMnemonicsDisplayEnabled(), keyEvent.isMetaDown());
        } else if (this.isMnemonicsDisplayEnabled()) {
            keyEvent2 = new KeyEvent(null, keyEvent.getTarget(), KeyEvent.KEY_PRESSED, keyEvent.getCharacter(), keyEvent.getText(), keyEvent.getCode(), keyEvent.isShiftDown(), keyEvent.isControlDown(), this.isMnemonicsDisplayEnabled(), keyEvent.isMetaDown());
        }
        ObservableList observableList = null;
        for (Map.Entry entry2 : this.mnemonics.entrySet()) {
            if (!((KeyCombination)entry2.getKey()).match(keyEvent2)) continue;
            observableList = (ObservableList)entry2.getValue();
            break;
        }
        if (observableList == null) {
            return;
        }
        boolean bl = false;
        entry2 = null;
        Mnemonic mnemonic = null;
        int n = -1;
        int n2 = -1;
        for (int i = 0; i < observableList.size(); ++i) {
            Mnemonic mnemonic2 = (Mnemonic)observableList.get(i);
            Node node = mnemonic2.getNode();
            if (mnemonic == null && NodeHelper.isTreeVisible(node) && !node.isDisabled()) {
                mnemonic = mnemonic2;
            }
            if (NodeHelper.isTreeVisible(node) && node.isFocusTraversable() && !node.isDisabled()) {
                if (entry2 == null) {
                    entry2 = node;
                } else {
                    bl = true;
                    if (n != -1 && n2 == -1) {
                        n2 = i;
                    }
                }
            }
            if (!node.isFocused()) continue;
            n = i;
        }
        if (entry2 != null) {
            if (!bl) {
                entry2.requestFocus();
                keyEvent.consume();
            } else if (n == -1) {
                entry2.requestFocus();
                keyEvent.consume();
            } else if (n >= observableList.size()) {
                entry2.requestFocus();
                keyEvent.consume();
            } else {
                if (n2 != -1) {
                    ((Mnemonic)observableList.get(n2)).getNode().requestFocus();
                } else {
                    entry2.requestFocus();
                }
                keyEvent.consume();
            }
        }
        if (!bl && mnemonic != null) {
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                keyEvent.consume();
            } else {
                mnemonic.fire();
                keyEvent.consume();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void processAccelerators(KeyEvent keyEvent) {
        if (this.acceleratorsBackingMap != null) {
            this.acceleratorsBackingMap.lock();
            try {
                for (Map.Entry entry : this.acceleratorsBackingMap.backingMap.entrySet()) {
                    Runnable runnable;
                    if (!((KeyCombination)entry.getKey()).match(keyEvent) || (runnable = (Runnable)entry.getValue()) == null) continue;
                    runnable.run();
                    keyEvent.consume();
                }
            }
            finally {
                this.acceleratorsBackingMap.unlock();
            }
        }
    }

    private void processMnemonicsKeyDisplay() {
        ObservableList observableList = null;
        if (this.mnemonics != null) {
            for (Map.Entry entry : this.mnemonics.entrySet()) {
                observableList = (ObservableList)entry.getValue();
                if (observableList == null) continue;
                for (int i = 0; i < observableList.size(); ++i) {
                    Node node = ((Mnemonic)observableList.get(i)).getNode();
                    NodeHelper.setShowMnemonics(node, this.mnemonicsDisplayEnabled);
                }
            }
        }
    }

    public boolean isMnemonicsDisplayEnabled() {
        return this.mnemonicsDisplayEnabled;
    }

    public void setMnemonicsDisplayEnabled(boolean bl) {
        if (bl != this.mnemonicsDisplayEnabled) {
            this.mnemonicsDisplayEnabled = bl;
            this.processMnemonicsKeyDisplay();
        }
    }

    public void clearNodeMnemonics(Node node) {
        if (this.mnemonics != null) {
            for (ObservableList observableList : this.mnemonics.values()) {
                Iterator iterator = observableList.iterator();
                while (iterator.hasNext()) {
                    Mnemonic mnemonic = (Mnemonic)iterator.next();
                    if (mnemonic.getNode() != node) continue;
                    iterator.remove();
                }
            }
        }
    }

    private static class CopyOnWriteMap<K, V>
    extends AbstractMap<K, V> {
        private Map<K, V> backingMap = new HashMap();
        private boolean lock;

        private CopyOnWriteMap() {
        }

        public void lock() {
            this.lock = true;
        }

        public void unlock() {
            this.lock = false;
        }

        @Override
        public V put(K k, V v) {
            if (this.lock) {
                this.backingMap = new HashMap<K, V>(this.backingMap);
                this.lock = false;
            }
            return this.backingMap.put(k, v);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new AbstractSet<Map.Entry<K, V>>(){

                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return new Iterator<Map.Entry<K, V>>(){
                        private Iterator<Map.Entry<K, V>> backingIt;
                        private Map<K, V> backingMapAtCreation;
                        private Map.Entry<K, V> lastNext;
                        {
                            this.backingIt = backingMap.entrySet().iterator();
                            this.backingMapAtCreation = backingMap;
                            this.lastNext = null;
                        }

                        @Override
                        public boolean hasNext() {
                            this.checkCoMod();
                            return this.backingIt.hasNext();
                        }

                        private void checkCoMod() {
                            if (backingMap != this.backingMapAtCreation) {
                                throw new ConcurrentModificationException();
                            }
                        }

                        @Override
                        public Map.Entry<K, V> next() {
                            this.checkCoMod();
                            this.lastNext = this.backingIt.next();
                            return this.lastNext;
                        }

                        @Override
                        public void remove() {
                            this.checkCoMod();
                            if (this.lastNext == null) {
                                throw new IllegalStateException();
                            }
                            if (lock) {
                                backingMap = new HashMap(backingMap);
                                this.backingIt = backingMap.entrySet().iterator();
                                while (!this.lastNext.equals(this.backingIt.next())) {
                                }
                                lock = false;
                            }
                            this.backingIt.remove();
                            this.lastNext = null;
                        }
                    };
                }

                @Override
                public int size() {
                    return backingMap.size();
                }
            };
        }
    }
}

