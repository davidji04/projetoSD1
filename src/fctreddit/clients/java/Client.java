package fctreddit.clients.java;

import com.sun.research.ws.wadl.Response;

public abstract class Client {

    protected static final int READ_TIMEOUT = 5000;
    protected static final int CONNECT_TIMEOUT = 5000;

    protected static final int MAX_RETRIES = 10;
    protected static final int RETRY_SLEEP = 5000;
}
