package tokeab.smsforwarderking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsListener extends BroadcastReceiver {

    private String remotePhoneNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {

            String senderPhoneNumber = "";
            ArrayList<String> messagesList = new ArrayList<String>();

            Boolean first = true;
            Boolean IsSenderRemote = false;

            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {

                String messageBody = "";

                if(first){
                    senderPhoneNumber = smsMessage.getOriginatingAddress();
                    IsSenderRemote = senderPhoneNumber.equals(remotePhoneNumber);

                    if(!IsSenderRemote) {
                        Integer conversationId = ConversationHandler.SaveConversation(context, senderPhoneNumber);
                        messageBody += "#" + conversationId + " "  + senderPhoneNumber + ": ";
                    }

                    first = false;
                }

                messageBody += smsMessage.getMessageBody();

                messagesList.add(messageBody);

                Log.i("SmsListener", "Message recieved: " + messageBody);
            }

            //If remote phone is sender
            if(IsSenderRemote){
                ArrayList<String> latestNumberAndSMS = SmsReader.GetLatestNumberAndSMS(context, remotePhoneNumber);
                String phoneNumberToSendSMS = latestNumberAndSMS.get(0);

                String firstMessage = messagesList.get(0);
                Pattern pattern = Pattern.compile("^#(\\d+)\\s(.*)");
                Matcher matcher = pattern.matcher(firstMessage);
                if (matcher.matches()) {
                    String conversationIdString = matcher.group(1);
                    int conversationId = 0;

                    try {
                        conversationId = Integer.parseInt(conversationIdString);
                        if(conversationId > 0) {
                            phoneNumberToSendSMS = ConversationHandler.GetPhoneNumber(context, conversationId);
                            messagesList.set(0, matcher.group(2));
                        }
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }

                    SmsSender.SendSms(phoneNumberToSendSMS, messagesList);
                } else {
                    ArrayList<String> noIdFoundMessage = new ArrayList<>();
                    noIdFoundMessage.add("No conversation id found. Type a message with the format\n #id message");

                    SmsSender.SendSms(remotePhoneNumber, noIdFoundMessage);
                }
            } else {

                SmsSender.SendSms(remotePhoneNumber, messagesList);
            }

        }
    }
}
