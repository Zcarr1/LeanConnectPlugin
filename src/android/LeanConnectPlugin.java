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
    private static final String ENABLE_DISABLE_NDEF = "enableDisableNdef";

    private LeanConnectInterface leanConnectInterface;
    CallbackContext myCallbackContext;

    // Dictionary used to store info (Uuid, TagType, Media) about tags read.
    // The dictionary key is uuid.
    // The map contains 1 pairs: [tagtype:value]
    // This way the user has not to input tag info saved by commands.
    Dictionary<String, Map<String, String>> uuidInfos = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(INIT)) {
            this.myCallbackContext = callbackContext;
            this.init();
            return true;
        } else if (action.equals(CONNECT)) {
            this.myCallbackContext = callbackContext;
            this.connect();
            return true;
        } else if (action.equals(DISCONNECT)) {
            this.myCallbackContext = callbackContext;
            this.disconnect();
            return true;
        } else if (action.equals(IS_CONNECTED)) {
            this.myCallbackContext = callbackContext;
            this.isConnected();
            return true;
        } else if (action.equals(GET_TAG)) {
            this.myCallbackContext = callbackContext;
            this.getTag(args);
            return true;
        } else if (action.equals(READ_TAG)) {
            this.myCallbackContext = callbackContext;
            this.readTag(args);
            return true;
        } else if (action.equals(HELLO)) {
            this.myCallbackContext = callbackContext;
            this.hello();
            return true;
        } else if (action.equals(GET_LOGICAL_READERS)) {
            this.myCallbackContext = callbackContext;
            this.getLogicalReaders();
            return true;
        } else if (action.equals(ENABLE_NDEF)) {
            this.myCallbackContext = callbackContext;
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_ENABLE);
            this.enableDisableNdef(args);
            return true;
        } else if (action.equals(DISABLE_NDEF)) {
            this.myCallbackContext = callbackContext;
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_DISABLE);
            this.enableDisableNdef(args);
            return true;
        } else if (action.equals(SWITCH_NDEF)) {
            this.myCallbackContext = callbackContext;
            args.put(LeanConnectInterface.COMMAND_ENABLEDISABLENDEF_ACTION_SWITCH);
            this.enableDisableNdef(args);
            return true;
        } else if (action.equals(ENABLE_DISABLE_NDEF)) {
            this.myCallbackContext = callbackContext;
            this.enableDisableNdef(args);
            return true;
        }

        return false;
    }

    private void init() {
        try {
            Context context = this.cordova.getActivity().getApplicationContext();
            this.leanConnectInterface = new LeanConnectMobile(context);
                    
            this.leanConnectInterface.setOnConnectionListener(new LeanConnectInterface.OnConnectionListener() {
                @Override
                public void onConnectionCompleted() {
                    myCallbackContext.success("Connected");
                }
        
                @Override
                public void onDisconnectionCompleted() {
                    myCallbackContext.success("Disconnected");
                }
        
                @Override
                public void onInitialized() {
                    //myCallbackContext.success("Initialized");
                }
            });
            
            this.leanConnectInterface.setOnCommandResponseListener(new LeanConnectInterface.OnCommandResponseListener() {
                // callback getLogicalReaders
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
                        
                        myCallbackContext.success(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myCallbackContext.error(e.getMessage());
                    }
                }
                
                // callback getTag
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
                        myCallbackContext.success(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myCallbackContext.error(e.getMessage());
                    }
                }
                
                // callback readTag
                @Override
                public void onReadTagResponse(String uid, String xmlReport, int error) {
                    try {
                        String response = new JSONObject()
                                        .put("uid", uid)
                                        .put("xmlReport", xmlReport)
                                        .put("error", error)
                                        .toString();

                        myCallbackContext.success(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myCallbackContext.error(e.getMessage());
                    }
                }
                
                // callback enableDisableNDef
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
                        
                        myCallbackContext.success(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myCallbackContext.error(e.getMessage());
                    }
                }
            });

            myCallbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // services: controlla se è bound
    private void isConnected() {
        try {
            boolean res = leanConnectInterface.isConnected();
            PluginResult result = new PluginResult(PluginResult.Status.OK, res);
            result.setKeepCallback(false);
            myCallbackContext.sendPluginResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // services: fa la bind()
    private void connect() {
        try {
            leanConnectInterface.connect();
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // services: fa la unbind()
    private void disconnect() {
        try {
            leanConnectInterface.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // Used to get infos and recognize a tag
    private void getTag(JSONArray args) {
        try {
            String logicalReader = args.getString(0); // logical reader name
            String domain = args.getString(1);        // domain name
            String commandCycle = args.getString(2);  // command cycle in order to wait for tags, e.g "s10". Optional
            String uidType = args.getString(3);       // type of uid requested. Optional

            commandCycle = (commandCycle.equals("null") || commandCycle.isEmpty()) ? null : commandCycle;
            uidType = (uidType.equals("null") || uidType.isEmpty()) ? null : uidType;
            
            leanConnectInterface.getTag(logicalReader, domain, commandCycle, uidType);
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // Read data from tag.
    private void readTag(JSONArray args) {
        try {
            String logicalReader = args.getString(0);  //logical reader name
            String domain = args.getString(1);         //domain name
            String commandCycle = args.getString(2);   //command cycle in order to wait for tags, e.g "s10". Optional
            String uidType = args.getString(3);        //type of uid requested. Optional
            String uuid = args.getString(4);           //tag uid. Optional. If missing, no check will be executed.
            String xmlReport = args.getString(5);      //xml report with empty xml tags. it will be returned with values in 'onReadTagResponse()'.
            
            commandCycle = (commandCycle.equals("null") || commandCycle.isEmpty()) ? null : commandCycle;
            uidType = (uidType.equals("null") || uidType.isEmpty()) ? null : uidType;
            uuid = (uuid.equals("null") || uuid.isEmpty()) ? null : uuid;
            
            leanConnectInterface.readTag(logicalReader, domain, commandCycle, uidType, uuid, xmlReport);
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    private void hello() {
        try {
            leanConnectInterface.hello();
            myCallbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }

    // Get the different logical readers supported by/connected to this lean connect
    // return the list of logical readers or null if the information is not yet ready.
    private void getLogicalReaders() {
        try {
            leanConnectInterface.getLogicalReaders();
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
        }
    }
    
    // Enable or disable a tag.
    private void enableDisableNdef(JSONArray args) {
        try {
            String logicalReader = args.getString(0);   // logical reader name
            String domain = args.getString(1);          // domain name
            String commandCycle = args.getString(2);    // command cycle in order to wait for tags, e.g "s10". Optional
            String uidType = args.getString(3);         // type of uid requested. Optional
            String uuid = args.getString(4);            // tag uid. Optional. If missing, no check will be executed.
            int action = args.getInt(5);                // actions permitted: enable, disable, switch. Use defined constants 'COMMAND_ENABLEDISABLENDEF_ACTION_*'.

            commandCycle = (commandCycle.equals("null") || commandCycle.isEmpty()) ? null : commandCycle;
            uidType = (uidType.equals("null") || uidType.isEmpty()) ? null : uidType;
            uuid = (uuid.equals("null") || uuid.isEmpty()) ? null : uuid;

            leanConnectInterface.enableDisableNdef(logicalReader, domain, commandCycle, uidType, uuid, action);
        } catch (Exception e) {
            e.printStackTrace();
            myCallbackContext.error(e.getMessage());
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