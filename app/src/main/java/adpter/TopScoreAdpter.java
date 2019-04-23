package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.rechargepannel.R;

import java.util.ArrayList;

import model.TopScoreModel;

public class TopScoreAdpter extends RecyclerView.Adapter<TopScoreAdpter.RecyclerViewHolder>
{
    public ArrayList<TopScoreModel> orderList;
    public Context mContext;
    String operatorType;
    int count=0;
    public TopScoreAdpter(ArrayList<TopScoreModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_top_score, parent, false);
        return new TopScoreAdpter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final TopScoreModel account_model = orderList.get(position);
        holder.TV_Top_User_Name.setText(account_model.getUsername());
        holder.TV_Top_Score.setText(account_model.getScore());
        holder.TV_Price.setText("10 \u20B9");
        count++;
        holder.TV_Top_Rank.setText(""+count);
        if (count%2==0)
        {
            holder.rl_top_score.setBackgroundColor(Color.WHITE);
        }
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Top_Rank,TV_Top_User_Name,TV_Top_Score,TV_Price;
        RelativeLayout rl_top_score;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Top_Rank = (TextView) itemView.findViewById(R.id.tv_top_rank);
            TV_Top_User_Name = (TextView) itemView.findViewById(R.id.tv_top_username);
            TV_Top_Score = (TextView) itemView.findViewById(R.id.tv_top_userscore);
            TV_Price = (TextView) itemView.findViewById(R.id.tv_top_price);
            rl_top_score = (RelativeLayout) itemView.findViewById(R.id.rl_top_score);
        }
    }
}
