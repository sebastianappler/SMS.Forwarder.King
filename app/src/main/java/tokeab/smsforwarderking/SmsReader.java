package tokeab.smsforwarderking;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsReader {

    public static ArrayList<String> GetLatestNumberAndSMS(Context context, String remoteNumber) {
        Uri myMessage = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(myMessage, new String[] { "_id",
                        "address", "date", "body", "read" }, null,
                null, null);

        String smsSenderNumber = "";
        String smsBody = "";


        if (cursor.moveToFirst()) {
            try {
                do {
                    smsSenderNumber = cursor.getString(
                            cursor.getColumnIndexOrThrow("address")).toString();
                    smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                            .toString();
                }
                while (cursor.moveToNext() && smsSenderNumber.equals(remoteNumber));

                cursor.close();


            } catch(Exception e){
                e.printStackTrace();
            }
        }

        ArrayList<String> latestnumberAndSMS = new ArrayList<>();
        latestnumberAndSMS.add(smsSenderNumber);
        latestnumberAndSMS.add(smsBody);

        return latestnumberAndSMS;
    }
}