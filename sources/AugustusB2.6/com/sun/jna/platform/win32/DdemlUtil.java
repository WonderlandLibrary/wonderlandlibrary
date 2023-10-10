// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Memory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;
import com.sun.jna.Pointer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.io.Closeable;

public abstract class DdemlUtil
{
    public static class StandaloneDdeClient implements IDdeClient, Closeable
    {
        private final User32Util.MessageLoopThread messageLoop;
        private final IDdeClient ddeClient;
        private final IDdeClient clientDelegate;
        
        public StandaloneDdeClient() {
            this.messageLoop = new User32Util.MessageLoopThread();
            this.ddeClient = new DdeClient();
            final IDdeClient messageLoopHandler = (IDdeClient)Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[] { IDdeClient.class }, this.messageLoop.new Handler(this.ddeClient));
            this.clientDelegate = (IDdeClient)Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[] { IDdeClient.class }, new MessageLoopWrapper(this.messageLoop, messageLoopHandler));
            this.messageLoop.setDaemon(true);
            this.messageLoop.start();
        }
        
        @Override
        public Integer getInstanceIdentitifier() {
            return this.ddeClient.getInstanceIdentitifier();
        }
        
        @Override
        public void initialize(final int afCmd) throws DdemlException {
            this.clientDelegate.initialize(afCmd);
        }
        
        @Override
        public Ddeml.HSZ createStringHandle(final String value) throws DdemlException {
            return this.clientDelegate.createStringHandle(value);
        }
        
        @Override
        public void nameService(final Ddeml.HSZ name, final int afCmd) throws DdemlException {
            this.clientDelegate.nameService(name, afCmd);
        }
        
        @Override
        public int getLastError() {
            return this.clientDelegate.getLastError();
        }
        
        @Override
        public IDdeConnection connect(final Ddeml.HSZ service, final Ddeml.HSZ topic, final Ddeml.CONVCONTEXT convcontext) {
            return this.clientDelegate.connect(service, topic, convcontext);
        }
        
        @Override
        public String queryString(final Ddeml.HSZ value) throws DdemlException {
            return this.clientDelegate.queryString(value);
        }
        
        @Override
        public Ddeml.HDDEDATA createDataHandle(final Pointer pSrc, final int cb, final int cbOff, final Ddeml.HSZ hszItem, final int wFmt, final int afCmd) {
            return this.clientDelegate.createDataHandle(pSrc, cb, cbOff, hszItem, wFmt, afCmd);
        }
        
        @Override
        public void freeDataHandle(final Ddeml.HDDEDATA hData) {
            this.clientDelegate.freeDataHandle(hData);
        }
        
        @Override
        public Ddeml.HDDEDATA addData(final Ddeml.HDDEDATA hData, final Pointer pSrc, final int cb, final int cbOff) {
            return this.clientDelegate.addData(hData, pSrc, cb, cbOff);
        }
        
        @Override
        public int getData(final Ddeml.HDDEDATA hData, final Pointer pDst, final int cbMax, final int cbOff) {
            return this.clientDelegate.getData(hData, pDst, cbMax, cbOff);
        }
        
        @Override
        public Pointer accessData(final Ddeml.HDDEDATA hData, final WinDef.DWORDByReference pcbDataSize) {
            return this.clientDelegate.accessData(hData, pcbDataSize);
        }
        
        @Override
        public void unaccessData(final Ddeml.HDDEDATA hData) {
            this.clientDelegate.unaccessData(hData);
        }
        
        @Override
        public void postAdvise(final Ddeml.HSZ hszTopic, final Ddeml.HSZ hszItem) {
            this.clientDelegate.postAdvise(hszTopic, hszItem);
        }
        
        @Override
        public void close() throws IOException {
            this.clientDelegate.uninitialize();
            this.messageLoop.exit();
        }
        
        @Override
        public boolean freeStringHandle(final Ddeml.HSZ value) {
            return this.clientDelegate.freeStringHandle(value);
        }
        
        @Override
        public boolean keepStringHandle(final Ddeml.HSZ value) {
            return this.clientDelegate.keepStringHandle(value);
        }
        
        @Override
        public void abandonTransactions() {
            this.clientDelegate.abandonTransactions();
        }
        
        @Override
        public IDdeConnectionList connectList(final Ddeml.HSZ service, final Ddeml.HSZ topic, final IDdeConnectionList existingList, final Ddeml.CONVCONTEXT ctx) {
            return this.clientDelegate.connectList(service, topic, existingList, ctx);
        }
        
        @Override
        public boolean enableCallback(final int wCmd) {
            return this.clientDelegate.enableCallback(wCmd);
        }
        
        @Override
        public IDdeConnection wrap(final Ddeml.HCONV conv) {
            return this.clientDelegate.wrap(conv);
        }
        
        @Override
        public IDdeConnection connect(final String service, final String topic, final Ddeml.CONVCONTEXT convcontext) {
            return this.clientDelegate.connect(service, topic, convcontext);
        }
        
        @Override
        public boolean uninitialize() {
            return this.clientDelegate.uninitialize();
        }
        
        @Override
        public void postAdvise(final String hszTopic, final String hszItem) {
            this.clientDelegate.postAdvise(hszTopic, hszItem);
        }
        
        @Override
        public IDdeConnectionList connectList(final String service, final String topic, final IDdeConnectionList existingList, final Ddeml.CONVCONTEXT ctx) {
            return this.clientDelegate.connectList(service, topic, existingList, ctx);
        }
        
        @Override
        public void nameService(final String name, final int afCmd) throws DdemlException {
            this.clientDelegate.nameService(name, afCmd);
        }
        
        @Override
        public void registerAdvstartHandler(final AdvstartHandler handler) {
            this.clientDelegate.registerAdvstartHandler(handler);
        }
        
        @Override
        public void unregisterAdvstartHandler(final AdvstartHandler handler) {
            this.clientDelegate.unregisterAdvstartHandler(handler);
        }
        
        @Override
        public void registerAdvstopHandler(final AdvstopHandler handler) {
            this.clientDelegate.registerAdvstopHandler(handler);
        }
        
        @Override
        public void unregisterAdvstopHandler(final AdvstopHandler handler) {
            this.clientDelegate.unregisterAdvstopHandler(handler);
        }
        
        @Override
        public void registerConnectHandler(final ConnectHandler handler) {
            this.clientDelegate.registerConnectHandler(handler);
        }
        
        @Override
        public void unregisterConnectHandler(final ConnectHandler handler) {
            this.clientDelegate.unregisterConnectHandler(handler);
        }
        
        @Override
        public void registerAdvReqHandler(final AdvreqHandler handler) {
            this.clientDelegate.registerAdvReqHandler(handler);
        }
        
        @Override
        public void unregisterAdvReqHandler(final AdvreqHandler handler) {
            this.clientDelegate.unregisterAdvReqHandler(handler);
        }
        
        @Override
        public void registerRequestHandler(final RequestHandler handler) {
            this.clientDelegate.registerRequestHandler(handler);
        }
        
        @Override
        public void unregisterRequestHandler(final RequestHandler handler) {
            this.clientDelegate.unregisterRequestHandler(handler);
        }
        
        @Override
        public void registerWildconnectHandler(final WildconnectHandler handler) {
            this.clientDelegate.registerWildconnectHandler(handler);
        }
        
        @Override
        public void unregisterWildconnectHandler(final WildconnectHandler handler) {
            this.clientDelegate.unregisterWildconnectHandler(handler);
        }
        
        @Override
        public void registerAdvdataHandler(final AdvdataHandler handler) {
            this.clientDelegate.registerAdvdataHandler(handler);
        }
        
        @Override
        public void unregisterAdvdataHandler(final AdvdataHandler handler) {
            this.clientDelegate.unregisterAdvdataHandler(handler);
        }
        
        @Override
        public void registerExecuteHandler(final ExecuteHandler handler) {
            this.clientDelegate.registerExecuteHandler(handler);
        }
        
        @Override
        public void unregisterExecuteHandler(final ExecuteHandler handler) {
            this.clientDelegate.unregisterExecuteHandler(handler);
        }
        
        @Override
        public void registerPokeHandler(final PokeHandler handler) {
            this.clientDelegate.registerPokeHandler(handler);
        }
        
        @Override
        public void unregisterPokeHandler(final PokeHandler handler) {
            this.clientDelegate.unregisterPokeHandler(handler);
        }
        
        @Override
        public void registerConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.clientDelegate.registerConnectConfirmHandler(handler);
        }
        
        @Override
        public void unregisterConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.clientDelegate.unregisterConnectConfirmHandler(handler);
        }
        
        @Override
        public void registerDisconnectHandler(final DisconnectHandler handler) {
            this.clientDelegate.registerDisconnectHandler(handler);
        }
        
        @Override
        public void unregisterDisconnectHandler(final DisconnectHandler handler) {
            this.clientDelegate.unregisterDisconnectHandler(handler);
        }
        
        @Override
        public void registerErrorHandler(final ErrorHandler handler) {
            this.clientDelegate.registerErrorHandler(handler);
        }
        
        @Override
        public void unregisterErrorHandler(final ErrorHandler handler) {
            this.clientDelegate.unregisterErrorHandler(handler);
        }
        
        @Override
        public void registerRegisterHandler(final RegisterHandler handler) {
            this.clientDelegate.registerRegisterHandler(handler);
        }
        
        @Override
        public void unregisterRegisterHandler(final RegisterHandler handler) {
            this.clientDelegate.unregisterRegisterHandler(handler);
        }
        
        @Override
        public void registerXactCompleteHandler(final XactCompleteHandler handler) {
            this.clientDelegate.registerXactCompleteHandler(handler);
        }
        
        @Override
        public void unregisterXactCompleteHandler(final XactCompleteHandler handler) {
            this.clientDelegate.unregisterXactCompleteHandler(handler);
        }
        
        @Override
        public void registerUnregisterHandler(final UnregisterHandler handler) {
            this.clientDelegate.registerUnregisterHandler(handler);
        }
        
        @Override
        public void unregisterUnregisterHandler(final UnregisterHandler handler) {
            this.clientDelegate.unregisterUnregisterHandler(handler);
        }
        
        @Override
        public void registerMonitorHandler(final MonitorHandler handler) {
            this.clientDelegate.registerMonitorHandler(handler);
        }
        
        @Override
        public void unregisterMonitorHandler(final MonitorHandler handler) {
            this.clientDelegate.unregisterMonitorHandler(handler);
        }
    }
    
    private static class MessageLoopWrapper implements InvocationHandler
    {
        private final Object delegate;
        private final User32Util.MessageLoopThread loopThread;
        
        public MessageLoopWrapper(final User32Util.MessageLoopThread thread, final Object delegate) {
            this.loopThread = thread;
            this.delegate = delegate;
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            try {
                Object result = method.invoke(this.delegate, args);
                Class<?> wrapClass = null;
                if (result instanceof IDdeConnection) {
                    wrapClass = IDdeConnection.class;
                }
                else if (result instanceof IDdeConnectionList) {
                    wrapClass = IDdeConnectionList.class;
                }
                else if (result instanceof IDdeClient) {
                    wrapClass = IDdeClient.class;
                }
                if (wrapClass != null && method.getReturnType().isAssignableFrom(wrapClass)) {
                    result = this.wrap(result, wrapClass);
                }
                return result;
            }
            catch (InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
                throw ex;
            }
        }
        
        private <V> V wrap(final V delegate, final Class clazz) {
            final V messageLoopHandler = (V)Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[] { clazz }, this.loopThread.new Handler(delegate));
            final V clientDelegate = (V)Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[] { clazz }, new MessageLoopWrapper(this.loopThread, messageLoopHandler));
            return clientDelegate;
        }
    }
    
    public static class DdeConnection implements IDdeConnection
    {
        private Ddeml.HCONV conv;
        private final IDdeClient client;
        
        public DdeConnection(final IDdeClient client, final Ddeml.HCONV conv) {
            this.conv = conv;
            this.client = client;
        }
        
        @Override
        public Ddeml.HCONV getConv() {
            return this.conv;
        }
        
        @Override
        public void abandonTransaction(final int transactionId) {
            final boolean result = Ddeml.INSTANCE.DdeAbandonTransaction(this.client.getInstanceIdentitifier(), this.conv, transactionId);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
        
        @Override
        public void abandonTransactions() {
            final boolean result = Ddeml.INSTANCE.DdeAbandonTransaction(this.client.getInstanceIdentitifier(), this.conv, 0);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
        
        @Override
        public Ddeml.HDDEDATA clientTransaction(final Pointer data, final int dataLength, final Ddeml.HSZ item, final int wFmt, final int transaction, final int timeout, WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            if (timeout == -1 && result == null) {
                result = new WinDef.DWORDByReference();
            }
            final Ddeml.HDDEDATA returnData = Ddeml.INSTANCE.DdeClientTransaction(data, dataLength, this.conv, item, wFmt, transaction, timeout, result);
            if (returnData == null) {
                throw DdemlException.create(this.client.getLastError());
            }
            if (userHandle != null) {
                if (timeout != -1) {
                    this.setUserHandle(-1, userHandle);
                }
                else {
                    this.setUserHandle(result.getValue().intValue(), userHandle);
                }
            }
            return returnData;
        }
        
        @Override
        public Ddeml.HDDEDATA clientTransaction(final Pointer data, final int dataLength, final String item, final int wFmt, final int transaction, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            Ddeml.HSZ itemHSZ = null;
            try {
                itemHSZ = this.client.createStringHandle(item);
                return this.clientTransaction(data, dataLength, itemHSZ, wFmt, transaction, timeout, result, userHandle);
            }
            finally {
                this.client.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public void poke(final Pointer data, final int dataLength, final Ddeml.HSZ item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            this.clientTransaction(data, dataLength, item, wFmt, 16528, timeout, result, userHandle);
        }
        
        @Override
        public void poke(final Pointer data, final int dataLength, final String item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            Ddeml.HSZ itemHSZ = null;
            try {
                itemHSZ = this.client.createStringHandle(item);
                this.poke(data, dataLength, itemHSZ, wFmt, timeout, result, userHandle);
            }
            finally {
                this.client.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public Ddeml.HDDEDATA request(final Ddeml.HSZ item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            return this.clientTransaction(Pointer.NULL, 0, item, wFmt, 8368, timeout, result, userHandle);
        }
        
        @Override
        public Ddeml.HDDEDATA request(final String item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            Ddeml.HSZ itemHSZ = null;
            try {
                itemHSZ = this.client.createStringHandle(item);
                return this.request(itemHSZ, wFmt, timeout, result, userHandle);
            }
            finally {
                this.client.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public void execute(final String executeString, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            final Memory mem = new Memory(executeString.length() * 2 + 2);
            mem.setWideString(0L, executeString);
            this.clientTransaction(mem, (int)mem.size(), (Ddeml.HSZ)null, 0, 16464, timeout, result, userHandle);
        }
        
        @Override
        public void advstart(final Ddeml.HSZ item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            this.clientTransaction(Pointer.NULL, 0, item, wFmt, 4144, timeout, result, userHandle);
        }
        
        @Override
        public void advstart(final String item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            Ddeml.HSZ itemHSZ = null;
            try {
                itemHSZ = this.client.createStringHandle(item);
                this.advstart(itemHSZ, wFmt, timeout, result, userHandle);
            }
            finally {
                this.client.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public void advstop(final Ddeml.HSZ item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            this.clientTransaction(Pointer.NULL, 0, item, wFmt, 32832, timeout, result, userHandle);
        }
        
        @Override
        public void advstop(final String item, final int wFmt, final int timeout, final WinDef.DWORDByReference result, final BaseTSD.DWORD_PTR userHandle) {
            Ddeml.HSZ itemHSZ = null;
            try {
                itemHSZ = this.client.createStringHandle(item);
                this.advstop(itemHSZ, wFmt, timeout, result, userHandle);
            }
            finally {
                this.client.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public void impersonateClient() {
            final boolean result = Ddeml.INSTANCE.DdeImpersonateClient(this.conv);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
        
        @Override
        public void close() {
            final boolean result = Ddeml.INSTANCE.DdeDisconnect(this.conv);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
        
        @Override
        public void reconnect() {
            final Ddeml.HCONV newConv = Ddeml.INSTANCE.DdeReconnect(this.conv);
            if (newConv != null) {
                this.conv = newConv;
                return;
            }
            throw DdemlException.create(this.client.getLastError());
        }
        
        @Override
        public boolean enableCallback(final int wCmd) {
            final boolean result = Ddeml.INSTANCE.DdeEnableCallback(this.client.getInstanceIdentitifier(), this.conv, wCmd);
            if (!result && wCmd == 2) {
                throw DdemlException.create(this.client.getLastError());
            }
            return result;
        }
        
        @Override
        public void setUserHandle(final int id, final BaseTSD.DWORD_PTR hUser) throws DdemlException {
            final boolean result = Ddeml.INSTANCE.DdeSetUserHandle(this.conv, id, hUser);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
        
        @Override
        public Ddeml.CONVINFO queryConvInfo(final int idTransaction) throws DdemlException {
            final Ddeml.CONVINFO convInfo = new Ddeml.CONVINFO();
            convInfo.cb = convInfo.size();
            convInfo.ConvCtxt.cb = convInfo.ConvCtxt.size();
            convInfo.write();
            final int result = Ddeml.INSTANCE.DdeQueryConvInfo(this.conv, idTransaction, convInfo);
            if (result == 0) {
                throw DdemlException.create(this.client.getLastError());
            }
            return convInfo;
        }
    }
    
    public static class DdeConnectionList implements IDdeConnectionList
    {
        private final IDdeClient client;
        private final Ddeml.HCONVLIST convList;
        
        public DdeConnectionList(final IDdeClient client, final Ddeml.HCONVLIST convList) {
            this.convList = convList;
            this.client = client;
        }
        
        @Override
        public Ddeml.HCONVLIST getHandle() {
            return this.convList;
        }
        
        @Override
        public IDdeConnection queryNextServer(final IDdeConnection prevConnection) {
            final Ddeml.HCONV conv = Ddeml.INSTANCE.DdeQueryNextServer(this.convList, (prevConnection != null) ? prevConnection.getConv() : null);
            if (conv != null) {
                return new DdeConnection(this.client, conv);
            }
            return null;
        }
        
        @Override
        public void close() {
            final boolean result = Ddeml.INSTANCE.DdeDisconnectList(this.convList);
            if (!result) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
    }
    
    public static class DdeClient implements IDdeClient
    {
        private Integer idInst;
        private final DdeAdapter ddeAdapter;
        
        public DdeClient() {
            this.ddeAdapter = new DdeAdapter();
        }
        
        @Override
        public Integer getInstanceIdentitifier() {
            return this.idInst;
        }
        
        @Override
        public void initialize(final int afCmd) throws DdemlException {
            final WinDef.DWORDByReference pidInst = new WinDef.DWORDByReference();
            final Integer result = Ddeml.INSTANCE.DdeInitialize(pidInst, this.ddeAdapter, afCmd, 0);
            if (result != 0) {
                throw DdemlException.create(result);
            }
            this.idInst = pidInst.getValue().intValue();
            if (this.ddeAdapter instanceof DdeAdapter) {
                this.ddeAdapter.setInstanceIdentifier(this.idInst);
            }
        }
        
        @Override
        public Ddeml.HSZ createStringHandle(final String value) throws DdemlException {
            if (value == null) {
                return null;
            }
            int codePage;
            if (W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS) {
                codePage = 1200;
            }
            else {
                codePage = 1004;
            }
            final Ddeml.HSZ handle = Ddeml.INSTANCE.DdeCreateStringHandle(this.idInst, value, codePage);
            if (handle == null) {
                throw DdemlException.create(this.getLastError());
            }
            return handle;
        }
        
        @Override
        public void nameService(final Ddeml.HSZ name, final int afCmd) throws DdemlException {
            final Ddeml.HDDEDATA handle = Ddeml.INSTANCE.DdeNameService(this.idInst, name, new Ddeml.HSZ(), afCmd);
            if (handle == null) {
                throw DdemlException.create(this.getLastError());
            }
        }
        
        @Override
        public void nameService(final String name, final int afCmd) throws DdemlException {
            Ddeml.HSZ nameHSZ = null;
            try {
                nameHSZ = this.createStringHandle(name);
                this.nameService(nameHSZ, afCmd);
            }
            finally {
                this.freeStringHandle(nameHSZ);
            }
        }
        
        @Override
        public int getLastError() {
            return Ddeml.INSTANCE.DdeGetLastError(this.idInst);
        }
        
        @Override
        public IDdeConnection connect(final Ddeml.HSZ service, final Ddeml.HSZ topic, final Ddeml.CONVCONTEXT convcontext) {
            final Ddeml.HCONV hconv = Ddeml.INSTANCE.DdeConnect(this.idInst, service, topic, convcontext);
            if (hconv == null) {
                throw DdemlException.create(this.getLastError());
            }
            return new DdeConnection(this, hconv);
        }
        
        @Override
        public IDdeConnection connect(final String service, final String topic, final Ddeml.CONVCONTEXT convcontext) {
            Ddeml.HSZ serviceHSZ = null;
            Ddeml.HSZ topicHSZ = null;
            try {
                serviceHSZ = this.createStringHandle(service);
                topicHSZ = this.createStringHandle(topic);
                return this.connect(serviceHSZ, topicHSZ, convcontext);
            }
            finally {
                this.freeStringHandle(topicHSZ);
                this.freeStringHandle(serviceHSZ);
            }
        }
        
        @Override
        public String queryString(final Ddeml.HSZ value) throws DdemlException {
            int codePage;
            int byteWidth;
            if (W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS) {
                codePage = 1200;
                byteWidth = 2;
            }
            else {
                codePage = 1004;
                byteWidth = 1;
            }
            final Memory buffer = new Memory(257 * byteWidth);
            try {
                final int length = Ddeml.INSTANCE.DdeQueryString(this.idInst, value, buffer, 256, codePage);
                if (W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS) {
                    return buffer.getWideString(0L);
                }
                return buffer.getString(0L);
            }
            finally {
                buffer.valid();
            }
        }
        
        @Override
        public Ddeml.HDDEDATA createDataHandle(final Pointer pSrc, final int cb, final int cbOff, final Ddeml.HSZ hszItem, final int wFmt, final int afCmd) {
            final Ddeml.HDDEDATA returnData = Ddeml.INSTANCE.DdeCreateDataHandle(this.idInst, pSrc, cb, cbOff, hszItem, wFmt, afCmd);
            if (returnData == null) {
                throw DdemlException.create(this.getLastError());
            }
            return returnData;
        }
        
        @Override
        public void freeDataHandle(final Ddeml.HDDEDATA hData) {
            final boolean result = Ddeml.INSTANCE.DdeFreeDataHandle(hData);
            if (!result) {
                throw DdemlException.create(this.getLastError());
            }
        }
        
        @Override
        public Ddeml.HDDEDATA addData(final Ddeml.HDDEDATA hData, final Pointer pSrc, final int cb, final int cbOff) {
            final Ddeml.HDDEDATA newHandle = Ddeml.INSTANCE.DdeAddData(hData, pSrc, cb, cbOff);
            if (newHandle == null) {
                throw DdemlException.create(this.getLastError());
            }
            return newHandle;
        }
        
        @Override
        public int getData(final Ddeml.HDDEDATA hData, final Pointer pDst, final int cbMax, final int cbOff) {
            final int result = Ddeml.INSTANCE.DdeGetData(hData, pDst, cbMax, cbOff);
            final int errorCode = this.getLastError();
            if (errorCode != 0) {
                throw DdemlException.create(errorCode);
            }
            return result;
        }
        
        @Override
        public Pointer accessData(final Ddeml.HDDEDATA hData, final WinDef.DWORDByReference pcbDataSize) {
            final Pointer result = Ddeml.INSTANCE.DdeAccessData(hData, pcbDataSize);
            if (result == null) {
                throw DdemlException.create(this.getLastError());
            }
            return result;
        }
        
        @Override
        public void unaccessData(final Ddeml.HDDEDATA hData) {
            final boolean result = Ddeml.INSTANCE.DdeUnaccessData(hData);
            if (!result) {
                throw DdemlException.create(this.getLastError());
            }
        }
        
        @Override
        public void postAdvise(final Ddeml.HSZ hszTopic, final Ddeml.HSZ hszItem) {
            final boolean result = Ddeml.INSTANCE.DdePostAdvise(this.idInst, hszTopic, hszItem);
            if (!result) {
                throw DdemlException.create(this.getLastError());
            }
        }
        
        @Override
        public void postAdvise(final String topic, final String item) {
            Ddeml.HSZ itemHSZ = null;
            Ddeml.HSZ topicHSZ = null;
            try {
                topicHSZ = this.createStringHandle(topic);
                itemHSZ = this.createStringHandle(item);
                this.postAdvise(topicHSZ, itemHSZ);
            }
            finally {
                this.freeStringHandle(topicHSZ);
                this.freeStringHandle(itemHSZ);
            }
        }
        
        @Override
        public boolean freeStringHandle(final Ddeml.HSZ value) {
            return value == null || Ddeml.INSTANCE.DdeFreeStringHandle(this.idInst, value);
        }
        
        @Override
        public boolean keepStringHandle(final Ddeml.HSZ value) {
            return Ddeml.INSTANCE.DdeKeepStringHandle(this.idInst, value);
        }
        
        @Override
        public void abandonTransactions() {
            final boolean result = Ddeml.INSTANCE.DdeAbandonTransaction(this.idInst, null, 0);
            if (!result) {
                throw DdemlException.create(this.getLastError());
            }
        }
        
        @Override
        public IDdeConnectionList connectList(final Ddeml.HSZ service, final Ddeml.HSZ topic, final IDdeConnectionList existingList, final Ddeml.CONVCONTEXT ctx) {
            final Ddeml.HCONVLIST convlist = Ddeml.INSTANCE.DdeConnectList(this.idInst, service, topic, (existingList != null) ? existingList.getHandle() : null, ctx);
            if (convlist == null) {
                throw DdemlException.create(this.getLastError());
            }
            return new DdeConnectionList(this, convlist);
        }
        
        @Override
        public IDdeConnectionList connectList(final String service, final String topic, final IDdeConnectionList existingList, final Ddeml.CONVCONTEXT ctx) {
            Ddeml.HSZ serviceHSZ = null;
            Ddeml.HSZ topicHSZ = null;
            try {
                serviceHSZ = this.createStringHandle(service);
                topicHSZ = this.createStringHandle(topic);
                return this.connectList(serviceHSZ, topicHSZ, existingList, ctx);
            }
            finally {
                this.freeStringHandle(topicHSZ);
                this.freeStringHandle(serviceHSZ);
            }
        }
        
        @Override
        public boolean enableCallback(final int wCmd) {
            final boolean result = Ddeml.INSTANCE.DdeEnableCallback(this.idInst, null, wCmd);
            if (!result && wCmd != 2) {
                final int errorCode = this.getLastError();
                if (errorCode != 0) {
                    throw DdemlException.create(this.getLastError());
                }
            }
            return result;
        }
        
        @Override
        public boolean uninitialize() {
            return Ddeml.INSTANCE.DdeUninitialize(this.idInst);
        }
        
        @Override
        public void close() {
            this.uninitialize();
        }
        
        @Override
        public IDdeConnection wrap(final Ddeml.HCONV hconv) {
            return new DdeConnection(this, hconv);
        }
        
        @Override
        public void unregisterDisconnectHandler(final DisconnectHandler handler) {
            this.ddeAdapter.unregisterDisconnectHandler(handler);
        }
        
        @Override
        public void registerAdvstartHandler(final AdvstartHandler handler) {
            this.ddeAdapter.registerAdvstartHandler(handler);
        }
        
        @Override
        public void unregisterAdvstartHandler(final AdvstartHandler handler) {
            this.ddeAdapter.unregisterAdvstartHandler(handler);
        }
        
        @Override
        public void registerAdvstopHandler(final AdvstopHandler handler) {
            this.ddeAdapter.registerAdvstopHandler(handler);
        }
        
        @Override
        public void unregisterAdvstopHandler(final AdvstopHandler handler) {
            this.ddeAdapter.unregisterAdvstopHandler(handler);
        }
        
        @Override
        public void registerConnectHandler(final ConnectHandler handler) {
            this.ddeAdapter.registerConnectHandler(handler);
        }
        
        @Override
        public void unregisterConnectHandler(final ConnectHandler handler) {
            this.ddeAdapter.unregisterConnectHandler(handler);
        }
        
        @Override
        public void registerAdvReqHandler(final AdvreqHandler handler) {
            this.ddeAdapter.registerAdvReqHandler(handler);
        }
        
        @Override
        public void unregisterAdvReqHandler(final AdvreqHandler handler) {
            this.ddeAdapter.unregisterAdvReqHandler(handler);
        }
        
        @Override
        public void registerRequestHandler(final RequestHandler handler) {
            this.ddeAdapter.registerRequestHandler(handler);
        }
        
        @Override
        public void unregisterRequestHandler(final RequestHandler handler) {
            this.ddeAdapter.unregisterRequestHandler(handler);
        }
        
        @Override
        public void registerWildconnectHandler(final WildconnectHandler handler) {
            this.ddeAdapter.registerWildconnectHandler(handler);
        }
        
        @Override
        public void unregisterWildconnectHandler(final WildconnectHandler handler) {
            this.ddeAdapter.unregisterWildconnectHandler(handler);
        }
        
        @Override
        public void registerAdvdataHandler(final AdvdataHandler handler) {
            this.ddeAdapter.registerAdvdataHandler(handler);
        }
        
        @Override
        public void unregisterAdvdataHandler(final AdvdataHandler handler) {
            this.ddeAdapter.unregisterAdvdataHandler(handler);
        }
        
        @Override
        public void registerExecuteHandler(final ExecuteHandler handler) {
            this.ddeAdapter.registerExecuteHandler(handler);
        }
        
        @Override
        public void unregisterExecuteHandler(final ExecuteHandler handler) {
            this.ddeAdapter.unregisterExecuteHandler(handler);
        }
        
        @Override
        public void registerPokeHandler(final PokeHandler handler) {
            this.ddeAdapter.registerPokeHandler(handler);
        }
        
        @Override
        public void unregisterPokeHandler(final PokeHandler handler) {
            this.ddeAdapter.unregisterPokeHandler(handler);
        }
        
        @Override
        public void registerConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.ddeAdapter.registerConnectConfirmHandler(handler);
        }
        
        @Override
        public void unregisterConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.ddeAdapter.unregisterConnectConfirmHandler(handler);
        }
        
        @Override
        public void registerDisconnectHandler(final DisconnectHandler handler) {
            this.ddeAdapter.registerDisconnectHandler(handler);
        }
        
        @Override
        public void registerErrorHandler(final ErrorHandler handler) {
            this.ddeAdapter.registerErrorHandler(handler);
        }
        
        @Override
        public void unregisterErrorHandler(final ErrorHandler handler) {
            this.ddeAdapter.unregisterErrorHandler(handler);
        }
        
        @Override
        public void registerRegisterHandler(final RegisterHandler handler) {
            this.ddeAdapter.registerRegisterHandler(handler);
        }
        
        @Override
        public void unregisterRegisterHandler(final RegisterHandler handler) {
            this.ddeAdapter.unregisterRegisterHandler(handler);
        }
        
        @Override
        public void registerXactCompleteHandler(final XactCompleteHandler handler) {
            this.ddeAdapter.registerXactCompleteHandler(handler);
        }
        
        @Override
        public void unregisterXactCompleteHandler(final XactCompleteHandler handler) {
            this.ddeAdapter.xactCompleteXactCompleteHandler(handler);
        }
        
        @Override
        public void registerUnregisterHandler(final UnregisterHandler handler) {
            this.ddeAdapter.registerUnregisterHandler(handler);
        }
        
        @Override
        public void unregisterUnregisterHandler(final UnregisterHandler handler) {
            this.ddeAdapter.unregisterUnregisterHandler(handler);
        }
        
        @Override
        public void registerMonitorHandler(final MonitorHandler handler) {
            this.ddeAdapter.registerMonitorHandler(handler);
        }
        
        @Override
        public void unregisterMonitorHandler(final MonitorHandler handler) {
            this.ddeAdapter.unregisterMonitorHandler(handler);
        }
    }
    
    public static class DdeAdapter implements Ddeml.DdeCallback
    {
        private static final Logger LOG;
        private int idInst;
        private final List<AdvstartHandler> advstartHandler;
        private final List<AdvstopHandler> advstopHandler;
        private final List<ConnectHandler> connectHandler;
        private final List<AdvreqHandler> advReqHandler;
        private final List<RequestHandler> requestHandler;
        private final List<WildconnectHandler> wildconnectHandler;
        private final List<AdvdataHandler> advdataHandler;
        private final List<ExecuteHandler> executeHandler;
        private final List<PokeHandler> pokeHandler;
        private final List<ConnectConfirmHandler> connectConfirmHandler;
        private final List<DisconnectHandler> disconnectHandler;
        private final List<ErrorHandler> errorHandler;
        private final List<RegisterHandler> registerHandler;
        private final List<XactCompleteHandler> xactCompleteHandler;
        private final List<UnregisterHandler> unregisterHandler;
        private final List<MonitorHandler> monitorHandler;
        
        public DdeAdapter() {
            this.advstartHandler = new CopyOnWriteArrayList<AdvstartHandler>();
            this.advstopHandler = new CopyOnWriteArrayList<AdvstopHandler>();
            this.connectHandler = new CopyOnWriteArrayList<ConnectHandler>();
            this.advReqHandler = new CopyOnWriteArrayList<AdvreqHandler>();
            this.requestHandler = new CopyOnWriteArrayList<RequestHandler>();
            this.wildconnectHandler = new CopyOnWriteArrayList<WildconnectHandler>();
            this.advdataHandler = new CopyOnWriteArrayList<AdvdataHandler>();
            this.executeHandler = new CopyOnWriteArrayList<ExecuteHandler>();
            this.pokeHandler = new CopyOnWriteArrayList<PokeHandler>();
            this.connectConfirmHandler = new CopyOnWriteArrayList<ConnectConfirmHandler>();
            this.disconnectHandler = new CopyOnWriteArrayList<DisconnectHandler>();
            this.errorHandler = new CopyOnWriteArrayList<ErrorHandler>();
            this.registerHandler = new CopyOnWriteArrayList<RegisterHandler>();
            this.xactCompleteHandler = new CopyOnWriteArrayList<XactCompleteHandler>();
            this.unregisterHandler = new CopyOnWriteArrayList<UnregisterHandler>();
            this.monitorHandler = new CopyOnWriteArrayList<MonitorHandler>();
        }
        
        public void setInstanceIdentifier(final int idInst) {
            this.idInst = idInst;
        }
        
        @Override
        public WinDef.PVOID ddeCallback(final int wType, final int wFmt, final Ddeml.HCONV hConv, final Ddeml.HSZ hsz1, final Ddeml.HSZ hsz2, final Ddeml.HDDEDATA hData, final BaseTSD.ULONG_PTR lData1, final BaseTSD.ULONG_PTR lData2) {
            final String transactionTypeName = null;
            try {
                switch (wType) {
                    case 4144: {
                        final boolean booleanResult = this.onAdvstart(wType, wFmt, hConv, hsz1, hsz2);
                        return new WinDef.PVOID(Pointer.createConstant(new WinDef.BOOL(booleanResult).intValue()));
                    }
                    case 4194: {
                        Ddeml.CONVCONTEXT convcontext = null;
                        if (lData1.toPointer() != null) {
                            convcontext = new Ddeml.CONVCONTEXT(new Pointer(lData1.longValue()));
                        }
                        final boolean booleanResult = this.onConnect(wType, hsz1, hsz2, convcontext, lData2 != null && lData2.intValue() != 0);
                        return new WinDef.PVOID(Pointer.createConstant(new WinDef.BOOL(booleanResult).intValue()));
                    }
                    case 8226: {
                        final int count = lData1.intValue() & 0xFFFF;
                        final Ddeml.HDDEDATA data = this.onAdvreq(wType, wFmt, hConv, hsz1, hsz2, count);
                        if (data == null) {
                            return new WinDef.PVOID();
                        }
                        return new WinDef.PVOID(data.getPointer());
                    }
                    case 8368: {
                        final Ddeml.HDDEDATA data = this.onRequest(wType, wFmt, hConv, hsz1, hsz2);
                        if (data == null) {
                            return new WinDef.PVOID();
                        }
                        return new WinDef.PVOID(data.getPointer());
                    }
                    case 8418: {
                        Ddeml.CONVCONTEXT convcontext = null;
                        if (lData1.toPointer() != null) {
                            convcontext = new Ddeml.CONVCONTEXT(new Pointer(lData1.longValue()));
                        }
                        final Ddeml.HSZPAIR[] hszPairs = this.onWildconnect(wType, hsz1, hsz2, convcontext, lData2 != null && lData2.intValue() != 0);
                        if (hszPairs == null || hszPairs.length == 0) {
                            return new WinDef.PVOID();
                        }
                        int size = 0;
                        for (final Ddeml.HSZPAIR hp : hszPairs) {
                            hp.write();
                            size += hp.size();
                        }
                        final Ddeml.HDDEDATA data = Ddeml.INSTANCE.DdeCreateDataHandle(this.idInst, hszPairs[0].getPointer(), size, 0, null, wFmt, 0);
                        return new WinDef.PVOID(data.getPointer());
                    }
                    case 16400: {
                        final int intResult = this.onAdvdata(wType, wFmt, hConv, hsz1, hsz2, hData);
                        return new WinDef.PVOID(Pointer.createConstant(intResult));
                    }
                    case 16464: {
                        final int intResult = this.onExecute(wType, hConv, hsz1, hData);
                        Ddeml.INSTANCE.DdeFreeDataHandle(hData);
                        return new WinDef.PVOID(Pointer.createConstant(intResult));
                    }
                    case 16528: {
                        final int intResult = this.onPoke(wType, wFmt, hConv, hsz1, hsz2, hData);
                        return new WinDef.PVOID(Pointer.createConstant(intResult));
                    }
                    case 32832: {
                        this.onAdvstop(wType, wFmt, hConv, hsz1, hsz2);
                        break;
                    }
                    case 32882: {
                        this.onConnectConfirm(wType, hConv, hsz1, hsz2, lData2 != null && lData2.intValue() != 0);
                        break;
                    }
                    case 32962: {
                        this.onDisconnect(wType, hConv, lData2 != null && lData2.intValue() != 0);
                        break;
                    }
                    case 32770: {
                        this.onError(wType, hConv, (int)(lData2.longValue() & 0xFFFFL));
                        break;
                    }
                    case 32930: {
                        this.onRegister(wType, hsz1, hsz2);
                        break;
                    }
                    case 32896: {
                        this.onXactComplete(wType, wFmt, hConv, hsz1, hsz2, hData, lData1, lData2);
                        break;
                    }
                    case 32978: {
                        this.onUnregister(wType, hsz1, hsz2);
                        break;
                    }
                    case 33010: {
                        this.onMonitor(wType, hData, lData2.intValue());
                        break;
                    }
                    default: {
                        DdeAdapter.LOG.log(Level.FINE, String.format("Not implemented Operation - Transaction type: 0x%X (%s)", wType, transactionTypeName));
                        break;
                    }
                }
            }
            catch (BlockException ex2) {
                return new WinDef.PVOID(Pointer.createConstant(-1));
            }
            catch (Throwable ex) {
                DdeAdapter.LOG.log(Level.WARNING, "Exception in DDECallback", ex);
            }
            return new WinDef.PVOID();
        }
        
        public void registerAdvstartHandler(final AdvstartHandler handler) {
            this.advstartHandler.add(handler);
        }
        
        public void unregisterAdvstartHandler(final AdvstartHandler handler) {
            this.advstartHandler.remove(handler);
        }
        
        private boolean onAdvstart(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item) {
            boolean oneHandlerTrue = false;
            for (final AdvstartHandler handler : this.advstartHandler) {
                if (handler.onAdvstart(transactionType, dataFormat, hconv, topic, item)) {
                    oneHandlerTrue = true;
                }
            }
            return oneHandlerTrue;
        }
        
        public void registerAdvstopHandler(final AdvstopHandler handler) {
            this.advstopHandler.add(handler);
        }
        
        public void unregisterAdvstopHandler(final AdvstopHandler handler) {
            this.advstopHandler.remove(handler);
        }
        
        private void onAdvstop(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item) {
            for (final AdvstopHandler handler : this.advstopHandler) {
                handler.onAdvstop(transactionType, dataFormat, hconv, topic, item);
            }
        }
        
        public void registerConnectHandler(final ConnectHandler handler) {
            this.connectHandler.add(handler);
        }
        
        public void unregisterConnectHandler(final ConnectHandler handler) {
            this.connectHandler.remove(handler);
        }
        
        private boolean onConnect(final int transactionType, final Ddeml.HSZ topic, final Ddeml.HSZ service, final Ddeml.CONVCONTEXT convcontext, final boolean sameInstance) {
            boolean oneHandlerTrue = false;
            for (final ConnectHandler handler : this.connectHandler) {
                if (handler.onConnect(transactionType, topic, service, convcontext, sameInstance)) {
                    oneHandlerTrue = true;
                }
            }
            return oneHandlerTrue;
        }
        
        public void registerAdvReqHandler(final AdvreqHandler handler) {
            this.advReqHandler.add(handler);
        }
        
        public void unregisterAdvReqHandler(final AdvreqHandler handler) {
            this.advReqHandler.remove(handler);
        }
        
        private Ddeml.HDDEDATA onAdvreq(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item, final int count) {
            for (final AdvreqHandler handler : this.advReqHandler) {
                final Ddeml.HDDEDATA result = handler.onAdvreq(transactionType, dataFormat, hconv, topic, item, count);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
        
        public void registerRequestHandler(final RequestHandler handler) {
            this.requestHandler.add(handler);
        }
        
        public void unregisterRequestHandler(final RequestHandler handler) {
            this.requestHandler.remove(handler);
        }
        
        private Ddeml.HDDEDATA onRequest(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item) {
            for (final RequestHandler handler : this.requestHandler) {
                final Ddeml.HDDEDATA result = handler.onRequest(transactionType, dataFormat, hconv, topic, item);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
        
        public void registerWildconnectHandler(final WildconnectHandler handler) {
            this.wildconnectHandler.add(handler);
        }
        
        public void unregisterWildconnectHandler(final WildconnectHandler handler) {
            this.wildconnectHandler.remove(handler);
        }
        
        private Ddeml.HSZPAIR[] onWildconnect(final int transactionType, final Ddeml.HSZ topic, final Ddeml.HSZ service, final Ddeml.CONVCONTEXT convcontext, final boolean sameInstance) {
            final List<Ddeml.HSZPAIR> hszpairs = new ArrayList<Ddeml.HSZPAIR>(1);
            for (final WildconnectHandler handler : this.wildconnectHandler) {
                hszpairs.addAll(handler.onWildconnect(transactionType, topic, service, convcontext, sameInstance));
            }
            return hszpairs.toArray(new Ddeml.HSZPAIR[0]);
        }
        
        public void registerAdvdataHandler(final AdvdataHandler handler) {
            this.advdataHandler.add(handler);
        }
        
        public void unregisterAdvdataHandler(final AdvdataHandler handler) {
            this.advdataHandler.remove(handler);
        }
        
        private int onAdvdata(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item, final Ddeml.HDDEDATA hdata) {
            for (final AdvdataHandler handler : this.advdataHandler) {
                final int result = handler.onAdvdata(transactionType, dataFormat, hconv, topic, item, hdata);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
        
        public void registerExecuteHandler(final ExecuteHandler handler) {
            this.executeHandler.add(handler);
        }
        
        public void unregisterExecuteHandler(final ExecuteHandler handler) {
            this.executeHandler.remove(handler);
        }
        
        private int onExecute(final int transactionType, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HDDEDATA commandString) {
            for (final ExecuteHandler handler : this.executeHandler) {
                final int result = handler.onExecute(transactionType, hconv, topic, commandString);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
        
        public void registerPokeHandler(final PokeHandler handler) {
            this.pokeHandler.add(handler);
        }
        
        public void unregisterPokeHandler(final PokeHandler handler) {
            this.pokeHandler.remove(handler);
        }
        
        private int onPoke(final int transactionType, final int dataFormat, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ item, final Ddeml.HDDEDATA hdata) {
            for (final PokeHandler handler : this.pokeHandler) {
                final int result = handler.onPoke(transactionType, dataFormat, hconv, topic, item, hdata);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
        
        public void registerConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.connectConfirmHandler.add(handler);
        }
        
        public void unregisterConnectConfirmHandler(final ConnectConfirmHandler handler) {
            this.connectConfirmHandler.remove(handler);
        }
        
        private void onConnectConfirm(final int transactionType, final Ddeml.HCONV hconv, final Ddeml.HSZ topic, final Ddeml.HSZ service, final boolean sameInstance) {
            for (final ConnectConfirmHandler handler : this.connectConfirmHandler) {
                handler.onConnectConfirm(transactionType, hconv, topic, service, sameInstance);
            }
        }
        
        public void registerDisconnectHandler(final DisconnectHandler handler) {
            this.disconnectHandler.add(handler);
        }
        
        public void unregisterDisconnectHandler(final DisconnectHandler handler) {
            this.disconnectHandler.remove(handler);
        }
        
        private void onDisconnect(final int transactionType, final Ddeml.HCONV hconv, final boolean sameInstance) {
            for (final DisconnectHandler handler : this.disconnectHandler) {
                handler.onDisconnect(transactionType, hconv, sameInstance);
            }
        }
        
        public void registerErrorHandler(final ErrorHandler handler) {
            this.errorHandler.add(handler);
        }
        
        public void unregisterErrorHandler(final ErrorHandler handler) {
            this.errorHandler.remove(handler);
        }
        
        private void onError(final int transactionType, final Ddeml.HCONV hconv, final int errorCode) {
            for (final ErrorHandler handler : this.errorHandler) {
                handler.onError(transactionType, hconv, errorCode);
            }
        }
        
        public void registerRegisterHandler(final RegisterHandler handler) {
            this.registerHandler.add(handler);
        }
        
        public void unregisterRegisterHandler(final RegisterHandler handler) {
            this.registerHandler.remove(handler);
        }
        
        private void onRegister(final int transactionType, final Ddeml.HSZ baseServiceName, final Ddeml.HSZ instanceSpecificServiceName) {
            for (final RegisterHandler handler : this.registerHandler) {
                handler.onRegister(transactionType, baseServiceName, instanceSpecificServiceName);
            }
        }
        
        public void registerXactCompleteHandler(final XactCompleteHandler handler) {
            this.xactCompleteHandler.add(handler);
        }
        
        public void xactCompleteXactCompleteHandler(final XactCompleteHandler handler) {
            this.xactCompleteHandler.remove(handler);
        }
        
        private void onXactComplete(final int transactionType, final int dataFormat, final Ddeml.HCONV hConv, final Ddeml.HSZ topic, final Ddeml.HSZ item, final Ddeml.HDDEDATA hdata, final BaseTSD.ULONG_PTR transactionIdentifier, final BaseTSD.ULONG_PTR statusFlag) {
            for (final XactCompleteHandler handler : this.xactCompleteHandler) {
                handler.onXactComplete(transactionType, dataFormat, hConv, topic, item, hdata, transactionIdentifier, statusFlag);
            }
        }
        
        public void registerUnregisterHandler(final UnregisterHandler handler) {
            this.unregisterHandler.add(handler);
        }
        
        public void unregisterUnregisterHandler(final UnregisterHandler handler) {
            this.unregisterHandler.remove(handler);
        }
        
        private void onUnregister(final int transactionType, final Ddeml.HSZ baseServiceName, final Ddeml.HSZ instanceSpecificServiceName) {
            for (final UnregisterHandler handler : this.unregisterHandler) {
                handler.onUnregister(transactionType, baseServiceName, instanceSpecificServiceName);
            }
        }
        
        public void registerMonitorHandler(final MonitorHandler handler) {
            this.monitorHandler.add(handler);
        }
        
        public void unregisterMonitorHandler(final MonitorHandler handler) {
            this.monitorHandler.remove(handler);
        }
        
        private void onMonitor(final int transactionType, final Ddeml.HDDEDATA hdata, final int dwData2) {
            for (final MonitorHandler handler : this.monitorHandler) {
                handler.onMonitor(transactionType, hdata, dwData2);
            }
        }
        
        static {
            LOG = Logger.getLogger(DdeAdapter.class.getName());
        }
        
        public static class BlockException extends RuntimeException
        {
        }
    }
    
    public static class DdemlException extends RuntimeException
    {
        private static final Map<Integer, String> ERROR_CODE_MAP;
        private final int errorCode;
        
        public static DdemlException create(final int errorCode) {
            final String errorName = DdemlException.ERROR_CODE_MAP.get(errorCode);
            return new DdemlException(errorCode, String.format("%s (Code: 0x%X)", (errorName != null) ? errorName : "", errorCode));
        }
        
        public DdemlException(final int errorCode, final String message) {
            super(message);
            this.errorCode = errorCode;
        }
        
        public int getErrorCode() {
            return this.errorCode;
        }
        
        static {
            final Map<Integer, String> errorCodeMapBuilder = new HashMap<Integer, String>();
            for (final Field f : Ddeml.class.getFields()) {
                final String name = f.getName();
                if (name.startsWith("DMLERR_") && !name.equals("DMLERR_FIRST") && !name.equals("DMLERR_LAST")) {
                    try {
                        errorCodeMapBuilder.put(f.getInt(null), name);
                    }
                    catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    }
                    catch (IllegalAccessException ex2) {
                        throw new RuntimeException(ex2);
                    }
                }
            }
            ERROR_CODE_MAP = Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)errorCodeMapBuilder);
        }
    }
    
    public interface IDdeConnectionList extends Closeable
    {
        Ddeml.HCONVLIST getHandle();
        
        IDdeConnection queryNextServer(final IDdeConnection p0);
        
        void close();
    }
    
    public interface IDdeConnection extends Closeable
    {
        Ddeml.HCONV getConv();
        
        void execute(final String p0, final int p1, final WinDef.DWORDByReference p2, final BaseTSD.DWORD_PTR p3);
        
        void poke(final Pointer p0, final int p1, final Ddeml.HSZ p2, final int p3, final int p4, final WinDef.DWORDByReference p5, final BaseTSD.DWORD_PTR p6);
        
        void poke(final Pointer p0, final int p1, final String p2, final int p3, final int p4, final WinDef.DWORDByReference p5, final BaseTSD.DWORD_PTR p6);
        
        Ddeml.HDDEDATA request(final Ddeml.HSZ p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        Ddeml.HDDEDATA request(final String p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        Ddeml.HDDEDATA clientTransaction(final Pointer p0, final int p1, final Ddeml.HSZ p2, final int p3, final int p4, final int p5, final WinDef.DWORDByReference p6, final BaseTSD.DWORD_PTR p7);
        
        Ddeml.HDDEDATA clientTransaction(final Pointer p0, final int p1, final String p2, final int p3, final int p4, final int p5, final WinDef.DWORDByReference p6, final BaseTSD.DWORD_PTR p7);
        
        void advstart(final Ddeml.HSZ p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        void advstart(final String p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        void advstop(final Ddeml.HSZ p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        void advstop(final String p0, final int p1, final int p2, final WinDef.DWORDByReference p3, final BaseTSD.DWORD_PTR p4);
        
        void abandonTransaction(final int p0);
        
        void abandonTransactions();
        
        void impersonateClient();
        
        void close();
        
        void reconnect();
        
        boolean enableCallback(final int p0);
        
        void setUserHandle(final int p0, final BaseTSD.DWORD_PTR p1) throws DdemlException;
        
        Ddeml.CONVINFO queryConvInfo(final int p0) throws DdemlException;
    }
    
    public interface IDdeClient extends Closeable
    {
        Integer getInstanceIdentitifier();
        
        void initialize(final int p0) throws DdemlException;
        
        Ddeml.HSZ createStringHandle(final String p0) throws DdemlException;
        
        String queryString(final Ddeml.HSZ p0) throws DdemlException;
        
        boolean freeStringHandle(final Ddeml.HSZ p0);
        
        boolean keepStringHandle(final Ddeml.HSZ p0);
        
        void nameService(final Ddeml.HSZ p0, final int p1) throws DdemlException;
        
        void nameService(final String p0, final int p1) throws DdemlException;
        
        int getLastError();
        
        IDdeConnection connect(final Ddeml.HSZ p0, final Ddeml.HSZ p1, final Ddeml.CONVCONTEXT p2);
        
        IDdeConnection connect(final String p0, final String p1, final Ddeml.CONVCONTEXT p2);
        
        Ddeml.HDDEDATA createDataHandle(final Pointer p0, final int p1, final int p2, final Ddeml.HSZ p3, final int p4, final int p5);
        
        void freeDataHandle(final Ddeml.HDDEDATA p0);
        
        Ddeml.HDDEDATA addData(final Ddeml.HDDEDATA p0, final Pointer p1, final int p2, final int p3);
        
        int getData(final Ddeml.HDDEDATA p0, final Pointer p1, final int p2, final int p3);
        
        Pointer accessData(final Ddeml.HDDEDATA p0, final WinDef.DWORDByReference p1);
        
        void unaccessData(final Ddeml.HDDEDATA p0);
        
        void postAdvise(final Ddeml.HSZ p0, final Ddeml.HSZ p1);
        
        void postAdvise(final String p0, final String p1);
        
        void abandonTransactions();
        
        IDdeConnectionList connectList(final Ddeml.HSZ p0, final Ddeml.HSZ p1, final IDdeConnectionList p2, final Ddeml.CONVCONTEXT p3);
        
        IDdeConnectionList connectList(final String p0, final String p1, final IDdeConnectionList p2, final Ddeml.CONVCONTEXT p3);
        
        boolean enableCallback(final int p0);
        
        boolean uninitialize();
        
        IDdeConnection wrap(final Ddeml.HCONV p0);
        
        void registerAdvstartHandler(final AdvstartHandler p0);
        
        void unregisterAdvstartHandler(final AdvstartHandler p0);
        
        void registerAdvstopHandler(final AdvstopHandler p0);
        
        void unregisterAdvstopHandler(final AdvstopHandler p0);
        
        void registerConnectHandler(final ConnectHandler p0);
        
        void unregisterConnectHandler(final ConnectHandler p0);
        
        void registerAdvReqHandler(final AdvreqHandler p0);
        
        void unregisterAdvReqHandler(final AdvreqHandler p0);
        
        void registerRequestHandler(final RequestHandler p0);
        
        void unregisterRequestHandler(final RequestHandler p0);
        
        void registerWildconnectHandler(final WildconnectHandler p0);
        
        void unregisterWildconnectHandler(final WildconnectHandler p0);
        
        void registerAdvdataHandler(final AdvdataHandler p0);
        
        void unregisterAdvdataHandler(final AdvdataHandler p0);
        
        void registerExecuteHandler(final ExecuteHandler p0);
        
        void unregisterExecuteHandler(final ExecuteHandler p0);
        
        void registerPokeHandler(final PokeHandler p0);
        
        void unregisterPokeHandler(final PokeHandler p0);
        
        void registerConnectConfirmHandler(final ConnectConfirmHandler p0);
        
        void unregisterConnectConfirmHandler(final ConnectConfirmHandler p0);
        
        void registerDisconnectHandler(final DisconnectHandler p0);
        
        void unregisterDisconnectHandler(final DisconnectHandler p0);
        
        void registerErrorHandler(final ErrorHandler p0);
        
        void unregisterErrorHandler(final ErrorHandler p0);
        
        void registerRegisterHandler(final RegisterHandler p0);
        
        void unregisterRegisterHandler(final RegisterHandler p0);
        
        void registerXactCompleteHandler(final XactCompleteHandler p0);
        
        void unregisterXactCompleteHandler(final XactCompleteHandler p0);
        
        void registerUnregisterHandler(final UnregisterHandler p0);
        
        void unregisterUnregisterHandler(final UnregisterHandler p0);
        
        void registerMonitorHandler(final MonitorHandler p0);
        
        void unregisterMonitorHandler(final MonitorHandler p0);
    }
    
    public interface AdvstartHandler
    {
        boolean onAdvstart(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4);
    }
    
    public interface AdvstopHandler
    {
        void onAdvstop(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4);
    }
    
    public interface ConnectHandler
    {
        boolean onConnect(final int p0, final Ddeml.HSZ p1, final Ddeml.HSZ p2, final Ddeml.CONVCONTEXT p3, final boolean p4);
    }
    
    public interface AdvreqHandler
    {
        Ddeml.HDDEDATA onAdvreq(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4, final int p5);
    }
    
    public interface RequestHandler
    {
        Ddeml.HDDEDATA onRequest(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4);
    }
    
    public interface WildconnectHandler
    {
        List<Ddeml.HSZPAIR> onWildconnect(final int p0, final Ddeml.HSZ p1, final Ddeml.HSZ p2, final Ddeml.CONVCONTEXT p3, final boolean p4);
    }
    
    public interface ExecuteHandler
    {
        int onExecute(final int p0, final Ddeml.HCONV p1, final Ddeml.HSZ p2, final Ddeml.HDDEDATA p3);
    }
    
    public interface PokeHandler
    {
        int onPoke(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4, final Ddeml.HDDEDATA p5);
    }
    
    public interface ConnectConfirmHandler
    {
        void onConnectConfirm(final int p0, final Ddeml.HCONV p1, final Ddeml.HSZ p2, final Ddeml.HSZ p3, final boolean p4);
    }
    
    public interface DisconnectHandler
    {
        void onDisconnect(final int p0, final Ddeml.HCONV p1, final boolean p2);
    }
    
    public interface ErrorHandler
    {
        void onError(final int p0, final Ddeml.HCONV p1, final int p2);
    }
    
    public interface RegisterHandler
    {
        void onRegister(final int p0, final Ddeml.HSZ p1, final Ddeml.HSZ p2);
    }
    
    public interface XactCompleteHandler
    {
        void onXactComplete(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4, final Ddeml.HDDEDATA p5, final BaseTSD.ULONG_PTR p6, final BaseTSD.ULONG_PTR p7);
    }
    
    public interface UnregisterHandler
    {
        void onUnregister(final int p0, final Ddeml.HSZ p1, final Ddeml.HSZ p2);
    }
    
    public interface MonitorHandler
    {
        void onMonitor(final int p0, final Ddeml.HDDEDATA p1, final int p2);
    }
    
    public interface AdvdataHandler
    {
        int onAdvdata(final int p0, final int p1, final Ddeml.HCONV p2, final Ddeml.HSZ p3, final Ddeml.HSZ p4, final Ddeml.HDDEDATA p5);
    }
}
