package info.androidabcd.plugins.leanconnect;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

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

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Context context = this.cordova.getActivity().getApplicationContext();
        boolean result = false;

        if (action.equals(CONNECT)) {
            this.leanConnectInterface = new LeanConnectMobile(context);
            this.addOnConnectionListener(callbackContext);
            this.addOnCommandResponseListener(callbackContext);
            this.connect(callbackContext);
            result = true;
        } else if (action.equals(DISCONNECT)) {
            this.disconnect(callbackContext);
            result = true;
        } else if (action.equals(IS_CONNECTED)) {
            this.isConnected(callbackContext);
            result = true;
        } else if (action.equals(GET_TAG)) {
            this.getTag(args, callbackContext);
            result = true;
        } else if (action.equals(HELLO)) {
            this.hello(callbackContext);
            result = true;
        } else if (action.equals(GET_LOGICAL_READERS)) {
            this.getLogicalReaders(callbackContext);
            result = true;
        }

        return result;
    }

    private void isConnected(CallbackContext callbackContext) {
        try {
            boolean res = leanConnectInterface.isConnected();
            PluginResult result = new PluginResult(PluginResult.Status.OK, res);
            result.setKeepCallback(false);
            callbackContext.sendPluginResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void connect(final CallbackContext callbackContext) {
        try {
            leanConnectInterface.connect();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void disconnect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.disconnect();
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

    private void getLogicalReaders(final CallbackContext callbackContext) {
        try {
            leanConnectInterface.getLogicalReaders();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void addOnConnectionListener(final CallbackContext callbackContext) {
        this.leanConnectInterface.setOnConnectionListener(new LeanConnectInterface.OnConnectionListener() {
            @Override
            public void onConnectionCompleted() {
                callbackContext.success();
            }

            @Override
            public void onDisconnectionCompleted() {
                callbackContext.success();
            }

            @Override
            public void onInitialized() {
                callbackContext.success();
            }
        });
    }

    private void addOnCommandResponseListener(final CallbackContext callbackContext) {
        this.leanConnectInterface.setOnCommandResponseListener(new LeanConnectInterface.OnCommandResponseListener() {
            @Override
            public void onGetLogicalReadersResponse(String[] strings, String s) {
                try {
                    JSONArray readers = new JSONArray();
                    for (int i = 0; i < strings.length; i++) {
                        readers.put(strings[i]);
                    }

                    String jsonString = new JSONObject()
                                    .put("logicalReaders", readers)
                                    .put("errorMsg", s)
                                    .toString();
                    callbackContext.success(jsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }

            @Override
            public void onGetTagResponse(String s, String s1, int i) {
                try {
                    String jsonString = new JSONObject()
                                    .put("uid", s)
                                    .put("tagType", s1)
                                    .put("error", i)
                                    .toString();
                    callbackContext.success(jsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }
}
