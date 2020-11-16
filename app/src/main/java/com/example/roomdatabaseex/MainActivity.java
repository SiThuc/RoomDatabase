package com.example.roomdatabaseex;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomdatabaseex.Adapter.UserAdapter;
import com.example.roomdatabaseex.Callbacks.IUpdateUserListener;
import com.example.roomdatabaseex.Database.UserDatabase;
import com.example.roomdatabaseex.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private EditText edt_username;
    private EditText edt_userphone;
    private Button btn_add;
    private RecyclerView rcv_users;
    private TextView txt_clearAll;
    private EditText edt_search;

    private UserAdapter userAdapter;
    private List<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initData();

        userAdapter = new UserAdapter(new IUpdateUserListener() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_users.setLayoutManager(layoutManager);

        rcv_users.setAdapter(userAdapter);

        // Event add user
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        // Event clear all user
        txt_clearAll.setOnClickListener(v -> {
            clearAllProcess();
        });

        // Event to search
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    handleSearchUser();
                }
                return false;
            }
        });

        //Event to change Edit
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleSearchUser();
            }
        });

        loadData();
    }

    private void handleSearchUser() {
        String queryString = edt_search.getText().toString().trim();
        userList.clear();
        userList = UserDatabase.getInstance(this).userDAO().searchUser(queryString);
        userAdapter.setData(userList);
        hideSoftKeyboard();
    }

    private void clickDeleteUser(User user) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Do you really want to delete this user?")
                .setPositiveButton("Yes", (dialog1, which) -> {
                    //delete User
                    UserDatabase.getInstance(this).userDAO().deleteUser(user);
                    Toast.makeText(this, "Delete User successfully!", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("No", (dialog12, which) -> {
                }).show();
    }


    private void clickUpdateUser(User user) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);

        startActivityForResult(intent, REQUEST_CODE);

        hideSoftKeyboard();
    }


    private void addUser() {
        String username = edt_username.getText().toString().trim();
        String userphone = edt_userphone.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(userphone)){
            return;
        }
        User user = new User(username, userphone);
        
        //Check if user exist
        if(isUserExist(user)){
            Toast.makeText(this, "User already existed!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        
        UserDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this, "Add User successfully!", Toast.LENGTH_SHORT).show();

        edt_username.setText("");
        edt_userphone.setText("");

        hideSoftKeyboard();

        loadData();


    }

    private void clearAllProcess() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete All Users")
                .setMessage("Do you really want to delete all users ?")
                .setPositiveButton("Yes", (dialog1, which) -> {
                    //delete All User
                    UserDatabase.getInstance(this).userDAO().deleteAllUsers();
                    Toast.makeText(this, "Delete Users successfully!", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("No", (dialog12, which) -> {
                }).show();
    }

    private void loadData(){
        userList = UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(userList);

    }

    private void initData() {
        userList = new ArrayList<>();
    }

    private void initViews() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_userphone = (EditText) findViewById(R.id.edt_userphone);
        btn_add = (Button)findViewById(R.id.btn_add);
        rcv_users = (RecyclerView)findViewById(R.id.rcv_users);
        txt_clearAll = (TextView)findViewById(R.id.txt_clearAll);
        edt_search = (EditText)findViewById(R.id.edt_search);
    }

    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public boolean isUserExist(User user){
        List<User> uList = UserDatabase.getInstance(this).userDAO().checkUser(user.getUserName());
        if(uList != null && !uList.isEmpty())
            return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }
}