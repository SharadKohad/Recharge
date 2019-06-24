package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.logicaltech.gamerecharge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.TopScoreModel;
import util.SessionManeger;

public class TopthreeScoreAdapter extends RecyclerView.Adapter<TopthreeScoreAdapter.RecyclerViewHolder>
{
    public ArrayList<TopScoreModel> orderList;
    public Context mContext;
    int count=0;
    public TopthreeScoreAdapter(ArrayList<TopScoreModel> orderList , Context context) {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_top_three_score, parent, false);
        return new RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final TopScoreModel account_model = orderList.get(position);
        holder.TV_Topthree_User_Name.setText(account_model.getUsername());
        count++;
        holder.TV_Topthree_Rank.setText("#"+account_model.getRank());
        holder.TV_Score.setText(""+account_model.getScore());
        System.out.println("Img URL: "+account_model.getUserFile());
        if (account_model.getUserFile().equals("null"))
        {

        }
        else
        {
            Picasso.with(mContext).load(account_model.getUserFile()).into(holder.circularImageView);
        }
        if (count==2)
        {
            /*holder.rl_top_score.setVisibility(View.INVISIBLE);
            holder.circularImageView.getLayoutParams().height = 200;
            holder.circularImageView.getLayoutParams().width = 90;
            */
            ((RelativeLayout.LayoutParams) holder.rl_top_score.getLayoutParams()).setMargins(0, 5, 0, 0);
        }
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
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
