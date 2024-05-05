package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.module.modules.combat.HitboxesModule;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @ModifyVariable(
            method = "renderHitbox",
            ordinal = 0,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private static Box onRenderHitboxEditBox(Box box) {
        if (AsteriaMenu.isClientEnabled()) {
            HitboxesModule hitboxes = Phosphor.moduleManager().getModule(HitboxesModule.class);
            if (hitboxes.isEnabled()) {
                return box.expand(hitboxes.getRenderHitboxSize());
            }
        }

        return box;
    }
}