package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import java.util.Random;

@Module.Mod
public class TriggerBot extends Module
{
    @Op(min = 1.0, max = 10.0, increment = 0.5, name = "Delay")
    public double attackDelay;
    @Op(min = 1.0, max = 8.0, increment = 0.25, name = "Range")
    public double range;
    public double delay;
    @Op(name = "Random")
    public boolean random;
    private int[] nums;
    
    public TriggerBot() {
        this.nums = new int[] { 3, 4, 5, 6, 7, 8, 9 };
    }
    
    public int getRandom(final int[] array) {
        final int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    
    @EventTarget(3)
    public void onUpdate(final UpdateEvent event) {
        if (ClientUtils.mc().objectMouseOver.entityHit != null && this.isEntityValid(ClientUtils.mc().objectMouseOver.entityHit)) {
            ++this.delay;
            if (this.delay >= (this.random ? this.getRandom(this.nums) : this.attackDelay)) {
                this.attack((EntityLivingBase)ClientUtils.mc().objectMouseOver.entityHit);
                this.delay = 0.0;
            }
        }
    }
    
    public boolean isEntityValid(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (!ClientUtils.player().isEntityAlive() || !entityLiving.isEntityAlive() || entityLiving.getDistanceToEntity(ClientUtils.player()) > (ClientUtils.player().canEntityBeSeen(entityLiving) ? this.range : 3.0)) {
                return false;
            }
            if (entityLiving instanceof EntityPlayer) {
                final EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                return !FriendManager.isFriend(entityPlayer.getName());
            }
        }
        return false;
    }
    
    private void attack(final EntityLivingBase ent) {
        ClientUtils.player().swingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(ClientUtils.player().getHeldItem(), ent.getCreatureAttribute());
        final boolean vanillaCrit = ClientUtils.player().fallDistance > 0.0f && !ClientUtils.player().onGround && !ClientUtils.player().isOnLadder() && !ClientUtils.player().isInWater() && !ClientUtils.player().isPotionActive(Potion.blindness) && ClientUtils.player().ridingEntity == null;
        ClientUtils.player().sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        if (vanillaCrit) {
            ClientUtils.player().onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            ClientUtils.player().onEnchantmentCritical(ent);
        }
    }
}
