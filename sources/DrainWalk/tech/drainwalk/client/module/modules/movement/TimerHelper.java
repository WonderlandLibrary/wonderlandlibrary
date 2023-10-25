package tech.drainwalk.client.module.modules.movement;

import tech.drainwalk.utility.Helper;

public class TimerHelper implements Helper {
        private long ms = getCurrentMS();

        private static long getCurrentMS() {
            return System.currentTimeMillis();
        }

        public boolean hasReached(double milliseconds) {
            return ((getCurrentMS() - this.ms) > milliseconds);
        }

        public void reset() {
            this.ms = getCurrentMS();
        }

        public long getTime() {
            return getCurrentMS() - this.ms;
        }
    }
