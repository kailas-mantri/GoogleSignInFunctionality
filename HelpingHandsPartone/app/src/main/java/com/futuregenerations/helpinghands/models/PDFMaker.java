package com.futuregenerations.helpinghands.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.futuregenerations.helpinghands.BuildConfig;
import com.futuregenerations.helpinghands.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFMaker {
    Context context;
    Canvas canvas;
    Paint myPaint,titlePaint,movieTitlePaint,textPaint;
    Bitmap bitmapElse, scaledBitmap, logoBitmap, logoScaledBitmap,backgroundBitmap, bgScaledBitmap,qRBitmap,scaledQRBitmap;
    File folder;
    File file;
    PdfDocument document;
    PdfDocument.PageInfo myPageInfo;
    PdfDocument.Page myPage;
    String organizationName,location,date,time,transactionID,userName,userEmail,amount,category,status;

    public PDFMaker(Context context) {
        this.context = context;
    }

    public void generatePDF(UserPaymentModel paymentModel) {

        organizationName = paymentModel.getOrganizationDetails().getOrganizationTitle();
        location = paymentModel.getUserDetails().getUserState();
        date = paymentModel.getPaymentDetails().getDonationDate();
        time = paymentModel.getPaymentDetails().getDonationTime();
        amount = paymentModel.getPaymentDetails().getDonationAmount();
        transactionID = paymentModel.getPaymentDetails().getTransactionID();
        userName = paymentModel.getUserDetails().getUserName();
        userEmail = paymentModel.getUserDetails().getUserEmail();
        category = paymentModel.getOrganizationDetails().getOrganizationCategory();
        status = "Successful";

        document = new PdfDocument();
        myPaint = new Paint();
        titlePaint = new Paint();
        movieTitlePaint = new Paint();
        textPaint = new Paint();

        myPageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        myPage = document.startPage(myPageInfo);
        canvas = myPage.getCanvas();

        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_border);
        bgScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmap,1200,30,false);
        canvas.drawBitmap(bgScaledBitmap,0,0,myPaint);
        logoBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.pdf_logo);
        logoScaledBitmap = Bitmap.createScaledBitmap(logoBitmap,300,300,false);
        canvas.drawBitmap(logoScaledBitmap,40,75,myPaint);

        titlePaint.setTextSize(70);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("Helping\nHands",1200/2,450/2,titlePaint);

        bitmapElse = BitmapFactory.decodeResource(context.getResources(),R.drawable.paytm);
        scaledBitmap = Bitmap.createScaledBitmap(bitmapElse,320,320,false);
        canvas.drawBitmap(scaledBitmap,20,470,myPaint);

        movieTitlePaint.setColor(Color.BLACK);
        movieTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        movieTitlePaint.setTextSize(60);
        canvas.drawText(organizationName,370,550,movieTitlePaint);

        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setColor(Color.BLACK);
        canvas.drawText(category,370,600,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Name :",20,1000,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(userName,160,1000,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Email :",20,1060,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(userEmail,160,1060,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Transaction ID :",20,1120,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(transactionID,320,1120,textPaint);
        textPaint.reset();

        titlePaint.setTextSize(60);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("Donation Details",1200/2,1240,titlePaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,1280,1180,1360,myPaint);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        canvas.drawText("Date",40, 1330, textPaint);
        canvas.drawText("Time",225,1330,textPaint);
        canvas.drawText("Location",400,1330,textPaint);
        canvas.drawText("Status",800,1330,textPaint);
        canvas.drawText("Amount",1000,1330,textPaint);

        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(205,1290,205,1350,myPaint);
        canvas.drawLine(380,1290,380,1350,myPaint);
        canvas.drawLine(780,1290,780,1350,myPaint);
        canvas.drawLine(980,1290,980,1350,myPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas.drawText(date,30,1450,textPaint);
        canvas.drawText(time,215,1450,textPaint);
        canvas.drawText(location,390,1450,textPaint);
        canvas.drawText(amount,990,1450,textPaint);
        canvas.drawText(status,790,1450,textPaint);

        canvas.drawLine(780,1700,1180,1700,myPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setTextSize(30);
        canvas.drawText("Total :",790,1750,textPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setTextSize(30);
        canvas.drawText(amount,990,1750,textPaint);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String bookingDate = dateFormat.format(date);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        canvas.drawText("Date : "+bookingDate,20,1810,textPaint);
        textPaint.reset();

        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String bookingTime = timeFormat.format(time);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        canvas.drawText("Time : "+bookingTime,20,1850,textPaint);
        textPaint.reset();

        Bitmap footerBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.pdf_border);
        Bitmap scaledFooterBitmap = Bitmap.createScaledBitmap(footerBitmap,1200,30,false);
        canvas.drawBitmap(scaledFooterBitmap,0,1980,myPaint);

        document.finishPage(myPage);
        folder = new File(Environment.getExternalStorageDirectory()+File.separator+"Helping Hands"+File.separator+"Donation Receipts");
        boolean success = true;
        Date saveDate = new Date();
        DateFormat saveDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String dateSaved = saveDateFormat.format(saveDate);
        String fileName = organizationName+dateSaved+".pdf";
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            file = new File(folder,"/"+fileName);
            try {
                document.writeTo(new FileOutputStream(file));
                String path = folder.getPath();
                Toast.makeText(context, "Your Ticket is stored to "+path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID,file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri,"application/pdf");
                context.startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generatePDFWithQRCode(UserPaymentModel paymentModel) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.show();

        organizationName = paymentModel.getOrganizationDetails().getOrganizationTitle();
        location = paymentModel.getUserDetails().getUserState();
        date = paymentModel.getPaymentDetails().getDonationDate();
        time = paymentModel.getPaymentDetails().getDonationTime();
        amount = paymentModel.getPaymentDetails().getDonationAmount();
        transactionID = paymentModel.getPaymentDetails().getTransactionID();
        userName = paymentModel.getUserDetails().getUserName();
        userEmail = paymentModel.getUserDetails().getUserEmail();
        category = paymentModel.getOrganizationDetails().getOrganizationCategory();
        status = "Successful";

        document = new PdfDocument();
        myPaint = new Paint();
        titlePaint = new Paint();
        movieTitlePaint = new Paint();
        textPaint = new Paint();

        myPageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        myPage = document.startPage(myPageInfo);
        canvas = myPage.getCanvas();

        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_border);
        bgScaledBitmap = Bitmap.createScaledBitmap(backgroundBitmap,1200,30,false);
        canvas.drawBitmap(bgScaledBitmap,0,0,myPaint);
        logoBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.pdf_logo);
        logoScaledBitmap = Bitmap.createScaledBitmap(logoBitmap,300,300,false);
        canvas.drawBitmap(logoScaledBitmap,40,75,myPaint);

        titlePaint.setTextSize(70);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("Helping",1200/2,450/2,titlePaint);
        canvas.drawText("Hands",1200/2,300,titlePaint);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(transactionID, BarcodeFormat.QR_CODE,300,300);
            qRBitmap = Bitmap.createBitmap(300,300,Bitmap.Config.RGB_565);

            for (int x = 0;x<300;x++) {
                for (int y = 0;y<300;y++) {
                    qRBitmap.setPixel(x,y,bitMatrix.get(x,y)?Color.BLACK:Color.WHITE);
                }
            }

            scaledQRBitmap = Bitmap.createScaledBitmap(qRBitmap,300,300,false);
            canvas.drawBitmap(scaledQRBitmap,860,75,myPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmapElse = BitmapFactory.decodeResource(context.getResources(),R.drawable.paytm);
        scaledBitmap = Bitmap.createScaledBitmap(bitmapElse,320,320,false);
        canvas.drawBitmap(scaledBitmap,20,470,myPaint);

        movieTitlePaint.setColor(Color.BLACK);
        movieTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        movieTitlePaint.setTextSize(60);
        canvas.drawText(organizationName,370,550,movieTitlePaint);

        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setColor(Color.BLACK);
        canvas.drawText(category,370,600,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Name :",20,1000,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(userName,160,1000,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Email :",20,1060,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(userEmail,160,1060,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText("Transaction ID :",20,1120,textPaint);
        textPaint.reset();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(transactionID,320,1120,textPaint);
        textPaint.reset();

        titlePaint.setTextSize(60);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("Donation Details",1200/2,1240,titlePaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,1280,1180,1360,myPaint);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        canvas.drawText("Date",40, 1330, textPaint);
        canvas.drawText("Time",225,1330,textPaint);
        canvas.drawText("Location",400,1330,textPaint);
        canvas.drawText("Status",800,1330,textPaint);
        canvas.drawText("Amount",1000,1330,textPaint);

        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(205,1290,205,1350,myPaint);
        canvas.drawLine(380,1290,380,1350,myPaint);
        canvas.drawLine(780,1290,780,1350,myPaint);
        canvas.drawLine(980,1290,980,1350,myPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas.drawText(date,30,1450,textPaint);
        canvas.drawText(time,215,1450,textPaint);
        canvas.drawText(location,390,1450,textPaint);
        canvas.drawText(amount,990,1450,textPaint);
        canvas.drawText(status,790,1450,textPaint);

        canvas.drawLine(780,1700,1180,1700,myPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setTextSize(30);
        canvas.drawText("Total :",790,1750,textPaint);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        textPaint.setTextSize(30);
        canvas.drawText(amount,990,1750,textPaint);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String bookingDate = dateFormat.format(date);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        canvas.drawText("Date : "+bookingDate,20,1810,textPaint);
        textPaint.reset();

        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String bookingTime = timeFormat.format(time);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        canvas.drawText("Time : "+bookingTime,20,1850,textPaint);
        textPaint.reset();

        Bitmap footerBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.pdf_border);
        Bitmap scaledFooterBitmap = Bitmap.createScaledBitmap(footerBitmap,1200,30,false);
        canvas.drawBitmap(scaledFooterBitmap,0,1980,myPaint);

        document.finishPage(myPage);
        folder = new File(Environment.getExternalStorageDirectory()+File.separator+"Helping Hands"+File.separator+"Donation Receipts");
        boolean success = true;
        Date saveDate = new Date();
        DateFormat saveDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String dateSaved = saveDateFormat.format(saveDate);
        String fileName = organizationName+dateSaved+".pdf";
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            file = new File(folder,"/"+fileName);
            try {
                document.writeTo(new FileOutputStream(file));
                String path = folder.getPath();
                dialog.dismiss();
                Toast.makeText(context, "Your Ticket is stored to "+path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri,"application/pdf");
                context.startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}