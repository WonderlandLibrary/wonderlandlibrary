/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.input.ProxyInputStream;

public class ObservableInputStream
extends ProxyInputStream {
    private final List<Observer> observers = new ArrayList<Observer>();

    public ObservableInputStream(InputStream pProxy) {
        super(pProxy);
    }

    public void add(Observer pObserver) {
        this.observers.add(pObserver);
    }

    public void remove(Observer pObserver) {
        this.observers.remove(pObserver);
    }

    public void removeAllObservers() {
        this.observers.clear();
    }

    @Override
    public int read() throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read();
        }
        catch (IOException pException) {
            ioe = pException;
        }
        if (ioe != null) {
            this.noteError(ioe);
        } else if (result == -1) {
            this.noteFinished();
        } else {
            this.noteDataByte(result);
        }
        return result;
    }

    @Override
    public int read(byte[] pBuffer) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(pBuffer);
        }
        catch (IOException pException) {
            ioe = pException;
        }
        if (ioe != null) {
            this.noteError(ioe);
        } else if (result == -1) {
            this.noteFinished();
        } else if (result > 0) {
            this.noteDataBytes(pBuffer, 0, result);
        }
        return result;
    }

    @Override
    public int read(byte[] pBuffer, int pOffset, int pLength) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(pBuffer, pOffset, pLength);
        }
        catch (IOException pException) {
            ioe = pException;
        }
        if (ioe != null) {
            this.noteError(ioe);
        } else if (result == -1) {
            this.noteFinished();
        } else if (result > 0) {
            this.noteDataBytes(pBuffer, pOffset, result);
        }
        return result;
    }

    protected void noteDataBytes(byte[] pBuffer, int pOffset, int pLength) throws IOException {
        for (Observer observer : this.getObservers()) {
            observer.data(pBuffer, pOffset, pLength);
        }
    }

    protected void noteFinished() throws IOException {
        for (Observer observer : this.getObservers()) {
            observer.finished();
        }
    }

    protected void noteDataByte(int pDataByte) throws IOException {
        for (Observer observer : this.getObservers()) {
            observer.data(pDataByte);
        }
    }

    protected void noteError(IOException pException) throws IOException {
        for (Observer observer : this.getObservers()) {
            observer.error(pException);
        }
    }

    protected void noteClosed() throws IOException {
        for (Observer observer : this.getObservers()) {
            observer.closed();
        }
    }

    protected List<Observer> getObservers() {
        return this.observers;
    }

    @Override
    public void close() throws IOException {
        IOException ioe = null;
        try {
            super.close();
        }
        catch (IOException e2) {
            ioe = e2;
        }
        if (ioe == null) {
            this.noteClosed();
        } else {
            this.noteError(ioe);
        }
    }

    public void consume() throws IOException {
        int res;
        byte[] buffer = new byte[8192];
        while ((res = this.read(buffer)) != -1) {
        }
    }

    public static abstract class Observer {
        void data(int pByte) throws IOException {
        }

        void data(byte[] pBuffer, int pOffset, int pLength) throws IOException {
        }

        void finished() throws IOException {
        }

        void closed() throws IOException {
        }

        void error(IOException pException) throws IOException {
            throw pException;
        }
    }
}

