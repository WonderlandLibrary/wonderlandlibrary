/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.ParentHelper;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

final class TabOrderHelper {
    TabOrderHelper() {
    }

    private static Node findPreviousFocusableInList(List<Node> list, int n) {
        for (int i = n; i >= 0; --i) {
            ParentTraversalEngine parentTraversalEngine;
            Node node = list.get(i);
            if (TabOrderHelper.isDisabledOrInvisible(node)) continue;
            ParentTraversalEngine parentTraversalEngine2 = parentTraversalEngine = node instanceof Parent ? ParentHelper.getTraversalEngine((Parent)node) : null;
            if (node instanceof Parent) {
                ObservableList observableList;
                if (parentTraversalEngine != null && parentTraversalEngine.canTraverse()) {
                    observableList = parentTraversalEngine.selectLast();
                    if (observableList != null) {
                        return observableList;
                    }
                } else {
                    Node node2;
                    observableList = ((Parent)node).getChildrenUnmodifiable();
                    if (observableList.size() > 0 && (node2 = TabOrderHelper.findPreviousFocusableInList((List<Node>)observableList, observableList.size() - 1)) != null) {
                        return node2;
                    }
                }
            }
            if (!(parentTraversalEngine != null ? parentTraversalEngine.isParentTraversable() : node.isFocusTraversable())) continue;
            return node;
        }
        return null;
    }

    private static boolean isDisabledOrInvisible(Node node) {
        return node.isDisabled() || !NodeHelper.isTreeVisible(node);
    }

    public static Node findPreviousFocusablePeer(Node node, Parent parent) {
        Node node2 = node;
        Node node3 = null;
        List<Node> list = TabOrderHelper.findPeers(node2);
        if (list == null) {
            ObservableList observableList = ((Parent)node).getChildrenUnmodifiable();
            return TabOrderHelper.findPreviousFocusableInList((List<Node>)observableList, observableList.size() - 1);
        }
        int n = list.indexOf((Object)node2);
        node3 = TabOrderHelper.findPreviousFocusableInList(list, n - 1);
        while (node3 == null && node2.getParent() != parent) {
            Parent parent2 = node2.getParent();
            if (parent2 != null) {
                ParentTraversalEngine parentTraversalEngine = ParentHelper.getTraversalEngine(parent2);
                if (parentTraversalEngine != null ? parentTraversalEngine.isParentTraversable() : parent2.isFocusTraversable()) {
                    node3 = parent2;
                } else {
                    List<Node> list2 = TabOrderHelper.findPeers((Node)parent2);
                    if (list2 != null) {
                        int n2 = list2.indexOf((Object)parent2);
                        node3 = TabOrderHelper.findPreviousFocusableInList(list2, n2 - 1);
                    }
                }
            }
            node2 = parent2;
        }
        return node3;
    }

    private static List<Node> findPeers(Node node) {
        ObservableList observableList = null;
        Parent parent = node.getParent();
        if (parent != null) {
            observableList = parent.getChildrenUnmodifiable();
        }
        return observableList;
    }

    private static Node findNextFocusableInList(List<Node> list, int n) {
        for (int i = n; i < list.size(); ++i) {
            Node node;
            ObservableList observableList;
            ParentTraversalEngine parentTraversalEngine;
            Node node2 = list.get(i);
            if (TabOrderHelper.isDisabledOrInvisible(node2)) continue;
            ParentTraversalEngine parentTraversalEngine2 = parentTraversalEngine = node2 instanceof Parent ? ParentHelper.getTraversalEngine((Parent)node2) : null;
            if (parentTraversalEngine != null ? parentTraversalEngine.isParentTraversable() : node2.isFocusTraversable()) {
                return node2;
            }
            if (!(node2 instanceof Parent)) continue;
            if (parentTraversalEngine != null && parentTraversalEngine.canTraverse()) {
                observableList = parentTraversalEngine.selectFirst();
                if (observableList == null) continue;
                return observableList;
            }
            observableList = ((Parent)node2).getChildrenUnmodifiable();
            if (observableList.size() <= 0 || (node = TabOrderHelper.findNextFocusableInList((List<Node>)observableList, 0)) == null) continue;
            return node;
        }
        return null;
    }

    public static Node findNextFocusablePeer(Node node, Parent parent, boolean bl) {
        int n;
        List<Node> list;
        Node node2 = node;
        Node node3 = null;
        if (bl && node instanceof Parent) {
            node3 = TabOrderHelper.findNextFocusableInList((List<Node>)((Parent)node).getChildrenUnmodifiable(), 0);
        }
        if (node3 == null) {
            list = TabOrderHelper.findPeers(node2);
            if (list == null) {
                return null;
            }
            n = list.indexOf((Object)node2);
            node3 = TabOrderHelper.findNextFocusableInList(list, n + 1);
        }
        while (node3 == null && node2.getParent() != parent) {
            Parent parent2 = node2.getParent();
            if (parent2 != null && (list = TabOrderHelper.findPeers((Node)parent2)) != null) {
                n = list.indexOf((Object)parent2);
                node3 = TabOrderHelper.findNextFocusableInList(list, n + 1);
            }
            node2 = parent2;
        }
        return node3;
    }

    public static Node getFirstTargetNode(Parent parent) {
        ObservableList observableList;
        if (parent == null || TabOrderHelper.isDisabledOrInvisible((Node)parent)) {
            return null;
        }
        ParentTraversalEngine parentTraversalEngine = ParentHelper.getTraversalEngine(parent);
        if (parentTraversalEngine != null && parentTraversalEngine.canTraverse() && (observableList = parentTraversalEngine.selectFirst()) != null) {
            return observableList;
        }
        observableList = parent.getChildrenUnmodifiable();
        for (Node node : observableList) {
            Node node2;
            ParentTraversalEngine parentTraversalEngine2;
            if (TabOrderHelper.isDisabledOrInvisible(node)) continue;
            ParentTraversalEngine parentTraversalEngine3 = parentTraversalEngine2 = node instanceof Parent ? ParentHelper.getTraversalEngine((Parent)node) : null;
            if (parentTraversalEngine2 != null ? parentTraversalEngine2.isParentTraversable() : node.isFocusTraversable()) {
                return node;
            }
            if (!(node instanceof Parent) || (node2 = TabOrderHelper.getFirstTargetNode((Parent)node)) == null) continue;
            return node2;
        }
        return null;
    }

    public static Node getLastTargetNode(Parent parent) {
        ObservableList observableList;
        if (parent == null || TabOrderHelper.isDisabledOrInvisible((Node)parent)) {
            return null;
        }
        ParentTraversalEngine parentTraversalEngine = ParentHelper.getTraversalEngine(parent);
        if (parentTraversalEngine != null && parentTraversalEngine.canTraverse() && (observableList = parentTraversalEngine.selectLast()) != null) {
            return observableList;
        }
        observableList = parent.getChildrenUnmodifiable();
        for (int i = observableList.size() - 1; i >= 0; --i) {
            Node node;
            Node node2 = (Node)observableList.get(i);
            if (TabOrderHelper.isDisabledOrInvisible(node2)) continue;
            if (node2 instanceof Parent && (node = TabOrderHelper.getLastTargetNode((Parent)node2)) != null) {
                return node;
            }
            ParentTraversalEngine parentTraversalEngine2 = node = node2 instanceof Parent ? ParentHelper.getTraversalEngine((Parent)node2) : null;
            if (!(node != null ? node.isParentTraversable() : node2.isFocusTraversable())) continue;
            return node2;
        }
        return null;
    }
}

