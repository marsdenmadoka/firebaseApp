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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//login Activity
public class LogInActivity extends AppCompatActivity {
    private EditText mEmail;
    private  EditText mPassword;
    private Button BtnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");//this is the child we created when signup to store signup crediatials we MUST use the child
        mProgress=new ProgressDialog(this);
        mEmail= findViewById(R.id.loginEmail);
        mPassword=findViewById(R.id.loginPassword);

        BtnLogin=findViewById(R.id.btnLogIn);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin();
            }
        });

    }

    public void CheckLogin(){
        mProgress.setMessage("signin user....");
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        CheckUserExist();

                    }else{
                            mProgress.dismiss();
                        Toast.makeText(LogInActivity.this,"user does not exsist enter valid Email/password or check your connection",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }else{

            Toast.makeText(LogInActivity.this,"name and password cannot be blank!",Toast.LENGTH_LONG).show();
        }
    }

    public void CheckUserExist(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.hasChild(user_id)){
            Intent mainIntent=new Intent(LogInActivity.this,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);

        }else{
            Toast.makeText(LogInActivity.this,"please setup an account,",Toast.LENGTH_LONG).show();
        }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}

