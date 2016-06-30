package com.jalarrea.retry;

import java.util.Timer;
import java.util.TimerTask;

public class Retry {
	
	private boolean _retrying;
	private int _retryAttempts;
	private long _retryDelay;
	private long _retryDelayMax;
	private boolean skipReconnect;
	private double _randomizationFactor;
	private Options opts;
	private BackoffTime backoff;
	private long _timeout;
	public ReadyState readyState;
	
	final private Timer backgroundTimer = new Timer("backgroundTimer");
	
	enum ReadyState {
        CLOSED, OPEN
    }
	
	
	public Retry(Options opts) {
        if (opts == null) {
            opts = new Options();
        }

        this.set_retrying(opts.retrying);
        this.set_retryAttempts(opts.retryAttempts != 0 ? opts.retryAttempts : Integer.MAX_VALUE);
        this.set_retryDelay(opts.retryDelay != 0 ? opts.retryDelay : 1000);
        this.set_retryDelayMax(opts.retryDelayMax != 0 ? opts.retryDelayMax : 5000);
        this.set_randomizationFactor(opts.randomizationFactor != 0.0 ? opts.randomizationFactor : 0.5);
        this.backoff = new BackoffTime()
                .setMin(this.get_retryDelay())
                .setMax(this.get_retryDelayMax())
                .setJitter(this.get_randomizationFactor());
        this.set_timeout(opts.timeout);
        this.readyState = ReadyState.CLOSED;
    }
	
	private void retrying() {
        if (this.is_retrying()&&this.getSkipReconnect()) return;  //Skip attemp
        if (this.backoff.getAttempts() >= this._retryAttempts) {
            System.out.println("retry failed");
            this.backoff.reset();
          //  this.emitAll(EVENT_RECONNECT_FAILED);
            this.set_retrying(false);
        } else {
            long delay = this.backoff.duration();
            System.out.println(String.format("will wait %dms before retry attempt", delay));

            this.set_retrying(true);
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                	if(getSkipReconnect()){
                		return;
                	}	
                	else{
                		readyState = ReadyState.OPEN;
                        if(containerFunctions()){
                        	set_retrying(true);
                        	backoff.reset();
                        	readyState = ReadyState.CLOSED;
                        }else{
                        	setSkipReconnect(false);
                        }
                	}
                	
                }
            }, delay);
        }
    }
	
	//This is the important function.
	public boolean containerFunctions(){
		// TODO: Contains the code to execute
		return true;
	}
	
	
	public boolean getSkipReconnect() {
		return skipReconnect;
	}

	public void setSkipReconnect(boolean skipReconnect) {
		this.skipReconnect = skipReconnect;
	}


	
	public long get_timeout() {
		return _timeout;
	}

	public void set_timeout(long _timeout) {
		this._timeout = _timeout;
	}

	
	public boolean is_retrying() {
		return _retrying;
	}

	public void set_retrying(boolean _retrying) {
		this._retrying = _retrying;
	}

	
    public int get_retryAttempts() {
		return _retryAttempts;
	}

	public void set_retryAttempts(int _retryAttempts) {
		this._retryAttempts = _retryAttempts;
	}

	public long get_retryDelay() {
		return _retryDelay;
	}

	public void set_retryDelay(long _retryDelay) {
	    if (this.backoff != null) {
	    	this.backoff.setMin(_retryDelay);
	    }
		this._retryDelay = _retryDelay;
	}

	public long get_retryDelayMax() {
		return _retryDelayMax;
	}

	public void set_retryDelayMax(long _retryDelayMax) {
		this._retryDelayMax = _retryDelayMax;
	}

	public double get_randomizationFactor() {
		return _randomizationFactor;
	}

	public void set_randomizationFactor(double _randomizationFactor) {
		this._randomizationFactor = _randomizationFactor;
	}

	public Options getOpts() {
		return opts;
	}

	public void setOpts(Options opts) {
		this.opts = opts;
	}

	public Timer getBackgroundTimer() {
		return backgroundTimer;
	}

}
