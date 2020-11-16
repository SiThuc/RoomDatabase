package com.example.roomdatabaseex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomdatabaseex.Database.UserDatabase;
import com.example.roomdatabaseex.Model.User;

public class UpdateActivity extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_userphone;
    private Button btn_update;

    private User rUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_userphone = (EditText) findViewById(R.id.edt_userphone);
        btn_update = (Button)findViewById(R.id.btn_update);

        rUser = (User)getIntent().getExtras().get("object_user");
        if(rUser != null){
            edt_username.setText(rUser.getUserName());
            edt_userphone.setText(rUser.getUserPhone());
        }
        
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

    }

    private void updateUser() {
        String username = edt_username.getText().toString().trim();
        String userphone = edt_userphone.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(userphone)){
            return;
        }

        rUser.setUserName(username);
        rUser.setUserPhone(userphone);

        UserDatabase.getInstance(this).userDAO().updateUser(rUser);
        Toast.makeText(this, "Update Success!!", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}