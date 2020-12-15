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
        implements LeanConnectInterface.OnCommandResponseListener, LeanConnectInterface.OnConnectionListener {

    private static final String IS_CONNECTED = "isConnected";
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";
    private static final String GET_TAG = "getTag";
    private static final String HELLO = "hello";
    private static final String GET_LOGICAL_READERS = "getLogicalReaders";

    private LeanConnectInterface leanConnectInterface;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.leanConnectInterface = new LeanConnectMobile(this);
        //this.leanConnectInterface.setOnConnectionListener(this);
        //this.leanConnectInterface.setOnCommandResponseListener(this);

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
            callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void connect(CallbackContext callbackContext) {
        boolean res = false;
        try {
            res = leanConnectInterface.connect();
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
        String arg0 = "";
        try {
            arg0 = args.getString(0);
            leanConnectInterface.getTag(arg0);
            callbackContext.success();
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
        String[] res;
        try {
            res = leanConnectInterface.getLogicalReaders();
            callbackContext.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private LeanConnectInterface.OnCommandResponseListener lCommandResponseListener = new LeanConnectInterface.OnCommandResponseListener() {
        @Override
        public void onGetLogicalReadersResponse(String[] strings, String s) {
            
        }

        @Override
        public void onGetTagResponse(String s, String s1, int i) {

        }
    };

    private LeanConnectInterface.OnConnectionListener lConnectionListener = new LeanConnectInterface.OnConnectionListener() {
        @Override
        public void onConnectionCompleted() {
            
        }

        @Override
        public void onDisconnectionCompleted() {

        }

        @Override
        public void onInitialized() {

        }
    };
}
