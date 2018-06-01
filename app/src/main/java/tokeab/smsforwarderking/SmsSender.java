package tokeab.smsforwarderking;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

public class SmsSender {

    public static void SendSms(String phoneNumber, ArrayList<String> messageList) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(phoneNumber, null, messageList, null, null);
    }
}
