package com.example.smartsms;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.*;

public class SmsReceiver extends BroadcastReceiver {

    SqliteDB db;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static MessageListener mListener;
    MediaPlayer mediaPlayer;
    public static void bindListener(MessageListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        db = new SqliteDB(context);
        ArrayList<Rule> rules = db.getAllRule();
        abortBroadcast();

        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                for(Rule r : rules){
                    if(checkNumbers(r.phoneNumber,address)){
                        mediaPlayer = new MediaPlayer();
                        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  Long.parseLong( r.priority.musicPath ));
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {

                            mediaPlayer.setDataSource(context, contentUri);
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                            CountDownTimer timer = new CountDownTimer(8000, 8000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // Nothing to do
                                }

                                @Override
                                public void onFinish() {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                    }
                                }
                            };
                            timer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try{
                            mListener.messageReceived(r);
                        }catch (Exception ee){
                            Toast.makeText(context, r.name, Toast.LENGTH_SHORT).show();

                        }

                    }
                }
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }

        }
    }

    boolean checkNumbers(String number1, String number2){
        String pre = "+48";
        String tmp = number1.replace(pre,"");
        String tmp2 = number2.replace(pre,"");
        return tmp.equals(tmp2);
    }
}

