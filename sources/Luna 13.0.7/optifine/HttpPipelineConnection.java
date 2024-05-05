package optifine;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpPipelineConnection
{
  private String host;
  private int port;
  private Proxy proxy;
  private List<HttpPipelineRequest> listRequests;
  private List<HttpPipelineRequest> listRequestsSend;
  private Socket socket;
  private InputStream inputStream;
  private OutputStream outputStream;
  private HttpPipelineSender httpPipelineSender;
  private HttpPipelineReceiver httpPipelineReceiver;
  private int countRequests;
  private boolean responseReceived;
  private long keepaliveTimeoutMs;
  private int keepaliveMaxCount;
  private long timeLastActivityMs;
  private boolean terminated;
  private static final String LF = "\n";
  public static final int TIMEOUT_CONNECT_MS = 5000;
  public static final int TIMEOUT_READ_MS = 5000;
  private static final Pattern patternFullUrl = Pattern.compile("^[a-zA-Z]+://.*");
  
  public HttpPipelineConnection(String host, int port)
  {
    this(host, port, Proxy.NO_PROXY);
  }
  
  public HttpPipelineConnection(String host, int port, Proxy proxy)
  {
    this.host = null;
    this.port = 0;
    this.proxy = Proxy.NO_PROXY;
    this.listRequests = new LinkedList();
    this.listRequestsSend = new LinkedList();
    this.socket = null;
    this.inputStream = null;
    this.outputStream = null;
    this.httpPipelineSender = null;
    this.httpPipelineReceiver = null;
    this.countRequests = 0;
    this.responseReceived = false;
    this.keepaliveTimeoutMs = 5000L;
    this.keepaliveMaxCount = 1000;
    this.timeLastActivityMs = System.currentTimeMillis();
    this.terminated = false;
    this.host = host;
    this.port = port;
    this.proxy = proxy;
    this.httpPipelineSender = new HttpPipelineSender(this);
    this.httpPipelineSender.start();
    this.httpPipelineReceiver = new HttpPipelineReceiver(this);
    this.httpPipelineReceiver.start();
  }
  
  public synchronized boolean addRequest(HttpPipelineRequest pr)
  {
    if (isClosed()) {
      return false;
    }
    addRequest(pr, this.listRequests);
    addRequest(pr, this.listRequestsSend);
    this.countRequests += 1;
    return true;
  }
  
  private void addRequest(HttpPipelineRequest pr, List<HttpPipelineRequest> list)
  {
    list.add(pr);
    notifyAll();
  }
  
  public synchronized void setSocket(Socket s)
    throws IOException
  {
    if (!this.terminated)
    {
      if (this.socket != null) {
        throw new IllegalArgumentException("Already connected");
      }
      this.socket = s;
      this.socket.setTcpNoDelay(true);
      this.inputStream = this.socket.getInputStream();
      this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());
      onActivity();
      notifyAll();
    }
  }
  
  public synchronized OutputStream getOutputStream()
    throws IOException, InterruptedException
  {
    while (this.outputStream == null)
    {
      checkTimeout();
      wait(1000L);
    }
    return this.outputStream;
  }
  
  public synchronized InputStream getInputStream()
    throws IOException, InterruptedException
  {
    while (this.inputStream == null)
    {
      checkTimeout();
      wait(1000L);
    }
    return this.inputStream;
  }
  
  public synchronized HttpPipelineRequest getNextRequestSend()
    throws InterruptedException, IOException
  {
    if ((this.listRequestsSend.size() <= 0) && (this.outputStream != null)) {
      this.outputStream.flush();
    }
    return getNextRequest(this.listRequestsSend, true);
  }
  
  public synchronized HttpPipelineRequest getNextRequestReceive()
    throws InterruptedException
  {
    return getNextRequest(this.listRequests, false);
  }
  
  private HttpPipelineRequest getNextRequest(List<HttpPipelineRequest> list, boolean remove)
    throws InterruptedException
  {
    while (list.size() <= 0)
    {
      checkTimeout();
      wait(1000L);
    }
    onActivity();
    if (remove) {
      return (HttpPipelineRequest)list.remove(0);
    }
    return (HttpPipelineRequest)list.get(0);
  }
  
  private void checkTimeout()
  {
    if (this.socket != null)
    {
      long timeoutMs = this.keepaliveTimeoutMs;
      if (this.listRequests.size() > 0) {
        timeoutMs = 5000L;
      }
      long timeNowMs = System.currentTimeMillis();
      if (timeNowMs > this.timeLastActivityMs + timeoutMs) {
        terminate(new InterruptedException("Timeout " + timeoutMs));
      }
    }
  }
  
  private void onActivity()
  {
    this.timeLastActivityMs = System.currentTimeMillis();
  }
  
  public synchronized void onRequestSent(HttpPipelineRequest pr)
  {
    if (!this.terminated) {
      onActivity();
    }
  }
  
  public synchronized void onResponseReceived(HttpPipelineRequest pr, HttpResponse resp)
  {
    if (!this.terminated)
    {
      this.responseReceived = true;
      onActivity();
      if ((this.listRequests.size() > 0) && (this.listRequests.get(0) == pr))
      {
        this.listRequests.remove(0);
        pr.setClosed(true);
        String location = resp.getHeader("Location");
        if ((resp.getStatus() / 100 == 3) && (location != null) && (pr.getHttpRequest().getRedirects() < 5))
        {
          try
          {
            location = normalizeUrl(location, pr.getHttpRequest());
            HttpRequest listener1 = HttpPipeline.makeRequest(location, pr.getHttpRequest().getProxy());
            listener1.setRedirects(pr.getHttpRequest().getRedirects() + 1);
            HttpPipelineRequest hpr2 = new HttpPipelineRequest(listener1, pr.getHttpListener());
            HttpPipeline.addRequest(hpr2);
          }
          catch (IOException var6)
          {
            pr.getHttpListener().failed(pr.getHttpRequest(), var6);
          }
        }
        else
        {
          HttpListener listener = pr.getHttpListener();
          listener.finished(pr.getHttpRequest(), resp);
        }
        checkResponseHeader(resp);
      }
      else
      {
        throw new IllegalArgumentException("Response out of order: " + pr);
      }
    }
  }
  
  private String normalizeUrl(String url, HttpRequest hr)
  {
    if (patternFullUrl.matcher(url).matches()) {
      return url;
    }
    if (url.startsWith("//")) {
      return "http:" + url;
    }
    String server = hr.getHost();
    if (hr.getPort() != 80) {
      server = server + ":" + hr.getPort();
    }
    if (url.startsWith("/")) {
      return "http://" + server + url;
    }
    String file = hr.getFile();
    int pos = file.lastIndexOf("/");
    return "http://" + server + "/" + url;
  }
  
  private void checkResponseHeader(HttpResponse resp)
  {
    String connStr = resp.getHeader("Connection");
    if ((connStr != null) && (!connStr.toLowerCase().equals("keep-alive"))) {
      terminate(new EOFException("Connection not keep-alive"));
    }
    String keepAliveStr = resp.getHeader("Keep-Alive");
    if (keepAliveStr != null)
    {
      String[] parts = Config.tokenize(keepAliveStr, ",;");
      for (int i = 0; i < parts.length; i++)
      {
        String part = parts[i];
        String[] tokens = split(part, '=');
        if (tokens.length >= 2)
        {
          if (tokens[0].equals("timeout"))
          {
            int max = Config.parseInt(tokens[1], -1);
            if (max > 0) {
              this.keepaliveTimeoutMs = (max * 1000);
            }
          }
          if (tokens[0].equals("max"))
          {
            int max = Config.parseInt(tokens[1], -1);
            if (max > 0) {
              this.keepaliveMaxCount = max;
            }
          }
        }
      }
    }
  }
  
  private String[] split(String str, char separator)
  {
    int pos = str.indexOf(separator);
    if (pos < 0) {
      return new String[] { str };
    }
    String str1 = str.substring(0, pos);
    String str2 = str.substring(pos + 1);
    return new String[] { str1, str2 };
  }
  
  public synchronized void onExceptionSend(HttpPipelineRequest pr, Exception e)
  {
    terminate(e);
  }
  
  public synchronized void onExceptionReceive(HttpPipelineRequest pr, Exception e)
  {
    terminate(e);
  }
  
  private synchronized void terminate(Exception e)
  {
    if (!this.terminated)
    {
      this.terminated = true;
      terminateRequests(e);
      if (this.httpPipelineSender != null) {
        this.httpPipelineSender.interrupt();
      }
      if (this.httpPipelineReceiver != null) {
        this.httpPipelineReceiver.interrupt();
      }
      try
      {
        if (this.socket != null) {
          this.socket.close();
        }
      }
      catch (IOException localIOException) {}
      this.socket = null;
      this.inputStream = null;
      this.outputStream = null;
    }
  }
  
  private void terminateRequests(Exception e)
  {
    if (this.listRequests.size() > 0)
    {
      if (!this.responseReceived)
      {
        HttpPipelineRequest pr = (HttpPipelineRequest)this.listRequests.remove(0);
        pr.getHttpListener().failed(pr.getHttpRequest(), e);
        pr.setClosed(true);
      }
      while (this.listRequests.size() > 0)
      {
        HttpPipelineRequest pr = (HttpPipelineRequest)this.listRequests.remove(0);
        HttpPipeline.addRequest(pr);
      }
    }
  }
  
  public synchronized boolean isClosed()
  {
    return this.terminated;
  }
  
  public int getCountRequests()
  {
    return this.countRequests;
  }
  
  public synchronized boolean hasActiveRequests()
  {
    return this.listRequests.size() > 0;
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public Proxy getProxy()
  {
    return this.proxy;
  }
}
