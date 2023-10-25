/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.CompoundSelector
 *  javafx.css.Selector
 *  javafx.css.SimpleSelector
 *  javafx.css.StyleClass
 */
package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.css.CompoundSelector;
import javafx.css.Selector;
import javafx.css.SimpleSelector;
import javafx.css.StyleClass;

public final class SelectorPartitioning {
    private final Map<PartitionKey, Partition> idMap = new HashMap<PartitionKey, Partition>();
    private final Map<PartitionKey, Partition> typeMap = new HashMap<PartitionKey, Partition>();
    private final Map<PartitionKey, Partition> styleClassMap = new HashMap<PartitionKey, Partition>();
    private int ordinal;
    private static final int ID_BIT = 4;
    private static final int TYPE_BIT = 2;
    private static final int STYLECLASS_BIT = 1;
    private static final PartitionKey WILDCARD = new PartitionKey<String>("*");
    private static final Comparator<Selector> COMPARATOR = (selector, selector2) -> selector.getOrdinal() - selector2.getOrdinal();

    public void reset() {
        this.idMap.clear();
        this.typeMap.clear();
        this.styleClassMap.clear();
        this.ordinal = 0;
    }

    private static Partition getPartition(PartitionKey partitionKey, Map<PartitionKey, Partition> map) {
        Partition partition = map.get(partitionKey);
        if (partition == null) {
            partition = new Partition(partitionKey);
            map.put(partitionKey, partition);
        }
        return partition;
    }

    public void partition(Selector selector) {
        int n;
        Object object;
        SimpleSelector simpleSelector = null;
        if (selector instanceof CompoundSelector) {
            object = ((CompoundSelector)selector).getSelectors();
            n = object.size() - 1;
            simpleSelector = (SimpleSelector)object.get(n);
        } else {
            simpleSelector = (SimpleSelector)selector;
        }
        object = simpleSelector.getId();
        n = object != null && !((String)object).isEmpty() ? 1 : 0;
        PartitionKey<Object> partitionKey = n != 0 ? new PartitionKey<Object>(object) : null;
        String string = simpleSelector.getName();
        boolean bl = string != null && !string.isEmpty();
        PartitionKey<String> partitionKey2 = bl ? new PartitionKey<String>(string) : null;
        Set set = simpleSelector.getStyleClassSet();
        boolean bl2 = set != null && set.size() > 0;
        PartitionKey<Set> partitionKey3 = bl2 ? new PartitionKey<Set>(set) : null;
        int n2 = (n != 0 ? 4 : 0) | (bl ? 2 : 0) | (bl2 ? 1 : 0);
        Partition partition = null;
        Slot slot = null;
        selector.setOrdinal(this.ordinal++);
        switch (n2) {
            case 6: 
            case 7: {
                partition = SelectorPartitioning.getPartition(partitionKey, this.idMap);
                slot = partition.partition(partitionKey2, this.typeMap);
                if ((n2 & 1) == 1) {
                    slot = slot.partition(partitionKey3, this.styleClassMap);
                }
                slot.addSelector(selector);
                break;
            }
            case 2: 
            case 3: {
                partition = SelectorPartitioning.getPartition(partitionKey2, this.typeMap);
                if ((n2 & 1) == 1) {
                    slot = partition.partition(partitionKey3, this.styleClassMap);
                    slot.addSelector(selector);
                    break;
                }
                partition.addSelector(selector);
                break;
            }
            default: {
                assert (false);
                break;
            }
        }
    }

    public List<Selector> match(String string, String string2, Set<StyleClass> set) {
        boolean bl = string != null && !string.isEmpty();
        PartitionKey<String> partitionKey = bl ? new PartitionKey<String>(string) : null;
        boolean bl2 = string2 != null && !string2.isEmpty();
        PartitionKey partitionKey2 = bl2 ? new PartitionKey(string2) : null;
        boolean bl3 = set != null && set.size() > 0;
        PartitionKey<Set<StyleClass>> partitionKey3 = bl3 ? new PartitionKey<Set<StyleClass>>(set) : null;
        int n = (bl ? 4 : 0) | (bl2 ? 2 : 0) | (bl3 ? 1 : 0);
        Partition partition = null;
        Slot slot = null;
        ArrayList<Selector> arrayList = new ArrayList<Selector>();
        block6: while (n != 0) {
            switch (n) {
                case 6: 
                case 7: {
                    Set set2;
                    Set set3;
                    PartitionKey partitionKey4;
                    partition = this.idMap.get(partitionKey);
                    if (partition != null) {
                        if (partition.selectors != null) {
                            arrayList.addAll(partition.selectors);
                        }
                        partitionKey4 = partitionKey2;
                        do {
                            if ((slot = partition.slots.get(partitionKey4)) == null) continue;
                            if (slot.selectors != null) {
                                arrayList.addAll(slot.selectors);
                            }
                            if ((n & 1) != 1) continue;
                            set3 = (Set)partitionKey3.key;
                            for (Slot slot2 : slot.referents.values()) {
                                if (slot2.selectors == null || slot2.selectors.isEmpty() || !set3.containsAll(set2 = (Set)slot2.partition.key.key)) continue;
                                arrayList.addAll(slot2.selectors);
                            }
                        } while ((partitionKey4 = !WILDCARD.equals(partitionKey4) ? WILDCARD : null) != null);
                    }
                    n -= 4;
                    continue block6;
                }
                case 4: 
                case 5: {
                    n -= 4;
                    continue block6;
                }
                case 2: 
                case 3: {
                    Set set2;
                    Set set3;
                    PartitionKey partitionKey4 = partitionKey2;
                    do {
                        if ((partition = this.typeMap.get(partitionKey4)) == null) continue;
                        if (partition.selectors != null) {
                            arrayList.addAll(partition.selectors);
                        }
                        if ((n & 1) != 1) continue;
                        set3 = (Set)partitionKey3.key;
                        for (Slot slot2 : partition.slots.values()) {
                            if (slot2.selectors == null || slot2.selectors.isEmpty() || !set3.containsAll(set2 = (Set)slot2.partition.key.key)) continue;
                            arrayList.addAll(slot2.selectors);
                        }
                    } while ((partitionKey4 = !WILDCARD.equals(partitionKey4) ? WILDCARD : null) != null);
                    n -= 2;
                    continue block6;
                }
                case 1: {
                    --n;
                    continue block6;
                }
            }
            assert (false);
        }
        Collections.sort(arrayList, COMPARATOR);
        return arrayList;
    }

    private static final class Partition {
        private final PartitionKey key;
        private final Map<PartitionKey, Slot> slots;
        private List<Selector> selectors;

        private Partition(PartitionKey partitionKey) {
            this.key = partitionKey;
            this.slots = new HashMap<PartitionKey, Slot>();
        }

        private void addSelector(Selector selector) {
            if (this.selectors == null) {
                this.selectors = new ArrayList<Selector>();
            }
            this.selectors.add(selector);
        }

        private Slot partition(PartitionKey partitionKey, Map<PartitionKey, Partition> map) {
            Slot slot = this.slots.get(partitionKey);
            if (slot == null) {
                Partition partition = SelectorPartitioning.getPartition(partitionKey, map);
                slot = new Slot(partition);
                this.slots.put(partitionKey, slot);
            }
            return slot;
        }
    }

    private static final class PartitionKey<K> {
        private final K key;

        private PartitionKey(K k) {
            this.key = k;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            PartitionKey partitionKey = (PartitionKey)object;
            return this.key == partitionKey.key || this.key != null && this.key.equals(partitionKey.key);
        }

        public int hashCode() {
            int n = 7;
            n = 71 * n + (this.key != null ? this.key.hashCode() : 0);
            return n;
        }
    }

    private static final class Slot {
        private final Partition partition;
        private final Map<PartitionKey, Slot> referents;
        private List<Selector> selectors;

        private Slot(Partition partition) {
            this.partition = partition;
            this.referents = new HashMap<PartitionKey, Slot>();
        }

        private void addSelector(Selector selector) {
            if (this.selectors == null) {
                this.selectors = new ArrayList<Selector>();
            }
            this.selectors.add(selector);
        }

        private Slot partition(PartitionKey partitionKey, Map<PartitionKey, Partition> map) {
            Slot slot = this.referents.get(partitionKey);
            if (slot == null) {
                Partition partition = SelectorPartitioning.getPartition(partitionKey, map);
                slot = new Slot(partition);
                this.referents.put(partitionKey, slot);
            }
            return slot;
        }
    }
}

