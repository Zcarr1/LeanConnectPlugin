import CoreNFC
import LeanConnectMobile

@objc(LeanConnectPlugin) class LeanConnectPlugin : CDVPlugin{
    // MARK: Properties
    var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
    var sharedMobile = LeanConnectMobile.sharedInstance

    func isConnected(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
        
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
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        do {
            try sharedMobile.connect()
        } catch {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Something wrong")
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func disconnect(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        do {
            try sharedMobile.disconnect()
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }

    func getTag(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        let logicalReader = (command.arguments[0] as? NSObject)?.value(forKey: "arg0") as? String
        let domain = (command.arguments[0] as? NSObject)?.value(forKey: "arg1") as? String
        let commandCycle = (command.arguments[0] as? NSObject)?.value(forKey: "arg2") as? String
        let uidType = (command.arguments[0] as? NSObject)?.value(forKey: "arg3") as? String

        if (logicalReader == "null" || logicalReader.isEmpty()) {
            logicalReader = nil
        }
        if (domain == "null" || domain.isEmpty()) {
            domain = nil
        }
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

    func getTag(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        let logicalReader = (command.arguments[0] as? NSObject)?.value(forKey: "arg0") as? String
        let domain = (command.arguments[0] as? NSObject)?.value(forKey: "arg1") as? String
        let commandCycle = (command.arguments[0] as? NSObject)?.value(forKey: "arg2") as? String
        let uidType = (command.arguments[0] as? NSObject)?.value(forKey: "arg3") as? String
        let uuid = (command.arguments[0] as? NSObject)?.value(forKey: "arg4") as? String
        let xmlReport = (command.arguments[0] as? NSObject)?.value(forKey: "arg5") as? String

        if (logicalReader == "null" || logicalReader.isEmpty()) {
            logicalReader = nil
        }
        if (domain == "null" || domain.isEmpty()) {
            domain = nil
        }
        if (commandCycle == "null" || commandCycle.isEmpty()) {
            commandCycle = nil
        }
        if (uidType == "null" || uidType.isEmpty()) {
            uidType = nil
        }
        if (uuid == "null" || uuid.isEmpty()) {
            uuid = nil
        }
        if (xmlReport == "null" || xmlReport.isEmpty()) {
            xmlReport = nil
        }
        
        do {
            try sharedMobile.getTag(logicalReader, domain, commandCycle, uidType)
        } catch {
            print("Unexpected error: \(error).")
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error)
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
        }
    }
}