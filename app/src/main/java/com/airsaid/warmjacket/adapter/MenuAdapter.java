package com.airsaid.warmjacket.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airsaid.warmjacket.R;
import com.airsaid.warmjacket.bean.MenuResultBean;
import com.airsaid.warmjacket.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 菜谱Adapter
 */
public class MenuAdapter extends BaseAdapter {

    private List<MenuResultBean.ListBean> mList;

    public MenuAdapter(List<MenuResultBean.ListBean> listBeans) {
        this.mList = listBeans;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = UIUtils.inflate(R.layout.item_listview_menu);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_menu_name);
            holder.summary = (TextView) convertView.findViewById(R.id.tv_menu_summary);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_menu_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuResultBean.ListBean bean = mList.get(position);
        if (bean != null) {
            // 设置菜名
            holder.name.setText(bean.getName());
            // 设置摘要
            if(bean.getRecipe() != null && bean.getRecipe().getSumary() != null){
                holder.summary.setText(bean.getRecipe().getSumary());
            }
            // 判断是否有图
            if(!TextUtils.isEmpty(bean.getThumbnail())){
                Glide.with(UIUtils.getContext())
                        .load(bean.getThumbnail())
                        .error(R.mipmap.ic_empty)
                        .into(holder.img);
            }else{
                Glide.with(UIUtils.getContext())
                        .load(R.mipmap.ic_empty)
                        .into(holder.img);
            }

        }
        return convertView;
    }

    private final class ViewHolder {
        TextView name;
        TextView summary;
        ImageView img;
    }
}
