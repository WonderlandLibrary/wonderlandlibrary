/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "AttackEffects", description = "Renders an effect when you attack someone", category = Category.RENDER)
public final class AttackEffects extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Blood", "Blood", "Criticals", "Magic");
    private final NumberSetting amount = new NumberSetting("Amount", this, 5, 1, 10, 1);
    private final BooleanSetting sound = new BooleanSetting("Sound", this, true);

    private EntityLivingBase target;

    @Override
    public void onUpdateAlwaysInGui() {
        sound.hidden = !mode.is("Blood");
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            if (mc.thePlayer.ticksExisted > 3) {
                switch (mode.getMode()) {
                    case "Blood":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;

                    case "Criticals":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                        break;

                    case "Magic":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
                        break;
                }
            }

            target = null;
        }
    }
}
