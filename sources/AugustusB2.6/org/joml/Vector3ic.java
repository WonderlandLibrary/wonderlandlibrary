// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Vector3ic
{
    int x();
    
    int y();
    
    int z();
    
    IntBuffer get(final IntBuffer p0);
    
    IntBuffer get(final int p0, final IntBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Vector3ic getToAddress(final long p0);
    
    Vector3i sub(final Vector3ic p0, final Vector3i p1);
    
    Vector3i sub(final int p0, final int p1, final int p2, final Vector3i p3);
    
    Vector3i add(final Vector3ic p0, final Vector3i p1);
    
    Vector3i add(final int p0, final int p1, final int p2, final Vector3i p3);
    
    Vector3i mul(final int p0, final Vector3i p1);
    
    Vector3i mul(final Vector3ic p0, final Vector3i p1);
    
    Vector3i mul(final int p0, final int p1, final int p2, final Vector3i p3);
    
    Vector3i div(final float p0, final Vector3i p1);
    
    Vector3i div(final int p0, final Vector3i p1);
    
    long lengthSquared();
    
    double length();
    
    double distance(final Vector3ic p0);
    
    double distance(final int p0, final int p1, final int p2);
    
    long gridDistance(final Vector3ic p0);
    
    long gridDistance(final int p0, final int p1, final int p2);
    
    long distanceSquared(final Vector3ic p0);
    
    long distanceSquared(final int p0, final int p1, final int p2);
    
    Vector3i negate(final Vector3i p0);
    
    Vector3i min(final Vector3ic p0, final Vector3i p1);
    
    Vector3i max(final Vector3ic p0, final Vector3i p1);
    
    int get(final int p0) throws IllegalArgumentException;
    
    int maxComponent();
    
    int minComponent();
    
    Vector3i absolute(final Vector3i p0);
    
    boolean equals(final int p0, final int p1, final int p2);
}
