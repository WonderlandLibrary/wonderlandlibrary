// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import ru.tuskevich.util.chat.ChatUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.world.EnumDifficulty;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "GriefJoiner", desc = "", type = Type.MISC)
public class TestFeature extends Module
{
    public BooleanSetting mega;
    private final SliderSetting grief;
    
    public TestFeature() {
        this.mega = new BooleanSetting("Mega-Grief", true);
        this.grief = new SliderSetting("Grief", 1.0f, 1.0f, 23.0f, 1.0f, () -> !this.mega.get());
        this.add(this.grief, this.mega);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate update) {
        if (TestFeature.mc.getCurrentServerData() != null && TestFeature.mc.getCurrentServerData().serverIP != null && TestFeature.mc.getCurrentServerData().serverIP.contains("playrw")) {
            if (TestFeature.mc.world.getDifficulty() == EnumDifficulty.EASY) {
                final Minecraft mc = TestFeature.mc;
                if (Minecraft.player.ticksExisted % 10 == 0) {
                    if (this.mega.get()) {
                        final Minecraft mc2 = TestFeature.mc;
                        Minecraft.player.sendChatMessage("/grief-mega");
                    }
                    else {
                        final Minecraft mc3 = TestFeature.mc;
                        Minecraft.player.sendChatMessage("/grief-" + this.grief.getIntValue());
                    }
                }
            }
            if (TestFeature.mc.world.getDifficulty() == EnumDifficulty.HARD) {
                ChatUtility.addChatMessage("\u0412\u044b \u0437\u0430\u0448\u043b\u0438 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440!");
                final WorldClient world = TestFeature.mc.world;
                final Minecraft mc4 = TestFeature.mc;
                final EntityPlayerSP player = Minecraft.player;
                final Minecraft mc5 = TestFeature.mc;
                final double posX = Minecraft.player.posX;
                final Minecraft mc6 = TestFeature.mc;
                final double posY = Minecraft.player.posY;
                final Minecraft mc7 = TestFeature.mc;
                final double posZ = Minecraft.player.posZ;
                final SoundEvent entity_FIREWORK_LAUNCH = SoundEvents.ENTITY_FIREWORK_LAUNCH;
                final Minecraft mc8 = TestFeature.mc;
                world.playSound(player, posX, posY, posZ, entity_FIREWORK_LAUNCH, Minecraft.player.getSoundCategory(), 100.0f, 20.0f);
                this.toggle();
            }
        }
        if (TestFeature.mc.getCurrentServerData() != null && TestFeature.mc.getCurrentServerData().serverIP != null && TestFeature.mc.getCurrentServerData().serverIP.contains("reallyworld")) {
            if (TestFeature.mc.world.getDifficulty() == EnumDifficulty.EASY) {
                final Minecraft mc9 = TestFeature.mc;
                if (Minecraft.player.ticksExisted % 10 == 0) {
                    if (this.mega.get()) {
                        final Minecraft mc10 = TestFeature.mc;
                        Minecraft.player.sendChatMessage("/grief-mega");
                    }
                    else {
                        final Minecraft mc11 = TestFeature.mc;
                        Minecraft.player.sendChatMessage("/grief-" + this.grief.getIntValue());
                    }
                }
            }
            if (TestFeature.mc.world.getDifficulty() == EnumDifficulty.HARD) {
                ChatUtility.addChatMessage("\u0412\u044b \u0437\u0430\u0448\u043b\u0438 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440!");
                final WorldClient world2 = TestFeature.mc.world;
                final Minecraft mc12 = TestFeature.mc;
                final EntityPlayerSP player2 = Minecraft.player;
                final Minecraft mc13 = TestFeature.mc;
                final double posX2 = Minecraft.player.posX;
                final Minecraft mc14 = TestFeature.mc;
                final double posY2 = Minecraft.player.posY;
                final Minecraft mc15 = TestFeature.mc;
                final double posZ2 = Minecraft.player.posZ;
                final SoundEvent entity_FIREWORK_LAUNCH2 = SoundEvents.ENTITY_FIREWORK_LAUNCH;
                final Minecraft mc16 = TestFeature.mc;
                world2.playSound(player2, posX2, posY2, posZ2, entity_FIREWORK_LAUNCH2, Minecraft.player.getSoundCategory(), 100.0f, 20.0f);
                this.toggle();
            }
        }
    }
}
