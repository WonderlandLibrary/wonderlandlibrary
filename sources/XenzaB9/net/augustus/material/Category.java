// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material;

import net.augustus.utils.skid.tomorrow.AnimationUtils;
import net.augustus.modules.Categorys;

public class Category
{
    public Categorys moduleType;
    public boolean needRemove;
    public AnimationUtils rollAnim2;
    float anim;
    public boolean show;
    public float modsY2;
    public float modsY3;
    
    public Category(final Categorys mt, final int i, final boolean b) {
        this.rollAnim2 = new AnimationUtils();
        this.modsY2 = 0.0f;
        this.modsY3 = 0.0f;
        this.moduleType = mt;
        this.anim = (float)i;
        this.show = b;
    }
}
