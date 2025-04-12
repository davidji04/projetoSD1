package fctreddit.clients.java;

import fctreddit.api.java.Result;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;

public abstract class Client {

    protected static final int READ_TIMEOUT = 5000;
    protected static final int CONNECT_TIMEOUT = 5000;

    protected static final int MAX_RETRIES = 10;
    protected static final int RETRY_SLEEP = 5000;

    protected Response executeOperationPost(Invocation.Builder req, Entity<?> e){
        for(int i = 0; i < MAX_RETRIES ; i++) {
            try {
                    return req.post(e);

            } catch( ProcessingException x ) {

                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e1) {
                    //Nothing to be done here.
                }
            }
            catch( Exception x ) {
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationGet(Invocation.Builder req){
        for(int i = 0; i < MAX_RETRIES ; i++) {
            try {
                return req.get();
            } catch( ProcessingException x ) {
                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e1) {
                    //Nothing to be done here.
                }
            }
            catch( Exception x ) {
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationPut(Invocation.Builder req, Entity<?> e){
        for(int i = 0; i < MAX_RETRIES ; i++) {
            try {
                return req.put(e);
            } catch( ProcessingException x ) {
               // Log.info(x.getMessage());
                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e1) {
                    //Nothing to be done here.
                }
            }
            catch( Exception x ) {
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationDelete(Invocation.Builder req){
        for(int i = 0; i < MAX_RETRIES ; i++) {
            try {
                return req.delete();
            } catch( ProcessingException x ) {
                //Log.info(x.getMessage());
                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e1) {
                    //Nothing to be done here.
                }
            }
            catch( Exception x ) {
                x.printStackTrace();
            }
        }
        return null;
    }

    public static Result.ErrorCode getErrorCodeFrom(int status) {
        return switch (status) {
            case 200, 209 -> Result.ErrorCode.OK;
            case 409 -> Result.ErrorCode.CONFLICT;
            case 403 -> Result.ErrorCode.FORBIDDEN;
            case 404 -> Result.ErrorCode.NOT_FOUND;
            case 400 -> Result.ErrorCode.BAD_REQUEST;
            case 500 -> Result.ErrorCode.INTERNAL_ERROR;
            case 501 -> Result.ErrorCode.NOT_IMPLEMENTED;
            default -> Result.ErrorCode.INTERNAL_ERROR;
        };
    }
}
