/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.org.gdg.devfest.iosched.io;

import android.util.Log;
import ua.org.gdg.devfest.iosched.Config;
import ua.org.gdg.devfest.iosched.io.model.Room;
import ua.org.gdg.devfest.iosched.io.model.Rooms;
import ua.org.gdg.devfest.iosched.provider.ScheduleContract;
import ua.org.gdg.devfest.iosched.util.Lists;

import android.content.ContentProviderOperation;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static ua.org.gdg.devfest.iosched.util.LogUtils.makeLogTag;

public class RoomsHandler extends JSONHandler {

    private static final String TAG = makeLogTag(RoomsHandler.class);

    public RoomsHandler(Context context) {
        super(context);
    }

    public ArrayList<ContentProviderOperation> parse(String json) throws IOException {
        final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();
        Rooms roomsJson = new Rooms();
        try {
            roomsJson.fromJSON(new JSONObject(json));
            for(Room room: roomsJson.getRooms()) {
                parseRoom(room, batch);
            }
        } catch(JSONException e) {
            Log.e(Config.LOG_TAG, "Error parsing rooms", e);
        }
        return batch;
    }

    private static void parseRoom(Room room, ArrayList<ContentProviderOperation> batch) {
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(ScheduleContract.addCallerIsSyncAdapterParameter(
                        ScheduleContract.Rooms.CONTENT_URI));
        builder.withValue(ScheduleContract.Rooms.ROOM_ID, room.id);
        builder.withValue(ScheduleContract.Rooms.ROOM_NAME, room.name);
        builder.withValue(ScheduleContract.Rooms.ROOM_FLOOR, room.floor);
        batch.add(builder.build());
    }
}
