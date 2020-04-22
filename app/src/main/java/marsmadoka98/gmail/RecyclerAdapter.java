package marsmadoka98.gmail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {
Context context;
List<Details> MainList;

    public RecyclerAdapter(Context context, List<Details> TempList){
        this.MainList=TempList;
        this.context=context;

    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity2_recycler_holder,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
    Details details=MainList.get(position);
    holder.mytitle.setText(details.getTitle());
    holder.mydescription.setText(details.getDescription());
    //holder.mImage.setImageResource(Integer.parseInt(details.getImage()));



    }

    @Override
    public int getItemCount() {
        return MainList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

   public TextView mytitle;
   public TextView mydescription;
   //public ImageView mImage;
        public ViewHolder(View itemView){
            super(itemView);
            mytitle =itemView.findViewById(R.id.text_view1);
           // mytitle.setText(title);
            mydescription=itemView.findViewById(R.id.text_view2);
           // mImage=itemView.findViewById(R.id.image_view);
        }
    }

}
