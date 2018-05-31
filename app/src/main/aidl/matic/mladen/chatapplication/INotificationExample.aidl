// INotificationExample.aidl
package matic.mladen.chatapplication;

import matic.mladen.chatapplication.ICallbackExample;

// Declare any non-default types here with import statements

interface INotificationExample {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void setCallback(in ICallbackExample callback);
}
