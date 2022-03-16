#import <Cordova/CDV.h>
#import <CoreNFC/CoreNFC.h>
#import <LeanConnectPlugin>

@interface LeanConnectPlugin : CDVPlugin {}

- (void)pluginInitialize;
- (void)isConnected:(CDVInvokedUrlCommand*)command;

@end