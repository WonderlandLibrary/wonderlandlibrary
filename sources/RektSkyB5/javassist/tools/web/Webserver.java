/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.tools.web.BadHttpRequest;
import javassist.tools.web.ServiceThread;

public class Webserver {
    private ServerSocket socket;
    private ClassPool classPool;
    protected Translator translator;
    private static final byte[] endofline = new byte[]{13, 10};
    private static final int typeHtml = 1;
    private static final int typeClass = 2;
    private static final int typeGif = 3;
    private static final int typeJpeg = 4;
    private static final int typeText = 5;
    public String debugDir = null;
    public String htmlfileBase = null;

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            Webserver web = new Webserver(args[0]);
            web.run();
        } else {
            System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
        }
    }

    public Webserver(String port) throws IOException {
        this(Integer.parseInt(port));
    }

    public Webserver(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.classPool = null;
        this.translator = null;
    }

    public void setClassPool(ClassPool loader) {
        this.classPool = loader;
    }

    public void addTranslator(ClassPool cp, Translator t2) throws NotFoundException, CannotCompileException {
        this.classPool = cp;
        this.translator = t2;
        t2.start(this.classPool);
    }

    public void end() throws IOException {
        this.socket.close();
    }

    public void logging(String msg) {
        System.out.println(msg);
    }

    public void logging(String msg1, String msg2) {
        System.out.print(msg1);
        System.out.print(" ");
        System.out.println(msg2);
    }

    public void logging(String msg1, String msg2, String msg3) {
        System.out.print(msg1);
        System.out.print(" ");
        System.out.print(msg2);
        System.out.print(" ");
        System.out.println(msg3);
    }

    public void logging2(String msg) {
        System.out.print("    ");
        System.out.println(msg);
    }

    public void run() {
        System.err.println("ready to service...");
        while (true) {
            try {
                while (true) {
                    ServiceThread th = new ServiceThread(this, this.socket.accept());
                    th.start();
                }
            }
            catch (IOException e2) {
                this.logging(e2.toString());
                continue;
            }
            break;
        }
    }

    final void process(Socket clnt) throws IOException {
        BufferedInputStream in = new BufferedInputStream(clnt.getInputStream());
        String cmd = this.readLine(in);
        this.logging(clnt.getInetAddress().getHostName(), new Date().toString(), cmd);
        while (this.skipLine(in) > 0) {
        }
        BufferedOutputStream out = new BufferedOutputStream(clnt.getOutputStream());
        try {
            this.doReply(in, out, cmd);
        }
        catch (BadHttpRequest e2) {
            this.replyError(out, e2);
        }
        ((OutputStream)out).flush();
        ((InputStream)in).close();
        ((OutputStream)out).close();
        clnt.close();
    }

    private String readLine(InputStream in) throws IOException {
        int c2;
        StringBuffer buf = new StringBuffer();
        while ((c2 = in.read()) >= 0 && c2 != 13) {
            buf.append((char)c2);
        }
        in.read();
        return buf.toString();
    }

    private int skipLine(InputStream in) throws IOException {
        int c2;
        int len = 0;
        while ((c2 = in.read()) >= 0 && c2 != 13) {
            ++len;
        }
        in.read();
        return len;
    }

    public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
        InputStream fin;
        File file;
        String urlName;
        if (!cmd.startsWith("GET /")) {
            throw new BadHttpRequest();
        }
        String filename = urlName = cmd.substring(5, cmd.indexOf(32, 5));
        int fileType = filename.endsWith(".class") ? 2 : (filename.endsWith(".html") || filename.endsWith(".htm") ? 1 : (filename.endsWith(".gif") ? 3 : (filename.endsWith(".jpg") ? 4 : 5)));
        int len = filename.length();
        if (fileType == 2 && this.letUsersSendClassfile(out, filename, len)) {
            return;
        }
        this.checkFilename(filename, len);
        if (this.htmlfileBase != null) {
            filename = this.htmlfileBase + filename;
        }
        if (File.separatorChar != '/') {
            filename = filename.replace('/', File.separatorChar);
        }
        if ((file = new File(filename)).canRead()) {
            this.sendHeader(out, file.length(), fileType);
            FileInputStream fin2 = new FileInputStream(file);
            byte[] filebuffer = new byte[4096];
            while ((len = fin2.read(filebuffer)) > 0) {
                out.write(filebuffer, 0, len);
            }
            fin2.close();
            return;
        }
        if (fileType == 2 && (fin = this.getClass().getResourceAsStream("/" + urlName)) != null) {
            ByteArrayOutputStream barray = new ByteArrayOutputStream();
            byte[] filebuffer = new byte[4096];
            while ((len = fin.read(filebuffer)) > 0) {
                barray.write(filebuffer, 0, len);
            }
            byte[] classfile = barray.toByteArray();
            this.sendHeader(out, classfile.length, 2);
            out.write(classfile);
            fin.close();
            return;
        }
        throw new BadHttpRequest();
    }

    private void checkFilename(String filename, int len) throws BadHttpRequest {
        for (int i2 = 0; i2 < len; ++i2) {
            char c2 = filename.charAt(i2);
            if (Character.isJavaIdentifierPart(c2) || c2 == '.' || c2 == '/') continue;
            throw new BadHttpRequest();
        }
        if (filename.indexOf("..") >= 0) {
            throw new BadHttpRequest();
        }
    }

    private boolean letUsersSendClassfile(OutputStream out, String filename, int length) throws IOException, BadHttpRequest {
        byte[] classfile;
        if (this.classPool == null) {
            return false;
        }
        String classname = filename.substring(0, length - 6).replace('/', '.');
        try {
            if (this.translator != null) {
                this.translator.onLoad(this.classPool, classname);
            }
            CtClass c2 = this.classPool.get(classname);
            classfile = c2.toBytecode();
            if (this.debugDir != null) {
                c2.writeFile(this.debugDir);
            }
        }
        catch (Exception e2) {
            throw new BadHttpRequest(e2);
        }
        this.sendHeader(out, classfile.length, 2);
        out.write(classfile);
        return true;
    }

    private void sendHeader(OutputStream out, long dataLength, int filetype) throws IOException {
        out.write("HTTP/1.0 200 OK".getBytes());
        out.write(endofline);
        out.write("Content-Length: ".getBytes());
        out.write(Long.toString(dataLength).getBytes());
        out.write(endofline);
        if (filetype == 2) {
            out.write("Content-Type: application/octet-stream".getBytes());
        } else if (filetype == 1) {
            out.write("Content-Type: text/html".getBytes());
        } else if (filetype == 3) {
            out.write("Content-Type: image/gif".getBytes());
        } else if (filetype == 4) {
            out.write("Content-Type: image/jpg".getBytes());
        } else if (filetype == 5) {
            out.write("Content-Type: text/plain".getBytes());
        }
        out.write(endofline);
        out.write(endofline);
    }

    private void replyError(OutputStream out, BadHttpRequest e2) throws IOException {
        this.logging2("bad request: " + e2.toString());
        out.write("HTTP/1.0 400 Bad Request".getBytes());
        out.write(endofline);
        out.write(endofline);
        out.write("<H1>Bad Request</H1>".getBytes());
    }
}

