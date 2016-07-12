# Retry Algorithm

###Exponential backoff

This is a library with strategy to retry to send functions asynchronous with a random factor exponential to send a http message to server.

This library is based on `http://supercoderz.in/things-to-consider-when-building-connection-retry-and-automatic-failover/`


####How to use this library?
```java 
public class RetryChild extends Retry{
  	public RetryChild(com.jalarrea.retry.Options options){
  		super(options);
  	}
	@Override
	public boolean containerFunctions(){
		//TODO:Put your call of request get or post here
		//You can use setSkipReconnect(true); to retry success or setSkipReconnect(false); to fail
		return true;
	}
}
```

