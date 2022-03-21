/********* LeanConnectPlugin.m Cordova Plugin Implementation *******/

#import "LeanConnectPlugin.h"

@implementation LeanConnectPlugin

- (void)init {
    setOnConnectionListener()
}

- (void)isConnected:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    BOOL res = isConnected();

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:res];
        [pluginResult setKeepCallbackAsBool:res];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)connect:(CDVInvokedUrlCommand*)command
{
    
}

@end
