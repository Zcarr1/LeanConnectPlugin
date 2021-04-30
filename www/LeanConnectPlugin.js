var exec = require('cordova/exec');

exports.init = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'init', []);
};

exports.isConnected = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'isConnected', []);
};

exports.connect = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'connect', []);
};

exports.disconnect = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'disconnect', []);
};

exports.getTag = function (arg0, arg1, arg2, arg3, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'getTag', [arg0, arg1, arg2, arg3]);
};

exports.readTag = function (arg0, arg1, arg2, arg3, arg4, arg5, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'readTag', [arg0, arg1, arg2, arg3, arg4, arg5]);
};

exports.hello = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'hello', []);
};

exports.getLogicalReaders = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'getLogicalReaders', []);
};

exports.enableNdef = function (arg0, arg1, arg2, arg3, arg4, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'enableNdef', [arg0, arg1, arg2, arg3, arg4]);
};

exports.disableNdef = function (arg0, arg1, arg2, arg3, arg4, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'disableNdef', [arg0, arg1, arg2, arg3, arg4]);
};

exports.switchNdef = function (arg0, arg1, arg2, arg3, arg4, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'switchNdef', [arg0, arg1, arg2, arg3, arg4]);
};