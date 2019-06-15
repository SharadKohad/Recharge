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
import com.logicaltech.gamerecharge.UpcomeingCricketContestActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
        holder.TV_Team1.setText(account_model.getTeam1().toUpperCase());
        holder.TV_Team2.setText(account_model.getTeam2().toUpperCase());
        holder.TV_Matches.setText(""+account_model.getTeam1()+" Vs "+account_model.getTeam2());
        holder.TV_Timing.setText(""+account_model.getDate());

        Picasso.with(mContext).load(account_model.getTeamflag1()).into(holder.IV_Team1);
        Picasso.with(mContext).load(account_model.getTeamflag2()).into(holder.IV_Team2);

        holder.LL_Select_Player.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, UpcomeingCricketContestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("m_id",account_model.getUnique_id());
                intent.putExtra("gtype","10");
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
        CircleImageView IV_Team1,IV_Team2;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Team1 = (TextView) itemView.findViewById(R.id.textview_team1);
            TV_Team2 = (TextView) itemView.findViewById(R.id.textview_team2);
            TV_Matches = (TextView) itemView.findViewById(R.id.textview_current_matches);
            TV_Timing = (TextView)itemView.findViewById(R.id.textview_match_time);
            LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.ll_select_player);
            IV_Team1 = (CircleImageView) itemView.findViewById(R.id.iv_team1);
            IV_Team2 = (CircleImageView) itemView.findViewById(R.id.iv_team2);

        }
    }
}
