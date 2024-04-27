/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.List;
import kotlin.collections.CollectionsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class TacoCommand
extends Command
implements Listenable {
    private boolean toggle;
    private int image;
    private float running;
    private final IResourceLocation[] tacoTextures = new IResourceLocation[]{MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/1.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/2.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/3.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/4.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/5.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/6.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/7.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/8.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/9.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/10.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/11.png"), MinecraftInstance.classProvider.createResourceLocation("liquidbounce/taco/12.png")};

    @Override
    public void execute(String[] args) {
        this.toggle = !this.toggle;
        ClientUtils.displayChatMessage(this.toggle ? "\u00a7aTACO TACO TACO. :)" : "\u00a7cYou made the little taco sad! :(");
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        if (!this.toggle) {
            return;
        }
        this.running += 0.15f * (float)RenderUtils.deltaTime;
        IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
        RenderUtils.drawImage(this.tacoTextures[this.image], (int)this.running, scaledResolution.getScaledHeight() - 60, 64, 32);
        if ((float)scaledResolution.getScaledWidth() <= this.running) {
            this.running = -64.0f;
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        if (!this.toggle) {
            this.image = 0;
            return;
        }
        int n = this.image;
        this.image = n + 1;
        if (this.image >= this.tacoTextures.length) {
            this.image = 0;
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return CollectionsKt.listOf((Object)"TACO");
    }

    public TacoCommand() {
        super("taco", new String[0]);
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
    }
}

