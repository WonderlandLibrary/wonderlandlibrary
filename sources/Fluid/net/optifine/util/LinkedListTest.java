// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.util;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import net.optifine.render.VboRange;

public class LinkedListTest
{
    public static void main(final String[] args) throws Exception {
        final LinkedList<VboRange> linkedlist = new LinkedList<VboRange>();
        final List<VboRange> list = new ArrayList<VboRange>();
        final List<VboRange> list2 = new ArrayList<VboRange>();
        final Random random = new Random();
        final int i = 100;
        for (int j = 0; j < i; ++j) {
            final VboRange vborange = new VboRange();
            vborange.setPosition(j);
            list.add(vborange);
        }
        for (int k = 0; k < 100000; ++k) {
            checkLists(list, list2, i);
            checkLinkedList(linkedlist, list2.size());
            if (k % 5 == 0) {
                dbgLinkedList(linkedlist);
            }
            if (random.nextBoolean()) {
                if (!list.isEmpty()) {
                    final VboRange vborange2 = list.get(random.nextInt(list.size()));
                    final LinkedList.Node<VboRange> node2 = vborange2.getNode();
                    if (random.nextBoolean()) {
                        linkedlist.addFirst(node2);
                        dbg("Add first: " + vborange2.getPosition());
                    }
                    else if (random.nextBoolean()) {
                        linkedlist.addLast(node2);
                        dbg("Add last: " + vborange2.getPosition());
                    }
                    else {
                        if (list2.isEmpty()) {
                            continue;
                        }
                        final VboRange vborange3 = list2.get(random.nextInt(list2.size()));
                        final LinkedList.Node<VboRange> node3 = vborange3.getNode();
                        linkedlist.addAfter(node3, node2);
                        dbg("Add after: " + vborange3.getPosition() + ", " + vborange2.getPosition());
                    }
                    list.remove(vborange2);
                    list2.add(vborange2);
                }
            }
            else if (!list2.isEmpty()) {
                final VboRange vborange4 = list2.get(random.nextInt(list2.size()));
                final LinkedList.Node<VboRange> node4 = vborange4.getNode();
                linkedlist.remove(node4);
                dbg("Remove: " + vborange4.getPosition());
                list2.remove(vborange4);
                list.add(vborange4);
            }
        }
    }
    
    private static void dbgLinkedList(final LinkedList<VboRange> linkedList) {
        final StringBuffer stringbuffer = new StringBuffer();
        linkedList.iterator().forEachRemaining(vboRangeNode -> {
            final LinkedList.Node<VboRange> node = vboRangeNode;
            if (node.getItem() == null) {
                return;
            }
            else {
                final VboRange vborange = node.getItem();
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(vborange.getPosition());
                return;
            }
        });
        dbg("List: " + (Object)stringbuffer);
    }
    
    private static void checkLinkedList(final LinkedList<VboRange> linkedList, final int used) {
        if (linkedList.getSize() != used) {
            throw new RuntimeException("Wrong size, linked: " + linkedList.getSize() + ", used: " + used);
        }
        int i = 0;
        for (LinkedList.Node<VboRange> node = linkedList.getFirst(); node != null; node = node.getNext()) {
            ++i;
        }
        if (linkedList.getSize() != i) {
            throw new RuntimeException("Wrong count, linked: " + linkedList.getSize() + ", count: " + i);
        }
        int j = 0;
        for (LinkedList.Node<VboRange> node2 = linkedList.getLast(); node2 != null; node2 = node2.getPrev()) {
            ++j;
        }
        if (linkedList.getSize() != j) {
            throw new RuntimeException("Wrong count back, linked: " + linkedList.getSize() + ", count: " + j);
        }
    }
    
    private static void checkLists(final List<VboRange> listFree, final List<VboRange> listUsed, final int count) {
        final int i = listFree.size() + listUsed.size();
        if (i != count) {
            throw new RuntimeException("Total size: " + i);
        }
    }
    
    private static void dbg(final String str) {
        System.out.println(str);
    }
}
