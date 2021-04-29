package info.androidabcd.plugins.leanconnect;

import org.apache.cordova.CordovaPlugin;

import androidx.annotation.NonNull;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import hsc.com.leanConnectInterfaceLib.LeanConnectInterface;
import hsc.com.leanconnectlibforservices.LeanConnectMobile;

/**
 * This class echoes a string called from JavaScript.
 */
public class LeanConnectPlugin extends CordovaPlugin {

    private static final String INIT = "init";
    private static final String IS_CONNECTED = "isConnected";
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";
    private static final String GET_TAG = "getTag";
    private static final String READ_TAG = "readTag";
    private static final String HELLO = "hello";
    private static final String GET_LOGICAL_READERS = "getLogicalReaders";
    private static final String ENABLE_NDEF = "enableNdef";
    private static final String DISABLE_NDEF = "disableNdef";
    private static final String SWITCH_NDEF = "switchNdef";

    private LeanConnectInterface leanConnectInterface;

    Dictionary<String, Map<String, String>> uuidInfos = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(INIT)) {
            this.init(callbackContext);
            return true;
        } else if (action.equals(CONNECT)) {
            this.connect(callbackContext);
            return true;
        } else if (action.equals(DISCONNECT)) {
            this.disconnect(callbackContext);
            return true;
        } else if (action.equals(IS_CONNECTED)) {
            this.isConnected(callbackContext);
            return true;
        } else if (action.equals(GET_TAG)) {
            this.getTag(args, callbackContext);
            return true;
        } else if (action.equals(READ_TAG)) {
            this.readTag(args, callbackContext);
            return true;
        } else if (action.equals(HELLO)) {
            this.hello(callbackContext);
            return true;
        } else if (action.equals(GET_LOGICAL_READERS)) {
            this.getLogicalReaders(callbackContext);
            return true;
        } else if (action.equals(ENABLE_NDEF)) {
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_ENABLE);
            this.enableDisableNdef(args, callbackContext);
            return true;
        } else if (action.equals(DISABLE_NDEF)) {
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_DISABLE);
            this.enableDisableNdef(args, callbackContext);
            return true;
        } else if (action.equals(SWITCH_NDEF)) {
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_SWITCH);
            this.enableDisableNdef(args, callbackContext);
            return true;
        }

        return false;
    }

    private void init(final CallbackContext callbackContext) {
        try {
            Context context = this.cordova.getActivity().getApplicationContext();
            this.leanConnectInterface = new LeanConnectMobile(context);

            this.leanConnectInterface.setOnConnectionListener(new LeanConnectInterface.OnConnectionListener() {
                @Override
                public void onConnectionCompleted() {
                    //callbackContext.success();
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "");
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                }
        
                @Override
                public void onDisconnectionCompleted() {
                    //callbackContext.success();
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "");
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                }
        
                @Override
                public void onInitialized() {
                    //callbackContext.success();
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "");
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                }
            });

            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }

        this.leanConnectInterface.setOnCommandResponseListener(new LeanConnectInterface.OnCommandResponseListener() {
            @Override
            public void onGetLogicalReadersResponse(String[] logicalReaders, String errorMsg) {
                try {
                    JSONArray readers = new JSONArray();
                    if (logicalReaders != null) {
                        for (int i = 0; i < logicalReaders.length; i++) {
                            readers.put(logicalReaders[i]);
                        }
                    }
    
                    String response = new JSONObject()
                                    .put("logicalReaders", readers)
                                    .put("errorMsg", errorMsg)
                                    .toString();
                    
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, response);
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
    
            @Override
            public void onGetTagResponse(String uid, String tagType, String[] mediaList, int error) {
                try {
                    JSONArray media = new JSONArray();
                    if (mediaList != null) {
                        for (int i = 0; i < mediaList.length; i++) {
                            media.put(mediaList[i]);
                        }
                    }
                    
                    String response = new JSONObject()
                                    .put("uid", uid)
                                    .put("tagType", tagType)
                                    .put("mediaList", media)
                                    .put("error", error)
                                    .toString();
                    
                    putUuidInfo(uid, tagType);
                    
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, response);
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
            
            @Override
            public void onReadTagResponse(String uid, String xmlReport, int error) {
                try {
                    String response = new JSONObject()
                                    .put("uid", uid)
                                    .put("xmlReport", xmlReport)
                                    .put("error", error)
                                    .toString();
                    
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, response);
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
    
            @Override
            public void onEnableDisableNDefResponse(String uid, int action, int prevStatus, int newStatus, int error) {
                try {
                    String response = new JSONObject()
                                    .put("uid", uid)
                                    .put("action", action)
                                    .put("prevStatus", prevStatus)
                                    .put("newStatus", newStatus)
                                    .put("error", error)
                                    .toString();
                    
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, response);
                    pluginResult.setKeepCallback(false);
                    callbackContext.sendPluginResult(pluginResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
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

    private void connect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.connect();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void disconnect(CallbackContext callbackContext) {
        try {
            leanConnectInterface.disconnect();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void getTag(JSONArray args, CallbackContext callbackContext) {
        try {
            String logicalReader = args.getString(0);
            String domain = args.getString(1);
            String commandCycle = args.getString(2);
            String uidType = args.getString(3);

            commandCycle = (commandCycle.equals("null")) ? null : commandCycle;
            uidType = (uidType.equals("null")) ? null : uidType;
            
            leanConnectInterface.getTag(logicalReader, domain, commandCycle, uidType);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void readTag(JSONArray args, CallbackContext callbackContext) {
        try {
            String logicalReader = args.getString(0);
            String domain = args.getString(1);
            String commandCycle = args.getString(2);
            String uidType = args.getString(3);
            String uuid = args.getString(4);
            //String tagType = getUuidInfoTagType(uuid);
            String xmlReport = args.getString(5);
            
            commandCycle = (commandCycle.equals("null")) ? null : commandCycle;
            uidType = (uidType.equals("null")) ? null : uidType;
            uuid = (uuid.equals("null")) ? null : uuid;
            
            leanConnectInterface.readTag(logicalReader, domain, commandCycle, uidType, uuid, xmlReport);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
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
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void enableDisableNdef(JSONArray args, CallbackContext callbackContext) {
        try {
            String logicalReader = args.getString(0);
            String domain = args.getString(1);
            String uuid = args.getString(2);
            String tagtype = getUuidInfoTagType(uuid);
            int action = args.getInt(3);

            leanConnectInterface.enableDisableNdef(logicalReader, domain, null, null, uuid, action);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    protected boolean putUuidInfo(@NonNull String uuid, @NonNull String tagtype) {
        if (uuidInfos == null) uuidInfos = new Hashtable<String, Map<String, String>>();
        if (uuid.isEmpty()) return false;

        Map<String, String> infos = new Hashtable<String, String>();
        infos.put("tagtype", tagtype);
        uuidInfos.put(uuid, infos);
        return true;
    }

    protected Map<String, String> getUuidInfo(@NonNull String uuid) {
        if (uuid.isEmpty()) return null;

        Map<String, String> infos = uuidInfos.get(uuid);
        return (infos == null ? new Hashtable<String, String>() : infos);
    }

    protected String getUuidInfoTagType(@NonNull String uuid) {
        Map<String, String> infos = getUuidInfo(uuid);
        return (infos == null ? "" : infos.get("tagtype"));
    }
}
