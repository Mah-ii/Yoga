package com.example.yoga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener{

    private EditText editemail, editpass;
    private Button signinbutton;
    private TextView signuptextview,forgotpassword;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.setTitle("Sign In");
        editemail = (EditText) findViewById(R.id.signinemail);
        editpass = (EditText) findViewById(R.id.signinpassword);
        signinbutton = (Button) findViewById(R.id.signinbutton);
        signuptextview = (TextView) findViewById(R.id.signuptext);
        forgotpassword=(TextView)findViewById(R.id.forgetpasstext);

        signuptextview.setOnClickListener(this);
        signinbutton.setOnClickListener(view ->
                loginUser()
        );
        forgotpassword.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void loginUser() {

        String email=editemail.getText().toString().trim();
        String password=editpass.getText().toString().trim();

        //checking validity of email
        if(email.isEmpty())
        {
            editemail.setError("Enter an email address");
            editemail.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editemail.setError("Enter a valid email address");
            editemail.requestFocus();
            return;
        }
        //checking validity of password
        if(password.isEmpty())
        {
            editpass.setError("Enter a password");
            editpass.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editpass.setError("Minimum length of a password should be 6");
            editpass.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    //email varification
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified())
                    {
                        Toast.makeText(SignIn.this,"User logged in successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(SignIn.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(SignIn.this,"Login error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.signinbutton:

                break;
            case R.id.signuptext:
                Intent intent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            case R.id.forgetpasstext:
                startActivity(new Intent(this,ForgotPassword.class));

        }

    }
}