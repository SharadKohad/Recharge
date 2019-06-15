package adpter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.SingleBattleDetailActivity;

import java.util.ArrayList;

import model.BattleModel;

public class BattleAdpter extends RecyclerView.Adapter<BattleAdpter.RecyclerViewHolder>
{
    public ArrayList<BattleModel> orderList;
    public Context mContext;
    public BattleAdpter(ArrayList<BattleModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_battle, parent, false);
        return new BattleAdpter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final BattleModel account_model = orderList.get(position);
        holder.btn_join.setText("\u20B9 "+account_model.getEnteryfee());

       /* if (account_model.getGame_amt_type().equals("POINTS"))
        {
            holder.ImageViewBab.setImageResource(R.drawable.coin);
            holder.ImageViewcoin.setImageResource(R.drawable.coin);
        }*/

        holder.TV_Name.setText(account_model.getGamename());
        int sec  = Integer.parseInt(account_model.getTime_left());
        reverseTimer(sec,holder.TV_Time);
        holder.TV_Win_Amount.setText(""+account_model.getWiningprice());

        if (account_model.getTotal_joining().equals("0"))
        {
            holder.TV_Total_Join.setText("1");
        }
        else
        {
            holder.TV_Total_Join.setText(""+account_model.getTotal_joining());
        }

        holder.RL_Ballte.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(mContext, SingleBattleDetailActivity.class);
                intent.putExtra("gametype",account_model.getGame_type());
                intent.putExtra("srno",account_model.getSrno());
                mContext.startActivity(intent);

                /*else if(gtype.equals("2"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("3"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("4"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("5"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("7"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("8"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("9"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("11"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("12"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("13"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("14"))
                {
                    Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                    intent.putExtra("gametype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }*/
            }
        });

      /*  holder.TV_top_score.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (account_model.getTotal_joining().equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Please join the contest first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(ContestListActivity.this,TopScoreActivity.class);
                    intent.putExtra("price",account_model.getWiningprice());
                    intent.putExtra("srno",account_model.getSrno());
                    startActivity(intent);
                }
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
        TextView TV_Name,TV_Win_Amount,TV_Total_Join,TV_Time;
        RelativeLayout RL_Ballte;
        Button btn_join;
        ProgressBar progressBar;
        ImageView ImageViewcoin,ImageViewBab;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Name = (TextView) itemView.findViewById(R.id.tv_cotest_name);
            TV_Win_Amount = (TextView) itemView.findViewById(R.id.tv_wining_price);
            TV_Total_Join = (TextView)itemView.findViewById(R.id.total_join);
            TV_Time = (TextView) itemView.findViewById(R.id.tv_tournament_time);
            btn_join = (Button) itemView.findViewById(R.id.btn_jointou);
            RL_Ballte = (RelativeLayout) itemView.findViewById(R.id.rl_battle_fild);

            /*TV_Entry_fee = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_show_persentage);
            RL_partisipet = (RelativeLayout) itemView.findViewById(R.id.rl_time_value);
            ImageViewcoin = (ImageView) itemView.findViewById(R.id.imageViewcontest);
            ImageViewBab = (ImageView) itemView.findViewById(R.id.imageViewcontest1);*/
        }
    }

    public void reverseTimer(int Seconds,final TextView time) {
        new CountDownTimer(Seconds* 1000+1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                int seconds = (int) (millisUntilFinished / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                    time.setText(String.format("%02d", hours) + "H " + String.format("%02d", minutes) + "M " + String.format("%02d", seconds)+"S");
            }
            public void onFinish()
            {
                time.setText("Completed");
            }
        }.start();
    }
}
