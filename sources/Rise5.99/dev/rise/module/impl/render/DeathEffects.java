/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "DeathEffects", description = "Renders a death effect when your opponent dies", category = Category.RENDER)
public final class DeathEffects extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Lightning", "Lightning", "Flames", "Cloud", "Blood");
    private final NumberSetting amount = new NumberSetting("Amount", this, 1, 1, 10, 1);
    private final BooleanSetting sound = new BooleanSetting("Sound", this, true);

    private EntityLivingBase target;
    private int entityID;

    @Override
    public void onUpdateAlwaysInGui() {
        amount.hidden = mode.is("Lightning") || mode.is("Blood");
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase && ((EntityLivingBase) event.getTarget()).getHealth() > 0)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        entityID = 0;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && !mc.theWorld.loadedEntityList.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 100) {
            if (mc.thePlayer.ticksExisted > 3) {
                switch (mode.getMode()) {
                    case "Lightning":
                        final EntityLightningBolt entityLightningBolt = new EntityLightningBolt(mc.theWorld, target.posX, target.posY, target.posZ);
                        mc.theWorld.addEntityToWorld(entityID--, entityLightningBolt);

                        if (sound.isEnabled()) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("ambient.weather.thunder"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
                        break;

                    case "Flames":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.FLAME);

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("item.fireCharge.use"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;

                    case "Cloud":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CLOUD);

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("fireworks.twinkle"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;

                    case "Blood":
                        for (int i = 0; i < 50; i++)
                            mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;
                }
            }

            target = null;
        }
    }
}
