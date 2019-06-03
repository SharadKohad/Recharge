package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.logicaltech.gamerecharge.R;
import java.util.ArrayList;
import model.Batting_Model;

public class BowingAdpter extends RecyclerView.Adapter<BowingAdpter.RecyclerViewHolder>
{
    public ArrayList<Batting_Model> orderList;
    public Context mContext;
    int count = 0;
    public BowingAdpter(ArrayList<Batting_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_bowing_list, parent, false);
        return new BowingAdpter.RecyclerViewHolder(view);
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
        holder.TV_Bower_name.setText(account_model.getBatsman());
        holder.TV_O.setText(account_model.getO());
        holder.TV_M.setText(""+account_model.getM());
        holder.TV_R.setText(""+account_model.getR());
        holder.TV_W.setText(""+account_model.getW());
        holder.TV_E.setText(""+account_model.getEconimy());

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
        TextView TV_Bower_name,TV_O,TV_M,TV_R,TV_W,TV_E;
        LinearLayout LL_Select_Player;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Bower_name = (TextView) itemView.findViewById(R.id.bower_name);
            TV_O = (TextView) itemView.findViewById(R.id.o);
            TV_M = (TextView) itemView.findViewById(R.id.m);
            TV_R = (TextView)itemView.findViewById(R.id.r);
            TV_W = (TextView)itemView.findViewById(R.id.w);
            TV_E = (TextView)itemView.findViewById(R.id.e);
            LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.LL_scoreboard);
        }
    }
}
