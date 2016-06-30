package com.jalarrea.retry;

public class Options {
        public boolean retrying = true;
        public int retryAttempts;
        public long retryDelay;
        public long retryDelayMax;
        public double randomizationFactor;

        /**
         * Connection timeout (ms). Set -1 to disable.
         */
        public long timeout = 20000;
}
