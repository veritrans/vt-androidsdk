/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.co.veritrans.sdk.example.gcmutils;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.utilities.NotificationUtils;

public class VeritransGcmListenerService extends GcmListenerService {

    private static final String TAG = "VeritransGcmListenerService";


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //String message = data.getString("message");
        //Logger.d(TAG,"data:"+data);
        Logger.d(TAG, "From: " + from);
        //Logger.d(TAG, "Message: " + message);

     /*   if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }*/

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        NotificationUtils.sendNotification(getApplicationContext(),data);
        // [END_EXCLUDE]
    }
    // [END receive_message]


}
