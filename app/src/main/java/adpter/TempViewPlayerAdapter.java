package adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PlayerModel;

public class TempViewPlayerAdapter extends RecyclerView.Adapter<TempViewPlayerAdapter.Holder>
{
    private Context context;
    private ArrayList<PlayerModel> list;
    public TempViewPlayerAdapter(Context context, ArrayList<PlayerModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public TempViewPlayerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.child_temp_viewplayerlist, viewGroup, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TempViewPlayerAdapter.Holder holder, final int position)
    {
        final PlayerModel playerModel = list.get(position);
        holder.txtPlayerName.setText(playerModel.getName());
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView txtPlayerName;
        CircleImageView CIM_player;
        public Holder(@NonNull View itemView)
        {
            super(itemView);
            txtPlayerName = itemView.findViewById(R.id.textview_temp_player_name);
       //     CIM_player = itemView.findViewById(R.id.imageView_select_player);
        }
    }
}
