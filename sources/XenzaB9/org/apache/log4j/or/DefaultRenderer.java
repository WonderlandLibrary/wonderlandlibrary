// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.or;

class DefaultRenderer implements ObjectRenderer
{
    @Override
    public String doRender(final Object o) {
        try {
            return o.toString();
        }
        catch (final Exception ex) {
            return ex.toString();
        }
    }
}
