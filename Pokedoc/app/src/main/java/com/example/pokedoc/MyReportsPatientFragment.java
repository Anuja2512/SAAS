package com.example.pokedoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;


public class MyReportsPatientFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button addPatients;
    String myuid, docuid, med, dosage,description;
    LinearLayout mainlayout;
    Button btn_filePicker;
    Intent myfileIntent;
    EditText editPDFName;
    Button btn_upload;
    Button viewPDFFiles;
    ArrayList<String> FileNames = new ArrayList<>();
    StorageReference storageReference;
    DatabaseReference databaseReference, reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_myreports_pat, container, false);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reports");
        editPDFName = (EditText) rootview.findViewById(R.id.txt_pdfName);
        btn_upload = (Button) rootview.findViewById(R.id.btn_Upload);
        btn_filePicker = (Button) rootview.findViewById(R.id.btn_Upload);
        viewPDFFiles=(Button) rootview.findViewById(R.id.btn_viewFiles);
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reports");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    if (snapshot.exists()) {
                        for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                            CollectUserNames((Map<String, Object>) snapshot2.getValue());
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Fetching UserNames..", Toast.LENGTH_SHORT).show();
            }
        });
        btn_filePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myfileIntent=new Intent(Intent.ACTION_GET_CONTENT);
                myfileIntent.setType("*/*");
                startActivityForResult(myfileIntent,10);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editPDFName.getText().toString()) ){
                editPDFName.setError("Please Enter File Name");
                editPDFName.requestFocus();

                }
                if(FileNames.contains(editPDFName.getText().toString()))
                {
                    editPDFName.setError("File name already exists, Please enter another file Name");
                    editPDFName.requestFocus();
                }
                else{
                    selectPDFFile();
                }

            }
        });

        viewPDFFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ViewAllFiles.class);
                startActivity(i);
            }
        });

        mainlayout=rootview.findViewById(R.id.mainlayout);
        myuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        return rootview;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            uploadPDFFile(data.getData());
        }
    }

    private void selectPDFFile() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url=uri.getResult();
                        UploadFiless uploadFile=new UploadFiless(editPDFName.getText().toString(), url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadFile);
                        Toast.makeText(getActivity(),"File Uploaded",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        editPDFName.setText("");
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0* taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded:"+(int)progress+"%");
            }
        });
    }
    private void CollectUserNames(Map<String, Object> Surveys) {
        for (Map.Entry<String, Object> entry : Surveys.entrySet()) {
            if (entry.getKey().toString().equals("name"))
            {
                FileNames.add(entry.getValue().toString());
            }

        }
    }
}
