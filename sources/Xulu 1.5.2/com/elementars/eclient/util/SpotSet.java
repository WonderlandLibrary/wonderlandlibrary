// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.function.Predicate;
import com.elementars.eclient.module.render.LogoutSpots;
import java.util.HashSet;

public class SpotSet extends HashSet<LogoutSpots.LogoutPos>
{
    public Pair<Boolean, LogoutSpots.LogoutPos> removeIfReturn(final Predicate<? super LogoutSpots.LogoutPos> filter) {
        final Set<LogoutSpots.LogoutPos> oldlist = new HashSet<LogoutSpots.LogoutPos>(this);
        final boolean removed = this.removeIf(filter);
        if (removed) {
            LogoutSpots.LogoutPos logoutPos = null;
            for (final LogoutSpots.LogoutPos pos : oldlist) {
                if (!this.contains(pos)) {
                    logoutPos = pos;
                    break;
                }
            }
            return new Pair<Boolean, LogoutSpots.LogoutPos>(true, logoutPos);
        }
        return new Pair<Boolean, LogoutSpots.LogoutPos>(false, null);
    }
}
