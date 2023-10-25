/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.CommPort
 *  gnu.io.CommPortIdentifier
 *  gnu.io.SerialPort
 */
package io.netty.channel.rxtx;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.rxtx.DefaultRxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class RxtxChannel
extends OioByteStreamChannel {
    private RxtxDeviceAddress deviceAddress;
    private boolean open = true;
    private final RxtxChannelConfig config = new DefaultRxtxChannelConfig(this);
    private SerialPort serialPort;
    private static final RxtxDeviceAddress LOCAL_ADDRESS = new RxtxDeviceAddress("localhost");

    @Override
    protected RxtxDeviceAddress localAddress0() {
        return LOCAL_ADDRESS;
    }

    @Override
    protected RxtxDeviceAddress remoteAddress0() {
        return this.deviceAddress;
    }

    @Override
    protected void doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        RxtxDeviceAddress rxtxDeviceAddress = (RxtxDeviceAddress)socketAddress;
        CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier((String)rxtxDeviceAddress.value());
        CommPort commPort = commPortIdentifier.open(this.getClass().getName(), 1000);
        commPort.enableReceiveTimeout(this.config().getOption(RxtxChannelOption.READ_TIMEOUT).intValue());
        this.deviceAddress = rxtxDeviceAddress;
        this.serialPort = (SerialPort)commPort;
    }

    @Override
    public RxtxChannelConfig config() {
        return this.config;
    }

    @Override
    protected void doClose() throws Exception {
        block0: {
            this.open = false;
            super.doClose();
            if (this.serialPort == null) break block0;
            this.serialPort.removeEventListener();
            this.serialPort.close();
            this.serialPort = null;
        }
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected void doInit() throws Exception {
        this.serialPort.setSerialPortParams(this.config().getOption(RxtxChannelOption.BAUD_RATE).intValue(), this.config().getOption(RxtxChannelOption.DATA_BITS).value(), this.config().getOption(RxtxChannelOption.STOP_BITS).value(), this.config().getOption(RxtxChannelOption.PARITY_BIT).value());
        this.serialPort.setDTR(this.config().getOption(RxtxChannelOption.DTR).booleanValue());
        this.serialPort.setRTS(this.config().getOption(RxtxChannelOption.RTS).booleanValue());
        this.activate(this.serialPort.getInputStream(), this.serialPort.getOutputStream());
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public RxtxDeviceAddress remoteAddress() {
        return (RxtxDeviceAddress)super.remoteAddress();
    }

    @Override
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new RxtxUnsafe();
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    public RxtxChannel() {
        super(null);
    }

    @Override
    public RxtxDeviceAddress localAddress() {
        return (RxtxDeviceAddress)super.localAddress();
    }

    private final class RxtxUnsafe
    extends AbstractChannel.AbstractUnsafe {
        private RxtxUnsafe() {
            super(RxtxChannel.this);
        }

        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, final ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                return;
            }
            try {
                final boolean bl = RxtxChannel.this.isActive();
                RxtxChannel.this.doConnect(socketAddress, socketAddress2);
                int n = RxtxChannel.this.config().getOption(RxtxChannelOption.WAIT_TIME);
                if (n > 0) {
                    RxtxChannel.this.eventLoop().schedule(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                RxtxChannel.this.doInit();
                                RxtxUnsafe.this.safeSetSuccess(channelPromise);
                                if (!bl && RxtxChannel.this.isActive()) {
                                    RxtxChannel.this.pipeline().fireChannelActive();
                                }
                            }
                            catch (Throwable throwable) {
                                RxtxUnsafe.this.safeSetFailure(channelPromise, throwable);
                                RxtxUnsafe.this.closeIfClosed();
                            }
                        }
                    }, (long)n, TimeUnit.MILLISECONDS);
                } else {
                    RxtxChannel.this.doInit();
                    this.safeSetSuccess(channelPromise);
                    if (!bl && RxtxChannel.this.isActive()) {
                        RxtxChannel.this.pipeline().fireChannelActive();
                    }
                }
            }
            catch (Throwable throwable) {
                this.safeSetFailure(channelPromise, throwable);
                this.closeIfClosed();
            }
        }
    }
}

