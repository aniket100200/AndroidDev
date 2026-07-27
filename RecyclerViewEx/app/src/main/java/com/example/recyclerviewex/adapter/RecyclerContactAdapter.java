package com.example.recyclerviewex.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewex.MainActivity;
import com.example.recyclerviewex.R;
import com.example.recyclerviewex.models.Contact;

import java.util.ArrayList;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {

    Context context;
    ArrayList<Contact>contacts;


    int lastPositon=-1;
   public RecyclerContactAdapter(Context context, ArrayList<Contact>contacts){
        this.context=context;
        this.contacts=contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(contacts.get(position).getImage());
        holder.number.setText(contacts.get(position).getNumber());
        holder.name.setText(contacts.get(position).getName());
        /**
         * For Animation
         */

        setAnimation(holder.itemView,position);


        /**
         * on click and longPress listener
         */


        holder.llRow.setOnClickListener((v)->{
            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.add_update_lay);

            EditText edtName=dialog.findViewById(R.id.edtName);
            EditText edtNumber=dialog.findViewById(R.id.edtNumber);
            AppCompatButton btnAction=dialog.findViewById(R.id.btnAction);

            TextView txtTitle= dialog.findViewById(R.id.txtTitle);
            txtTitle.setText("Update Contact");

            btnAction.setText("Update");

            edtName.setText(contacts.get(position).getName());
            edtNumber.setText(contacts.get(position).getNumber());

            btnAction.setOnClickListener((view)->{
                String name=contacts.get(position).getName();
                boolean isEdited=false;
                if(!edtName.getText().toString().equals(name)){
                    name=edtName.getText().toString();
                    isEdited=true;
                }
                String number=contacts.get(position).getNumber();

                if(!number.equals(edtNumber.getText().toString())){
                    number=edtNumber.getText().toString();
                    isEdited=true;
                }

                if("".equals(name) || number.equals("") || !isEdited){
                    Toast.makeText(context,"Please Enter a Valid Name or Number ", Toast.LENGTH_SHORT).show();
                    return;
                }


                Contact currContact=contacts.get(position);
                currContact.setName(name);
                currContact.setNumber(number);

                notifyItemChanged(position);


                dialog.dismiss();
            });

            dialog.show();
        });

        holder.llRow.setOnLongClickListener((v)->{
            AlertDialog.Builder builder=new AlertDialog.Builder(context)
                    .setTitle("Delete Contact")
                    .setMessage("Are you sure want to Delete")
                    .setIcon(R.drawable.outline_10mp_24)
                    .setPositiveButton("Yes",(dialog,which)->{
                        contacts.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No",((dialog, which) -> {

                    }));

            builder.show();


            return true;
        });






    }


    private void setAnimation(View viewToAnimate,int position){
       if(position>lastPositon) {
           Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

           viewToAnimate.startAnimation(slideIn);
           lastPositon=position;
       }


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

       ImageView image;
       TextView name,number;

       LinearLayout llRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.txtContactName);
            number=itemView.findViewById(R.id.txtNumber);
            image=itemView.findViewById(R.id.imageContact);

            llRow=itemView.findViewById(R.id.llrow);
        }
    }

}
