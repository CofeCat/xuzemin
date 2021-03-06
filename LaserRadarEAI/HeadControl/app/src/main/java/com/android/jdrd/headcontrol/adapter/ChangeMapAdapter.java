package com.android.jdrd.headcontrol.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jdrd.headcontrol.R;
import com.android.jdrd.headcontrol.util.Constant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Administrator on 2017/3/3.
 * test for changeAdapter
 */

public class ChangeMapAdapter extends BaseAdapter {
    private HashMap<String,HashMap<String,Vector<Float>>> arraylist = new HashMap<>();
    private Context context;
    private String key;
    private double number;
    private Handler handler;

    public ChangeMapAdapter(HashMap<String,HashMap<String,Vector<Float>>> arrayPlanLists, String key, Context context, Handler handler){
        this.arraylist = arrayPlanLists;
        this.context = context;
        this.key = key;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return arraylist.get(key).get("point_xs").size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(key).get("point_xs").get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutTag layoutTag;
        if (convertView == null) {
            layoutTag=new LayoutTag();
            convertView = LayoutInflater.from(context).inflate(R.layout.change_plan_adapter, null);
            layoutTag.number = (TextView) convertView.findViewById(R.id.number);
            layoutTag.change_number_xs = (TextView) convertView.findViewById(R.id.change_number_xs);
            layoutTag.change_number_ys = (TextView) convertView.findViewById(R.id.change_number_ys);
            layoutTag.x_add = (ImageButton) convertView.findViewById(R.id.point_x_add);
            layoutTag.y_add = (ImageButton) convertView.findViewById(R.id.point_y_add);
            layoutTag.x_subtract = (ImageButton) convertView.findViewById(R.id.point_x_subtract);
            layoutTag.y_subtract = (ImageButton) convertView.findViewById(R.id.point_y_subtract);
            convertView.setTag(layoutTag);

        } else {
            layoutTag = (LayoutTag) convertView.getTag();
        }
        layoutTag.number.setText("第"+(position+1)+"个目标");
        number =(arraylist.get(key).get("point_xs").get(position) -40 )  / Constant.SCALE_NUMBER;
        layoutTag.change_number_xs.setText(String.valueOf(new BigDecimal(number).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue()));
        number = (arraylist.get(key).get("point_ys").get(position) -40 ) / Constant.SCALE_NUMBER;
        layoutTag.change_number_ys.setText(String.valueOf(new BigDecimal(number).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue()));
        layoutTag.x_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.get(key).get("point_xs").setElementAt((arraylist.get(key).get("point_xs").get(position))+ (Constant.SCALE_NUMBER /10 ),position);
                Message m  =  new Message();
                m.what = 5;
                m.obj = position;
                handler.sendMessage(m);
                try{
                    ChangeMapAdapter.this.notify();
                    handler.sendEmptyMessage(5);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        layoutTag.x_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.get(key).get("point_xs").setElementAt((arraylist.get(key).get("point_xs").get(position))- (Constant.SCALE_NUMBER /10 ),position);
                Message m  =  new Message();
                m.what = 5;
                m.obj = position;
                handler.sendMessage(m);
                try{
                    this.notify();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        layoutTag.y_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.get(key).get("point_ys").setElementAt((arraylist.get(key).get("point_ys").get(position)) + (Constant.SCALE_NUMBER /10 ),position);
                Message m  =  new Message();
                m.what = 5;
                m.obj = position;
                handler.sendMessage(m);
                try{
                    this.notify();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        layoutTag.y_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.get(key).get("point_ys").setElementAt((arraylist.get(key).get("point_ys").get(position)) - (Constant.SCALE_NUMBER/10 ),position);
                    Message m  =  new Message();
                    m.what = 5;
                    m.obj = position;
                    handler.sendMessage(m);
                try{
                    this.notify();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }
    private final class LayoutTag{
        ImageButton x_add;
        ImageButton y_add;
        ImageButton x_subtract;
        ImageButton y_subtract;
        TextView change_number_xs;
        TextView number;
        TextView change_number_ys;
    }
    public void update(int index,ListView listview){
        Constant.debugLog(index +"int");
        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        LayoutTag holder = (LayoutTag) view.getTag();
        holder.change_number_xs = (TextView) view.findViewById(R.id.change_number_xs);
        holder.change_number_ys = (TextView) view.findViewById(R.id.change_number_ys);
        number = (arraylist.get(key).get("point_xs").get(index) -40 ) / Constant.SCALE_NUMBER;
        holder.change_number_xs.setText(String.valueOf(new BigDecimal(number).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue()));
        number = (arraylist.get(key).get("point_ys").get(index) -40 ) / Constant.SCALE_NUMBER;
        holder.change_number_ys.setText(String.valueOf(new BigDecimal(number).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue()));
    }
}
