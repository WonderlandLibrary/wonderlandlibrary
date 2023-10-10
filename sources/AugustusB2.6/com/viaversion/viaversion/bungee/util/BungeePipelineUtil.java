// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.util;

import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Method;

public class BungeePipelineUtil
{
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    
    public static List<Object> callDecode(final MessageToMessageDecoder decoder, final ChannelHandlerContext ctx, final ByteBuf input) throws InvocationTargetException {
        final List<Object> output = new ArrayList<Object>();
        try {
            BungeePipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static ByteBuf callEncode(final MessageToByteEncoder encoder, final ChannelHandlerContext ctx, final ByteBuf input) throws InvocationTargetException {
        final ByteBuf output = ctx.alloc().buffer();
        try {
            BungeePipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, input, output);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static ByteBuf decompress(final ChannelHandlerContext ctx, final ByteBuf bytebuf) {
        try {
            return callDecode((MessageToMessageDecoder)ctx.pipeline().get("decompress"), ctx.pipeline().context("decompress"), bytebuf).get(0);
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
            return ctx.alloc().buffer();
        }
    }
    
    public static ByteBuf compress(final ChannelHandlerContext ctx, final ByteBuf bytebuf) {
        try {
            return callEncode((MessageToByteEncoder)ctx.pipeline().get("compress"), ctx.pipeline().context("compress"), bytebuf);
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
            return ctx.alloc().buffer();
        }
    }
    
    static {
        try {
            (BungeePipelineUtil.DECODE_METHOD = MessageToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            (BungeePipelineUtil.ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
