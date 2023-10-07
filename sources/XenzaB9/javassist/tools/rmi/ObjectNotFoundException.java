// 
// Decompiled by Procyon v0.6.0
// 

package javassist.tools.rmi;

public class ObjectNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ObjectNotFoundException(final String name) {
        super(name + " is not exported");
    }
    
    public ObjectNotFoundException(final String name, final Exception e) {
        super(name + " because of " + e.toString());
    }
}
