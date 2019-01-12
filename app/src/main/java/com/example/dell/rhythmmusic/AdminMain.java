package com.example.dell.rhythmmusic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminMain extends AppCompatActivity {
    private static final int AUDIO_REQUEST = 20;
    private Button uploadbtn, requestsbtn;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();

    private HashMap<Uri, String> songList = new HashMap<>();
    private Bundle bundle;
    private EditText ecat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        setTitle("Admin");

        uploadbtn = findViewById(R.id.uploadbtn);
        requestsbtn = findViewById(R.id.requestsbtn);
        ecat = findViewById(R.id.ecat);


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = ecat.getText().toString();
                if(c.isEmpty()){
                    Toast.makeText(getBaseContext(), "Select category First", Toast.LENGTH_SHORT).show();
                }
                else{
                    showFileChooser();
                }


            }
        });
        requestsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.aframe,new SongRequests()).addToBackStack(null).commit();
                startActivity(new Intent(getBaseContext(),Requests.class));
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {

            if (data.getData() != null) {
                Uri filePath = data.getData();

                try {
//
                   /* songList.put(filePath, getFileName(filePath));

                    bundle = new Bundle();
                    bundle.putSerializable("songMap", songList);
*/

                    uploadFile(filePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();



                    uploadFile(fileUri);
                }

            }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getBaseContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    result = result.substring(0, result.indexOf("."));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void uploadFile(final Uri uri) {
        final String category = ecat.getText().toString();
            String str = getFileName(uri);
            final DatabaseReference dr1 = database.getReference("audios/").child(category).child(str);

            if (dr1 != null) {
                Toast.makeText(this, getFileName(uri), Toast.LENGTH_SHORT).show();


                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final DatabaseReference rsong = database.getReference("audios");
                final StorageReference rref = storageReference.child("audios/").child(category).child(getFileName(uri));

                rref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                rref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String url = uri.toString();

                                        AudioContent audioContent = new AudioContent();
                                        audioContent.setSongName(rref.getName());
                                        audioContent.setCategory(category);
                                        audioContent.setSongUrl(url);
                                        audioContent.setId(rsong.push().getKey());

                                        dr1.setValue(audioContent);
                                        progressDialog.dismiss();
                                    }
                                });


                                Toast.makeText(getApplicationContext(), getFileName(uri) + " Uploaded ", Toast.LENGTH_LONG).show();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })

                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("Uploaded " + getFileName(uri) + " " + ((int) progress) + " %...");
                            }
                        });
            } else {
                Toast.makeText(this, "there is not any file", Toast.LENGTH_SHORT).show();
            }



    }
    @Override
    public void onBackPressed(){
        Intent it = new Intent(getBaseContext(),MusicActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

}