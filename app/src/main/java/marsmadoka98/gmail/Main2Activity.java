package marsmadoka98.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//Activity to fetching data from firebase with use of recycler view and adapter
public class Main2Activity extends AppCompatActivity {
private RecyclerView recyclerview;
private DatabaseReference mDatabase;
ProgressDialog progressDialog;
List<Details> list=new ArrayList<>();
RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerview=findViewById(R.id.myrecyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(Main2Activity.this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Details details = dataSnapshot.getValue(Details.class);
                    list.add(details);
                }

                adapter=new RecyclerAdapter(Main2Activity.this,list);
                recyclerview.setAdapter(adapter);
                progressDialog.dismiss();
                Toast.makeText(Main2Activity.this, "data loaded successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
progressDialog.dismiss();
            }
        });





    }


}
