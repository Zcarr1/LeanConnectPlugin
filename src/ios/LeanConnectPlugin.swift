import CoreNFC
import LeanConnectMobileKit

@objc(LeanConnectPlugin) class LeanConnectPlugin : CDVPlugin{
    // MARK: Properties
    var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
    var sharedMobile = LeanConnectMobile.sharedInstance

    func pluginInitialize(_ command: CDVInvokedUrlCommand) {
        sharedMobile.setOnConnectionListener(LeanConnectMobileOnConnectionListener {
            func onConnectionCompleted() {
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Connected")
            }
	
            func onDisconnectionCompleted() {
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Disconnected")
            }
            
            func onInitialized() {

            }
        })

        sharedMobile.setOnCommandResponseListener(LeanConnectMobileOnCommandResponseListener {
            func onGetLogicalReadersResponse(logicalReaders:[String], errorMsg:String) {
                let jsResponse : [String:Any] = [String:Any]()
                jsResponse["logicalReaders"] = logicalReaders
                jsResponse["errorMsg"] = errorMsg

                let response = jsonToString(jsResponse)
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
            }

            func onGetTagResponse(uid:String, tagType:String, mediaList:[String], error:Int) {
                let jsResponse : [String:Any] = [String:Any]()
                jsResponse["uid"] = uid
                jsResponse["tagType"] = tagType
                jsResponse["mediaList"] = mediaList
                jsResponse["error"] = error

                let response = jsonToString(jsResponse)
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
            }
	
            func onReadTagResponse(uid:String, xmlReport:String, error:Int) {
                let jsResponse : [String:Any] = [String:Any]()
                jsResponse["uid"] = uid
                jsResponse["xmlReport"] = tagType
                jsResponse["error"] = mediaList

                let response = jsonToString(jsResponse) 
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
            }
            
            func onEnableDisableNDefResponse(uid:String, action:Int, prevStatus:Int, newStatus:Int, error:Int) {
                let jsResponse : [String:Any] = [String:Any]()
                jsResponse["uid"] = uid
                jsResponse["action"] = action
                jsResponse["prevStatus"] = mediaList
                jsResponse["newStatus"] = mediaList
                jsResponse["error"] = mediaList

                let response = jsonToString(jsResponse) 
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
            }
        })

        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
    }

    func hello(_ command: CDVInvokedUrlCommand) { 
        do {
            var res = try sharedMobile.hello()
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: res)
        } catch {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Something wrong")
        }

        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
    }

    func isConnected(_ command: CDVInvokedUrlCommand) {
        do {
            var res = try sharedMobile.isConnected()
            pluginResult.setKeepCallback(false);
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: res)
        } catch {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Something wrong")
        }

        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
    }

    func connect(_ command: CDVInvokedUrlCommand) {   
        do {
            try sharedMobile.connect()
        } catch {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Something wrong")
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func disconnect(_ command: CDVInvokedUrlCommand) {
        do {
            try sharedMobile.disconnect()
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    func getLogicalReaders(_ command: CDVInvokedUrlCommand) {
        do {
            try sharedMobile.getLogicalReaders()
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func getTag(_ command: CDVInvokedUrlCommand) {
        let logicalReader = (command.arguments[0] as? NSObject)?.value(forKey: "arg0") as? String
        let domain = (command.arguments[0] as? NSObject)?.value(forKey: "arg1") as? String
        let commandCycle = (command.arguments[0] as? NSObject)?.value(forKey: "arg2") as? String
        let uidType = (command.arguments[0] as? NSObject)?.value(forKey: "arg3") as? String

        if (commandCycle == "null" || commandCycle.isEmpty()) {
            commandCycle = nil
        }
        if (uidType == "null" || uidType.isEmpty()) {
            uidType = nil
        }

        do {
            try sharedMobile.getTag(logicalReader, domain, commandCycle, uidType)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func readTag(_ command: CDVInvokedUrlCommand) {
        let logicalReader = (command.arguments[0] as? NSObject)?.value(forKey: "arg0") as? String
        let domain = (command.arguments[0] as? NSObject)?.value(forKey: "arg1") as? String
        let commandCycle = (command.arguments[0] as? NSObject)?.value(forKey: "arg2") as? String
        let uidType = (command.arguments[0] as? NSObject)?.value(forKey: "arg3") as? String
        let uuid = (command.arguments[0] as? NSObject)?.value(forKey: "arg4") as? String
        let xmlReport = (command.arguments[0] as? NSObject)?.value(forKey: "arg5") as? String

        if commandCycle == "null" || commandCycle.isEmpty() {
            commandCycle = nil
        }
        if uidType == "null" || uidType.isEmpty() {
            uidType = nil
        }
        if uuid == "null" || uuid.isEmpty() {
            uuid = nil
        }
        if xmlReport == "null" || xmlReport.isEmpty() {
            xmlReport = nil
        }
        
        do {
            try sharedMobile.readTag(logicalReader, domain, commandCycle, uidType, uuid, xmlReport)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func enableDisableNdef(_ command: CDVInvokedUrlCommand) {
        let logicalReader = (command.arguments[0] as? NSObject)?.value(forKey: "arg0") as? String
        let domain = (command.arguments[0] as? NSObject)?.value(forKey: "arg1") as? String
        let commandCycle = (command.arguments[0] as? NSObject)?.value(forKey: "arg2") as? String
        let uidType = (command.arguments[0] as? NSObject)?.value(forKey: "arg3") as? String
        let uuid = (command.arguments[0] as? NSObject)?.value(forKey: "arg4") as? String
        let action = (command.arguments[0] as? NSObject)?.value(forKey: "arg5") as? Int

        if commandCycle == "null" || commandCycle.isEmpty() {
            commandCycle = nil
        }
        if uidType == "null" || uidType.isEmpty() {
            uidType = nil
        }
        if uuid == "null" || uuid.isEmpty() {
            uuid = nil
        }
        
        do {
            try sharedMobile.enableDisableNdef(logicalReader, domain, commandCycle, uidType, uuid, action)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func jsonToString(jsonObj: [String: Any]) -> String {
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonObj)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue) as! String
        return jsonString
    }
}