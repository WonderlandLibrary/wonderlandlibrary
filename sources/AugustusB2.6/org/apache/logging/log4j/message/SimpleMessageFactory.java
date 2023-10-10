// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public final class SimpleMessageFactory extends AbstractMessageFactory
{
    public static final SimpleMessageFactory INSTANCE;
    private static final long serialVersionUID = 4418995198790088516L;
    
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return new SimpleMessage(message);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return new SimpleMessage(message);
    }
    
    static {
        INSTANCE = new SimpleMessageFactory();
    }
}
