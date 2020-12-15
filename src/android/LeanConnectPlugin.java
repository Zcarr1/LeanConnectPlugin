package info.androidabcd.plugins.leanconnect;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private LeanConnectInterface leanConnectInterface;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.leanConnectInterface = new LeanConnectMobile(this);
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
        }
        return false;
    }

    private void isConnected(CallbackContext callbackContext) {
        try {
            boolean res = leanConnectInterface.isConnected();
            callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void connect(CallbackContext callbackContext) {
        try {
            boolean res = leanConnectInterface.connect();
            callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void disconnect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.disconnect();
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void getTag(JSONArray args, CallbackContext callbackContext) {
        String var1 = "";
        try {
            var1 = args.getString(0);
            leanConnectInterface.getTag(var1);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("Tag is undefined");
        }
    }
}
