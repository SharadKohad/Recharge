package adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.ContestListActivity;
import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.SelectPlayerActivity;

import java.util.ArrayList;
import model.Matches_Model;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.RecyclerViewHolder>
{
    public ArrayList<Matches_Model> orderList;
    public Context mContext;
    public MatchesAdapter(ArrayList<Matches_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_matches_list, parent, false);
        return new MatchesAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final Matches_Model account_model = orderList.get(position);
        holder.TV_Team1.setText(account_model.getTeam1());
        holder.TV_Team2.setText(account_model.getTeam2());
        holder.TV_Matches.setText(""+account_model.getTeam1()+" Vs "+account_model.getTeam2());
        holder.TV_Timing.setText(""+account_model.getDate());

        holder.LL_Select_Player.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, SelectPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("match_unique_id",account_model.getUnique_id());
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TV_Team1,TV_Team2,TV_Matches,TV_Timing;
        LinearLayout LL_Select_Player;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Team1 = (TextView) itemView.findViewById(R.id.textview_team1);
            TV_Team2 = (TextView) itemView.findViewById(R.id.textview_team2);
            TV_Matches = (TextView) itemView.findViewById(R.id.textview_current_matches);
            TV_Timing = (TextView)itemView.findViewById(R.id.textview_match_time);
            LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.ll_select_player);
        }
    }
}
