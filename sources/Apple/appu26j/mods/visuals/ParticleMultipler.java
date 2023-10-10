package appu26j.mods.visuals;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventAttack2;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

@ModInterface(name = "Particle Multipler", description = "Multiples particles when attacking someone.", category = Category.VISUALS)
public class ParticleMultipler extends Mod
{
    public ParticleMultipler()
    {
        this.addSetting(new Setting("Multipler", this, 1, 3, 10, 1));
    }
    
    @Subscribe
    public void onAttack(EventAttack2 e)
    {
        int multipler = (int) this.getSetting("Multipler").getSliderValue();
        
        if (multipler != 1)
        {
            Entity entity = e.getTarget();
            
            if (entity instanceof EntityLivingBase)
            {
                boolean critical = this.mc.thePlayer.fallDistance > 0.0F && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null;
                float enchantment = EnchantmentHelper.getModifierForCreature(this.mc.thePlayer.getHeldItem(), ((EntityLivingBase) entity).getCreatureAttribute());
                
                for (int i = 1; i < multipler; i++)
                {
                    if (critical)
                    {
                        this.mc.thePlayer.onCriticalHit(entity);
                    }
                    
                    if (enchantment > 0)
                    {
                        this.mc.thePlayer.onEnchantmentCritical(entity);
                    }
                }
            }
        }
    }
}
