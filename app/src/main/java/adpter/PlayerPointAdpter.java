package adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PlayerModel;
import model.PlayerPointModel;

public class PlayerPointAdpter extends RecyclerView.Adapter<PlayerPointAdpter.Holder>
{
    private Context context;
    private List<PlayerPointModel> list;
    public PlayerPointAdpter(Context context, List<PlayerPointModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public PlayerPointAdpter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.child_view_teamplayer_list, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerPointAdpter.Holder holder, final int position)
    {
        final PlayerPointModel playerModel = list.get(position);
        holder.txtPlayerName.setText(playerModel.getName());
        holder.txtPlayerPoint.setText(""+playerModel.getPoint());
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView txtPlayerName,txtPlayerPoint;
        CircleImageView CIM_player;
        public Holder(@NonNull View itemView)
        {
            super(itemView);
            txtPlayerName = itemView.findViewById(R.id.textview_temp_player_name);
            txtPlayerPoint = itemView.findViewById(R.id.textview_temp_player_point);
            CIM_player = itemView.findViewById(R.id.imageView_select_player);
        }
    }
}
