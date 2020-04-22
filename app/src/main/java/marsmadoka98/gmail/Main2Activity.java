package marsmadoka98.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {
private RecyclerView recyclerview;
private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile");
        recyclerview=findViewById(R.id.myrecyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<RecyclerAdapter,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RecyclerAdapter, ViewHolder>(
                RecyclerAdapter.class,
                R.layout.activity2_recycler_holder,
                ViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RecyclerAdapter model) {

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ViewHolder(View itemView){
            super(itemView);
            itemView=mView;

        }
        public void setTitle(String title){

            TextView mytitle =mView.findViewById(R.id.text_view1);
            mytitle.setText(title);
        }
        public void setDesc(String description){
            TextView mydesc=mView.findViewById(R.id.text_view2);
            mydesc.setText(description);

        }

    }

}
