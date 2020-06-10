package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import com.example.myapplication.snapshot.headphone.HeadphoneActivity;
import com.example.myapplication.fence.headphone.HeadphoneFenceActivity;

import com.google.android.gms.common.api.GoogleApiClient;



public class MainActivity extends AppCompatActivity {
    @BindView(R.id.layout_headphones) RelativeLayout mHeadphonesLayout;
    @BindView(R.id.image_headphones) ImageView mHeadphoneImage;
    @BindView(R.id.text_headphone_state) TextView mHeadphoneText;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.text_headphone_state)
    public void onHeadphoneStateTextClick() {
        Intent intent = new Intent(this, HeadphoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_headphone_fence)
    public void onHeadphoneFenceTextClick() {
        Intent intent = new Intent(this, HeadphoneFenceActivity.class);
        startActivity(intent);
    }
}