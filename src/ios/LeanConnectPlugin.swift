import Foundation
import CoreNFC
import LeanConnectMobileKit

@objc(LeanConnectPlugin)class LeanConnectPlugin : CDVPlugin, LeanConnectMobileOnConnectionListener, LeanConnectMobileOnCommandResponseListener{
    var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
    var sharedMobile = LeanConnectMobile.sharedInstance
    var callbackId = ""
    
    func onConnectionCompleted() {
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Connected")
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    func onDisconnectionCompleted() {
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Disconnected")
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    func onInitialized() {
        
    }
    
    func onGetLogicalReadersResponse(logicalReaders: [String], errorMsg: String) {
        
    }
    
    func onGetTagResponse(uid: String, tagType: String, mediaList: [String], error: Int) {
        var jsResponse : [String:Any] = [String:Any]()
        jsResponse["uid"] = uid
        jsResponse["tagType"] = tagType
        jsResponse["mediaList"] = mediaList
        jsResponse["error"] = error

        let response = jsonToString(jsonObj: jsResponse)
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
        
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    func onReadTagResponse(uid: String, xmlReport: String, error: Int) {
        var jsResponse : [String:Any] = [String:Any]()
        jsResponse["uid"] = uid
        jsResponse["xmlReport"] = xmlReport
        jsResponse["error"] = error

        let response = jsonToString(jsonObj: jsResponse)
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
        
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    func onEnableDisableNDefResponse(uid: String, action: Int, prevStatus: Int, newStatus: Int, error: Int) {
        var jsResponse : [String:Any] = [String:Any]()
        jsResponse["uid"] = uid
        jsResponse["action"] = action
        jsResponse["prevStatus"] = prevStatus
        jsResponse["newStatus"] = newStatus
        jsResponse["error"] = error

        let response = jsonToString(jsonObj: jsResponse)
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
        
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    
    @objc(init:)
    func pluginInitialize(_ command: CDVInvokedUrlCommand) {
        sharedMobile = LeanConnectMobile.sharedInstance
        sharedMobile.setOnCommandResponseListener(self)
        sharedMobile.setOnConnectionListener(self)
    }
    
    /*
    func hello(_ command: CDVInvokedUrlCommand) { 
        do {
            var res = try sharedMobile.hello()
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: res)
        } catch {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Something wrong")
        }

        self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
    }
     */
    
    @objc(isConnected:)
    func isConnected(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let res = sharedMobile.isConnected()
        pluginResult?.setKeepCallbackAs(res)
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: res)
        self.commandDelegate!.send(pluginResult, callbackId: callbackId)
    }
    
    @objc(connect:)
    func connect(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        do {
            try sharedMobile.connect {success in
                guard success else {
                    return
                }
                return
            }
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    @objc(disconnect:)
    func disconnect(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        do {
            try sharedMobile.disconnect()
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    @objc(getLogicalReaders:)
    func getLogicalReaders(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        do {
            let logicalReaders = try sharedMobile.getLogicalReaders()
    
            var jsResponse : [String:Any] = [String:Any]()
            jsResponse["logicalReaders"] = logicalReaders
            jsResponse["errorMsg"] = ""
            
            let response = jsonToString(jsonObj: jsResponse)
            
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response)
            
            self.commandDelegate!.send(pluginResult, callbackId: callbackId)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    @objc(getTag:)
    func getTag(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let logicalReader = command.argument(at: 0) as! String? ?? ""
        let domain = command.argument(at: 1) as! String? ?? ""
        let commandCycle = command.argument(at: 2) as! String? ?? ""
        let uidType = command.argument(at: 3) as! String? ?? ""

        do {
            try sharedMobile.getTag(logicalReader: logicalReader, domain: domain, commandCycle: commandCycle, uidType)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    @objc(readTag:)
    func readTag(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let logicalReader = command.argument(at: 0) as! String? ?? ""
        let domain = command.argument(at: 1) as! String? ?? ""
        let commandCycle = command.argument(at: 2) as! String? ?? ""
        let uidType = command.argument(at: 3) as! String? ?? ""
        let uuid = command.argument(at: 4) as! String? ?? ""
        let xmlReport = command.argument(at: 5) as! String? ?? ""

        do {
            try sharedMobile.readTag(logicalReader: logicalReader, domain: domain, commandCycle: commandCycle, uidType: uidType, uuid: uuid, xmlReport: xmlReport)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
    
    @objc(enableDisableNdef:)
    func enableDisableNdef(_ command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let logicalReader = command.argument(at: 0) as! String? ?? ""
        let domain = command.argument(at: 1) as! String? ?? ""
        let commandCycle = command.argument(at: 2) as! String? ?? ""
        let uidType = command.argument(at: 3) as! String? ?? ""
        let uuid = command.argument(at: 4) as! String? ?? ""
        let action = command.argument(at: 5) as! Int
        
        do {
            try sharedMobile.enableDisableNdef(logicalReader: logicalReader, domain: domain, commandCycle: commandCycle, uidType, uuid, action)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func jsonToString(jsonObj: [String: Any]) -> String {
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonObj)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        return jsonString
    }
}