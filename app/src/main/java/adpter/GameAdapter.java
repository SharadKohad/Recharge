package adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.ContestListActivity;
import com.logicaltech.gamerecharge.MainActivity;
import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.UpcomeingMatchesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.GameModel;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.RecyclerViewHolder>
{
    public ArrayList<GameModel> orderList;
    public Context mContext;
    public GameAdapter(ArrayList<GameModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_game_type, parent, false);
        return new GameAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final GameModel account_model = orderList.get(position);
        holder.TV_Game_Name.setText(account_model.getGame_type());
        Picasso.with(mContext).load(account_model.getLogo()).into(holder.IV_Game_Logo);
        holder.TV_JoinMember.setText(""+account_model.getJoinMember());
        holder.TV_Winner.setText("Won by "+account_model.getWinner()+" player");

        holder.LL_game_type.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (account_model.getSrno().equals("10"))
                {
                    Intent intent = new Intent(mContext, UpcomeingMatchesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("gtype",account_model.srno);
                    mContext.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mContext, ContestListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("gtype",account_model.srno);
                    mContext.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Game_Name,TV_JoinMember,TV_Winner;
        ImageView IV_Game_Logo;
        LinearLayout LL_game_type;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Game_Name = (TextView) itemView.findViewById(R.id.tv_game_name);
            TV_JoinMember = (TextView) itemView.findViewById(R.id.txtjoinmember);
            TV_Winner = (TextView) itemView.findViewById(R.id.txtwinner);
            IV_Game_Logo = (ImageView) itemView.findViewById(R.id.img_game_logo);
            LL_game_type = (LinearLayout) itemView.findViewById(R.id.linear_layout_jump_fish_home);
        }
    }
}
