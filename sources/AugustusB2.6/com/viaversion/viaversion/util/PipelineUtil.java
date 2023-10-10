// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.lang.reflect.Method;

public class PipelineUtil
{
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;
    
    public static List<Object> callDecode(final ByteToMessageDecoder decoder, final ChannelHandlerContext ctx, final Object input) throws InvocationTargetException {
        final List<Object> output = new ArrayList<Object>();
        try {
            PipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static void callEncode(final MessageToByteEncoder encoder, final ChannelHandlerContext ctx, final Object msg, final ByteBuf output) throws InvocationTargetException {
        try {
            PipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Object> callDecode(final MessageToMessageDecoder decoder, final ChannelHandlerContext ctx, final Object msg) throws InvocationTargetException {
        final List<Object> output = new ArrayList<Object>();
        try {
            PipelineUtil.MTM_DECODE.invoke(decoder, ctx, msg, output);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static boolean containsCause(Throwable t, final Class<?> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }
    
    public static ChannelHandlerContext getContextBefore(final String name, final ChannelPipeline pipeline) {
        boolean mark = false;
        for (final String s : pipeline.names()) {
            if (mark) {
                return pipeline.context(pipeline.get(s));
            }
            if (!s.equalsIgnoreCase(name)) {
                continue;
            }
            mark = true;
        }
        return null;
    }
    
    public static ChannelHandlerContext getPreviousContext(final String name, final ChannelPipeline pipeline) {
        String previous = null;
        for (final String entry : pipeline.toMap().keySet()) {
            if (entry.equals(name)) {
                return pipeline.context(previous);
            }
            previous = entry;
        }
        return null;
    }
    
    static {
        try {
            (PipelineUtil.DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            (PipelineUtil.ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            (PipelineUtil.MTM_DECODE = MessageToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
