/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventAir;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.MoveUtil;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import me.Tengoku.Terror.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class LongJump
extends Module {
    protected double startY = 0.0;
    protected boolean boosted = false;
    protected double motionVa = 2.8;
    public static double yPos;
    Timer timer = new Timer();
    int count = 0;
    boolean jumped = false;
    private boolean damaged;
    protected MoveUtil move;
    int jumpcount = 0;
    boolean canFly = false;
    public static double yPos2;
    TimerUtils timer3;
    int prevSlot;
    Timer timer2 = new Timer();
    boolean shotArrow = false;

    @Override
    public void onEnable() {
        super.onEnable();
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("LongJump Mode", this).getValString();
        if (Minecraft.thePlayer != null) {
            this.prevSlot = Minecraft.thePlayer.inventory.currentItem;
        }
        if (string.equalsIgnoreCase("BowDamage")) {
            Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem());
            Minecraft.gameSettings.keyBindUseItem.pressed = true;
            if (this.timer.hasTimeElapsed(200L, true)) {
                Minecraft.gameSettings.keyBindUseItem.pressed = false;
            }
        }
        yPos = Minecraft.thePlayer.posY / 50.0 + (double)0.42f;
        yPos2 = Minecraft.thePlayer.posY;
        if (string.equalsIgnoreCase("Hypixel")) {
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
                this.damaged = true;
            }
        }
        if (string.equalsIgnoreCase("Damage")) {
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, 3.0001, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                    Minecraft.thePlayer.motionY += (double)0.32f;
                }
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.12, Minecraft.thePlayer.posZ);
            }
        }
    }

    public static int getLastHotbarSlot() {
        int n = -1;
        int n2 = 0;
        while (n2 < 9) {
            Minecraft.getMinecraft();
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && itemStack.getItem() instanceof ItemBow && itemStack.stackSize > 1) {
                n = n2;
            }
            ++n2;
        }
        return n;
    }

    @EventTarget
    public void onAir(EventAir eventAir) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("LongJump Mode").getValString();
        if (string.equalsIgnoreCase("Hypixel") && this.canFly) {
            eventAir.setCancelled(true);
        }
    }

    public static int grabBowSlot() {
        int n = -1;
        int n2 = -1;
        boolean bl = false;
        int n3 = 0;
        while (n3 < 36) {
            Minecraft.getMinecraft();
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n3];
            if (itemStack != null && itemStack.getItem() instanceof ItemBow && itemStack.stackSize > 0) {
                int n4;
                Minecraft.getMinecraft();
                if (Minecraft.thePlayer.inventory.mainInventory[n3].stackSize > n2 && n3 < 9) {
                    Minecraft.getMinecraft();
                    n2 = Minecraft.thePlayer.inventory.mainInventory[n3].stackSize;
                    n = n3;
                    if (n == LongJump.getLastHotbarSlot()) {
                        bl = true;
                    }
                }
                if (n3 > 8 && !bl && (n4 = LongJump.getFreeHotbarSlot()) != -1) {
                    Minecraft.getMinecraft();
                    if (Minecraft.thePlayer.inventory.mainInventory[n3].stackSize > n2) {
                        Minecraft.getMinecraft();
                        n2 = Minecraft.thePlayer.inventory.mainInventory[n3].stackSize;
                        n = n3;
                    }
                }
            }
            ++n3;
        }
        if (n > 8) {
            n3 = LongJump.getFreeHotbarSlot();
            if (n3 != -1) {
                Minecraft.getMinecraft();
                Minecraft.getMinecraft();
                int n5 = Minecraft.thePlayer.inventoryContainer.windowId;
                Minecraft.getMinecraft();
                Minecraft.playerController.windowClick(n5, n, n3, 2, Minecraft.thePlayer);
            } else {
                return -1;
            }
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        block51: {
            block50: {
                String string;
                block45: {
                    float f;
                    float f2;
                    block49: {
                        block47: {
                            block48: {
                                block46: {
                                    block40: {
                                        float f3;
                                        block44: {
                                            block42: {
                                                block43: {
                                                    float f4;
                                                    block41: {
                                                        string = Exodus.INSTANCE.settingsManager.getSettingByName("LongJump Mode").getValString();
                                                        this.setDisplayName("LongJump \ufffdf" + string);
                                                        float f5 = (float)Exodus.INSTANCE.settingsManager.getSettingByClass("Boost Factor", LongJump.class).getValDouble();
                                                        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByClass("Slow", LongJump.class).getValBoolean();
                                                        if (string.equalsIgnoreCase("Hypixel")) {
                                                            LongJump.mc.timer.timerSpeed = bl ? 0.45f : 1.0f;
                                                            if (this.damaged) {
                                                                if (Minecraft.theWorld != null) {
                                                                    this.damaged = true;
                                                                }
                                                                if (this.damaged) {
                                                                    Minecraft.gameSettings.keyBindForward.pressed = true;
                                                                    f4 = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
                                                                    f2 = (float)Math.cos((double)(f4 + 90.0f) * Math.PI / 180.0);
                                                                    f3 = (float)Math.sin((double)(f4 + 90.0f) * Math.PI / 180.0);
                                                                    float f6 = (float)(1.1632689389165578 * MathUtils.getBaseMovementSpeed() * Exodus.INSTANCE.settingsManager.getSettingByModule("Boost Factor", this).getValDouble() / (double)2.05f) + 0.05f;
                                                                    if (Minecraft.gameSettings.keyBindForward.isKeyDown() | Minecraft.gameSettings.keyBindLeft.isKeyDown() | Minecraft.gameSettings.keyBindRight.isKeyDown() | Minecraft.gameSettings.keyBindBack.isKeyDown()) {
                                                                        if (Minecraft.thePlayer.onGround) {
                                                                            Minecraft.thePlayer.jump();
                                                                            Minecraft.thePlayer.motionX = f2 * f6;
                                                                            Minecraft.thePlayer.motionZ = f3 * f6;
                                                                        }
                                                                        Minecraft.gameSettings.keyBindJump.pressed = true;
                                                                        if (this.timer3.waitUntil(10.0)) {
                                                                            this.toggle();
                                                                            this.timer3.reset();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (!string.equalsIgnoreCase("Watchdog_Test")) break block40;
                                                        if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block41;
                                                        if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block41;
                                                        if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block41;
                                                        if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block40;
                                                    }
                                                    if (!Minecraft.gameSettings.keyBindJump.isKeyDown()) break block40;
                                                    f4 = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
                                                    f2 = (float)Math.cos((double)(f4 + 90.0f) * Math.PI / 180.0);
                                                    f3 = (float)Math.sin((double)(f4 + 90.0f) * Math.PI / 180.0);
                                                    if (!Minecraft.thePlayer.isCollidedVertically) break block42;
                                                    if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block43;
                                                    if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block43;
                                                    if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block43;
                                                    if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block42;
                                                }
                                                if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                                                    Minecraft.thePlayer.motionX = f2 * 0.05f;
                                                    Minecraft.thePlayer.motionZ = f3 * 0.05f;
                                                }
                                            }
                                            if (Minecraft.thePlayer.motionY != 0.33319999363422365) break block40;
                                            if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block44;
                                            if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block44;
                                            if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block44;
                                            if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block40;
                                        }
                                        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                            Minecraft.thePlayer.motionX = (double)f2 * 1.04;
                                            Minecraft.thePlayer.motionZ = (double)f3 * 1.04;
                                        } else {
                                            Minecraft.thePlayer.motionX = (double)f2 * 1.061;
                                            Minecraft.thePlayer.motionZ = (double)f3 * 1.061;
                                        }
                                    }
                                    if (string.equalsIgnoreCase("BowDamage")) {
                                        if (Minecraft.thePlayer.hurtTime > 0) {
                                            Minecraft.gameSettings.keyBindLeft.pressed = false;
                                            Minecraft.gameSettings.keyBindRight.pressed = false;
                                            LongJump.mc.timer.timerSpeed = 0.81258714f;
                                            boolean bl = Math.abs(Minecraft.thePlayer.rotationYawHead - Minecraft.thePlayer.rotationYaw) < 90.0f;
                                            if (Minecraft.thePlayer.moveForward > 0.0f) {
                                                if (Minecraft.thePlayer.hurtTime < 5) {
                                                    Minecraft.thePlayer.setSprinting(true);
                                                    f2 = Utils.getDirection();
                                                    double d = Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
                                                    double d2 = bl ? 1.0964 : 1.001;
                                                    double d3 = Utils.getDirection();
                                                    Minecraft.thePlayer.motionY *= 1.25 * d2;
                                                    Minecraft.thePlayer.motionZ *= 1.25 * d2;
                                                    ++this.count;
                                                    if (this.count > 100) {
                                                        LongJump.mc.timer.timerSpeed = 0.25f;
                                                        this.count = 0;
                                                    } else {
                                                        LongJump.mc.timer.timerSpeed = 0.81258714f;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (string.equalsIgnoreCase("skywars.com")) {
                                        Minecraft.thePlayer.setSprinting(true);
                                        LongJump.mc.timer.timerSpeed = 1.029518f;
                                        if (Minecraft.thePlayer.isAirBorne) {
                                            Minecraft.thePlayer.motionX *= 1.19;
                                            Minecraft.thePlayer.motionZ *= 1.19;
                                        }
                                        if (Minecraft.thePlayer.motionX > 1.2) {
                                            Minecraft.thePlayer.motionX = 1.19;
                                        }
                                        if (Minecraft.thePlayer.motionZ > 1.2) {
                                            Minecraft.thePlayer.motionX = 1.19;
                                        }
                                    }
                                    if (!string.equalsIgnoreCase("Spartan")) break block45;
                                    LongJump.mc.timer.timerSpeed = 0.75f;
                                    if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block46;
                                    if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block46;
                                    if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block46;
                                    if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block45;
                                }
                                if (!Minecraft.gameSettings.keyBindJump.isKeyDown()) break block45;
                                float f7 = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
                                f2 = (float)Math.cos((double)(f7 + 90.0f) * Math.PI / 180.0);
                                f = (float)Math.sin((double)(f7 + 90.0f) * Math.PI / 180.0);
                                if (!Minecraft.thePlayer.isCollidedVertically) break block47;
                                if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block48;
                                if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block48;
                                if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block48;
                                if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block47;
                            }
                            if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                                Minecraft.thePlayer.motionX = f2 * 0.29f;
                                Minecraft.thePlayer.motionZ = f * 0.29f;
                            }
                        }
                        if (Minecraft.thePlayer.motionY != 0.33319999363422365) break block45;
                        if (Minecraft.gameSettings.keyBindForward.isKeyDown()) break block49;
                        if (Minecraft.gameSettings.keyBindLeft.isKeyDown()) break block49;
                        if (Minecraft.gameSettings.keyBindRight.isKeyDown()) break block49;
                        if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) break block45;
                    }
                    if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        Minecraft.thePlayer.motionX = (double)f2 * 1.34;
                        Minecraft.thePlayer.motionZ = (double)f * 1.34;
                    } else {
                        Minecraft.thePlayer.motionX = (double)f2 * 1.261;
                        Minecraft.thePlayer.motionZ = (double)f * 1.261;
                    }
                }
                if (!string.equalsIgnoreCase("Normal")) return;
                if (Minecraft.gameSettings.keyBindForward.pressed) break block50;
                if (Minecraft.gameSettings.keyBindBack.pressed) break block50;
                if (Minecraft.gameSettings.keyBindLeft.pressed) break block50;
                if (!Minecraft.gameSettings.keyBindRight.pressed) break block51;
                if (!Minecraft.gameSettings.keyBindJump.pressed) break block51;
            }
            if (Minecraft.thePlayer.isAirBorne) {
                Minecraft.thePlayer.motionX *= 1.08;
                Minecraft.thePlayer.motionY *= 1.08;
            }
            if (Minecraft.thePlayer.motionX > 1.2) {
                Minecraft.thePlayer.motionX = 1.08;
            }
            if (!(Minecraft.thePlayer.motionY > 1.2)) return;
            Minecraft.thePlayer.motionY = 1.08;
            return;
        }
        Minecraft.thePlayer.motionX *= 1.0;
        Minecraft.thePlayer.motionY *= 1.0;
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Spartan");
        arrayList.add("skywars.com");
        arrayList.add("Watchdog_Test");
        arrayList.add("Hypixel");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Slow", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Boost Factor", this, 2.0, 1.0, 15.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("LongJump Mode", (Module)this, "Normal", arrayList));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        LongJump.mc.timer.timerSpeed = 1.0f;
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("LongJump Mode", this).getValString();
        if (string.equalsIgnoreCase("BowDamage")) {
            Minecraft.thePlayer.inventory.currentItem = this.prevSlot;
        }
        this.jumped = false;
        Minecraft.thePlayer.speedInAir = 0.02f;
        this.damaged = false;
        Minecraft.gameSettings.keyBindForward.pressed = false;
        Minecraft.thePlayer.setSprinting(false);
        Minecraft.gameSettings.keyBindJump.pressed = false;
        this.jumpcount = 0;
        this.shotArrow = false;
        this.canFly = false;
    }

    public LongJump() {
        super("LongJump", 45, Category.MOVEMENT, "Ran out of ideas for this one.");
        this.timer3 = new TimerUtils();
    }

    public static int getFreeHotbarSlot() {
        int n = -1;
        int n2 = 0;
        while (n2 < 9) {
            Minecraft.getMinecraft();
            n = Minecraft.thePlayer.inventory.mainInventory[n2] == null ? n2 : 7;
            ++n2;
        }
        return n;
    }
}

