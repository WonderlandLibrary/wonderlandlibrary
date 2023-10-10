// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import java.io.PrintStream;

class OutputChoice
{
    final OutputChoiceType outputChoiceType;
    final PrintStream targetPrintStream;
    
    OutputChoice(final OutputChoiceType outputChoiceType) {
        if (outputChoiceType == OutputChoiceType.FILE) {
            throw new IllegalArgumentException();
        }
        if ((this.outputChoiceType = outputChoiceType) == OutputChoiceType.CACHED_SYS_OUT) {
            this.targetPrintStream = System.out;
        }
        else if (outputChoiceType == OutputChoiceType.CACHED_SYS_ERR) {
            this.targetPrintStream = System.err;
        }
        else {
            this.targetPrintStream = null;
        }
    }
    
    OutputChoice(final PrintStream printStream) {
        this.outputChoiceType = OutputChoiceType.FILE;
        this.targetPrintStream = printStream;
    }
    
    PrintStream getTargetPrintStream() {
        switch (this.outputChoiceType) {
            case SYS_OUT: {
                return System.out;
            }
            case SYS_ERR: {
                return System.err;
            }
            case CACHED_SYS_ERR:
            case CACHED_SYS_OUT:
            case FILE: {
                return this.targetPrintStream;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    enum OutputChoiceType
    {
        SYS_OUT, 
        CACHED_SYS_OUT, 
        SYS_ERR, 
        CACHED_SYS_ERR, 
        FILE;
    }
}
