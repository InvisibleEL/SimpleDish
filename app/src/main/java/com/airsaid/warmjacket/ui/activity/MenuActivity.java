package com.airsaid.warmjacket.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.airsaid.warmjacket.R;
import com.airsaid.warmjacket.adapter.MenuAdapter;
import com.airsaid.warmjacket.base.BaseActivity;
import com.airsaid.warmjacket.bean.CookCategoryBean;
import com.airsaid.warmjacket.bean.MenuResultBean;
import com.airsaid.warmjacket.common.CommonConstants;
import com.airsaid.warmjacket.utils.GsonUtils;
import com.airsaid.warmjacket.utils.ToastUtils;
import com.airsaid.warmjacket.utils.UIUtils;
import com.airsaid.warmjacket.widget.LoadMoreListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private CookCategoryBean.ChildsBean.CategoryInfoBean mCategoryInfoBean;
    private List<MenuResultBean.ListBean> mListBeans = new ArrayList<>();
    public static final String MENULISTBEAN = "MenuListBean";

    private int mPage = 1;// 页数
    private int mSize = 20;// 条数

    private LoadMoreListView mListView;
    private MenuAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mCid;
    private int mType;
    private String mQuery;
    private Toolbar mToolBar;
    private TextView mTitleView;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initToolBar();
        initView();
        initData();
        addListener();
    }

    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mTitleView = (TextView) mToolBar.findViewById(R.id.tv_title);
        mToolBar.setNavigationIcon(R.mipmap.ic_back);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mListView = (LoadMoreListView) findViewById(R.id.listView);
//        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            // 判断跳转来源
            Bundle bundle = getIntent().getExtras();
            mType = (int) bundle.get(CommonConstants.ENTER_TYPE);
            if (MainActivity.TYPE_MENU == mType) {
                // 获取传输过来的菜谱数据
                mCategoryInfoBean = (CookCategoryBean.ChildsBean.CategoryInfoBean) bundle.get(MainActivity.CATEGORY_INFO);
                mCid = mCategoryInfoBean.getCtgId();
                mSwipeRefreshLayout.setRefreshing(true);
                getMenuData(CommonConstants.KEY, mCid, "", String.valueOf(mPage), String.valueOf(mSize));
                mTitleView.setText(mCategoryInfoBean.getName());
            } else if (MainActivity.TYPE_QUERY == mType) {
                mQuery = (String) bundle.get(MainActivity.QUERY);
                getMenuData(CommonConstants.KEY, "", mQuery, String.valueOf(mPage), String.valueOf(mSize));
                mTitleView.setText(mQuery + UIUtils.getString(R.string.query_result));
            }
        }
    }

    private void addListener(){
        mToolBar.setNavigationOnClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 请求网络获取菜单数据
     *
     * @param key  mobapi key
     * @param cid  标签id
     * @param page 起始页(默认1)
     * @param size 返回数据条数(默认20)
     */
    private void getMenuData(String key, String cid, String name, String page, String size) {
        mDialog = ProgressDialog.show(this, null, UIUtils.getString(R.string.loading_menu), false, true);
        OkHttpUtils.get().url(CommonConstants.COOK_MENU_SEARCH)
                .addParams("key", key)
                .addParams("cid", cid)
                .addParams("name", name)
                .addParams("page", page)
                .addParams("size", size)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Request request, Exception e) {
                        refreshFinish();
                        ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.request_error));
                    }

                    @Override
                    public void onResponse(String response) {
                        refreshFinish();
                        if (!TextUtils.isEmpty(response)) {
                            MenuResultBean bean = GsonUtils.getInstance().fromJson(response, MenuResultBean.class);
                            if (CommonConstants.CODE_SUCCESS.equals(bean.getRetCode())) {
                                mListBeans.addAll(bean.getResult().getList());
                                if (mAdapter == null) {
                                    mAdapter = new MenuAdapter(mListBeans);
                                    mListView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                }
                                ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.loading_success));
                            } else {
                                ToastUtils.show(UIUtils.getContext(), bean.getMsg());
                            }
                        }
                    }
                });
    }

    // 下拉刷新
    @Override
    public void onRefresh() {
        mPage = 1;
        mListBeans.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        if (mType == MainActivity.TYPE_MENU) {
            getMenuData(CommonConstants.KEY, mCid, "", String.valueOf(mPage), String.valueOf(mSize));
        } else if (mType == MainActivity.TYPE_QUERY) {
            getMenuData(CommonConstants.KEY, "", mQuery, String.valueOf(mPage), String.valueOf(mSize));
        }
    }

    // 上拉加载
    @Override
    public void onLoadingMore() {
        mPage += 1;
        if (mType == MainActivity.TYPE_MENU) {
            getMenuData(CommonConstants.KEY, mCid, "", String.valueOf(mPage), String.valueOf(mSize));
        } else if (mType == MainActivity.TYPE_QUERY) {
            getMenuData(CommonConstants.KEY, "", mQuery, String.valueOf(mPage), String.valueOf(mSize));
        }
    }

    /**
     * 刷新完成
     */
    private void refreshFinish() {
        if(mDialog != null)
            mDialog.dismiss();
        mListView.loadMoreComplete();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // 点击进入菜谱详情页
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MenuDetailedActivity.class);
        intent.putExtra(MenuActivity.MENULISTBEAN, mListBeans.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
