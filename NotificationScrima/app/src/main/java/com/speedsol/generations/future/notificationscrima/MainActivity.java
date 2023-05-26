package com.speedsol.generations.future.notificationscrima;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnShow);
        button1 = findViewById(R.id.btninbox);
    }

    public void showNotification(View view) {
        simpleNotification();
    }

    private void simpleNotification() {
        int notification_Action_ID = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setContentTitle("Welcome To SCRIMATEC")
                .setContentText("We are Android Expertize")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_Action_ID,builder.build());
    }

    public void inBoxNotification(View view) {
        int notification_id = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setStyle(new NotificationCompat.InboxStyle().addLine("Hello").addLine("Are you free?")
                .setBigContentTitle("2 messages for you").setSummaryText("Inbox"))
                .setAutoCancel(true);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_id,builder.build());
    }

    public void big_Picture_Notification(View view) {
        int notification_id = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bugatti);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setContentTitle("Big Picture Notification View")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setAutoCancel(true);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_id,builder.build());
    }

    public void bigTextNotification(View view) {
        int notification_id = 3;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setContentTitle("Big Text Notification View")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("SCRIMATEC IT SOLUTIONS PVT.LTD."))
                .setAutoCancel(true);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_id,builder.build());
    }

    public void messagingStyleNotification(View view) {
        int notification_id = 4;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setContentTitle("Messaging Style Notification")
                .setStyle(new NotificationCompat.MessagingStyle("Karuna : ").setConversationTitle("Scrimatec Android Group")
                .addMessage("Hello Karuna",0,"Abhishek :")
                .addMessage("Yes You are Late",0,"Abhishek :")
                .addMessage("True",0,"Karuna :"))
                .setAutoCancel(true);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_id,builder.build());
    }

    public void notificationActionStyle(View view) {
        int notification_id = 5;

        Intent  intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.scrimatec.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round))
                .setContentTitle("Notification Action View")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Click on Button to visit Google"))
                .addAction(android.R.drawable.ic_menu_view,"GO",pendingIntent)
                .setAutoCancel(true);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channel_id,"CHANNEL HUMAN READABLE TITLE",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        manager.notify(notification_id,builder.build());
    }
}