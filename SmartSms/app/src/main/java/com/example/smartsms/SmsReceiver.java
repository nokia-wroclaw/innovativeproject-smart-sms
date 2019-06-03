package com.example.smartsms;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.*;

public class SmsReceiver extends BroadcastReceiver {

    public static int counter = 0;
    SqliteDB db;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static MessageListener mListener;
    public static MediaPlayer mediaPlayer;
    public static void bindListener(MessageListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        db = new SqliteDB(context);
        ArrayList<Rule> rules = db.getAllRule();
        abortBroadcast();
        mediaPlayer = new MediaPlayer();
        Bundle intentExtras = intent.getExtras();
        System.out.println(counter);
        if (intentExtras != null ) {

            Object[] sms = (Object[]) intentExtras.get("pdus");
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                for(Rule r : rules){
                    boolean play = false;
                    if(PhoneNumberUtils.compare(r.phoneNumber,address) && Arrays.asList(smsBody.split(" ")).contains(r.phrase)){
                        play = true;
                    }else if((r.phrase.equals("") && PhoneNumberUtils.compare(r.phoneNumber,address)) || (Arrays.asList(smsBody.split(" ")).contains(r.phrase) && r.phoneNumber.equals(""))){
                        play = true;
                    }

                    if(play) {

                        ArrayList<CapturedRule> list = db.getAllCapturedRule();
                        Random random = new Random();
                        int seed = random.nextInt();
                        while (!isSeedFree(seed, list)) {
                            seed = random.nextInt();
                        }
                        CapturedRule capturedRule = new CapturedRule(r.name, smsBody, seed);
                        db.addCapturedRule(capturedRule);
                        switch(counter){
                            case 1:
                            {
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                /*if (mediaPlayer.isPlaying()) {
                                    System.out.println("tu");
                                    //counter--;
                                }*/
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer = MediaPlayer.create(context, R.raw.example2);
                                /*try {
                                    mediaPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                            }break;
                            case 0:
                            {

                                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(r.priority.musicPath));
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mediaPlayer.setDataSource(context, contentUri);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mediaPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        mediaPlayer.start();
                        counter++;
                        CountDownTimer timer = new CountDownTimer(8000, 8000) {


                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    mediaPlayer.release();
                                    counter=0;
                                    System.out.println("Po zmniejszeniu " + counter);

                                }
                            }

                        };

                        timer.start();
                        if(counter == 2)
                            counter = 0;

                        //----------------------------------------------
                        /*if(counter == 2 && !mediaPlayer.isPlaying()){
                            counter = 0;
                        }
                        if (counter == 0){
                            //mediaPlayer = new MediaPlayer();
                            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(r.priority.musicPath));
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            counter++;
                            try {

                                /*if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                }
                                mediaPlayer.setDataSource(context, contentUri);
                                mediaPlayer.prepare();
                                mediaPlayer.start();

                                CountDownTimer timer = new CountDownTimer(8000, 8000) {


                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        System.out.println("Piosenka podstawowa " + counter);
                                        if(counter==2 && mediaPlayer.isPlaying()){
                                            mediaPlayer.stop();
                                            mediaPlayer.reset();
                                            mediaPlayer.release();
                                            counter--;
                                            System.out.println("Przerwanie w srodku");
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                            mediaPlayer.reset();
                                            mediaPlayer.release();
                                            counter=0;
                                            System.out.println("Podstawowa piosenka; Po zmniejszeniu " + counter);

                                        }
                                    }

                                };

                                timer.start();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if(counter == 1){
                            //mediaPlayer = new MediaPlayer();
                            counter ++;
                            try {
                                if(SmsReceiver.mediaPlayer.isPlaying()){
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    mediaPlayer.release();
                                    counter--;
                                }

                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    mediaPlayer.release();
                                    counter--;
                                }

                                //mediaPlayer = new MediaPlayer();
                                mediaPlayer = MediaPlayer.create(context, R.raw.example2);
                                //mediaPlayer.prepare();
                                mediaPlayer.start();

                                CountDownTimer timer = new CountDownTimer(8000, 8000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // Nothing to do
                                        System.out.println("Piosenka awatyjna " + counter);
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                            mediaPlayer.reset();
                                            mediaPlayer.release();
                                            counter=0;
                                            System.out.println("Piosenka zastepcza; Po zmniejszeniu " + counter);

                                        }
                                    }
                                };
                                timer.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                                counter=0;
                            }
                        }*/
                        try{
                            mListener.messageReceived(capturedRule);
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

    boolean isSeedFree(int seed, ArrayList<CapturedRule> capturedRules){
        boolean free = true;
        for (CapturedRule cr : capturedRules){
            if(seed==cr.seed)
                free = false;
        }
        return free;
    }

    //useless right now
    boolean checkNumbers(String number1, String number2){
        char plus = '+';
        String zeroes = "00";
        String tmp = number1.replaceAll("\\s+","");
        String tmp2 = number2.replaceAll("\\s+","");
        try {
            if(tmp.charAt(0) == plus){
                tmp = tmp.substring(3);
            }
            if(tmp.equals(tmp.substring(0,2))){
                tmp = tmp.substring(2);
            }
            if(tmp2.charAt(0) == plus){
                tmp2 = tmp2.substring(3);
            }
            if(zeroes.equals(tmp2.substring(0,2))){
                tmp2 = tmp2.substring(2);
            }
        }catch (Exception e){

        }

        return tmp.equals(tmp2);
    }
}

