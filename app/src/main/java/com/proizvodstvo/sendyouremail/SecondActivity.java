package com.proizvodstvo.sendyouremail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button confirm = findViewById(R.id.confirm_btn);
        confirm.setOnClickListener(view -> {
            EditText eAddress = findViewById(R.id.et_email_address);
            EditText eSubject = findViewById(R.id.et_email_subject);
            EditText eText = findViewById(R.id.et_email_text);

            String s1 = eAddress.getText().toString();
            String s2 = eSubject.getText().toString();
            String s3 = eText.getText().toString();

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_EMAIL, s1);
            intent.putExtra(Intent.EXTRA_SUBJECT, s2);
            intent.putExtra(Intent.EXTRA_TEXT, s3);

            setResult(RESULT_OK, intent);
            finish();
        });

        Button cancel = findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}