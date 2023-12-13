/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.network.NetworkSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingResponseHandler
extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger();
    private NetworkSystem networkSystem;
    private static final String __OBFID = "CL_00001444";

    public PingResponseHandler(NetworkSystem networkSystemIn) {
        this.networkSystem = networkSystemIn;
    }

    /*
     * Exception decompiling
     */
    @Override
    public void channelRead(ChannelHandlerContext p_channelRead_1_, Object p_channelRead_2_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 8[CASE]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    private void writeAndFlush(ChannelHandlerContext ctx, ByteBuf data) {
        ctx.pipeline().firstContext().writeAndFlush(data).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf getStringBuffer(String string) {
        ByteBuf var2 = Unpooled.buffer();
        var2.writeByte(255);
        char[] var3 = string.toCharArray();
        var2.writeShort(var3.length);
        char[] var4 = var3;
        int var5 = var3.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            char var7 = var4[var6];
            var2.writeChar(var7);
        }
        return var2;
    }
}

