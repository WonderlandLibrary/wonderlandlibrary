/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.Tengoku.Terror.module.skyblock;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Mouse;

public class AutoFarm
extends Module {
    private float prevPosZ;
    private float posX;
    private float posZ;
    private boolean canGoRight = false;
    Timer timer = new Timer();
    private boolean caca = false;
    private boolean canGoBack = false;
    private boolean canGoLeft = true;
    private boolean canGoForward = true;
    private float prevPosX;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("AutoFarm Mode", this).getValString();
        if (string.equalsIgnoreCase("Potato")) {
            Minecraft.thePlayer.rotationYaw = -90.0f;
            this.posZ = (float)Minecraft.thePlayer.posZ;
        }
        if (AutoFarm.mc.currentScreen == null) {
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld != null && AutoFarm.mc.objectMouseOver != null && AutoFarm.mc.objectMouseOver.getBlockPos() != null && AutoFarm.mc.objectMouseOver.entityHit == null && !Mouse.isButtonDown((int)0)) {
                    Block block = Minecraft.theWorld.getBlockState(AutoFarm.mc.objectMouseOver.getBlockPos()).getBlock();
                    if (string.equalsIgnoreCase("Netherwart")) {
                        Minecraft.thePlayer.rotationPitch = 20.0f;
                        Minecraft.thePlayer.rotationYaw = 180.0f;
                        Minecraft.gameSettings.keyBindAttack.pressed = block instanceof BlockNetherWart;
                        if (!Minecraft.thePlayer.isCollidedHorizontally) {
                            Minecraft.gameSettings.keyBindLeft.pressed = false;
                            Minecraft.gameSettings.keyBindRight.pressed = true;
                            Minecraft.gameSettings.keyBindForward.pressed = false;
                        } else {
                            Minecraft.gameSettings.keyBindForward.pressed = true;
                            Minecraft.gameSettings.keyBindRight.pressed = false;
                            Minecraft.gameSettings.keyBindLeft.pressed = true;
                        }
                    }
                    if (string.equalsIgnoreCase("Potato")) {
                        if (this.canGoForward) {
                            Minecraft.gameSettings.keyBindForward.pressed = true;
                            Minecraft.gameSettings.keyBindAttack.pressed = true;
                            Minecraft.gameSettings.keyBindBack.pressed = false;
                            Minecraft.gameSettings.keyBindLeft.pressed = false;
                        }
                        if (this.canGoBack) {
                            Minecraft.gameSettings.keyBindBack.pressed = true;
                            Minecraft.gameSettings.keyBindForward.pressed = false;
                            Minecraft.gameSettings.keyBindAttack.pressed = true;
                            Minecraft.gameSettings.keyBindLeft.pressed = false;
                        }
                        if (Minecraft.thePlayer.isCollidedHorizontally) {
                            if (this.canGoBack) {
                                if (!this.caca) {
                                    this.prevPosZ = this.posZ;
                                    this.caca = true;
                                }
                                if (Math.abs(this.posZ - this.prevPosZ) < 0.5f) {
                                    Minecraft.gameSettings.keyBindLeft.pressed = true;
                                    Minecraft.gameSettings.keyBindAttack.pressed = false;
                                } else {
                                    Minecraft.gameSettings.keyBindLeft.pressed = false;
                                    this.canGoForward = true;
                                    this.canGoBack = false;
                                    this.caca = false;
                                }
                            }
                            if (this.canGoForward) {
                                if (!this.caca) {
                                    this.prevPosZ = this.posZ;
                                    this.caca = true;
                                }
                                if (Math.abs(this.posZ - this.prevPosZ) < 0.5f) {
                                    Minecraft.gameSettings.keyBindLeft.pressed = true;
                                } else {
                                    Minecraft.gameSettings.keyBindLeft.pressed = false;
                                    this.canGoForward = false;
                                    this.canGoBack = true;
                                    this.caca = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timer.reset();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook && Exodus.INSTANCE.settingsManager.getSettingByModule("FallbackCheck", this).getValBoolean()) {
            this.toggle();
        }
    }

    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("FallbackCheck", this, true));
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Netherwart");
        arrayList.add("Potato");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("AutoFarm Mode", (Module)this, "Potato", arrayList));
    }

    public AutoFarm() {
        super("AutoFarm", 0, Category.SKYBLOCK, "Allows you to farm automatically on hypixel skyblock.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.gameSettings.keyBindForward.pressed = false;
        Minecraft.gameSettings.keyBindLeft.pressed = false;
        Minecraft.gameSettings.keyBindAttack.pressed = false;
        Minecraft.gameSettings.keyBindLeft.pressed = false;
        this.canGoForward = true;
        this.canGoBack = false;
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        this.posX = (float)EventMotion.getX();
    }
}

