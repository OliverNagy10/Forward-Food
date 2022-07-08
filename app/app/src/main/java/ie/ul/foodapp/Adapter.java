package ie.ul.foodapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private ArrayList<String> data;
    private ArrayList<String> desc;

    public Adapter(Context context, ArrayList<String> data, ArrayList<String> desc){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.desc = desc;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_card,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String title = data.get(i);
        String offer = desc.get(i);
        viewHolder.textTitle.setText(title);
        viewHolder.textDescription.setText(offer);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textDescription;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            //set onclicker listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), Details.class);
                    intent.putExtra("title", data.get(getAdapterPosition()));
                    intent.putExtra("description", desc.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);

                }
            });

            textTitle = itemView.findViewById(R.id.card1);
            textDescription = itemView.findViewById(R.id.card1Description);
        }
    }
}
