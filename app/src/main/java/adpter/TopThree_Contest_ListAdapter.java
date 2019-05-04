package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.TopScoreModel;

public class TopThree_Contest_ListAdapter extends RecyclerView.Adapter<TopThree_Contest_ListAdapter.RecyclerViewHolder>
{
    public ArrayList<TopScoreModel> orderList;
    public Context mContext;
    int count=0;

    public TopThree_Contest_ListAdapter(ArrayList<TopScoreModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_top_three_contestlist, parent, false);
        return new RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final TopScoreModel account_model = orderList.get(position);
        holder.TV_Topthree_User_Name.setText(account_model.getUsername());
        count++;
        holder.TV_Topthree_Rank.setText("#"+account_model.getRank());
        holder.TV_Score.setText(""+account_model.getScore());
        if (count==2)
        {
            ((RelativeLayout.LayoutParams) holder.rl_top_score.getLayoutParams()).setMargins(0, 10, 0, 0);
        }
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Topthree_User_Name,TV_Topthree_Rank,TV_Score;
        RelativeLayout rl_top_score;
        CircleImageView circularImageView;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Topthree_User_Name = (TextView) itemView.findViewById(R.id.tv_top_threeusername);
            TV_Topthree_Rank = (TextView) itemView.findViewById(R.id.tv_rank);
            TV_Score = (TextView) itemView.findViewById(R.id.tv_top_threescore);
            circularImageView = (CircleImageView) itemView.findViewById(R.id.img_topthree_profile);
            rl_top_score = (RelativeLayout) itemView.findViewById(R.id.rl_top_three);
        }
    }
}
