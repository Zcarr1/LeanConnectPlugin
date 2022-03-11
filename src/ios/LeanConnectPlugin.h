#import <Cordova/CDV.h>

@interface LeanConnectPlugin : CDVPlugin {}

- (void)pluginInitialize;
- (void)isConnected:(CDVInvokedUrlCommand*)command;

@end