package net.ccbluex.liquidbounce.features.command.commands;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
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
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000P\n\n\n\n\b\n\b\n\u0000\n\n\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n \n\b\u00002020B¢J02\f\b00\tH¢J\b0\rHJ020HJ020HJ!\b002\f\b00\tH¢R0X¢\n\u0000R0X¢\n\u0000R\b\b0\n0\tX¢\nR\f0\rX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/TacoCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "image", "", "running", "", "tacoTextures", "", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "[Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "toggle", "", "execute", "", "args", "", "([Ljava/lang/String;)V", "handleEvents", "onRender2D", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class TacoCommand
extends Command
implements Listenable {
    private boolean toggle;
    private int image;
    private float running;
    private final IResourceLocation[] tacoTextures = new IResourceLocation[]{MinecraftInstance.classProvider.createResourceLocation("pride/taco/1.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/2.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/3.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/4.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/5.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/6.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/7.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/8.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/9.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/10.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/11.png"), MinecraftInstance.classProvider.createResourceLocation("pride/taco/12.png")};

    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        this.toggle = !this.toggle;
        ClientUtils.displayChatMessage(this.toggle ? "§aTACO TACO TACO. :)" : "§cYou made the little taco sad! :(");
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!this.toggle) {
            return;
        }
        this.running += 0.15f * (float)RenderUtils.deltaTime;
        IMinecraft iMinecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(iMinecraft, "mc");
        IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
        RenderUtils.drawImage(this.tacoTextures[this.image], (int)this.running, scaledResolution.getScaledHeight() - 60, 64, 32);
        if ((float)scaledResolution.getScaledWidth() <= this.running) {
            this.running = -64.0f;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        return CollectionsKt.listOf("TACO");
    }

    public TacoCommand() {
        super("taco", new String[0]);
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
    }
}
