package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicaltech.rechargepannel.R;

import java.util.ArrayList;

import model.WithdrawHistoryModel;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.RecyclerViewHolder>
{
    public ArrayList<WithdrawHistoryModel> orderList;
    public Context mContext;
    int count=0;
    public WalletAdapter(ArrayList<WithdrawHistoryModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_wallet_history, parent, false);
        return new WalletAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final WithdrawHistoryModel account_model = orderList.get(position);
        holder.TV_Email.setText("Email: "+account_model.getEmail());
        holder.TV_Date.setText(account_model.getTdate());
        holder.TV_Price.setText(account_model.getAmount()+" \u20B9");
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Email,TV_Date,TV_Price;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Email = (TextView) itemView.findViewById(R.id.tv_email_id);
            TV_Date = (TextView) itemView.findViewById(R.id.tv_date_time);
            TV_Price = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
        }
    }
}
