package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.block.*;

public class EventCollide extends Event
{
    private Entity entity;
    private double posX;
    private double posY;
    private double posZ;
    private AxisAlignedBB boundingBox;
    private Block block;
    
    public EventCollide(final Entity entity, final double posX, final double posY, final double posZ, final AxisAlignedBB boundingBox, final Block block) {
        this.entity = entity;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.boundingBox = boundingBox;
        this.block = block;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
