package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 10/05/2017.
 */

public class ResetPassword extends BaseActivity {

    TextView judulTv;
    EditText resetPassword;
    Button savePassword;
    String passwordBaru;
    Toolbar toolbar;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        user = FirebaseAuth.getInstance().getCurrentUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        judulTv = (TextView) findViewById(R.id.textView9);
        resetPassword = (EditText) findViewById(R.id.editPassword);
        savePassword = (Button) findViewById(R.id.savePassword);

        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        judulTv.setText("Password Baru");
        judulTv.setTypeface(face);
        savePassword.setText("SIMPAN");
        savePassword.setTypeface(face);

        savePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                passwordBaru = resetPassword.getText().toString();
                Log.d("TAGES", passwordBaru);

                // updating password
                user.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this,"Update password success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Update password failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}