// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.dummy;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class DummyMod extends Module
{
    String info;
    
    public DummyMod(final String name) {
        super(name, "Dummy", 0, Category.DUMMY, true);
    }
    
    public DummyMod(final String name, final String info) {
        super(name, "Dummy", 0, Category.DUMMY, true);
        this.info = info;
    }
    
    @Override
    public String getHudInfo() {
        return this.info;
    }
}
