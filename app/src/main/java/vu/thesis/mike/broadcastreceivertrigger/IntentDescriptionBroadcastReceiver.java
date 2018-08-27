package vu.thesis.mike.broadcastreceivertrigger;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_DEBUG_LOG_RESOLUTION;
import static android.content.Intent.FLAG_EXCLUDE_STOPPED_PACKAGES;
import static android.content.Intent.FLAG_FROM_BACKGROUND;
import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_PREFIX_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;
import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;
import static android.content.Intent.FLAG_RECEIVER_NO_ABORT;
import static android.content.Intent.FLAG_RECEIVER_REGISTERED_ONLY;
import static android.content.Intent.FLAG_RECEIVER_REPLACE_PENDING;
import static android.content.Intent.FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS;

public class IntentDescriptionBroadcastReceiver extends BroadcastReceiver {
    // Receives broadcast from adb shell
    // Command: adb shell am broadcast -a vu.thesis.mike.broadcastreceivertrigger.IntentDescriptionBroadcastReceiver --es 'IntentDescription' '{\"Action\":\"vu.thesis.mike.broadcastreceivertrigger.IntentTrigger\"\,\"ExtraName\":\"data\"\,\"ExtraValue\":\"This\ is\ a\ test!\"}' -n "vu.thesis.mike.broadcastreceivertrigger/.IntentDescriptionBroadcastReceiver"

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final PendingResult pendingResult = goAsync();

        Thread thread = new Thread() {
            public void run() {
                // 1. Target (Action) collected from Intent description
                // 2. Data (Extra) collected from Intent description
                // 3. Construct Intent
                // 4. Broadcast constructed Intent

                String IntentDescription = intent.getStringExtra("IntentDescription");

                try {
                    JSONObject jObject = new JSONObject(IntentDescription);
                    Intent newIntent = new Intent();

                    // Set Intent action
                    if (jObject.has("setAction")) {
                        newIntent.setAction(jObject.getString("setAction"));
                    }

                    // Set Intent component for explicit broadcasts
                    if (jObject.has("setComponent")) {
                        JSONObject component = (JSONObject) jObject.get("setComponent");

                        if(component.getString("Package").length() != 0) {
                            newIntent.setComponent(new ComponentName(component.getString("Package"), component.getString("Class")));
                        }
                    }

                    if (jObject.has("setData")) {
                        Uri UriData = Uri.parse(jObject.getString("setData"));
                        newIntent.setData(UriData);
                    }

                    if (jObject.has("setType")) {
                        newIntent.setType(jObject.getString("setType"));
                    }

                    if (jObject.has("setDataAndType")) {
                        JSONObject component = (JSONObject) jObject.get("setDataAndType");

                        if(component.getString("Data").length() != 0) {
                            Uri UriData = Uri.parse(component.getString("Data"));

                            if(component.getString("Type").length() != 0) {
                                newIntent.setDataAndType(UriData, component.getString("Type"));
                            }
                        }
                    }

                    if (jObject.has("addCategory")) {
                        JSONArray jArrayObject = jObject.getJSONArray("addCategory");
                            for (int i = 0; i < jArrayObject.length(); i++) {
                                if(jArrayObject.getString(i).length() != 0) {
                                    newIntent.addCategory(jArrayObject.getString(i));
                                }
                            }
                    }

                    if (jObject.has("addFlags")) {
                        JSONArray jArrayObject = jObject.getJSONArray("addFlags");

                        for (int i = 0; i < jArrayObject.length(); i++) {
                            switch(jArrayObject.getString(i)) {
                                case "FLAG_RECEIVER_FOREGROUND":
                                    newIntent.addFlags(FLAG_RECEIVER_FOREGROUND);
                                case "FLAG_RECEIVER_NO_ABORT":
                                    newIntent.addFlags(FLAG_RECEIVER_NO_ABORT);
                                case "FLAG_RECEIVER_REGISTERED_ONLY":
                                    newIntent.addFlags(FLAG_RECEIVER_REGISTERED_ONLY);
                                case "FLAG_RECEIVER_REPLACE_PENDING":
                                    newIntent.addFlags(FLAG_RECEIVER_REPLACE_PENDING);
                                case "FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS":
                                    newIntent.addFlags(FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS);
                                case "FLAG_INCLUDE_STOPPED_PACKAGES":
                                    newIntent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
                                case "FLAG_EXCLUDE_STOPPED_PACKAGES":
                                    newIntent.addFlags(FLAG_EXCLUDE_STOPPED_PACKAGES);
                                case "FLAG_DEBUG_LOG_RESOLUTION":
                                    newIntent.addFlags(FLAG_DEBUG_LOG_RESOLUTION);
                                case "FLAG_FROM_BACKGROUND":
                                    newIntent.addFlags(FLAG_FROM_BACKGROUND);
                                case "FLAG_GRANT_PERSISTABLE_URI_PERMISSION":
                                    newIntent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                case "FLAG_GRANT_PREFIX_URI_PERMISSION":
                                    newIntent.addFlags(FLAG_GRANT_PREFIX_URI_PERMISSION);
                                case "FLAG_GRANT_READ_URI_PERMISSION":
                                    newIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                                case "FLAG_GRANT_WRITE_URI_PERMISSION":
                                    newIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            }
                        }
                    }

                    // Set Intent extras
                    if (jObject.has("putExtra")) {
                        JSONArray jArrayObject = jObject.getJSONArray("putExtra");
                        for (int i = 0; i < jArrayObject.length(); i++) {
                            JSONObject jChildObject = jArrayObject.getJSONObject(i);

                            if (jChildObject.has("Name")) {
                                String name = jChildObject.getString("Name");
                                if (jChildObject.has("Value")) {
                                    if (jChildObject.has("DataType")) {
                                        switch(jChildObject.getString("DataType")) {
                                            case "String":
                                                newIntent.putExtra(name, jChildObject.getString("Value"));
                                                break;
                                            case "String[]":
                                                JSONArray StringJArray = jChildObject.getJSONArray("Value");
                                                String[] StringArray = new String[StringJArray.length()];

                                                for (int j = 0; j < StringJArray.length(); ++j) {
                                                    StringArray[j] = StringJArray.optString(j);
                                                }

                                                newIntent.putExtra(name, StringArray);
                                                break;
                                            case "CharSequence":
                                                newIntent.putExtra(name, jChildObject.getString("Value"));
                                                break;
                                            case "CharSequence[]":
                                                JSONArray CharSequenceJArray = jChildObject.getJSONArray("Value");
                                                String[] CharSequenceArray = new String[CharSequenceJArray.length()];

                                                for (int j = 0; j < CharSequenceJArray.length(); ++j) {
                                                    CharSequenceArray[j] = CharSequenceJArray.optString(j);
                                                }

                                                newIntent.putExtra(name, CharSequenceArray);
                                                break;
                                            case "char":
                                                newIntent.putExtra(name, jChildObject.getString("Value"));
                                                break;
                                            case "char[]":
                                                JSONArray charJArray = jChildObject.getJSONArray("Value");
                                                char[] charArray = new char[charJArray.length()];

                                                for (int j = 0; j < charJArray.length(); ++j) {
                                                    charArray[j] = (char) charJArray.opt(j);
                                                }

                                                newIntent.putExtra(name, charArray);
                                                break;
                                            case "int":
                                                int valueInt = Integer.parseInt(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueInt);
                                                break;
                                            case "int[]":
                                                JSONArray intJArray = jChildObject.getJSONArray("Value");
                                                int[] intArray = new int[intJArray.length()];

                                                for (int j = 0; j < intJArray.length(); ++j) {
                                                    intArray[j] = intJArray.optInt(j);
                                                }

                                                newIntent.putExtra(name, intArray);
                                                break;
                                            case "long":
                                                long valueLong = Long.parseLong(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueLong);
                                                break;
                                            case "long[]":
                                                JSONArray longJArray = jChildObject.getJSONArray("Value");
                                                long[] longArray = new long[longJArray.length()];

                                                for (int j = 0; j < longJArray.length(); ++j) {
                                                    longArray[j] = longJArray.optLong(j);
                                                }

                                                newIntent.putExtra(name, longArray);
                                                break;
                                            case "short":
                                                short valueShort = Short.parseShort(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueShort);
                                                break;
                                            case "short[]":
                                                JSONArray shortJArray = jChildObject.getJSONArray("Value");
                                                short[] shortArray = new short[shortJArray.length()];

                                                for (int j = 0; j < shortJArray.length(); ++j) {
                                                    shortArray[j] = Short.parseShort(shortJArray.optString(j));
                                                }

                                                newIntent.putExtra(name, shortArray);
                                                break;
                                            case "float":
                                                float valueFloat = Float.parseFloat(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueFloat);
                                                break;
                                            case "float[]":
                                                JSONArray floatJArray = jChildObject.getJSONArray("Value");
                                                float[] floatArray = new float[floatJArray.length()];

                                                for (int j = 0; j < floatJArray.length(); ++j) {
                                                    floatArray[j] = Float.parseFloat(floatJArray.optString(j));
                                                }

                                                newIntent.putExtra(name, floatArray);
                                                break;
                                            case "double":
                                                double valueDouble = Double.parseDouble(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueDouble);
                                                break;
                                            case "double[]":
                                                JSONArray doubleJArray = jChildObject.getJSONArray("Value");
                                                double[] doubleArray = new double[doubleJArray.length()];

                                                for (int j = 0; j < doubleJArray.length(); ++j) {
                                                    doubleArray[j] = doubleJArray.optDouble(j);
                                                }

                                                newIntent.putExtra(name, doubleArray);
                                                break;
                                            case "boolean":
                                                boolean valueBool = Boolean.parseBoolean(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueBool);
                                                break;
                                            case "boolean[]":
                                                JSONArray boolJArray = jChildObject.getJSONArray("Value");
                                                boolean[] boolArray = new boolean[boolJArray.length()];

                                                for (int j = 0; j < boolJArray.length(); ++j) {
                                                    boolArray[j] = boolJArray.optBoolean(j);
                                                }

                                                newIntent.putExtra(name, boolArray);
                                                break;
                                            case "byte":
                                                byte valueByte = Byte.parseByte(jChildObject.getString("Value"));
                                                newIntent.putExtra(name, valueByte);
                                                break;
                                            case "byte[]":
                                                JSONArray byteJArray = jChildObject.getJSONArray("Value");
                                                byte[] byteArray = new byte[byteJArray.length()];

                                                for (int j = 0; j < byteJArray.length(); ++j) {
                                                    byteArray[j] = Byte.parseByte(byteJArray.optString(j));
                                                }

                                                newIntent.putExtra(name, byteArray);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (jObject.has("putCharSequenceArrayListExtra")) {
                        JSONArray jArrayObject = jObject.getJSONArray("putCharSequenceArrayListExtra");
                        for (int i = 0; i < jArrayObject.length(); i++) {
                            JSONObject jChildObject = jArrayObject.getJSONObject(i);

                            if (jChildObject.has("Name")) {
                                String name = jChildObject.getString("Name");
                                if (jChildObject.has("Value")) {
                                    JSONArray array = jChildObject.getJSONArray("Value");

                                    ArrayList<CharSequence> list = new ArrayList<CharSequence>();

                                    for (int j=0;j<array.length();j++){
                                        list.add(array.get(j).toString());
                                    }

                                    newIntent.putCharSequenceArrayListExtra(name, list);
                                }
                            }
                        }
                    }

                    if (jObject.has("putIntegerArrayListExtra")) {
                        JSONArray jArrayObject = jObject.getJSONArray("putIntegerArrayListExtra");
                        for (int i = 0; i < jArrayObject.length(); i++) {
                            JSONObject jChildObject = jArrayObject.getJSONObject(i);

                            if (jChildObject.has("Name")) {
                                String name = jChildObject.getString("Name");
                                if (jChildObject.has("Value")) {
                                    JSONArray array = jChildObject.getJSONArray("Value");

                                    ArrayList<Integer> list = new ArrayList<Integer>();

                                    for (int j=0;j<array.length();j++){
                                        list.add(Integer.parseInt(array.get(j).toString()));
                                    }

                                    newIntent.putIntegerArrayListExtra(name, list);
                                }
                            }
                        }
                    }

                    if (jObject.has("putStringArrayListExtra")) {
                        JSONArray jArrayObject = jObject.getJSONArray("putStringArrayListExtra");
                        for (int i = 0; i < jArrayObject.length(); i++) {
                            JSONObject jChildObject = jArrayObject.getJSONObject(i);

                            if (jChildObject.has("Name")) {
                                String name = jChildObject.getString("Name");
                                if (jChildObject.has("Value")) {
                                    JSONArray array = jChildObject.getJSONArray("Value");

                                    ArrayList<String> list = new ArrayList<String>();

                                    for (int j=0;j<array.length();j++){
                                        list.add(array.get(j).toString());
                                    }

                                    newIntent.putStringArrayListExtra(name, list);
                                }
                            }
                        }
                    }

                    if (jObject.has("permission")) {
                        if (jObject.getString("permission").length() == 0) {
                            context.sendBroadcast(newIntent);
                        }
                        context.sendBroadcast(newIntent, jObject.getString("permission"));
                    } else {
                        context.sendBroadcast(newIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Must call finish() so the BroadcastReceiver can be recycled.
                pendingResult.finish();
            }
        };

        thread.start();
    }
}