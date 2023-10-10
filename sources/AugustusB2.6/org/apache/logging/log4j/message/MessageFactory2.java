// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public interface MessageFactory2 extends MessageFactory
{
    Message newMessage(final CharSequence charSequence);
    
    Message newMessage(final String message, final Object p0);
    
    Message newMessage(final String message, final Object p0, final Object p1);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
}
