package marsmadoka98.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//activity to post data to firebase
public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtn;
    private EditText Edtitle;
    private EditText Eddesc;
    private Button btnPost;
    private Uri mImgUri = null;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListerner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //checkin if the user is SignedIn
        mAuth=FirebaseAuth.getInstance();
        mAuthListerner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null);//this mean if our user is not logged in

                Intent loginIntent = new Intent(MainActivity.this,RegisterActivity.class);
              loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        };

    mAuth.addAuthStateListener(mAuthListerner);
        mStorage = FirebaseStorage.getInstance().getReference();//Storage reference
        mDatabase= FirebaseDatabase.getInstance().getReference().child("profile"); //databaseReference
       mDatabase.keepSynced(true);
        imgbtn=findViewById(R.id.imageButton);
        Edtitle=findViewById(R.id.EditTxt1);
        Eddesc=findViewById(R.id.EditTxt2);
        btnPost=findViewById(R.id.buttonB);

        mProgress=new ProgressDialog(this);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //open camera/gallery
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() { //posting data image to firebase
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){ //getting the image selected
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImgUri = data.getData();
            imgbtn.setImageURI(mImgUri);

        }

    }

    public void startPosting(){ //posting method when btnOnClick
    mProgress.setMessage("posting to firebase....");

        final String titlevalue=Edtitle.getText().toString().trim();
        final String descvalue=Eddesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titlevalue) && !TextUtils.isEmpty(descvalue) && mImgUri != null){
            mProgress.show();
            final StorageReference filepath = mStorage.child("Blog_Images").child(mImgUri.getLastPathSegment()); //store the images under in Blog_Images folder in STORAGE
        filepath.putFile(mImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
              filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      final Uri downloadUrl = uri;

                      DatabaseReference newPost=mDatabase.push();//after storage in blog_images push the files to the realtime database child called profile giving a random id
                      newPost.child("title").setValue(titlevalue);
                      newPost.child("description").setValue(descvalue);
                      newPost.child("image").setValue(downloadUrl.toString());

                      mProgress.dismiss();
                      Toast.makeText(MainActivity.this, "file uploaded successful!", Toast.LENGTH_SHORT).show();
                      //return back to activity2
                      startActivity(new Intent(MainActivity.this,Main2Activity.class));
                  }
              });
            }
        });
        }else{

            Toast.makeText(MainActivity.this," please insert values to upload",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this,Main2Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}