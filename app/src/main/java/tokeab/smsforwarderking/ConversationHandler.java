package tokeab.smsforwarderking;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class ConversationHandler {

    private static String CONVERSATION_CURRENT_ID = "CONVERSATION_CURRENT_ID";
    private static String CONVERSATION_PHONENUMBER_TO_ID = "CONVERSATION_PHONENUMBER_TO_ID";
    private static String CONVERSATION_ID_TO_PHONENUMBER = "CONVERSATION_ID_TO_PHONENUMBER";

    public static Integer GetNextConversationId(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONVERSATION_CURRENT_ID, MODE_PRIVATE);
        Integer conversationId = sharedPreferences.getInt(CONVERSATION_CURRENT_ID, 0);

        conversationId++;

        SharedPreferences.Editor editor = context.getSharedPreferences(CONVERSATION_CURRENT_ID, MODE_PRIVATE).edit();
        editor.putInt(CONVERSATION_CURRENT_ID, conversationId);
        editor.apply();

        return conversationId;
    }
    public static Integer SaveConversation(Context context, String phoneNumber) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONVERSATION_PHONENUMBER_TO_ID, MODE_PRIVATE);
        Integer conversationId = sharedPreferences.getInt(phoneNumber, 0);

        if(conversationId == 0){
            conversationId = GetNextConversationId(context);

            SharedPreferences.Editor editorPhoneNumber = context.getSharedPreferences(CONVERSATION_PHONENUMBER_TO_ID, MODE_PRIVATE).edit();
            editorPhoneNumber.putInt(phoneNumber, conversationId);
            editorPhoneNumber.apply();

            SharedPreferences.Editor editorConversationId = context.getSharedPreferences(CONVERSATION_ID_TO_PHONENUMBER, MODE_PRIVATE).edit();
            editorConversationId.putString(String.valueOf(conversationId), phoneNumber);
            editorConversationId.apply();
        }

        return conversationId;
    }

    public static String GetPhoneNumber(Context context, Integer conversationId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONVERSATION_ID_TO_PHONENUMBER, MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(String.valueOf(conversationId), "");

        return phoneNumber;
    }
}
