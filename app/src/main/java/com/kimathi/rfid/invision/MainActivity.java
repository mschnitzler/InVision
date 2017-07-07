package com.kimathi.rfid.invision;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public Intent intent;
    public String action;
    public String tagInfo = "";
    public String sId;
    String txTag;
    Tag tag;
    byte[] tagId;
    public TextView personal;
    public NfcAdapter mNfcAdapter;
    public  PendingIntent pendingIntent;
    public IntentFilter[] intentArrayFilter;
    public  String techlist[][];
    public ArrayList<InvisionController> activities;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle(getResources().getString(R.string.app_name)+ getInfo());
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        final InvisionController invisionController = new InvisionController();
        final List<InvisionActivity> activities = invisionController.getActivities();
        final Spinner activity_spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, activities);
        personal = (TextView) findViewById(R.id.personal);
        activity_spinner.setAdapter(adapter);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                           You should specify only the ones that you need. */
            ndef.addDataScheme("http");
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentArrayFilter = new IntentFilter[] {ndef};
        techlist = new String[][] { new String[] { MifareUltralight.class.getName(), Ndef.class.getName(), NfcA.class.getName()},
                new String[] { MifareClassic.class.getName(), Ndef.class.getName(), NfcA.class.getName()}};


        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                sId = String.valueOf(activities.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button sendBu = (Button) findViewById(R.id.sendBu);
        sendBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(sId) < 0){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.errorActivity), Toast.LENGTH_SHORT).show();
                }
                else if(tagInfo == "" || tagInfo == null){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.errorRFID), Toast.LENGTH_SHORT).show();
                }
                else {
                    invisionController.logActivity(tagInfo, sId);
                    tagInfo = "";
                    sId = "0";
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.messageSuccessful), Toast.LENGTH_SHORT).show();
                    personal.setText(getResources().getString(R.string.rfid_nummer));
                }
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
        mNfcAdapter.disableForegroundDispatch(this);
    }catch (NullPointerException e){
        e.printStackTrace();
    }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentArrayFilter, techlist);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        intent = getIntent();
        action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            tagId = tag.getId();


            for (int i = 0; i < tagId.length; i++) {
                tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
            }
            txTag = getResources().getString(R.string.rfid_nummer)+ tagInfo;
            personal.setText(txTag);
        }

    }
    public String getInfo(){
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException io) {
            io.printStackTrace();
            return "";
        }
    }
}






