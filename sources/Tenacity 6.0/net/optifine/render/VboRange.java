// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.render;

import net.optifine.util.LinkedList;

public class VboRange
{
    private int position;
    private int size;
    private LinkedList.Node<VboRange> node;
    
    public VboRange() {
        this.position = -1;
        this.size = 0;
        this.node = new LinkedList.Node<VboRange>(this);
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getPositionNext() {
        return this.position + this.size;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public LinkedList.Node<VboRange> getNode() {
        return this.node;
    }
    
    public VboRange getPrev() {
        final LinkedList.Node<VboRange> node = this.node.getPrev();
        return (node == null) ? null : node.getItem();
    }
    
    public VboRange getNext() {
        final LinkedList.Node<VboRange> node = this.node.getNext();
        return (node == null) ? null : node.getItem();
    }
    
    @Override
    public String toString() {
        return "" + this.position + "/" + this.size + "/" + (this.position + this.size);
    }
}
