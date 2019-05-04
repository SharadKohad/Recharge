package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;

import java.util.ArrayList;

import model.ContestJoinHisModel;

public class ContestHistoryAdapter extends RecyclerView.Adapter<ContestHistoryAdapter.RecyclerViewHolder>
{
    public ArrayList<ContestJoinHisModel> orderList;
    public Context mContext;
    int count=0;
    public ContestHistoryAdapter(ArrayList<ContestJoinHisModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_contest_history, parent, false);
        return new ContestHistoryAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final ContestJoinHisModel account_model = orderList.get(position);
        holder.TV_Srno.setText("Srno: "+account_model.getSrno());
        holder.TV_Date.setText(account_model.getTtime());
        holder.TV_Price.setText(account_model.entry_amt+" \u20B9");
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Srno,TV_Date,TV_Price;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Srno = (TextView) itemView.findViewById(R.id.tv_srno);
            TV_Date = (TextView) itemView.findViewById(R.id.tv_date_time);
            TV_Price = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
        }
    }
}
