// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import java.util.Vector;

class ProvisionNode extends Vector
{
    private static final long serialVersionUID = -4479121426311014469L;
    
    ProvisionNode(final Logger logger) {
        this.addElement(logger);
    }
}
