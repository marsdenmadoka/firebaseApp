package marsmadoka98.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNamefield;
    private EditText mEmailfield;
    private EditText mPasswordfield;
    private Button mSignButton;
    private Button SignIn;
    private ProgressDialog mProgress;
     private FirebaseAuth mAuth;
     private DatabaseReference mDatabase;
     private SignInButton mGooglebtn;
     private  static  int RC_SIGN_IN=1;
     private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
     mAuth=FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.keepSynced(true);

        mNamefield=findViewById(R.id.editUsername);
        mEmailfield=findViewById(R.id.editEmail);
        mPasswordfield=findViewById(R.id.editPassword);
        mGooglebtn=findViewById(R.id.googlebtn);

        mSignButton=findViewById(R.id.btnSignUp);
        mProgress=new ProgressDialog(this);
        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartRegister();

            }
        });

        SignIn=findViewById(R.id.btnLogiN);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        /*...google signup... */
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
             Toast.makeText(getApplicationContext(),"failed to LOgin with google",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mGooglebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("getting account.....");
                mProgress.show();
                signIn();
            }
        });

    }
    //still google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            mProgress.setMessage("start sign in....");
            mProgress.show();
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }else {
            mProgress.dismiss();
            Toast.makeText(getApplicationContext(),"Authentication failed check your account",Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Authentication Failed please check your connection.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }



    public void StartRegister(){
        mProgress.setMessage("creating user....");
     final String name=mNamefield.getText().toString().trim();
   final  String email= mEmailfield.getText().toString().trim();
       final String password=mPasswordfield.getText().toString().trim();
if((!TextUtils.isEmpty(name) && name.length() > 3) && (!TextUtils.isEmpty(email)  && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())&& (!TextUtils.isEmpty(password) && password.length() > 5)){
    mProgress.show();
    mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String user_id=mAuth.getCurrentUser().getUid();//  final String user_id = mAuth.getCurrentUser().getUid();
                     DatabaseReference current_user=mDatabase.child(user_id);//create child userid
                    current_user.child("name").setValue(name);
                    current_user.child("image").setValue("default");

                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this,"your have Successful Registered",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this,"Register failed!! Connect to the internet and try again, password must be more than four characters",Toast.LENGTH_LONG).show();
                }

            }
        });

}else if(name.length() <= 3){
    mNamefield.setError("at least 4 characters");
}else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
    mEmailfield.setError("enter a valid email address");
}else if(password.length() <= 5){
    mPasswordfield.setError("between 6 and 10 characters");
}else{
    Toast.makeText(RegisterActivity.this, "please fill all the fields ,cannot be empty!!", Toast.LENGTH_SHORT).show();
}

    }
}
