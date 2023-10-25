package ru.salam4ik.bot.bot.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.util.text.ITextComponent;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.entity.BotPlayer;

public final class BotLoginClient
        implements INetHandlerLoginClient {
    private final BotNetwork networkManager;

    public BotLoginClient(BotNetwork botNetwork) {
        this.networkManager = botNetwork;
    }

    @Override
    public void handleLoginSuccess(SPacketLoginSuccess sPacketLoginSuccess) {
        this.networkManager.setConnectionState(EnumConnectionState.PLAY);
        this.networkManager.setNetHandler(new BotPlayClient(this.networkManager, sPacketLoginSuccess.getProfile()));
    }

    @Override
    public void handleEncryptionRequest(SPacketEncryptionRequest sPacketEncryptionRequest) {
    }

    @Override
    public void handleEnableCompression(SPacketEnableCompression sPacketEnableCompression) {
        if (!this.networkManager.isLocalChannel()) {
            this.networkManager.setCompressionThreshold(sPacketEnableCompression.getCompressionThreshold());
        }
    }


    @Override
    public void handleDisconnect(SPacketDisconnect sPacketDisconnect) {

    }

    @Override
    public void onDisconnect(ITextComponent iTextComponent) {
    }
}
 