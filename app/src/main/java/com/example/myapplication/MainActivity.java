package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.status)
    TextView titleTextView;


    private static final int RC_SIGN_IN = 9001;
    private static final int MY_PERMISSION_LOCATION = 1;

    private GoogleSignInClient mSignInClient;

    private GoogleApi mGoogleApi;
    private SnapshotClient mSnapshotClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        mSignInClient = GoogleSignIn.getClient(this, options);
        ButterKnife.bind(this);

//        signIn();
    }

    @OnClick(R.id.headphone)
    public void headphoneClick() {
//        Toast.makeText(this, "안녕하세요!!!", Toast.LENGTH_LONG).show();
        getHeadphoneState();
    }

    @OnClick(R.id.location)
    public void locationClick(){
        getLocation();
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION
            );
        }
    }

    public void getLocation() {
        checkPermission();
        Awareness.getSnapshotClient(this).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationResponse> task) {
                if (task.isSuccessful()) {
                    LocationResponse result = task.getResult();

                    assert result != null;
                    Location loc = result.getLocation();
                    System.out.println(loc);

                } else {
                    task.getException().printStackTrace();
                    titleTextView.setText("위치 상태 가져올 수 없음");
                }
            }
        });
    }


        //TODO: Method 1 of handling the task result with onComplete Listener
    public void getHeadphoneState() {
        Awareness.getSnapshotClient(this).getHeadphoneState().addOnCompleteListener(new OnCompleteListener<HeadphoneStateResponse>() {
            @Override
            public void onComplete(@NonNull Task<HeadphoneStateResponse> task) {
                if (task.isSuccessful()) {
                    HeadphoneStateResponse result = task.getResult();
                    int state = result.getHeadphoneState().getState();

                    if (state == HeadphoneState.PLUGGED_IN) {
                        titleTextView.setText("헤드폰 꽂힘");
                    } else if (state == HeadphoneState.UNPLUGGED) {
                        titleTextView.setText("헤드폰 꽂히지 않음");
                    }
                } else {
                    task.getException().printStackTrace();
                    titleTextView.setText("헤드폰 상태 가져올 수 없음");
                }
            }
        });
    }

    //TODO: Method 2 of handling the task result with onSuccessListener and onFailureListener
    public void getHeadphoneStateV2(final TextView v) {
        Awareness.getSnapshotClient(this).getHeadphoneState().addOnSuccessListener(new OnSuccessListener<HeadphoneStateResponse>() {
            @Override
            public void onSuccess(HeadphoneStateResponse headphoneStateResponse) {
                int state = headphoneStateResponse.getHeadphoneState().getState();

                if (state == HeadphoneState.PLUGGED_IN) {
//                    v.append("\n___\nHeadphones plugged in.");
                    System.out.println("헤드폰 연결됨");
                } else if (state == HeadphoneState.UNPLUGGED) {
//                    v.append("\n___\nHeadphones unplugged.");
                    System.out.println("헤드폰 연결되지 않음");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                v.append("\n___\nUnable to Get Headphones!");
            }
        });
    }



    private void signIn() {
        // Launches the sign in flow, the result is returned in onActivityResult
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            if (((Task) task).isSuccessful()) {
                // Sign in succeeded, proceed with account
                GoogleSignInAccount acct = task.getResult();
            } else {
                // Sign in failed, handle failure and update UI
                // ...
            }
        }
    }







}