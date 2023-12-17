// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArmor;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemAir;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.init.MobEffects;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "WaterSpeed", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class WaterSpeed extends Module
{
    public BooleanSetting speedCheck;
    public BooleanSetting smart;
    public SliderSetting speed;
    public BooleanSetting miniJump;
    public static float tick;
    
    public WaterSpeed() {
        this.speedCheck = new BooleanSetting("Speed Potion Check", false);
        this.smart = new BooleanSetting("Smart", false);
        this.speed = new SliderSetting("Speed", 0.4f, 0.1f, 1.0f, 0.01f, () -> !this.smart.get());
        this.miniJump = new BooleanSetting("Mini Jump", true);
        this.add(this.speedCheck, this.smart, this.speed, this.miniJump);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (!this.smart.get()) {
            final Minecraft mc = WaterSpeed.mc;
            if (!Minecraft.player.collidedHorizontally) {
                final Minecraft mc2 = WaterSpeed.mc;
                if (Minecraft.player.isInWater()) {
                    if (this.speedCheck.get()) {
                        final Minecraft mc3 = WaterSpeed.mc;
                        if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                            return;
                        }
                    }
                    MoveUtility.setMotion(this.speed.getFloatValue());
                }
            }
            return;
        }
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        final Minecraft mc4 = WaterSpeed.mc;
        Minecraft.player.getArmorInventoryList().forEach(stacks::add);
        stacks.removeIf(w -> w.getItem() instanceof ItemAir);
        float motion = MoveUtility.getSpeed();
        boolean hasEnchantments = false;
        for (final ItemStack stack : stacks) {
            int enchantmentLevel = 0;
            if (this.buildEnchantments(stack, 1.0f)) {
                enchantmentLevel = 1;
            }
            if (enchantmentLevel > 0) {
                motion = 0.5f;
                hasEnchantments = true;
            }
        }
        final Minecraft mc5 = WaterSpeed.mc;
        if (Minecraft.player.collidedHorizontally) {
            WaterSpeed.tick = 0.0f;
            return;
        }
        final Minecraft mc6 = WaterSpeed.mc;
        if (!Minecraft.player.isInWater()) {
            return;
        }
        if (WaterSpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
            final Minecraft mc7 = WaterSpeed.mc;
            if (!Minecraft.player.isSneaking()) {
                final WorldClient world = WaterSpeed.mc.world;
                final Minecraft mc8 = WaterSpeed.mc;
                if (!(world.getBlockState(Minecraft.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
                    final Minecraft mc9 = WaterSpeed.mc;
                    Minecraft.player.motionY = 0.11999999731779099;
                }
            }
        }
        if (WaterSpeed.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final Minecraft mc10 = WaterSpeed.mc;
            Minecraft.player.motionY = -0.3499999940395355;
        }
        if (this.speedCheck.get()) {
            final Minecraft mc11 = WaterSpeed.mc;
            if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                WaterSpeed.tick = 0.0f;
                return;
            }
        }
        if (this.miniJump.get() && hasEnchantments) {
            final WorldClient world2 = WaterSpeed.mc.world;
            final Minecraft mc12 = WaterSpeed.mc;
            if (world2.getBlockState(Minecraft.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir && WaterSpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
                ++WaterSpeed.tick;
                final Minecraft mc13 = WaterSpeed.mc;
                Minecraft.player.motionY = 0.11999999731779099;
            }
        }
        if (hasEnchantments) {
            ++WaterSpeed.tick;
            MoveUtility.setMotion(0.4000000059604645);
            Strafe.oldSpeed = 0.4000000059604645;
        }
    }
    
    @Override
    public void onDisable() {
        WaterSpeed.tick = 0.0f;
        super.onDisable();
    }
    
    public boolean buildEnchantments(final ItemStack stack, final float strenght) {
        return stack != null && stack.getItem() instanceof ItemArmor && EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) > 0;
    }
    
    static {
        WaterSpeed.tick = 0.0f;
    }
}
