package info.androidabcd.plugins.leanconnect;

import java.util.Arrays;
import java.util.List;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import hsc.com.leanConnectInterfaceLib.LeanConnectInterface;
import hsc.com.leanconnectlibforservices.LeanConnectMobile;

/**
 * This class echoes a string called from JavaScript.
 */
public class LeanConnectPlugin extends CordovaPlugin {

    private static final String IS_CONNECTED = "isConnected";
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";
    private static final String GET_TAG = "getTag";
    private static final String HELLO = "hello";
    private static final String GET_LOGICAL_READERS = "getLogicalReaders";

    private LeanConnectInterface leanConnectInterface;
    private CallbackContext callbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        Context context = this.cordova.getActivity().getApplicationContext();
        this.leanConnectInterface = new LeanConnectMobile(context);
        this.addOnCommandResponseListener();

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

    private void addOnCommandResponseListener() {
        this.leanConnectInterface.setOnCommandResponseListener(new LeanConnectInterface.OnCommandResponseListener() {
            @Override
            public void onGetLogicalReadersResponse(String[] strings, String s) {
                String[] readers = strings;

                if (readers == null) {
                    readers = new String[0];
                }
                
                try {
                    String jsonString = new JSONObject()
                                    .put("logicalReaders", new JSONArray(Arrays.asList(readers)))
                                    .put("errorMsg", s)
                                    .toString();
                    PluginResult result = new PluginResult(PluginResult.Status.OK, jsonString);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }

            @Override
            public void onGetTagResponse(String s, String s1, int i) {

            }
        });
    }
}
