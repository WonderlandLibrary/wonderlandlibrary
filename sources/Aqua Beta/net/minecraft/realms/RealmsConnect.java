package net.minecraft.realms;

import net.minecraft.network.NetworkManager;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsConnect {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen onlineScreen;
    private volatile boolean aborted = false;
    private NetworkManager connection;

    public RealmsConnect(RealmsScreen p_i1079_1_) {
        this.onlineScreen = p_i1079_1_;
    }

    public void connect(String p_connect_1_, int p_connect_2_) {
        Realms.setConnectedToRealms((boolean)true);
        new /* Unavailable Anonymous Inner Class!! */.start();
    }

    public void abort() {
        this.aborted = true;
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isChannelOpen()) {
                this.connection.processReceivedPackets();
            } else {
                this.connection.checkDisconnected();
            }
        }
    }

    static /* synthetic */ boolean access$000(RealmsConnect x0) {
        return x0.aborted;
    }

    static /* synthetic */ NetworkManager access$100(RealmsConnect x0) {
        return x0.connection;
    }

    static /* synthetic */ RealmsScreen access$200(RealmsConnect x0) {
        return x0.onlineScreen;
    }

    static /* synthetic */ Logger access$300() {
        return LOGGER;
    }
}
