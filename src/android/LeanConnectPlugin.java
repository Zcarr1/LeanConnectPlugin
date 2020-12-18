package info.androidabcd.plugins.leanconnect;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import hsc.com.leanConnectInterfaceLib.LeanConnectInterface;
import hsc.com.leanconnectlibforservices.LeanConnectMobile;

/**
 * This class echoes a string called from JavaScript.
 */
public class LeanConnectPlugin 
        extends CordovaPlugin 
        implements LeanConnectInterface.OnCommandResponseListener, LeanConnectInterface.OnConnectionListener{

    private static final String IS_CONNECTED = "isConnected";
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";
    private static final String GET_TAG = "getTag";
    private static final String HELLO = "hello";
    private static final String GET_LOGICAL_READERS = "getLogicalReaders";

    private LeanConnectInterface leanConnectInterface;
    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        Context context = this.cordova.getActivity().getApplicationContext();
        this.leanConnectInterface = new LeanConnectMobile(context);
        this.leanConnectInterface.setOnConnectionListener(this);
        this.leanConnectInterface.setOnCommandResponseListener(this);

        if (action.equals(IS_CONNECTED)) {
            this.isConnected(callbackContext);
            return true;
        } else if (action.equals(CONNECT)) {
            this.connect(callbackContext);
            return true;
        } else if (action.equals(DISCONNECT)) {
            this.disconnect(callbackContext);
            return true;
        } else if (action.equals(GET_TAG)) {
            this.getTag(args, callbackContext);
            return true;
        } else if (action.equals(HELLO)) {
            this.hello(callbackContext);
            return true;
        } else if (action.equals(GET_LOGICAL_READERS)) {
            this.getLogicalReaders(callbackContext);
            return true;
        }

        return false;
    }

    private void isConnected(CallbackContext callbackContext) {
        boolean res = false;
        try {
            res = leanConnectInterface.isConnected();
            String str = String.valueOf(res);
            callbackContext.success(str);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void connect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.connect();
            //callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void disconnect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.disconnect();
            //callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void getTag(JSONArray args, CallbackContext callbackContext) {
        String arg0 = "";
        try {
            arg0 = args.getString(0);
            leanConnectInterface.getTag(arg0);
            //callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("Tag name is undefined");
        }
    }

    private void hello(CallbackContext callbackContext) {
        try {
            leanConnectInterface.hello();
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void getLogicalReaders(CallbackContext callbackContext) {
        try {
            leanConnectInterface.getLogicalReaders();
            //callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    @Override
    public void onGetLogicalReadersResponse(String[] logicalReaders, String errorMsg) {
        String[] readers = (strings != null) ? strings : new String[0];

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run(){
                try {
                    String jsonString = new JSONObject()
                                    .put("logicalReaders", new JSONArray(logicalReaders))
                                    .put("errorMsg", errorMsg)
                                    .toString();
                    PluginResult result = new PluginResult(PluginResult.Status.OK, jsonString);
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    /*@Override
    public void onGetTagResponse(String s, String s1, int i) {
        try {
            String jsonString = new JSONObject()
                            .put("uid", s)
                            .put("tagType", s1)
                            .put("error", i)
                            .toString();
            callbackContext.success(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }    
    }*/
}
