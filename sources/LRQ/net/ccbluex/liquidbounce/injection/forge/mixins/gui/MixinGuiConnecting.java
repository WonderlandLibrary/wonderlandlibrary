/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
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

import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImplKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.mcleaks.MCLeaks;
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
    @Final
    private static AtomicInteger field_146372_a;
    @Shadow
    @Final
    private static Logger field_146370_f;
    @Shadow
    private NetworkManager field_146371_g;
    @Shadow
    private boolean field_146373_h;
    @Shadow
    @Final
    private GuiScreen field_146374_i;

    @Inject(method={"connect"}, at={@At(value="HEAD")})
    private void headConnect(String ip, int port, CallbackInfo callbackInfo) {
        ServerUtils.serverData = ServerDataImplKt.wrap(new ServerData("", ip + ":" + port, false));
    }

    @Inject(method={"connect"}, at={@At(value="NEW", target="net/minecraft/network/login/client/CPacketLoginStart")}, cancellable=true)
    private void mcLeaks(CallbackInfo callbackInfo) {
        if (MCLeaks.isAltActive()) {
            this.field_146371_g.func_179290_a((Packet)new CPacketLoginStart(new GameProfile(null, MCLeaks.getSession().getUsername())));
            callbackInfo.cancel();
        }
    }

    @Overwrite
    private void func_146367_a(String ip, int port) {
        field_146370_f.info("Connecting to " + ip + ", " + port);
        new Thread(() -> {
            InetAddress inetaddress = null;
            try {
                if (this.field_146373_h) {
                    return;
                }
                inetaddress = InetAddress.getByName(ip);
                this.field_146371_g = NetworkManager.func_181124_a((InetAddress)inetaddress, (int)port, (boolean)this.field_146297_k.field_71474_y.func_181148_f());
                this.field_146371_g.func_150719_a((INetHandler)new NetHandlerLoginClient(this.field_146371_g, this.field_146297_k, this.field_146374_i));
                this.field_146371_g.func_179290_a((Packet)new C00Handshake(ip, port, EnumConnectionState.LOGIN, true));
                this.field_146371_g.func_179290_a((Packet)new CPacketLoginStart(MCLeaks.isAltActive() ? new GameProfile(null, MCLeaks.getSession().getUsername()) : this.field_146297_k.func_110432_I().func_148256_e()));
            }
            catch (UnknownHostException unknownhostexception) {
                if (this.field_146373_h) {
                    return;
                }
                field_146370_f.error("Couldn't connect to server", (Throwable)unknownhostexception);
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (ITextComponent)new TextComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
            }
            catch (Exception exception) {
                if (this.field_146373_h) {
                    return;
                }
                field_146370_f.error("Couldn't connect to server", (Throwable)exception);
                String s = exception.toString();
                if (inetaddress != null) {
                    String s1 = inetaddress.toString() + ":" + port;
                    s = s.replaceAll(s1, "");
                }
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (ITextComponent)new TextComponentTranslation("disconnect.genericReason", new Object[]{s})));
            }
        }, "Server Connector #" + field_146372_a.incrementAndGet()).start();
    }

    @Overwrite
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        this.func_146276_q_();
        RenderUtils.drawLoadingCircle(scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 70);
        String ip = "Unknown";
        ServerData serverData = this.field_146297_k.func_147104_D();
        if (serverData != null) {
            ip = serverData.field_78845_b;
        }
        Fonts.font40.drawCenteredString("Connecting to", scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 110, 0xFFFFFF, true);
        Fonts.font35.drawCenteredString(ip, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 4 + 120, 5407227, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}

