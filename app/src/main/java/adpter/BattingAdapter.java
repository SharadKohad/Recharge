package adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;

import java.util.ArrayList;

import model.Batting_Model;

public class BattingAdapter extends RecyclerView.Adapter<BattingAdapter.RecyclerViewHolder>
{
    public ArrayList<Batting_Model> orderList;
    public Context mContext;
    int count = 0;
    public BattingAdapter(ArrayList<Batting_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_batting_list, parent, false);
        return new BattingAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        count++;
        if (count%2==0)
        {
            holder.LL_Select_Player.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        final Batting_Model account_model = orderList.get(position);
        holder.TV_Batsman_name.setText(account_model.getBatsman());
        holder.TV_R.setText(account_model.getR());
        holder.TV_B.setText(""+account_model.getB());
        holder.TV_S4.setText(""+account_model.getS4());
        holder.TV_S6.setText(""+account_model.getS6());
        holder.TV_SR.setText(""+account_model.getSR());
        holder.TV_Dismiss_info.setText(""+account_model.getDismissal_info());


       /* holder.LL_Select_Player.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, SelectPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("match_unique_id",account_model.getUnique_id());
                mContext.startActivity(intent);
            }
        });*/
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Batsman_name,TV_R,TV_B,TV_S4,TV_S6,TV_SR,TV_Dismiss_info;
        LinearLayout LL_Select_Player;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Batsman_name = (TextView) itemView.findViewById(R.id.batsman_name);
            TV_R = (TextView) itemView.findViewById(R.id.r);
            TV_B = (TextView) itemView.findViewById(R.id.b);
            TV_S4 = (TextView)itemView.findViewById(R.id.s4);
            TV_S6 = (TextView)itemView.findViewById(R.id.s6);
            TV_SR = (TextView)itemView.findViewById(R.id.sr);
            TV_Dismiss_info = (TextView)itemView.findViewById(R.id.dismissalinfo);
            LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.LL_scoreboard);
        }
    }
}
