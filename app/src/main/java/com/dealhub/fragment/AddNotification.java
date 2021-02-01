package com.dealhub.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dealhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;


public class AddNotification extends Fragment {

    AppCompatImageView notification_image;
    AppCompatEditText description;
    AppCompatButton send;
    private static final int NOTIFICATIONIMAGE = 102;
    private Uri URInotificationimg;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private StorageTask uplaodTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notification, container, false);
        notification_image=view.findViewById(R.id.pro_pic);
        description=view.findViewById(R.id.description);
        send=view.findViewById(R.id.post);

        storageReference = FirebaseStorage.getInstance().getReference("Shop Notitfications");
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop Notitfications");

        notification_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, NOTIFICATIONIMAGE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_description=description.getText().toString();
                if (str_description.isEmpty()){
                    Toast.makeText(getActivity(), "Desciption cannot be empty", Toast.LENGTH_SHORT).show();
                }else if(URInotificationimg==null){
                    Toast.makeText(getActivity(), "Image is required", Toast.LENGTH_SHORT).show();
                }else{
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Adding your notification");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);
                    String key = databaseReference.push().getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("description", str_description);
                    hashMap.put("id", key);
                    databaseReference.child(key).setValue(hashMap);
                    uploadImage(key);
                }
            }
        });

        return view;
    }

    private void uploadImage(final String key) {
        final StorageReference filerefrenceoffer = storageReference.child("" + key).child("notificationimg.jpg");
        uplaodTask = filerefrenceoffer.putFile(URInotificationimg);

        uplaodTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filerefrenceoffer.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String myUrl = null;
                    if (downloadUri != null) {
                        myUrl = downloadUri.toString();
                    }

                    DatabaseReference reference = databaseReference.child("" + key);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imgurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Notification Added Successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload offer image", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        URInotificationimg=null;
        notification_image.setBackgroundResource(R.drawable.add);
        notification_image.setImageResource(R.drawable.button_black);
        description.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case NOTIFICATIONIMAGE:
                    URInotificationimg = data.getData();
                    notification_image.setImageURI(URInotificationimg);
                    break;

            }
    }
}