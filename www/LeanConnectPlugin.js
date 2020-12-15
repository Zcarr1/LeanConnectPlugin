var exec = require('cordova/exec');

exports.isConnected = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'isConnected', []);
};

exports.connect = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'connect', []);
};

exports.disconnect = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'disconnect', []);
};

exports.getTag = function (arg0, success, error) {
    exec(success, error, 'LeanConnectPlugin', 'getTag', [arg0]);
};

exports.hello = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'hello', []);
};

exports.getLogicalReaders = function (success, error) {
    exec(success, error, 'LeanConnectPlugin', 'getLogicalReaders', []);
};