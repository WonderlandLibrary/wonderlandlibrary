package io.netty.handler.timeout;

public enum IdleState {
   READER_IDLE,
   WRITER_IDLE,
   ALL_IDLE;

   private static final IdleState[] $VALUES = new IdleState[]{READER_IDLE, WRITER_IDLE, ALL_IDLE};
}
