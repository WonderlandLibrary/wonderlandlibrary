/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Group
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import com.sun.javafx.scene.ParentHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;

public class GroupHelper
extends ParentHelper {
    private static final GroupHelper theInstance = new GroupHelper();
    private static GroupAccessor groupAccessor;

    private static GroupHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Group group) {
        GroupHelper.setHelper((Node)group, GroupHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return super.createPeerImpl(node);
    }

    @Override
    protected Bounds computeLayoutBoundsImpl(Node node) {
        groupAccessor.doComputeLayoutBounds(node);
        return super.computeLayoutBoundsImpl(node);
    }

    public static void setGroupAccessor(GroupAccessor groupAccessor) {
        if (GroupHelper.groupAccessor != null) {
            throw new IllegalStateException();
        }
        GroupHelper.groupAccessor = groupAccessor;
    }

    static {
        Utils.forceInit(Group.class);
    }

    public static interface GroupAccessor {
        public Bounds doComputeLayoutBounds(Node var1);
    }
}

