/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiDisconnected
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.GuiConnecting
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.network.NetHandlerLoginClient
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.login.client.CPacketLoginStart
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentTranslation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.Logger
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImplKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiConnecting.class})
public abstract class MixinGuiConnecting
extends GuiScreen {
    @Shadow
    private NetworkManager field_146371_g;
    @Shadow
    @Final
    private GuiScreen field_146374_i;
    @Shadow
    private boolean field_146373_h;
    @Shadow
    @Final
    private static Logger field_146370_f;
    @Shadow
    @Final
    private static AtomicInteger field_146372_a;

    @Overwrite
    public void func_73863_a(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        this.func_146276_q_();
        RenderUtils.drawLoadingCircle(scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 70);
        String string = "Unknown";
        ServerData serverData = this.field_146297_k.func_147104_D();
        if (serverData != null) {
            string = serverData.field_78845_b;
        }
        Fonts.roboto40.drawCenteredString("Connecting to", scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 110, 0xFFFFFF, true);
        Fonts.roboto35.drawCenteredString(string, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 120, 5407227, true);
        super.func_73863_a(n, n2, f);
    }

    @Overwrite
    private void func_146367_a(String string, int n) {
        field_146370_f.info("Connecting to " + string + ", " + n);
        new Thread(() -> this.lambda$connect$0(string, n), "Server Connector #" + field_146372_a.incrementAndGet()).start();
    }

    @Inject(method={"connect"}, at={@At(value="HEAD")})
    private void headConnect(String string, int n, CallbackInfo callbackInfo) {
        ServerUtils.serverData = ServerDataImplKt.wrap(new ServerData("", string + ":" + n, false));
    }

    private void lambda$connect$0(String string, int n) {
        InetAddress inetAddress = null;
        try {
            if (this.field_146373_h) {
                return;
            }
            inetAddress = InetAddress.getByName(string);
            this.field_146371_g = NetworkManager.func_181124_a((InetAddress)inetAddress, (int)n, (boolean)this.field_146297_k.field_71474_y.func_181148_f());
            this.field_146371_g.func_150719_a((INetHandler)new NetHandlerLoginClient(this.field_146371_g, this.field_146297_k, this.field_146374_i));
            this.field_146371_g.func_179290_a((Packet)new C00Handshake(string, n, EnumConnectionState.LOGIN, true));
            this.field_146371_g.func_179290_a((Packet)new CPacketLoginStart(this.field_146297_k.func_110432_I().func_148256_e()));
        }
        catch (UnknownHostException unknownHostException) {
            if (this.field_146373_h) {
                return;
            }
            field_146370_f.error("Couldn't connect to server", (Throwable)unknownHostException);
            this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (ITextComponent)new TextComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
        }
        catch (Exception exception) {
            if (this.field_146373_h) {
                return;
            }
            field_146370_f.error("Couldn't connect to server", (Throwable)exception);
            String string2 = exception.toString();
            if (inetAddress != null) {
                String string3 = inetAddress + ":" + n;
                string2 = string2.replaceAll(string3, "");
            }
            this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (ITextComponent)new TextComponentTranslation("disconnect.genericReason", new Object[]{string2})));
        }
    }
}

