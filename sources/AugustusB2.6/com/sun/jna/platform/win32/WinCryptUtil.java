// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;

public abstract class WinCryptUtil
{
    public static class MANAGED_CRYPT_SIGN_MESSAGE_PARA extends WinCrypt.CRYPT_SIGN_MESSAGE_PARA
    {
        private WinCrypt.CERT_CONTEXT[] rgpMsgCerts;
        private WinCrypt.CRL_CONTEXT[] rgpMsgCrls;
        private WinCrypt.CRYPT_ATTRIBUTE[] rgAuthAttrs;
        private WinCrypt.CRYPT_ATTRIBUTE[] rgUnauthAttrs;
        
        public void setRgpMsgCert(final WinCrypt.CERT_CONTEXT[] rgpMsgCerts) {
            this.rgpMsgCerts = rgpMsgCerts;
            if (rgpMsgCerts == null || rgpMsgCerts.length == 0) {
                this.rgpMsgCert = null;
                this.cMsgCert = 0;
            }
            else {
                this.cMsgCert = rgpMsgCerts.length;
                final Memory mem = new Memory(Native.POINTER_SIZE * rgpMsgCerts.length);
                for (int i = 0; i < rgpMsgCerts.length; ++i) {
                    mem.setPointer(i * Native.POINTER_SIZE, rgpMsgCerts[i].getPointer());
                }
                this.rgpMsgCert = mem;
            }
        }
        
        @Override
        public WinCrypt.CERT_CONTEXT[] getRgpMsgCert() {
            return this.rgpMsgCerts;
        }
        
        public void setRgpMsgCrl(final WinCrypt.CRL_CONTEXT[] rgpMsgCrls) {
            this.rgpMsgCrls = rgpMsgCrls;
            if (rgpMsgCrls == null || rgpMsgCrls.length == 0) {
                this.rgpMsgCert = null;
                this.cMsgCert = 0;
            }
            else {
                this.cMsgCert = rgpMsgCrls.length;
                final Memory mem = new Memory(Native.POINTER_SIZE * rgpMsgCrls.length);
                for (int i = 0; i < rgpMsgCrls.length; ++i) {
                    mem.setPointer(i * Native.POINTER_SIZE, rgpMsgCrls[i].getPointer());
                }
                this.rgpMsgCert = mem;
            }
        }
        
        @Override
        public WinCrypt.CRL_CONTEXT[] getRgpMsgCrl() {
            return this.rgpMsgCrls;
        }
        
        public void setRgAuthAttr(final WinCrypt.CRYPT_ATTRIBUTE[] rgAuthAttrs) {
            this.rgAuthAttrs = rgAuthAttrs;
            if (rgAuthAttrs == null || rgAuthAttrs.length == 0) {
                this.rgAuthAttr = null;
                this.cMsgCert = 0;
            }
            else {
                this.cMsgCert = this.rgpMsgCerts.length;
                this.rgAuthAttr = rgAuthAttrs[0].getPointer();
            }
        }
        
        @Override
        public WinCrypt.CRYPT_ATTRIBUTE[] getRgAuthAttr() {
            return this.rgAuthAttrs;
        }
        
        public void setRgUnauthAttr(final WinCrypt.CRYPT_ATTRIBUTE[] rgUnauthAttrs) {
            this.rgUnauthAttrs = rgUnauthAttrs;
            if (rgUnauthAttrs == null || rgUnauthAttrs.length == 0) {
                this.rgUnauthAttr = null;
                this.cMsgCert = 0;
            }
            else {
                this.cMsgCert = this.rgpMsgCerts.length;
                this.rgUnauthAttr = rgUnauthAttrs[0].getPointer();
            }
        }
        
        @Override
        public WinCrypt.CRYPT_ATTRIBUTE[] getRgUnauthAttr() {
            return this.rgUnauthAttrs;
        }
        
        @Override
        public void write() {
            if (this.rgpMsgCerts != null) {
                for (final WinCrypt.CERT_CONTEXT cc : this.rgpMsgCerts) {
                    cc.write();
                }
            }
            if (this.rgpMsgCrls != null) {
                for (final WinCrypt.CRL_CONTEXT cc2 : this.rgpMsgCrls) {
                    cc2.write();
                }
            }
            if (this.rgAuthAttrs != null) {
                for (final WinCrypt.CRYPT_ATTRIBUTE cc3 : this.rgAuthAttrs) {
                    cc3.write();
                }
            }
            if (this.rgUnauthAttrs != null) {
                for (final WinCrypt.CRYPT_ATTRIBUTE cc3 : this.rgUnauthAttrs) {
                    cc3.write();
                }
            }
            this.cbSize = this.size();
            super.write();
        }
        
        @Override
        public void read() {
            if (this.rgpMsgCerts != null) {
                for (final WinCrypt.CERT_CONTEXT cc : this.rgpMsgCerts) {
                    cc.read();
                }
            }
            if (this.rgpMsgCrls != null) {
                for (final WinCrypt.CRL_CONTEXT cc2 : this.rgpMsgCrls) {
                    cc2.read();
                }
            }
            if (this.rgAuthAttrs != null) {
                for (final WinCrypt.CRYPT_ATTRIBUTE cc3 : this.rgAuthAttrs) {
                    cc3.read();
                }
            }
            if (this.rgUnauthAttrs != null) {
                for (final WinCrypt.CRYPT_ATTRIBUTE cc3 : this.rgUnauthAttrs) {
                    cc3.read();
                }
            }
            super.read();
        }
    }
}
