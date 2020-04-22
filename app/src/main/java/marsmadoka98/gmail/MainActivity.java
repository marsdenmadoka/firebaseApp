package marsmadoka98.gmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtn;
    private EditText Edtitle;
    private EditText Eddesc;
    private Button btnPost;
    private Uri mImgUri = null;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorage = FirebaseStorage.getInstance().getReference();
        imgbtn=findViewById(R.id.imageButton);
        Edtitle=findViewById(R.id.EditTxt1);
        Eddesc=findViewById(R.id.EditTxt2);
        btnPost=findViewById(R.id.buttonB);

        mProgress=new ProgressDialog(this);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImgUri = data.getData();
            imgbtn.setImageURI(mImgUri);

        }

    }
    @Override
   public  boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void startPosting(){
    mProgress.setMessage("posting to firebase....");
    mProgress.show();
        String titlevalue=Edtitle.getText().toString().trim();
        String descvalue=Eddesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titlevalue) && !TextUtils.isEmpty(descvalue) && mImgUri != null){

            final StorageReference filepath = mStorage.child("Blog_Images").child(mImgUri.getLastPathSegment());
        filepath.putFile(mImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
              filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      final Uri downloadUrl = uri;
                      mProgress.dismiss();
                      Toast.makeText(MainActivity.this, "file uploaded successful!", Toast.LENGTH_SHORT).show();

                  }
              });


            }
        });

        }


    }
}