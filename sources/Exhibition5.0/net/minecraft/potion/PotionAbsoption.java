// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class PotionAbsoption extends Potion
{
    private static final String __OBFID = "CL_00001524";
    
    protected PotionAbsoption(final int p_i45901_1_, final ResourceLocation p_i45901_2_, final boolean p_i45901_3_, final int p_i45901_4_) {
        super(p_i45901_1_, p_i45901_2_, p_i45901_3_, p_i45901_4_);
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(final EntityLivingBase p_111187_1_, final BaseAttributeMap p_111187_2_, final int p_111187_3_) {
        p_111187_1_.setAbsorptionAmount(p_111187_1_.getAbsorptionAmount() - 4 * (p_111187_3_ + 1));
        super.removeAttributesModifiersFromEntity(p_111187_1_, p_111187_2_, p_111187_3_);
    }
    
    @Override
    public void applyAttributesModifiersToEntity(final EntityLivingBase p_111185_1_, final BaseAttributeMap p_111185_2_, final int p_111185_3_) {
        p_111185_1_.setAbsorptionAmount(p_111185_1_.getAbsorptionAmount() + 4 * (p_111185_3_ + 1));
        super.applyAttributesModifiersToEntity(p_111185_1_, p_111185_2_, p_111185_3_);
    }
}
