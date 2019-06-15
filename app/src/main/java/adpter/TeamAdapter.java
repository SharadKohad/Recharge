package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logicaltech.gamerecharge.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PlayerModel;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.Holder>
{
    private Context context;
    private List<PlayerModel> list;
    private TeamAdapterInterface listener;
    public TeamAdapter(Context context, List<PlayerModel> list, TeamAdapterInterface listener)
    {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    public void updateAdapter(List<PlayerModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public interface TeamAdapterInterface
    {
        void getPosition(int position);
    }
    @NonNull
    @Override
    public TeamAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.child_player_selection, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.Holder holder, final int position)
    {
        final PlayerModel playerModel = list.get(position);
        holder.txtPlayerName.setText(playerModel.getName());
        if (playerModel.isIs_selected())
        {
         //   holder.txtPlayerName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.RL_Background.setBackgroundColor(Color.parseColor("#e6e6e6"));
            Picasso.with(context).load(R.drawable.minus).into(holder.CIM_player);
        }
        else
        {
          //  holder.txtPlayerName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            holder.RL_Background.setBackgroundColor(Color.parseColor("#ffffff"));
            Picasso.with(context).load(R.drawable.plus).into(holder.CIM_player);
        }
        holder.CIM_player.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.getPosition(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtPlayerName;
        CircleImageView CIM_player;
        RelativeLayout RL_Background;

        public Holder(@NonNull View itemView)
        {
            super(itemView);
            txtPlayerName = itemView.findViewById(R.id.textview_player_name);
            CIM_player = itemView.findViewById(R.id.imageView_select_player);
            RL_Background = itemView.findViewById(R.id.rl_selectplayer);
        }
    }
}