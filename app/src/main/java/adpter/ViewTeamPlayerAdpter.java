package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PlayerModel;

public class ViewTeamPlayerAdpter extends RecyclerView.Adapter<ViewTeamPlayerAdpter.Holder>
{
    private Context context;
    private List<PlayerModel> list;
    public ViewTeamPlayerAdpter(Context context, List<PlayerModel> list)
    {
        this.context = context;
        this.list = list;
    }
    public void updateAdapter(List<PlayerModel> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public ViewTeamPlayerAdpter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.child_view_teamplayer_list, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTeamPlayerAdpter.Holder holder, final int position)
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
            txtPlayerName = itemView.findViewById(R.id.textview_player_name);
            CIM_player = itemView.findViewById(R.id.imageView_select_player);
        }
    }
}
