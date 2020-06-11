package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResponse;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

//import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.Task;

import com.example.myapplication.R;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mSignInClient;
    private GoogleApi mGoogleApi;
    private SnapshotClient mSnapshotClient;
    private TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        mSignInClient = GoogleSignIn.getClient(this, options);

        getHeadphoneStateV2(testView);

//        signIn();
    }

    //TODO: Method 1 of handling the task result with onComplete Listener
    public void getHeadphoneState(final TextView v) {
        Awareness.getSnapshotClient(this).getHeadphoneState().addOnCompleteListener(new OnCompleteListener<HeadphoneStateResponse>() {
            @Override
            public void onComplete(@NonNull Task<HeadphoneStateResponse> task) {
                if (task.isSuccessful()) {
                    HeadphoneStateResponse result = task.getResult();
                    int state = result.getHeadphoneState().getState();

                    if (state == HeadphoneState.PLUGGED_IN) {
                        v.append("\n___\nHeadphones plugged in.");
                    } else if (state == HeadphoneState.UNPLUGGED) {
                        v.append("\n___\nHeadphones unplugged.");
                    }
                } else {
                    task.getException().printStackTrace();
                    v.append("\n___\nUnable to Get Headphones!");
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