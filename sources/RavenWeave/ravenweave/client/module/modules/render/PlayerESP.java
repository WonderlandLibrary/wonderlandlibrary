package ravenweave.client.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.world.AntiBot;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

import java.awt.*;
import java.util.Iterator;

public class PlayerESP extends Module {
    public static DescriptionSetting g;
    public static SliderSetting expand;
    public static SliderSetting xShift;
    public static TickSetting rainbow;
    public static TickSetting showInvis;
    public static TickSetting redOnDamage;
    public static TickSetting enabledBox;
    public static TickSetting enabledShaded;
    public static TickSetting enabled2D;
    public static TickSetting enabledHealth;
    public static TickSetting enabledArrow;
    public static TickSetting enabledRing;
    public static TickSetting matchChestplate;
    public static RGBSetting rgb;

    public PlayerESP() {
        super("PlayerESP", ModuleCategory.render);
        this.registerSetting(rgb = new RGBSetting("RGB", 0, 255, 0));
        this.registerSetting(rainbow = new TickSetting("Rainbow", false));
        this.registerSetting(g = new DescriptionSetting("ESP Types"));
        this.registerSetting(enabled2D = new TickSetting("2D", false));
        this.registerSetting(enabledArrow = new TickSetting("Arrow", false));
        this.registerSetting(enabledBox = new TickSetting("Box", false));
        this.registerSetting(enabledHealth = new TickSetting("Health", true));
        this.registerSetting(enabledRing = new TickSetting("Ring", false));
        this.registerSetting(enabledShaded = new TickSetting("Shaded", false));
        this.registerSetting(expand = new SliderSetting("Expand", 0.0D, -0.3D, 2.0D, 0.1D));
        this.registerSetting(xShift = new SliderSetting("X-Shift", 0.0D, -35.0D, 10.0D, 1.0D));
        this.registerSetting(showInvis = new TickSetting("Show invis", true));
        this.registerSetting(redOnDamage = new TickSetting("Red on damage", true));
        this.registerSetting(matchChestplate = new TickSetting("Match Chestplate", false));
    }

    public void onDisable() {
        Utils.HUD.ring_c = false;
    }

    @SubscribeEvent
    public void onForgeEvent(RenderWorldEvent fe) {
        if (!this.enabled) return;
        if (Utils.Player.isPlayerInGame()) {
            int rgb = rainbow.isToggled() ? 0 : this.rgb.getRGB();
            Iterator var3;
            if (Raven.debugger) {
                var3 = mc.theWorld.loadedEntityList.iterator();

                while (var3.hasNext()) {
                    Entity en = (Entity) var3.next();
                    if (en instanceof EntityLivingBase && en != mc.thePlayer) {
                        this.r(en, rgb);
                    }
                }

            } else {
                var3 = mc.theWorld.playerEntities.iterator();

                while (true) {
                    EntityPlayer en;
                    do {
                        do {
                            do {
                                if (!var3.hasNext()) {
                                    return;
                                }

                                en = (EntityPlayer) var3.next();
                            } while (en == mc.thePlayer);
                        } while (en.deathTime != 0);
                    } while (!showInvis.isToggled() && en.isInvisible());

                    if (!AntiBot.bot(en)) {
                        if (matchChestplate.isToggled() && getColor(en.getCurrentArmor(2)) > 0) {
                            int E = new Color(getColor(en.getCurrentArmor(2))).getRGB();
                            this.r(en, E);
                        } else {
                            this.r(en, rgb);
                        }
                    }
                }
            }
        }
    }

    public int getColor(ItemStack stack) {
        if (stack == null)
            return -1; // not wearing a chestplate
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
            if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
                return nbttagcompound1.getInteger("color");
            }
        }

        return -2; // chestplate has no colour
    }

    private void r(Entity en, int rgb) {
        if (enabledBox.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 1, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (enabledShaded.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 2, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (enabled2D.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 3, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (enabledHealth.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 4, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (enabledArrow.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 5, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (enabledRing.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 6, expand.getInput(), xShift.getInput(), rgb, redOnDamage.isToggled());
        }

    }
}
