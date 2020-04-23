package marsmadoka98.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNamefield;
    private EditText mEmailfield;
    private EditText mPasswordfield;
    private Button mSignButton;
    private ProgressDialog mProgress;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    mAuth=FirebaseAuth.getInstance();

        mNamefield=findViewById(R.id.editUsername);
        mEmailfield=findViewById(R.id.editEmail);
        mPasswordfield=findViewById(R.id.editPassword);

        mSignButton=findViewById(R.id.btnSignUp);
        mProgress=new ProgressDialog(this);
        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartRegister();

            }
        });
    }

    public void StartRegister(){
        mProgress.setMessage("creating user....");
      String name=mNamefield.getText().toString().trim();
       String email= mEmailfield.getText().toString().trim();
        String password=mPasswordfield.getText().toString().trim();
if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
mProgress.show();
    mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this,"your have Successful Registered",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(RegisterActivity.this,"Register failed!! Connect to the internet and try again",Toast.LENGTH_SHORT).show();
                }

            }
        });

}else{
    Toast.makeText(RegisterActivity.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
}

    }
}