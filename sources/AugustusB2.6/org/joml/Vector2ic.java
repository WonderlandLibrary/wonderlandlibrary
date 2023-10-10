// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public interface Vector2ic
{
    int x();
    
    int y();
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    IntBuffer get(final IntBuffer p0);
    
    IntBuffer get(final int p0, final IntBuffer p1);
    
    Vector2ic getToAddress(final long p0);
    
    Vector2i sub(final Vector2ic p0, final Vector2i p1);
    
    Vector2i sub(final int p0, final int p1, final Vector2i p2);
    
    long lengthSquared();
    
    double length();
    
    double distance(final Vector2ic p0);
    
    double distance(final int p0, final int p1);
    
    long distanceSquared(final Vector2ic p0);
    
    long distanceSquared(final int p0, final int p1);
    
    long gridDistance(final Vector2ic p0);
    
    long gridDistance(final int p0, final int p1);
    
    Vector2i add(final Vector2ic p0, final Vector2i p1);
    
    Vector2i add(final int p0, final int p1, final Vector2i p2);
    
    Vector2i mul(final int p0, final Vector2i p1);
    
    Vector2i mul(final Vector2ic p0, final Vector2i p1);
    
    Vector2i mul(final int p0, final int p1, final Vector2i p2);
    
    Vector2i div(final float p0, final Vector2i p1);
    
    Vector2i div(final int p0, final Vector2i p1);
    
    Vector2i negate(final Vector2i p0);
    
    Vector2i min(final Vector2ic p0, final Vector2i p1);
    
    Vector2i max(final Vector2ic p0, final Vector2i p1);
    
    int maxComponent();
    
    int minComponent();
    
    Vector2i absolute(final Vector2i p0);
    
    int get(final int p0) throws IllegalArgumentException;
    
    boolean equals(final int p0, final int p1);
}
