package com.airsaid.warmjacket.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.airsaid.warmjacket.R;
import com.airsaid.warmjacket.adapter.CookExpandableListViewAdapter;
import com.airsaid.warmjacket.base.BaseActivity;
import com.airsaid.warmjacket.bean.CookCategoryBean;
import com.airsaid.warmjacket.common.CommonConstants;
import com.airsaid.warmjacket.utils.GsonUtils;
import com.airsaid.warmjacket.utils.ToastUtils;
import com.airsaid.warmjacket.utils.UIUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ExpandableListView.OnChildClickListener, SearchView.OnQueryTextListener, ExpandableListView.OnGroupExpandListener {

    public static final String CATEGORY_INFO = "categoryInfo";
    public static final String QUERY = "query";
    public static final int TYPE_QUERY = 0x01;
    public static final int TYPE_MENU = 0x02;

    private List<CookCategoryBean.ChildsBean> mChildsBeans = new ArrayList<>();
    private CookExpandableListViewAdapter mCookCategoryAdapter;
    private ExpandableListView mExpandableListView;
    private SearchView mSearchView;
    private LinearLayout mHintNotNetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(this);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        mHintNotNetView = (LinearLayout) findViewById(R.id.ll_hint_not_net);
        mExpandableListView.setOnChildClickListener(this);
        // 隐藏原箭头
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOnGroupExpandListener(this);
        mExpandableListView.setEmptyView(mHintNotNetView);
    }

    /**
     * 获取菜谱数据
     */
    public void getData() {
        final ProgressDialog dialog = ProgressDialog.show(this, null, UIUtils.getString(R.string.loading_menu), false, true);
        // 通过OkHttp获取
        OkHttpUtils.get().url(CommonConstants.COOK_CATEGORY_QURRY).addParams("key", CommonConstants.KEY).build().execute(new StringCallback() {

            @Override
            public void onError(Request request, Exception e) {
                dialog.dismiss();
                ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.request_error));
            }

            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(response)) {
                    CookCategoryBean bean = GsonUtils.getInstance().fromJson(response, CookCategoryBean.class);
                    if (CommonConstants.CODE_SUCCESS.equals(bean.getRetCode())) {
                        mChildsBeans.addAll(bean.getResult().getChilds());
                        if (mCookCategoryAdapter == null) {
                            mCookCategoryAdapter = new CookExpandableListViewAdapter(mChildsBeans);
                        } else {
                            mCookCategoryAdapter.notifyDataSetChanged();
                        }
                        mExpandableListView.setAdapter(mCookCategoryAdapter);
                        // 默认展开第一项
                        mExpandableListView.expandGroup(0);
                    } else {
                        ToastUtils.show(UIUtils.getContext(), bean.getMsg());
                    }
                }
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // 进入到子选项分类菜单
        Intent intent = new Intent(this, MenuActivity.class);
        CookCategoryBean.ChildsBean.CategoryInfoBean categoryInfo = mChildsBeans.get(groupPosition).getChilds().get(childPosition).getCategoryInfo();
        intent.putExtra(CommonConstants.ENTER_TYPE, MainActivity.TYPE_MENU);
        intent.putExtra(CATEGORY_INFO, categoryInfo);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.hint_not_empty));
        } else {
            // 进入到搜索结果
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra(CommonConstants.ENTER_TYPE, MainActivity.TYPE_QUERY);
            intent.putExtra(MainActivity.QUERY, query);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        for (int i = 0, count = mExpandableListView.getExpandableListAdapter().getGroupCount(); i < count; i++) {
            if (groupPosition != i) {// 关闭其他分组
                mExpandableListView.collapseGroup(i);
            }
        }
    }
}
