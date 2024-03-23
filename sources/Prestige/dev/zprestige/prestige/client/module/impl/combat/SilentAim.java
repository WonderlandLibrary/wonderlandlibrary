package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.api.interfaces.IRotatable;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.SwingHandEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.util.impl.EntityUtil;
import dev.zprestige.prestige.client.util.impl.Rotation;
import dev.zprestige.prestige.client.util.impl.RotationUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;

public class SilentAim extends Module implements IRotatable {

    public BooleanSetting weaponOnly;
    public boolean preClick;
    public boolean postClick;

    public SilentAim() {
        super("Silent Aim", Category.Combat, "Redirects missed hits to hit ones");
        weaponOnly = setting("Weapon Only", false).description("Only works with swords or axes");
    }

    @Override
    public void onEnable() {
        Prestige.Companion.getRotationManager().addRotation(this);
    }

    @Override
    public void onDisable() {
        Prestige.Companion.getRotationManager().removeRotation(this);
    }

    @Override
    public Rotation getRotation() {
        if (!preClick) {
            return null;
        }
        if (weaponOnly.getObject() && !(getMc().player.getMainHandStack().getItem() instanceof SwordItem) && !(getMc().player.getMainHandStack().getItem() instanceof AxeItem)) {
            return null;
        }
        HitResult hitResult = getMc().crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.MISS) {
            return null;
        }
        PlayerEntity playerEntity = EntityUtil.INSTANCE.getPlayer();
        if (playerEntity == null || ((getMc().player.distanceTo(playerEntity) > 3 && !getMc().player.canSee(playerEntity)))) {
            return null;
        }
        postClick = true;
        getMc().player.swingHand(Hand.MAIN_HAND);
        getMc().interactionManager.attackEntity(getMc().player, playerEntity);
        postClick = false;
        preClick = false;
        return RotationUtil.INSTANCE.getNeededRotations(playerEntity.getEyePos());
    }

    @EventListener
    public void event(SwingHandEvent event) {
        if (!postClick) {
            if (weaponOnly.getObject() && !(getMc().player.getMainHandStack().getItem() instanceof SwordItem) && !(getMc().player.getMainHandStack().getItem() instanceof AxeItem)) {
                return;
            }
            HitResult hitResult = getMc().crosshairTarget;
            if (hitResult == null || hitResult.getType() != HitResult.Type.MISS) {
                return;
            }
            PlayerEntity playerEntity = EntityUtil.INSTANCE.getPlayer();
            if (playerEntity == null || ((getMc().player.distanceTo(playerEntity) > 3 && !getMc().player.canSee(playerEntity)))) {
                return;
            }
        }
        preClick = true;
        event.setCancelled();
    }
}
