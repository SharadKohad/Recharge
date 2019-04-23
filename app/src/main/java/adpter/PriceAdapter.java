package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicaltech.rechargepannel.R;

import java.util.ArrayList;

import model.PriceModel;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.RecyclerViewHolder>
{
    public ArrayList<PriceModel> orderList;
    public Context mContext;
    int count=0;
    public PriceAdapter(ArrayList<PriceModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_price_distribution, parent, false);
        return new PriceAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final PriceModel account_model = orderList.get(position);
        String valueto = account_model.getRankto();
        if (valueto.equals("null"))
        {
            holder.TV_Price_From.setText("Rank: "+account_model.getRankfrom());
        }
        else
        {
            holder.TV_Price_From.setText("Rank: "+account_model.getRankfrom()+" To "+account_model.getRankto());
        }
        holder.TV_Price.setText(account_model.getRankamount()+" \u20B9");
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Price_From,TV_Price;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Price_From = (TextView) itemView.findViewById(R.id.tv_rank);
         //   TV_Price_To = (TextView) itemView.findViewById(R.id.tv_top_rank);
            TV_Price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
