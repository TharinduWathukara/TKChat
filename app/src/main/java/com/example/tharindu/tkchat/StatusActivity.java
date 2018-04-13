package com.example.tharindu.tkchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private TextInputLayout mStatus;
    private Button mStatusSaveBtn;

    //firebase database
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //progress dialog
    private ProgressDialog mStatusProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //firebase database
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mToolBar = (Toolbar) findViewById(R.id.status_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mStatusSaveBtn = (Button) findViewById(R.id.status_save_btn);

        mStatus.getEditText().setText(status_value);

        mStatusSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = mStatus.getEditText().getText().toString();

                if(!TextUtils.isEmpty(status)){
                    //progress bar
                    mStatusProgress = new ProgressDialog(StatusActivity.this);
                    mStatusProgress.setTitle("Updating Status");
                    mStatusProgress.setMessage("Please wait while we update your new status!");
                    mStatusProgress.show();

                    mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mStatusProgress.dismiss();
                                Intent settings_intent = new Intent(StatusActivity.this,SettingsActivity.class);
                                startActivity(settings_intent);
                                finish();

                            }else {
                                mStatusProgress.hide();
                                Toast.makeText(getApplicationContext(),"There is some errors in updating changes!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(StatusActivity.this,"Please add your status!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
