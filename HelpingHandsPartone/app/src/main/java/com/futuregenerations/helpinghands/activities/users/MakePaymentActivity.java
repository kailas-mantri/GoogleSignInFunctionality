package com.futuregenerations.helpinghands.activities.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.models.JSONParser;
import com.futuregenerations.helpinghands.models.NotificationDataModel;
import com.futuregenerations.helpinghands.models.PDFMaker;
import com.futuregenerations.helpinghands.models.PaymentDetailsModel;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.futuregenerations.helpinghands.models.UserSharedPrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MakePaymentActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    TextView textViewName, textViewType, textViewLocation;
    EditText editTextAmount;
    String userID, amount, organizationName, organizationType, organizationLocation, organizationID, categoryID,
            mid="ZzqSoL76017814651604",orderId="",custId="",amt="",transactionID, donationDate, donationTime, notification;
    GetOrganizationsDataModel organizationsModel;
    UserDataModel userModel;
    PaymentDetailsModel paymentDetailsModel;
    NotificationDataModel notificationModel;
    UserPaymentModel paymentModel;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference organizationReference,notificationReference,paymentReference,userReference;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setAllReferences();
        getIntentData();
        getViewsByID();
        generateRequiredData();
    }

    private void generateRequiredData() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        String datetime = format.format(calendar.getTime());
        orderId = userID+datetime;
        custId = userID;
    }

    private void setAllReferences() {
        organizationReference = FirebaseDatabase.getInstance().getReference("Organizations");
        paymentReference = FirebaseDatabase.getInstance().getReference("Payments");
        notificationReference = FirebaseDatabase.getInstance().getReference("UserNotifications");
        userReference = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser!=null) {
            userID = firebaseUser.getUid();
        }
    }

    private void getViewsByID() {
        textViewName = findViewById(R.id.organization_name_payment);
        textViewType = findViewById(R.id.organization_type_payment);
        textViewLocation = findViewById(R.id.organization_location_payment);
        editTextAmount = findViewById(R.id.edit_text_donation_amount);
        loader = findViewById(R.id.loader);

        setDataToView();
    }

    private void setDataToView() {
        organizationReference.child(categoryID).child(organizationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    organizationsModel = dataSnapshot.getValue(GetOrganizationsDataModel.class);
                    if (organizationsModel!=null) {
                        organizationName = organizationsModel.getOrganizationTitle();
                        organizationType = organizationsModel.getOrganizationType();
                        organizationLocation = organizationsModel.getOrganizationLocation();

                        textViewType.setText(organizationType);
                        textViewLocation.setText(organizationLocation);
                        textViewName.setText(organizationName);
                    }

                    getUserDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserDetails() {
        userReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userModel = dataSnapshot.getValue(UserDataModel.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        organizationID = intent.getStringExtra("organizationID");
        categoryID = intent.getStringExtra("categoryID");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void makePayment(View view) {
        if (ContextCompat.checkSelfPermission(MakePaymentActivity.this, Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(MakePaymentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakePaymentActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},101);
        }

        if (ContextCompat.checkSelfPermission(MakePaymentActivity.this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MakePaymentActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            amount = editTextAmount.getText().toString();
            int total = Integer.parseInt(amount);
            if (TextUtils.isEmpty(amount)) {
                Toast.makeText(this, "Please Enter Amount to Donate", Toast.LENGTH_SHORT).show();
            }
            else if (total<500) {
                Toast.makeText(this, "Please enter amount 500 or above to donate", Toast.LENGTH_SHORT).show();
            }
            else {
                amt = amount;
                editTextAmount.setText("");
                sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
                dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void UploadDataToFirebase() {
        loader.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        donationDate = dateFormat.format(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        donationTime = timeFormat.format(calendar.getTime());

        paymentDetailsModel = new PaymentDetailsModel(transactionID,organizationID,organizationName,donationDate,donationTime,amount,userID,orderId);
        paymentModel = new UserPaymentModel(userModel,organizationsModel,paymentDetailsModel);
        paymentReference.child(userID).child(orderId).setValue(paymentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storeNotifications();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MakePaymentActivity.this, "Failed To Add Data To Database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeNotifications() {
        notification = "Your Donation to organization "+organizationName+" is completed with transaction id "+transactionID+" of amount RS. "+amount;

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateToday = dateFormat.format(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        String timeToday = timeFormat.format(calendar.getTime());
        SimpleDateFormat pathFormat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        String notificationPath = pathFormat.format(calendar.getTime());

        notificationModel = new NotificationDataModel(notification,dateToday,timeToday);
        notificationReference.child(userID).child(notificationPath).setValue(notificationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                PDFMaker pdfMaker = new PDFMaker(MakePaymentActivity.this);
                pdfMaker.generatePDFWithQRCode(paymentModel);
                getAppNotification();
                Toast.makeText(MakePaymentActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void getAppNotification() {
        String notificationAccess = UserSharedPrefManager.getInstance(MakePaymentActivity.this).getData().getNotificationStatus();
        if (TextUtils.equals(notificationAccess,getResources().getString(R.string.allow_notification))) {
            int notification_Action_ID = 0;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo))
                    .setContentTitle(organizationName)
                    .setContentText(notification)
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
    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(MakePaymentActivity.this);

        String url ="https://mymoviezpoint.000webhostapp.com/paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        String CHECKSUMHASH ="";
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(MakePaymentActivity.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custId+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amt+"&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);

            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();

            HashMap<String, String> paramMap = new HashMap<>();

            paramMap.put("MID", mid);
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custId);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amt);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);

            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);

            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);

            Service.startPaymentTransaction(MakePaymentActivity.this, true, true,
                    MakePaymentActivity.this  );
        }
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Log.e("checksum ", " respon true " + inResponse.toString());
        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
            transactionID = inResponse.getString("TXNID");
            UploadDataToFirebase();
        }
        else {
            Intent failureIntent = new Intent(MakePaymentActivity.this,PaymentFailureActivity.class);
            startActivity(failureIntent);
            finish();
        }
    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Log.e("checksum ", " ui fail respon  "+ inErrorMessage );
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Log.e("checksum ", " error loading pagerespon true "+ inErrorMessage + "  s1 " + inFailingUrl);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Log.e("checksum ", "  transaction cancel " );
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
