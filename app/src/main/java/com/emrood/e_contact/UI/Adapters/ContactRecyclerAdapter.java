package com.emrood.e_contact.UI.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Listeners.ContactClickListener;
import com.emrood.e_contact.UI.Listeners.ContactLongClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {


    private LayoutInflater inflater;
    ArrayList<Contact> contacts;
    ArrayList<Contact> contactFiltered;
    Context mContext;
    ContactClickListener mContactClickListener;
    ContactLongClickListener mContactLongClickListener;
    private int lastPosition = -1;

    public ContactRecyclerAdapter(ArrayList<Contact> contacts, Context mContext) {
        this.contacts = contacts;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_contact, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        Contact c = contacts.get(i);
        String firstname = c.getFirst_name();
        String lastnma = c.getLast_name();
        String phone = " ";
        String email = " ";
        if(!TextUtils.isEmpty(c.getCellular_phone())){
            phone = c.getCellular_phone();
        }else if(!TextUtils.isEmpty(c.getWork_phone())){
            phone = c.getWork_phone();
        }else if(!TextUtils.isEmpty(c.getOther_phone())){
            phone = c.getOther_phone();
        }
        if(!TextUtils.isEmpty(c.getPersonal_email())){
            email = c.getPersonal_email();
        }else if(!TextUtils.isEmpty(c.getWork_email())){
            email = c.getWork_email();
        }else if(!TextUtils.isEmpty(c.getOther_email())){
            email = c.getOther_email();
        }

        if(TextUtils.equals(lastnma, firstname)){
            viewHolder.tvName.setText(firstname);
        }else{
            viewHolder.tvName.setText(lastnma + " " + firstname);
        }
        viewHolder.tvEmail.setText(email);
        viewHolder.tvPhone.setText(phone);
        if(c.getIsFav()){
            viewHolder.ivContactProperties.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_yellow_600_24dp));
        }else if(c.getIsSecret()){
            viewHolder.ivContactProperties.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_visibility_red_500_24dp));
        }else{
            viewHolder.ivContactProperties.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(c.getPhoto())){
            try{
//                Bitmap profil = BitmapFactory.decodeFile(c.getPhoto());
//                viewHolder.tvPhoto.setImageBitmap(profil);
                Uri uri = Uri.fromFile(new File(c.getPhoto()));
                Glide.with(mContext).load(uri).centerCrop().into(viewHolder.tvPhoto);
            }catch (Exception e){

            }
//            Glide.with(mContext).load("").into(viewHolder.tvPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName, tvPhone, tvEmail;
        CircleImageView tvPhoto;
        ImageView ivContactProperties;
        public RelativeLayout foreground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvContactName);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumber);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhoto = itemView.findViewById(R.id.ivContactPhoto);
            foreground = itemView.findViewById(R.id.foreground);
            ivContactProperties = itemView.findViewById(R.id.ivContactProperty);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mContactClickListener != null) {
                mContactClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mContactLongClickListener != null){
                mContactLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }


    public void makeContactFav(int pos){
        contacts.get(pos).setIsFav(true);
        notifyItemChanged(pos);
    }

    public void makeContactSecret(int pos){
        contacts.get(pos).setIsSecret(true);
        notifyItemChanged(pos);
    }


    public void removeContactFromFav(int pos){
        contacts.get(pos).setIsFav(false);
        notifyItemChanged(pos);
    }

    public void removeContactFromSecret(int pos){
        contacts.get(pos).setIsSecret(false);
        notifyItemChanged(pos);
    }

    public Contact getContact(int id){

        try{
            return contacts.get(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setContactClickListener(ContactClickListener contactClickListener){
        this.mContactClickListener = contactClickListener;
    }

    public void setmContactLongClickListener(ContactLongClickListener contactLongClickListener){
        this.mContactLongClickListener = contactLongClickListener;
    }

    public void setFilter(ArrayList<Contact> arrayList) {
        //arrayList = new ArrayList<>(); // remove this line
        contacts.clear(); // add this so that it will clear old data
        contacts.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void newAddeddata(ArrayList<Contact> contactList){

        if (contactList != null) {
            contacts.addAll(contactList);
        }

        notifyDataSetChanged();
    }

    public void newAddeddata(List<Contact> contactList){

        if (contactList != null) {
            contacts.addAll(contactList);
            notifyDataSetChanged();
        }
    }

    public void removeContact(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }


    public String returnNumber(int position){
        Contact c = contacts.get(position);
        if(!TextUtils.isEmpty(c.getCellular_phone())){
            return c.getCellular_phone();
        }else if(!TextUtils.isEmpty(c.getWork_phone())){
            return c.getWork_phone();
        }else if(!TextUtils.isEmpty(c.getOther_phone())){
            return c.getOther_phone();
        }

        return null;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
