package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SpdyFrameDecoder {
  private final int spdyVersion;
  
  private final int maxChunkSize;
  
  private final SpdyFrameDecoderDelegate delegate;
  
  private State state;
  
  private byte flags;
  
  private int length;
  
  private int streamId;
  
  private int numSettings;
  
  private enum State {
    READ_COMMON_HEADER, READ_DATA_FRAME, READ_SYN_STREAM_FRAME, READ_SYN_REPLY_FRAME, READ_RST_STREAM_FRAME, READ_SETTINGS_FRAME, READ_SETTING, READ_PING_FRAME, READ_GOAWAY_FRAME, READ_HEADERS_FRAME, READ_WINDOW_UPDATE_FRAME, READ_HEADER_BLOCK, DISCARD_FRAME, FRAME_ERROR;
  }
  
  public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate delegate) {
    this(spdyVersion, delegate, 8192);
  }
  
  public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate delegate, int maxChunkSize) {
    if (spdyVersion == null)
      throw new NullPointerException("spdyVersion"); 
    if (delegate == null)
      throw new NullPointerException("delegate"); 
    if (maxChunkSize <= 0)
      throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize); 
    this.spdyVersion = spdyVersion.getVersion();
    this.delegate = delegate;
    this.maxChunkSize = maxChunkSize;
    this.state = State.READ_COMMON_HEADER;
  }
  
  public void decode(ByteBuf buffer) {
    while (true) {
      boolean last;
      int statusCode;
      int frameOffset;
      int flagsOffset;
      int lengthOffset;
      boolean control;
      int version;
      int type;
      int dataLength;
      ByteBuf data;
      int offset;
      int associatedToStreamId;
      byte priority;
      boolean unidirectional;
      boolean clear;
      byte settingsFlags;
      int id;
      int value;
      boolean persistValue;
      boolean persisted;
      int pingId;
      int lastGoodStreamId;
      int deltaWindowSize;
      int compressedBytes;
      ByteBuf headerBlock;
      int numBytes;
      switch (this.state) {
        case READ_COMMON_HEADER:
          if (buffer.readableBytes() < 8)
            return; 
          frameOffset = buffer.readerIndex();
          flagsOffset = frameOffset + 4;
          lengthOffset = frameOffset + 5;
          buffer.skipBytes(8);
          control = ((buffer.getByte(frameOffset) & 0x80) != 0);
          if (control) {
            version = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset) & 0x7FFF;
            type = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset + 2);
            this.streamId = 0;
          } else {
            version = this.spdyVersion;
            type = 0;
            this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, frameOffset);
          } 
          this.flags = buffer.getByte(flagsOffset);
          this.length = SpdyCodecUtil.getUnsignedMedium(buffer, lengthOffset);
          if (version != this.spdyVersion) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid SPDY Version");
            continue;
          } 
          if (!isValidFrameHeader(this.streamId, type, this.flags, this.length)) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid Frame Error");
            continue;
          } 
          this.state = getNextState(type, this.length);
          continue;
        case READ_DATA_FRAME:
          if (this.length == 0) {
            this.state = State.READ_COMMON_HEADER;
            this.delegate.readDataFrame(this.streamId, hasFlag(this.flags, (byte)1), Unpooled.buffer(0));
            continue;
          } 
          dataLength = Math.min(this.maxChunkSize, this.length);
          if (buffer.readableBytes() < dataLength)
            return; 
          data = buffer.alloc().buffer(dataLength);
          data.writeBytes(buffer, dataLength);
          this.length -= dataLength;
          if (this.length == 0)
            this.state = State.READ_COMMON_HEADER; 
          last = (this.length == 0 && hasFlag(this.flags, (byte)1));
          this.delegate.readDataFrame(this.streamId, last, data);
          continue;
        case READ_SYN_STREAM_FRAME:
          if (buffer.readableBytes() < 10)
            return; 
          offset = buffer.readerIndex();
          this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, offset);
          associatedToStreamId = SpdyCodecUtil.getUnsignedInt(buffer, offset + 4);
          priority = (byte)(buffer.getByte(offset + 8) >> 5 & 0x7);
          last = hasFlag(this.flags, (byte)1);
          unidirectional = hasFlag(this.flags, (byte)2);
          buffer.skipBytes(10);
          this.length -= 10;
          if (this.streamId == 0) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid SYN_STREAM Frame");
            continue;
          } 
          this.state = State.READ_HEADER_BLOCK;
          this.delegate.readSynStreamFrame(this.streamId, associatedToStreamId, priority, last, unidirectional);
          continue;
        case READ_SYN_REPLY_FRAME:
          if (buffer.readableBytes() < 4)
            return; 
          this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          last = hasFlag(this.flags, (byte)1);
          buffer.skipBytes(4);
          this.length -= 4;
          if (this.streamId == 0) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid SYN_REPLY Frame");
            continue;
          } 
          this.state = State.READ_HEADER_BLOCK;
          this.delegate.readSynReplyFrame(this.streamId, last);
          continue;
        case READ_RST_STREAM_FRAME:
          if (buffer.readableBytes() < 8)
            return; 
          this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
          buffer.skipBytes(8);
          if (this.streamId == 0 || statusCode == 0) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid RST_STREAM Frame");
            continue;
          } 
          this.state = State.READ_COMMON_HEADER;
          this.delegate.readRstStreamFrame(this.streamId, statusCode);
          continue;
        case READ_SETTINGS_FRAME:
          if (buffer.readableBytes() < 4)
            return; 
          clear = hasFlag(this.flags, (byte)1);
          this.numSettings = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          buffer.skipBytes(4);
          this.length -= 4;
          if ((this.length & 0x7) != 0 || this.length >> 3 != this.numSettings) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid SETTINGS Frame");
            continue;
          } 
          this.state = State.READ_SETTING;
          this.delegate.readSettingsFrame(clear);
          continue;
        case READ_SETTING:
          if (this.numSettings == 0) {
            this.state = State.READ_COMMON_HEADER;
            this.delegate.readSettingsEnd();
            continue;
          } 
          if (buffer.readableBytes() < 8)
            return; 
          settingsFlags = buffer.getByte(buffer.readerIndex());
          id = SpdyCodecUtil.getUnsignedMedium(buffer, buffer.readerIndex() + 1);
          value = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
          persistValue = hasFlag(settingsFlags, (byte)1);
          persisted = hasFlag(settingsFlags, (byte)2);
          buffer.skipBytes(8);
          this.numSettings--;
          this.delegate.readSetting(id, value, persistValue, persisted);
          continue;
        case READ_PING_FRAME:
          if (buffer.readableBytes() < 4)
            return; 
          pingId = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
          buffer.skipBytes(4);
          this.state = State.READ_COMMON_HEADER;
          this.delegate.readPingFrame(pingId);
          continue;
        case READ_GOAWAY_FRAME:
          if (buffer.readableBytes() < 8)
            return; 
          lastGoodStreamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
          buffer.skipBytes(8);
          this.state = State.READ_COMMON_HEADER;
          this.delegate.readGoAwayFrame(lastGoodStreamId, statusCode);
          continue;
        case READ_HEADERS_FRAME:
          if (buffer.readableBytes() < 4)
            return; 
          this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          last = hasFlag(this.flags, (byte)1);
          buffer.skipBytes(4);
          this.length -= 4;
          if (this.streamId == 0) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid HEADERS Frame");
            continue;
          } 
          this.state = State.READ_HEADER_BLOCK;
          this.delegate.readHeadersFrame(this.streamId, last);
          continue;
        case READ_WINDOW_UPDATE_FRAME:
          if (buffer.readableBytes() < 8)
            return; 
          this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
          deltaWindowSize = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex() + 4);
          buffer.skipBytes(8);
          if (deltaWindowSize == 0) {
            this.state = State.FRAME_ERROR;
            this.delegate.readFrameError("Invalid WINDOW_UPDATE Frame");
            continue;
          } 
          this.state = State.READ_COMMON_HEADER;
          this.delegate.readWindowUpdateFrame(this.streamId, deltaWindowSize);
          continue;
        case READ_HEADER_BLOCK:
          if (this.length == 0) {
            this.state = State.READ_COMMON_HEADER;
            this.delegate.readHeaderBlockEnd();
            continue;
          } 
          if (!buffer.isReadable())
            return; 
          compressedBytes = Math.min(buffer.readableBytes(), this.length);
          headerBlock = buffer.alloc().buffer(compressedBytes);
          headerBlock.writeBytes(buffer, compressedBytes);
          this.length -= compressedBytes;
          this.delegate.readHeaderBlock(headerBlock);
          continue;
        case DISCARD_FRAME:
          numBytes = Math.min(buffer.readableBytes(), this.length);
          buffer.skipBytes(numBytes);
          this.length -= numBytes;
          if (this.length == 0) {
            this.state = State.READ_COMMON_HEADER;
            continue;
          } 
          return;
        case FRAME_ERROR:
          buffer.skipBytes(buffer.readableBytes());
          return;
      } 
      break;
    } 
    throw new Error("Shouldn't reach here.");
  }
  
  private static boolean hasFlag(byte flags, byte flag) {
    return ((flags & flag) != 0);
  }
  
  private static State getNextState(int type, int length) {
    switch (type) {
      case 0:
        return State.READ_DATA_FRAME;
      case 1:
        return State.READ_SYN_STREAM_FRAME;
      case 2:
        return State.READ_SYN_REPLY_FRAME;
      case 3:
        return State.READ_RST_STREAM_FRAME;
      case 4:
        return State.READ_SETTINGS_FRAME;
      case 6:
        return State.READ_PING_FRAME;
      case 7:
        return State.READ_GOAWAY_FRAME;
      case 8:
        return State.READ_HEADERS_FRAME;
      case 9:
        return State.READ_WINDOW_UPDATE_FRAME;
    } 
    if (length != 0)
      return State.DISCARD_FRAME; 
    return State.READ_COMMON_HEADER;
  }
  
  private static boolean isValidFrameHeader(int streamId, int type, byte flags, int length) {
    switch (type) {
      case 0:
        return (streamId != 0);
      case 1:
        return (length >= 10);
      case 2:
        return (length >= 4);
      case 3:
        return (flags == 0 && length == 8);
      case 4:
        return (length >= 4);
      case 6:
        return (length == 4);
      case 7:
        return (length == 8);
      case 8:
        return (length >= 4);
      case 9:
        return (length == 8);
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */