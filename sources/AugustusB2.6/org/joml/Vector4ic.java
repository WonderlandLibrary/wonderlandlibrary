// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Vector4ic
{
    int x();
    
    int y();
    
    int z();
    
    int w();
    
    IntBuffer get(final IntBuffer p0);
    
    IntBuffer get(final int p0, final IntBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Vector4ic getToAddress(final long p0);
    
    Vector4i sub(final Vector4ic p0, final Vector4i p1);
    
    Vector4i sub(final int p0, final int p1, final int p2, final int p3, final Vector4i p4);
    
    Vector4i add(final Vector4ic p0, final Vector4i p1);
    
    Vector4i add(final int p0, final int p1, final int p2, final int p3, final Vector4i p4);
    
    Vector4i mul(final Vector4ic p0, final Vector4i p1);
    
    Vector4i div(final Vector4ic p0, final Vector4i p1);
    
    Vector4i mul(final int p0, final Vector4i p1);
    
    Vector4i div(final float p0, final Vector4i p1);
    
    Vector4i div(final int p0, final Vector4i p1);
    
    long lengthSquared();
    
    double length();
    
    double distance(final Vector4ic p0);
    
    double distance(final int p0, final int p1, final int p2, final int p3);
    
    long gridDistance(final Vector4ic p0);
    
    long gridDistance(final int p0, final int p1, final int p2, final int p3);
    
    int distanceSquared(final Vector4ic p0);
    
    int distanceSquared(final int p0, final int p1, final int p2, final int p3);
    
    int dot(final Vector4ic p0);
    
    Vector4i negate(final Vector4i p0);
    
    Vector4i min(final Vector4ic p0, final Vector4i p1);
    
    Vector4i max(final Vector4ic p0, final Vector4i p1);
    
    int get(final int p0) throws IllegalArgumentException;
    
    int maxComponent();
    
    int minComponent();
    
    Vector4i absolute(final Vector4i p0);
    
    boolean equals(final int p0, final int p1, final int p2, final int p3);
}
