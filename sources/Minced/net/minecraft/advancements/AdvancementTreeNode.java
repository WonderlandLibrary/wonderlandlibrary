// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import java.util.Iterator;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.List;

public class AdvancementTreeNode
{
    private final Advancement advancement;
    private final AdvancementTreeNode parent;
    private final AdvancementTreeNode sibling;
    private final int index;
    private final List<AdvancementTreeNode> children;
    private AdvancementTreeNode ancestor;
    private AdvancementTreeNode thread;
    private int x;
    private float y;
    private float mod;
    private float change;
    private float shift;
    
    public AdvancementTreeNode(final Advancement advancementIn, @Nullable final AdvancementTreeNode parentIn, @Nullable final AdvancementTreeNode siblingIn, final int indexIn, final int xIn) {
        this.children = (List<AdvancementTreeNode>)Lists.newArrayList();
        if (advancementIn.getDisplay() == null) {
            throw new IllegalArgumentException("Can't position an invisible advancement!");
        }
        this.advancement = advancementIn;
        this.parent = parentIn;
        this.sibling = siblingIn;
        this.index = indexIn;
        this.ancestor = this;
        this.x = xIn;
        this.y = -1.0f;
        AdvancementTreeNode advancementtreenode = null;
        for (final Advancement advancement : advancementIn.getChildren()) {
            advancementtreenode = this.buildSubTree(advancement, advancementtreenode);
        }
    }
    
    @Nullable
    private AdvancementTreeNode buildSubTree(final Advancement advancementIn, @Nullable AdvancementTreeNode previous) {
        if (advancementIn.getDisplay() != null) {
            previous = new AdvancementTreeNode(advancementIn, this, previous, this.children.size() + 1, this.x + 1);
            this.children.add(previous);
        }
        else {
            for (final Advancement advancement : advancementIn.getChildren()) {
                previous = this.buildSubTree(advancement, previous);
            }
        }
        return previous;
    }
    
    private void firstWalk() {
        if (this.children.isEmpty()) {
            if (this.sibling != null) {
                this.y = this.sibling.y + 1.0f;
            }
            else {
                this.y = 0.0f;
            }
        }
        else {
            AdvancementTreeNode advancementtreenode = null;
            for (final AdvancementTreeNode advancementtreenode2 : this.children) {
                advancementtreenode2.firstWalk();
                advancementtreenode = advancementtreenode2.apportion((advancementtreenode == null) ? advancementtreenode2 : advancementtreenode);
            }
            this.executeShifts();
            final float f = (this.children.get(0).y + this.children.get(this.children.size() - 1).y) / 2.0f;
            if (this.sibling != null) {
                this.y = this.sibling.y + 1.0f;
                this.mod = this.y - f;
            }
            else {
                this.y = f;
            }
        }
    }
    
    private float secondWalk(final float p_192319_1_, final int p_192319_2_, float p_192319_3_) {
        this.y += p_192319_1_;
        this.x = p_192319_2_;
        if (this.y < p_192319_3_) {
            p_192319_3_ = this.y;
        }
        for (final AdvancementTreeNode advancementtreenode : this.children) {
            p_192319_3_ = advancementtreenode.secondWalk(p_192319_1_ + this.mod, p_192319_2_ + 1, p_192319_3_);
        }
        return p_192319_3_;
    }
    
    private void thirdWalk(final float yIn) {
        this.y += yIn;
        for (final AdvancementTreeNode advancementtreenode : this.children) {
            advancementtreenode.thirdWalk(yIn);
        }
    }
    
    private void executeShifts() {
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = this.children.size() - 1; i >= 0; --i) {
            final AdvancementTreeNode advancementTreeNode;
            final AdvancementTreeNode advancementtreenode = advancementTreeNode = this.children.get(i);
            advancementTreeNode.y += f;
            final AdvancementTreeNode advancementTreeNode2 = advancementtreenode;
            advancementTreeNode2.mod += f;
            f2 += advancementtreenode.change;
            f += advancementtreenode.shift + f2;
        }
    }
    
    @Nullable
    private AdvancementTreeNode getFirstChild() {
        if (this.thread != null) {
            return this.thread;
        }
        return this.children.isEmpty() ? null : this.children.get(0);
    }
    
    @Nullable
    private AdvancementTreeNode getLastChild() {
        if (this.thread != null) {
            return this.thread;
        }
        return this.children.isEmpty() ? null : this.children.get(this.children.size() - 1);
    }
    
    private AdvancementTreeNode apportion(AdvancementTreeNode nodeIn) {
        if (this.sibling == null) {
            return nodeIn;
        }
        AdvancementTreeNode advancementtreenode = this;
        AdvancementTreeNode advancementtreenode2 = this;
        AdvancementTreeNode advancementtreenode3 = this.sibling;
        AdvancementTreeNode advancementtreenode4 = this.parent.children.get(0);
        float f = this.mod;
        float f2 = this.mod;
        float f3 = advancementtreenode3.mod;
        float f4 = advancementtreenode4.mod;
        while (advancementtreenode3.getLastChild() != null && advancementtreenode.getFirstChild() != null) {
            advancementtreenode3 = advancementtreenode3.getLastChild();
            advancementtreenode = advancementtreenode.getFirstChild();
            advancementtreenode4 = advancementtreenode4.getFirstChild();
            advancementtreenode2 = advancementtreenode2.getLastChild();
            advancementtreenode2.ancestor = this;
            final float f5 = advancementtreenode3.y + f3 - (advancementtreenode.y + f) + 1.0f;
            if (f5 > 0.0f) {
                advancementtreenode3.getAncestor(this, nodeIn).moveSubtree(this, f5);
                f += f5;
                f2 += f5;
            }
            f3 += advancementtreenode3.mod;
            f += advancementtreenode.mod;
            f4 += advancementtreenode4.mod;
            f2 += advancementtreenode2.mod;
        }
        if (advancementtreenode3.getLastChild() != null && advancementtreenode2.getLastChild() == null) {
            advancementtreenode2.thread = advancementtreenode3.getLastChild();
            final AdvancementTreeNode advancementTreeNode = advancementtreenode2;
            advancementTreeNode.mod += f3 - f2;
        }
        else {
            if (advancementtreenode.getFirstChild() != null && advancementtreenode4.getFirstChild() == null) {
                advancementtreenode4.thread = advancementtreenode.getFirstChild();
                final AdvancementTreeNode advancementTreeNode2 = advancementtreenode4;
                advancementTreeNode2.mod += f - f4;
            }
            nodeIn = this;
        }
        return nodeIn;
    }
    
    private void moveSubtree(final AdvancementTreeNode nodeIn, final float p_192316_2_) {
        final float f = (float)(nodeIn.index - this.index);
        if (f != 0.0f) {
            nodeIn.change -= p_192316_2_ / f;
            this.change += p_192316_2_ / f;
        }
        nodeIn.shift += p_192316_2_;
        nodeIn.y += p_192316_2_;
        nodeIn.mod += p_192316_2_;
    }
    
    private AdvancementTreeNode getAncestor(final AdvancementTreeNode p_192326_1_, final AdvancementTreeNode p_192326_2_) {
        return (this.ancestor != null && p_192326_1_.parent.children.contains(this.ancestor)) ? this.ancestor : p_192326_2_;
    }
    
    private void updatePosition() {
        if (this.advancement.getDisplay() != null) {
            this.advancement.getDisplay().setPosition((float)this.x, this.y);
        }
        if (!this.children.isEmpty()) {
            for (final AdvancementTreeNode advancementtreenode : this.children) {
                advancementtreenode.updatePosition();
            }
        }
    }
    
    public static void layout(final Advancement root) {
        if (root.getDisplay() == null) {
            throw new IllegalArgumentException("Can't position children of an invisible root!");
        }
        final AdvancementTreeNode advancementtreenode = new AdvancementTreeNode(root, null, null, 1, 0);
        advancementtreenode.firstWalk();
        final float f = advancementtreenode.secondWalk(0.0f, 0, advancementtreenode.y);
        if (f < 0.0f) {
            advancementtreenode.thirdWalk(-f);
        }
        advancementtreenode.updatePosition();
    }
}
