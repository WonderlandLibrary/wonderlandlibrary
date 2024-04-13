package net.smoothboot.client.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.NumberSetting;

public class TriggerBot extends Mod {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public NumberSetting triggerbotdelay = new NumberSetting("Delay", 0, 1, 1, 0.01);
    public BooleanSetting triggerbotItem = new BooleanSetting("Check item", false);
    public BooleanSetting triggerbotSword = new BooleanSetting("Check sword", true);
    public BooleanSetting triggerbotTeam = new BooleanSetting("Team check", true);
    public BooleanSetting triggerbotCrit = new BooleanSetting("Critical timing", false);

    public TriggerBot() {
        super("Trigger Bot", "Hits target when aiming at them", Category.Combat);
        addsettings(triggerbotdelay, triggerbotItem, triggerbotSword, triggerbotTeam, triggerbotCrit);
    }


    @Override
    public void onTick() {
        Entity target = getTarget();
        if (nullCheck()) {
            return;
        }
        if (triggerbotItem.isEnabled() && mc.player.isUsingItem()) {
            return;
        }
        if (triggerbotSword.isEnabled() && !swordCheck()) {
            return;
        }
        if (target != null && isValid(target) && mc.currentScreen == null) {
            float fallDist = mc.player.fallDistance;
            if (triggerbotTeam.isEnabled() && target.isTeammate(mc.player)) {
                return;
            }
            if (triggerbotCrit.isEnabled() && fallDist <= 0.01F && !mc.player.isOnGround()) {
                return;
            }
            if (mc.player.getAttackCooldownProgress(0) >= triggerbotdelay.getValue()) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
            } else if (mc.player.getAttackCooldownProgress(0) >= triggerbotdelay.getValue()) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
        super.onTick();
    }

    private Entity getTarget() {
        HitResult hitResult = mc.crosshairTarget;

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            return entityHitResult.getEntity();
        }

        return null;
    }

    public boolean swordCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.NETHERITE_SWORD) || getItem.isOf(Items.DIAMOND_SWORD) || getItem.isOf(Items.GOLDEN_SWORD) || getItem.isOf(Items.IRON_SWORD) || getItem.isOf(Items.STONE_SWORD) || getItem.isOf(Items.WOODEN_SWORD));
    }


    private boolean isValid(Entity entity) {
        return (entity instanceof PlayerEntity && !((PlayerEntity) entity).isDead());
    }

}