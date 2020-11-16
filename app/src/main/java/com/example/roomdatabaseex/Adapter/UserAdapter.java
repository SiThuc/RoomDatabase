package com.example.roomdatabaseex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabaseex.Callbacks.IUpdateUserListener;
import com.example.roomdatabaseex.Model.User;
import com.example.roomdatabaseex.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
   private IUpdateUserListener listener;

    public UserAdapter(IUpdateUserListener listener) {
        this.listener = listener;
    }

    public void setData(List<User> users){
        userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if(user == null)
            return;
        holder.txt_username.setText(user.getUserName());
        holder.txt_phone.setText(user.getUserPhone());
        holder.btn_update.setOnClickListener(v -> {
            listener.updateUser(user);
        });
        holder.btn_delete.setOnClickListener(v -> {
            listener.deleteUser(user);
        });
    }

    @Override
    public int getItemCount() {
        if(userList != null)
            return userList.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_username;
        private TextView txt_phone;
        private Button btn_update;
        private Button btn_delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_username = (TextView)itemView.findViewById(R.id.txt_username);
            txt_phone = (TextView)itemView.findViewById(R.id.txt_phone);
            btn_update = (Button)itemView.findViewById(R.id.btn_update);
            btn_delete = (Button)itemView.findViewById(R.id.btn_delete);
        }
    }
}
