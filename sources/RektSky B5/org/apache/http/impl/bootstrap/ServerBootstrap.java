/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.bootstrap;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.SSLServerSetupHandler;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerMapper;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

public class ServerBootstrap {
    private int listenerPort;
    private InetAddress localAddress;
    private SocketConfig socketConfig;
    private ConnectionConfig connectionConfig;
    private LinkedList<HttpRequestInterceptor> requestFirst;
    private LinkedList<HttpRequestInterceptor> requestLast;
    private LinkedList<HttpResponseInterceptor> responseFirst;
    private LinkedList<HttpResponseInterceptor> responseLast;
    private String serverInfo;
    private HttpProcessor httpProcessor;
    private ConnectionReuseStrategy connStrategy;
    private HttpResponseFactory responseFactory;
    private HttpRequestHandlerMapper handlerMapper;
    private Map<String, HttpRequestHandler> handlerMap;
    private HttpExpectationVerifier expectationVerifier;
    private ServerSocketFactory serverSocketFactory;
    private SSLContext sslContext;
    private SSLServerSetupHandler sslSetupHandler;
    private HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
    private ExceptionLogger exceptionLogger;

    private ServerBootstrap() {
    }

    public static ServerBootstrap bootstrap() {
        return new ServerBootstrap();
    }

    public final ServerBootstrap setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
        return this;
    }

    public final ServerBootstrap setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    public final ServerBootstrap setSocketConfig(SocketConfig socketConfig) {
        this.socketConfig = socketConfig;
        return this;
    }

    public final ServerBootstrap setConnectionConfig(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
        return this;
    }

    public final ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
        this.httpProcessor = httpProcessor;
        return this;
    }

    public final ServerBootstrap addInterceptorFirst(HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.responseFirst == null) {
            this.responseFirst = new LinkedList();
        }
        this.responseFirst.addFirst(itcp);
        return this;
    }

    public final ServerBootstrap addInterceptorLast(HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.responseLast == null) {
            this.responseLast = new LinkedList();
        }
        this.responseLast.addLast(itcp);
        return this;
    }

    public final ServerBootstrap addInterceptorFirst(HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.requestFirst == null) {
            this.requestFirst = new LinkedList();
        }
        this.requestFirst.addFirst(itcp);
        return this;
    }

    public final ServerBootstrap addInterceptorLast(HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.requestLast == null) {
            this.requestLast = new LinkedList();
        }
        this.requestLast.addLast(itcp);
        return this;
    }

    public final ServerBootstrap setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
        return this;
    }

    public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
        this.connStrategy = connStrategy;
        return this;
    }

    public final ServerBootstrap setResponseFactory(HttpResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
        return this;
    }

    public final ServerBootstrap setHandlerMapper(HttpRequestHandlerMapper handlerMapper) {
        this.handlerMapper = handlerMapper;
        return this;
    }

    public final ServerBootstrap registerHandler(String pattern, HttpRequestHandler handler) {
        if (pattern == null || handler == null) {
            return this;
        }
        if (this.handlerMap == null) {
            this.handlerMap = new HashMap<String, HttpRequestHandler>();
        }
        this.handlerMap.put(pattern, handler);
        return this;
    }

    public final ServerBootstrap setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
        this.expectationVerifier = expectationVerifier;
        return this;
    }

    public final ServerBootstrap setConnectionFactory(HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory) {
        this.connectionFactory = connectionFactory;
        return this;
    }

    public final ServerBootstrap setSslSetupHandler(SSLServerSetupHandler sslSetupHandler) {
        this.sslSetupHandler = sslSetupHandler;
        return this;
    }

    public final ServerBootstrap setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
        this.serverSocketFactory = serverSocketFactory;
        return this;
    }

    public final ServerBootstrap setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public final ServerBootstrap setExceptionLogger(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
        return this;
    }

    /*
     * WARNING - void declaration
     */
    public HttpServer create() {
        ExceptionLogger exceptionLoggerCopy;
        DefaultBHttpServerConnectionFactory connectionFactoryCopy;
        void var4_15;
        HttpResponseFactory httpResponseFactory;
        ConnectionReuseStrategy connStrategyCopy;
        HttpRequestHandlerMapper handlerMapperCopy;
        HttpProcessor httpProcessorCopy = this.httpProcessor;
        if (httpProcessorCopy == null) {
            String serverInfoCopy;
            HttpProcessorBuilder b2 = HttpProcessorBuilder.create();
            if (this.requestFirst != null) {
                for (HttpRequestInterceptor httpRequestInterceptor : this.requestFirst) {
                    b2.addFirst(httpRequestInterceptor);
                }
            }
            if (this.responseFirst != null) {
                for (HttpResponseInterceptor httpResponseInterceptor : this.responseFirst) {
                    b2.addFirst(httpResponseInterceptor);
                }
            }
            if ((serverInfoCopy = this.serverInfo) == null) {
                serverInfoCopy = "Apache-HttpCore/1.1";
            }
            b2.addAll(new ResponseDate(), new ResponseServer(serverInfoCopy), new ResponseContent(), new ResponseConnControl());
            if (this.requestLast != null) {
                for (HttpRequestInterceptor httpRequestInterceptor : this.requestLast) {
                    b2.addLast(httpRequestInterceptor);
                }
            }
            if (this.responseLast != null) {
                for (HttpResponseInterceptor httpResponseInterceptor : this.responseLast) {
                    b2.addLast(httpResponseInterceptor);
                }
            }
            httpProcessorCopy = b2.build();
        }
        if ((handlerMapperCopy = this.handlerMapper) == null) {
            UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
            if (this.handlerMap != null) {
                for (Map.Entry<String, HttpRequestHandler> entry : this.handlerMap.entrySet()) {
                    reqistry.register(entry.getKey(), entry.getValue());
                }
            }
            handlerMapperCopy = reqistry;
        }
        if ((connStrategyCopy = this.connStrategy) == null) {
            connStrategyCopy = DefaultConnectionReuseStrategy.INSTANCE;
        }
        if ((httpResponseFactory = this.responseFactory) == null) {
            DefaultHttpResponseFactory defaultHttpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
        }
        HttpService httpService = new HttpService(httpProcessorCopy, connStrategyCopy, (HttpResponseFactory)var4_15, handlerMapperCopy, this.expectationVerifier);
        ServerSocketFactory serverSocketFactoryCopy = this.serverSocketFactory;
        if (serverSocketFactoryCopy == null) {
            serverSocketFactoryCopy = this.sslContext != null ? this.sslContext.getServerSocketFactory() : ServerSocketFactory.getDefault();
        }
        if ((connectionFactoryCopy = this.connectionFactory) == null) {
            connectionFactoryCopy = this.connectionConfig != null ? new DefaultBHttpServerConnectionFactory(this.connectionConfig) : DefaultBHttpServerConnectionFactory.INSTANCE;
        }
        if ((exceptionLoggerCopy = this.exceptionLogger) == null) {
            exceptionLoggerCopy = ExceptionLogger.NO_OP;
        }
        return new HttpServer(this.listenerPort > 0 ? this.listenerPort : 0, this.localAddress, this.socketConfig != null ? this.socketConfig : SocketConfig.DEFAULT, serverSocketFactoryCopy, httpService, connectionFactoryCopy, this.sslSetupHandler, exceptionLoggerCopy);
    }
}

