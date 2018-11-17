package com.example.frsamuel.frsamuelv13;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class UsersRecycleAdapter extends RecyclerView.Adapter<UsersRecycleAdapter.ViewHolder> {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;
    private String admin_id;

    public Context context;
    public List<Users> user_List_data;

    public UsersRecycleAdapter(List<Users> user_List_data)
    {
        this.user_List_data = user_List_data;
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list,viewGroup ,false);
        context = viewGroup.getContext();
        return new UsersRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String user_name = user_List_data.get(i).getName();
        final String user_id = user_List_data.get(i).getUser_id();
        final String user_phone = user_List_data.get(i).getPhone();
        final String user_address = user_List_data.get(i).getAddress();

        viewHolder.setNameText(user_name);
        viewHolder.setUserAdd(user_address);
        viewHolder.setUserPhone(user_phone);

        viewHolder.userDeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "manually", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return user_List_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private TextView user_Name;
        private TextView phone;
        private TextView address;
        private ImageView userDeletebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            userDeletebtn = mView.findViewById(R.id.UserDelete_UserList);
        }

        public void setNameText(String name){
            user_Name = mView.findViewById(R.id.UserName_UserList);
            user_Name.setText(name);
        }

        public void setUserPhone(String phon)
        {
            phone = mView.findViewById(R.id.UserPhone_UserList);
            phone.setText(phon);
        }

        public void setUserAdd(String add){
            address = mView.findViewById(R.id.UserAddress_UserList);
            address.setText(add);
        }

    }
}
