package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.logicaltech.gamerecharge.R;
import java.util.ArrayList;
import model.ContestJoinHisModel;

public class Total_Amount_History extends RecyclerView.Adapter<Total_Amount_History.RecyclerViewHolder>
{
    public ArrayList<ContestJoinHisModel> orderList;
    public Context mContext;
    public Total_Amount_History(ArrayList<ContestJoinHisModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_total_trancation_history, parent, false);
        return new Total_Amount_History.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final ContestJoinHisModel account_model = orderList.get(position);

        if (account_model.getColorcode().equals("#bb5c5c"))
        {
            holder.TV_Price.setTextColor(Color.parseColor("#bb5c5c"));
            holder.TV_Price.setText("-"+account_model.amount+" \u20B9");
        }
        holder.TV_Discription.setText(account_model.getDescription());
        holder.TV_Date.setText(account_model.getTtime());
        holder.TV_Price.setText(account_model.amount+" \u20B9");
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TV_Discription,TV_Date,TV_Price;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Date = (TextView) itemView.findViewById(R.id.tv_date_time);
            TV_Discription = (TextView) itemView.findViewById(R.id.tv_discription);
            TV_Price = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
        }
    }
}
