// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick.util.pathfinding;

public interface PathFindingContext
{
    Mover getMover();
    
    int getSourceX();
    
    int getSourceY();
    
    int getSearchDistance();
}
