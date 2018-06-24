package com.airsaid.warmjacket.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airsaid.warmjacket.R;
import com.airsaid.warmjacket.bean.CookCategoryBean;
import com.airsaid.warmjacket.utils.UIUtils;

import java.util.List;

/**
 * 菜谱Adapter
 */
public class CookExpandableListViewAdapter extends BaseExpandableListAdapter {

    private int[] mImgs = {
            R.mipmap.classify_layout_tablerow_changjianshicai,
            R.mipmap.classify_layout_tablerow_fangshi,
            R.mipmap.classify_layout_tablerow_zhongguocaixi,
            R.mipmap.classify_layout_tablerow_renqun,
            R.mipmap.classify_layout_tablerow_chuju};

    private List<CookCategoryBean.ChildsBean> mList;

    public CookExpandableListViewAdapter(List<CookCategoryBean.ChildsBean> cookCategoryBeans) {
        this.mList = cookCategoryBeans;
    }

    // 外面一层总数
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    // 子总数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getChilds().size();
    }

    // 获取指定组位置的组数据
    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition).getCategoryInfo().getName();
    }

    // 获取指定组位置、指定子列表项处的子列表项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getChilds().get(childPosition).getCategoryInfo().getName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    // 该方法决定每个组选项的外观
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View inflate = UIUtils.inflate(R.layout.item_expandable_listview_groupview);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_groupview_img);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_groupview_title);
        ImageView arrowView = (ImageView) inflate.findViewById(R.id.iv_groupview_arrow);
        imageView.setImageResource(mImgs[groupPosition]);
        textView.setText(getGroup(groupPosition).toString());
        arrowView.setImageResource(isExpanded ? R.mipmap.btn_weaken_arrowup_orange : R.mipmap.btn_weaken_arrowdown_orange);
        return inflate;
    }

    // 该方法决定每个子选项的外观
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View inflate = UIUtils.inflate(R.layout.item_expandable_listview_childview);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_child_name);
        textView.setText(getChild(groupPosition, childPosition).toString());
        return inflate;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public TextView getTextView(int left, int top, int right, int bottom) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(UIUtils.getContext());
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(20);
        textView.setTextColor(UIUtils.getColor(R.color.color_666));
        textView.setPadding(left, top, right, bottom);
        return textView;
    }
}