/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.event.events.SettingChangeEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.setting.SettingGroup;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.utils.Timer;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

@Module.Registration(name="AutoClicker", description="Clicks your left mouse button quickly, can also blockhit", alwaysListening=true, category=Module.Category.COMBAT)
public class AutoClicker
extends Module {
    private final DoubleSetting minCps = this.doubleSetting("MinCPS", 10.0, 1.0, 20.0).description("The minimum amount of clicks per second");
    private final DoubleSetting maxCps = this.doubleSetting("MaxCPS", 10.0, 1.0, 20.0).description("The maximum amount of clicks per second");
    private final BooleanSetting weapon = this.booleanSetting("WeaponOnly", false).description("Makes auto clicker only work when a sword is in your hand");
    private final SettingGroup blocking = new SettingGroup("BlockHit", this);
    private final BooleanSetting autoBlock = new BooleanSetting("Enabled", this.blocking, false).description("Automatically blockhits to deal more knockback to enemies");
    private final IntSetting chance = new IntSetting("Chance", this.blocking, 60, 0, 100).description("The chance that a blockhit will be done");
    private final IntSetting delay = new IntSetting("Delay", this.blocking, 200, 0, 500).description("The minimum amount of delay in milliseconds between blockhits");
    private final IntSetting minHold = new IntSetting("HoldTime", this.blocking, 50, 20, 500).description("The amount of time in milliseconds that the block button will be held down");
    private boolean breaking = false;
    private double tick = 0.0;
    private long releaseBlock;
    private final Random random = new Random();
    private double currentSpeed;
    Timer blockDelay = new Timer().reset();

    @SubscribeEvent
    public void onSettingChange(SettingChangeEvent event) {
        if (event.getSetting() == this.minCps) {
            if (!((Double)this.minCps.getValue() > (Double)this.maxCps.getValue())) return;
            this.minCps.setValueSilent(this.maxCps.getValue());
            return;
        }
        if (event.getSetting() != this.maxCps) return;
        if (!((Double)this.minCps.getValue() > (Double)this.maxCps.getValue())) return;
        this.maxCps.setValueSilent(this.minCps.getValue());
    }

    @Override
    public void onEnable() {
        this.blockDelay.reset();
        this.releaseBlock = -1L;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (AutoClicker.mc.currentScreen == null && Mouse.isButtonDown((int)0) && (!((Boolean)this.weapon.getValue()).booleanValue() || AutoClicker.mc.thePlayer.getHeldItem() != null && AutoClicker.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            Block blockState;
            int key = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
            BlockPos lookingBlock = AutoClicker.mc.objectMouseOver.getBlockPos();
            if (lookingBlock != null && (blockState = AutoClicker.mc.theWorld.getBlockState(lookingBlock).getBlock()) != Blocks.air && !(blockState instanceof BlockLiquid)) {
                if (!this.breaking) {
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                }
                this.breaking = true;
                return;
            }
            if (this.breaking) {
                this.breaking = false;
                KeyBinding.setKeyBindState(key, false);
                return;
            }
            this.tick += this.currentSpeed / 40.0;
            if (this.tick > 1.0) {
                this.tick -= 1.0;
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                KeyBinding.setKeyBindState(key, false);
            }
        }
        if (AutoClicker.mc.thePlayer.ticksExisted % (10 + this.random.nextInt(30)) != 0) {
            if (this.currentSpeed != 0.0) return;
        }
        double dif = (Double)this.maxCps.getValue() - (Double)this.minCps.getValue();
        dif *= this.random.nextDouble();
        this.currentSpeed = dif += ((Double)this.minCps.getValue()).doubleValue();
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (AutoClicker.mc.theWorld == null) return;
        if (AutoClicker.mc.thePlayer == null) return;
        if (!this.autoBlock.isOn()) return;
        if (!this.isEnabled()) {
            return;
        }
        BlockPos lookingBlock = AutoClicker.mc.objectMouseOver.getBlockPos();
        int key = AutoClicker.mc.gameSettings.keyBindUseItem.getKeyCode();
        if (AutoClicker.mc.thePlayer.isSprinting() && lookingBlock == null && AutoClicker.mc.currentScreen == null && Mouse.isButtonDown((int)0) && AutoClicker.mc.thePlayer.getHeldItem() != null && AutoClicker.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && !this.breaking && this.releaseBlock == -1L && this.blockDelay.passedMs(((Integer)this.delay.getValue()).intValue())) {
            EntityPlayer player;
            Iterator iterator = AutoClicker.mc.theWorld.playerEntities.iterator();
            do {
                if (!iterator.hasNext()) return;
            } while (!AntiBot.allowAttack(player = (EntityPlayer)iterator.next()) || !(AutoClicker.mc.thePlayer.getDistanceToEntity(player) < 6.0f) || player.hurtResistantTime <= 0);
            this.blockDelay.reset();
            if (this.random.nextInt(100) >= (Integer)this.chance.getValue()) return;
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            int offset = (int)(this.random.nextGaussian() * 15.0);
            this.releaseBlock = System.currentTimeMillis() + (long)offset + (long)((Integer)this.minHold.getValue()).intValue();
            return;
        }
        if (this.releaseBlock == -1L) return;
        this.blockDelay.reset();
        if (System.currentTimeMillis() <= this.releaseBlock) return;
        KeyBinding.setKeyBindState(key, false);
        this.releaseBlock = -1L;
    }
}

