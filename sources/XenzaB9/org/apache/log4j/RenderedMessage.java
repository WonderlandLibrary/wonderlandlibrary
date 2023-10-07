// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.logging.log4j.message.Message;

public class RenderedMessage implements Message
{
    private final ObjectRenderer renderer;
    private final Object object;
    private String rendered;
    
    public RenderedMessage(final ObjectRenderer renderer, final Object object) {
        this.rendered = null;
        this.renderer = renderer;
        this.object = object;
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.rendered == null) {
            this.rendered = this.renderer.doRender(this.object);
        }
        return this.rendered;
    }
    
    @Override
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    @Override
    public Object[] getParameters() {
        return null;
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
}
