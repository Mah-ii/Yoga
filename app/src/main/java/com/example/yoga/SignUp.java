package com.example.yoga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    private EditText editemail,editpass,editconfirmpass,editname,editdateofbirth,editmobile;
    private Button signupbutton;
    private DatePickerDialog picker;
    private RadioGroup gender;
    private RadioButton selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();
        this.setTitle("Sign Up");
        editname=(EditText) findViewById(R.id.fullname);
        editdateofbirth=(EditText) findViewById(R.id.dateofbirth);
        editmobile=(EditText) findViewById(R.id.mobile);
        editemail=(EditText) findViewById(R.id.signupemail);
        editpass=(EditText) findViewById(R.id.signuppassword);
        editconfirmpass=(EditText) findViewById(R.id.confirmpassword);
        signupbutton=(Button) findViewById(R.id.signupbutton);

        gender=findViewById(R.id.gender);
        gender.clearCheck();

        //date of birth
        editdateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                picker=new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        editdateofbirth.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },year,month,day);
                picker.show();


            }
        });


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

    @Override
    public void onClick(View view) {
        userRegister();
    }

    private void userRegister() {
        String name=editname.getText().toString().trim();
        String age=editdateofbirth.getText().toString().trim();
        String mobileNumber=editmobile.getText().toString().trim();
        String email=editemail.getText().toString().trim();
        String password=editpass.getText().toString().trim();
        String confirmpassword=editconfirmpass.getText().toString().trim();

        int selectGenderID=gender.getCheckedRadioButtonId();
        selectedGender=findViewById(selectGenderID);

        String genderText=selectedGender.getText().toString();




        //checking validity of email
        if(name.isEmpty())
        {
            editname.setError("Full name is required");
            editname.requestFocus();
            return;
        }
        if(age.isEmpty())
        {
            editdateofbirth.setError("Age is required");
            editdateofbirth.requestFocus();
            return;
        }
        if(mobileNumber.isEmpty())
        {
            editmobile.setError("Mobile number is required");
            editmobile.requestFocus();
            return;
        }
        if (mobileNumber.length()!=11)
        {
            Toast.makeText(SignUp.this,"Please re-enter your mobile number",Toast.LENGTH_LONG).show();
            editmobile.requestFocus();
            return;
        }
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
        if (gender.getCheckedRadioButtonId()==-1) {
            Toast.makeText(SignUp.this, "Please select your gender", Toast.LENGTH_LONG).show();
            gender.requestFocus();
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
        if(!password.equals(confirmpassword))
        {
            editconfirmpass.setError("Please enter same password");
            editconfirmpass.requestFocus();
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    UserInfo userInfo=new UserInfo(name,age,email,genderText,mobileNumber);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignUp.this,"User registered successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,SignIn.class));
                            }
                            else {
                                Toast.makeText(SignUp.this,"Registration error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(SignUp.this,"Registration error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}