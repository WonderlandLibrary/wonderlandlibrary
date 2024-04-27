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
 *  net.minecraft.network.login.client.C00PacketLoginStart
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.Logger
 */
package net.dev.important.injection.forge.mixins.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.ServerUtils;
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
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
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
    private static Logger field_146370_f;
    @Shadow
    private boolean field_146373_h;
    @Shadow
    @Final
    private GuiScreen field_146374_i;
    @Shadow
    @Final
    private static AtomicInteger field_146372_a;

    @Inject(method={"connect"}, at={@At(value="HEAD")})
    private void headConnect(String ip, int port, CallbackInfo callbackInfo) {
        ServerUtils.serverData = new ServerData("", ip + ":" + port, false);
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
                this.field_146371_g.func_179290_a((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN, true));
                this.field_146371_g.func_179290_a((Packet)new C00PacketLoginStart(this.field_146297_k.func_110432_I().func_148256_e()));
            }
            catch (UnknownHostException unknownhostexception) {
                if (this.field_146373_h) {
                    return;
                }
                field_146370_f.error("Couldn't connect to server", (Throwable)unknownhostexception);
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
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
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDisconnected(this.field_146374_i, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{s})));
            }
        }, "Server Connector #" + field_146372_a.incrementAndGet()).start();
    }

    @Overwrite
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        this.func_146276_q_();
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

