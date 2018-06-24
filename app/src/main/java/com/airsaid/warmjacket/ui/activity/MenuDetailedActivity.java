package com.airsaid.warmjacket.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.warmjacket.R;
import com.airsaid.warmjacket.base.BaseActivity;
import com.airsaid.warmjacket.bean.MenuDetailedBean;
import com.airsaid.warmjacket.bean.MenuResultBean;
import com.airsaid.warmjacket.common.CommonConstants;
import com.airsaid.warmjacket.utils.GsonUtils;
import com.airsaid.warmjacket.utils.ToastUtils;
import com.airsaid.warmjacket.utils.UIUtils;
import com.airsaid.warmjacket.widget.ZFlowLayout;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


public class MenuDetailedActivity extends BaseActivity implements View.OnClickListener {

    private MenuResultBean.ListBean mCategoryInfoBean;
    private String mMenuId;

    // 标题名
    private TextView mNameView;
    // 展示图
    private ImageView mImageView;
    // 材料准备
    private TextView mIngredientsView;
    // 最后总结
    private TextView mSumaryView;
    private LinearLayout mMethodView;
    private MenuDetailedBean menuData;
    private TextView mTitleView;
    private ZFlowLayout mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detailed);
        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        mTitleView = (TextView) toolbar.findViewById(R.id.tv_title);
        mFlowLayout = (ZFlowLayout) findViewById(R.id.flowLayout);
        mIngredientsView = (TextView) findViewById(R.id.tv_menu_detailed_ingredients);
        mSumaryView = (TextView) findViewById(R.id.tv_menu_detailed_sumary);
        mImageView = (ImageView) findViewById(R.id.iv_menu_detailed_img);
        mNameView = (TextView) findViewById(R.id.tv_menu_detailed_name);
        mMethodView = (LinearLayout) findViewById(R.id.ll_menu_detailed_method);
        toolbar.setNavigationOnClickListener(this);
    }

    private void initData() {
        mCategoryInfoBean = (MenuResultBean.ListBean) getIntent().getExtras().get(MenuActivity.MENULISTBEAN);
        mMenuId = mCategoryInfoBean.getMenuId();
        getMenuDetailedData(CommonConstants.KEY, mMenuId);
        initLabel(mCategoryInfoBean.getCtgTitles().split(","));
    }

    /**
     * 初始化标签
     * @param labels 标签数组
     */
    private void initLabel(final String[] labels) {
        // 设置边距
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 30, 20, 10);
        for (int i = 0; i < labels.length; i++) {
            final TextView textView = new TextView(this);
            textView.setTag(i);
            textView.setTextSize(12);
            textView.setText(labels[i]);
            textView.setTextColor(UIUtils.getColor(R.color.color_666));
            textView.setBackgroundResource(R.drawable.bg_label);
            mFlowLayout.addView(textView, layoutParams);
            // 标签点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = labels[(int) textView.getTag()];
                    if (TextUtils.isEmpty(tag)) {
                        ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.hint_not_empty));
                    } else {
                        Intent intent = new Intent(UIUtils.getContext(), MenuActivity.class);
                        intent.putExtra(CommonConstants.ENTER_TYPE, MainActivity.TYPE_QUERY);
                        intent.putExtra(MainActivity.QUERY, tag);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * 请求网络获取菜谱详情数据
     * @param key mobapi key
     * @param id 菜谱id
     */
    private void getMenuDetailedData(String key, String id){
        final ProgressDialog dialog = ProgressDialog.show(this, null, UIUtils.getString(R.string.loading_hint), false, true);
        OkHttpUtils.get().url(CommonConstants.COOK_MENU_QUERY)
                .addParams("key", key)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dialog.dismiss();
                        ToastUtils.show(UIUtils.getContext(), UIUtils.getString(R.string.request_error));
                    }

                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        MenuDetailedBean bean = GsonUtils.getInstance().fromJson(response, MenuDetailedBean.class);
                        if (bean != null && CommonConstants.CODE_SUCCESS.equals(bean.getRetCode())) {
                            setMenuData(bean);
                        } else {
                            ToastUtils.show(UIUtils.getContext(), bean.getMsg());
                        }
                    }
                });
    }

    /**
     * 设置菜谱数据
     * @param menuData
     */
    public void setMenuData(MenuDetailedBean menuData) {
        mTitleView.setText(menuData.getResult().getName());
        MenuDetailedBean.Recipe recipe = menuData.getResult().getRecipe();
        if (recipe != null) {
            // 设置标题
            mNameView.setText(recipe.getTitle());
            // 设置材料
            String ingredients = recipe.getIngredients();
            if(!TextUtils.isEmpty(ingredients)){
                mIngredientsView.setText(ingredients.subSequence(ingredients.indexOf("[\"") + 2, ingredients.lastIndexOf("\"]")));
            }
            // 设置备注
            mSumaryView.setText(recipe.getSumary());
            // 设置展示图
            if(TextUtils.isEmpty(recipe.getImg())){
                mImageView.setVisibility(View.GONE);
            }else{
                mImageView.setVisibility(View.VISIBLE);
                Glide.with(UIUtils.getContext()).load(recipe.getImg())
                        .error(R.mipmap.ic_empty)
                        .into(mImageView);
            }

            // 设置制作方法
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageParams.topMargin = 20;
            imageParams.bottomMargin = 10;
            textParams.bottomMargin = 20;
            if(!TextUtils.isEmpty(recipe.getMethod())){
                String[] split = recipe.getMethod().split("\\\"");
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    if (s.startsWith("http:")) {
                        // 设置图片
                        ImageView imageView = new ImageView(UIUtils.getContext());
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(UIUtils.getContext()).load(s).into(imageView);
                        mMethodView.addView(imageView, imageParams);
                    } else if (s.matches("\\d.*")) {
                        // 设置文字
                        TextView textView = new TextView(UIUtils.getContext());
                        textView.setText(s.trim());
                        textView.setTextColor(UIUtils.getColor(R.color.color_666));
                        textView.setLineSpacing(1.5f, 1.5f);
                        mMethodView.addView(textView, textParams);
                    }
                }
                // 重绘
                mMethodView.requestLayout();
                mMethodView.invalidate();
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
