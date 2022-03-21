#import <Cordova/CDV.h>
#import <CoreNFC/CoreNFC.h>
#import <LeanConnectMobile/LeanConnectMobile.h>

@interface LeanConnectPlugin : CDVPlugin {}

- (void)isConnected:(CDVInvokedUrlCommand*)command;
- (void)connect:(CDVInvokedUrlCommand*)command;
- (void)disconnect:(CDVInvokedUrlCommand*)command;
- (void)getLogicalReaders:(CDVInvokedUrlCommand*)command;
- (void)getTag:(CDVInvokedUrlCommand*)command;
- (void)readTag:(CDVInvokedUrlCommand*)command;
- (void)setOnCommandResponseListener:(CDVInvokedUrlCommand*)command;
- (void)setOnConnectionListener:(CDVInvokedUrlCommand*)command;
- (void)enableDisableNdef:(CDVInvokedUrlCommand*)command;

@end