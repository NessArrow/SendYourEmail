package com.proizvodstvo.sendyouremail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;

    private static final int REQUEST_CODE = 15;
    private static final int REQUEST_PICTURE_CODE = 20;

    private TextView toField;
    private TextView subjectField;
    private TextView messageField;

    private final Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toField = findViewById(R.id.tv_to);
        subjectField = findViewById(R.id.tv_subject);
        messageField = findViewById(R.id.tv_message);
        imageView = findViewById(R.id.image);

        Button details = findViewById(R.id.details_btn);
        details.setOnClickListener(view -> {
//            Intent intent = new Intent(this, SecondActivity.class);
//            startActivity(intent);

            Intent intent = new Intent(this, SecondActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        });

        Button cameraBtn = findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                startActivityForResult(takePictureIntent, REQUEST_PICTURE_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Невозможно открыть камеру", Toast.LENGTH_LONG).show();
            }
        });

        Button mSendButton = findViewById(R.id.send_btn);
        mSendButton.setOnClickListener(view -> {
            try {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.setType("message/rfc822");

                mail.putExtra(Intent.EXTRA_EMAIL, new String[]{map.get(Intent.EXTRA_EMAIL)});
                mail.putExtra(Intent.EXTRA_SUBJECT, map.get(Intent.EXTRA_SUBJECT));
                mail.putExtra(Intent.EXTRA_TEXT, map.get(Intent.EXTRA_TEXT));
                mail.putExtra(Intent.EXTRA_STREAM, imageUri);

                startActivity(Intent.createChooser(mail, "Choose email client"));

            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(this, "There are no email clients installed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    map.put(Intent.EXTRA_EMAIL, data.getStringExtra(Intent.EXTRA_EMAIL));
                    map.put(Intent.EXTRA_SUBJECT, data.getStringExtra(Intent.EXTRA_SUBJECT));
                    map.put(Intent.EXTRA_TEXT, data.getStringExtra(Intent.EXTRA_TEXT));

                    toField.setText(map.get(Intent.EXTRA_EMAIL));
                    subjectField.setText(map.get(Intent.EXTRA_SUBJECT));
                    messageField.setText(map.get(Intent.EXTRA_TEXT));
                }
            } else {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == REQUEST_PICTURE_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "screenshot.png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                imageUri = Uri.fromFile(outputFile);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}