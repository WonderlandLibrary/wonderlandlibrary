// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

public class SspiUtil
{
    public static class ManagedSecBufferDesc extends Sspi.SecBufferDesc
    {
        private final Sspi.SecBuffer[] secBuffers;
        
        public ManagedSecBufferDesc(final int type, final byte[] token) {
            this.secBuffers = new Sspi.SecBuffer[] { new Sspi.SecBuffer(type, token) };
            this.pBuffers = this.secBuffers[0].getPointer();
            this.cBuffers = this.secBuffers.length;
        }
        
        public ManagedSecBufferDesc(final int type, final int tokenSize) {
            this.secBuffers = new Sspi.SecBuffer[] { new Sspi.SecBuffer(type, tokenSize) };
            this.pBuffers = this.secBuffers[0].getPointer();
            this.cBuffers = this.secBuffers.length;
        }
        
        public ManagedSecBufferDesc(final int bufferCount) {
            this.cBuffers = bufferCount;
            this.secBuffers = (Sspi.SecBuffer[])new Sspi.SecBuffer().toArray(bufferCount);
            this.pBuffers = this.secBuffers[0].getPointer();
            this.cBuffers = this.secBuffers.length;
        }
        
        public Sspi.SecBuffer getBuffer(final int idx) {
            return this.secBuffers[idx];
        }
        
        @Override
        public void write() {
            for (final Sspi.SecBuffer sb : this.secBuffers) {
                sb.write();
            }
            this.writeField("ulVersion");
            this.writeField("pBuffers");
            this.writeField("cBuffers");
        }
        
        @Override
        public void read() {
            for (final Sspi.SecBuffer sb : this.secBuffers) {
                sb.read();
            }
        }
    }
}
