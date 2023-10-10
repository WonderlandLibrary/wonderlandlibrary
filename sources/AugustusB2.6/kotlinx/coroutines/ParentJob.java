// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import java.util.concurrent.CancellationException;

public interface ParentJob extends Job
{
    CancellationException getChildJobCancellationCause();
}
