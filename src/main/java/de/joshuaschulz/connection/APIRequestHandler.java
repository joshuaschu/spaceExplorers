package de.joshuaschulz.connection;


import java.util.Map;

public class APIRequestHandler implements Runnable {
    private APIRequest APIRequest;
    private AsyncAPICall asyncCall;

    public APIRequestHandler(String baseURl,AsyncAPICall asyncCall) throws Exception {
            this(new APIRequest(baseURl),asyncCall);
    }
    public APIRequestHandler(String baseURl, Map<String,String> params, AsyncAPICall asyncCall) throws Exception {
        this(new APIRequest(baseURl,params),asyncCall);
    }

    private APIRequestHandler(APIRequest serverRequest, AsyncAPICall asyncCall) {
        this.APIRequest = serverRequest;
        this.asyncCall = asyncCall;
    }

    @Override
    public void run() {
        this.asyncCall.onBefore();
        try {
            String result = this.APIRequest.performAPICall();
           // if (result.getException() == null) {
           //     this.asyncCall.onSuccess(result);
            //} else {
            //    this.asyncCall.onFailure(result.getException());
            //}
            this.asyncCall.onSuccess(result);
        } catch (Exception e) {
            this.asyncCall.onFailure(e);
        } finally {
            this.asyncCall.onAfter();
        }
    }
}
