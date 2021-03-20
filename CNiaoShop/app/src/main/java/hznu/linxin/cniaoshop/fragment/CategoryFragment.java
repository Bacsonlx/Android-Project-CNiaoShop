package hznu.linxin.cniaoshop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import hznu.linxin.cniaoshop.Contants;
import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.adapter.BaseAdapter;
import hznu.linxin.cniaoshop.adapter.CategoryAdapter;
import hznu.linxin.cniaoshop.adapter.WaresAdapter;
import hznu.linxin.cniaoshop.adapter.decoration.DividerGridItemDecoration;
import hznu.linxin.cniaoshop.bean.Banner;
import hznu.linxin.cniaoshop.bean.Category;
import hznu.linxin.cniaoshop.bean.Page;
import hznu.linxin.cniaoshop.bean.Wares;
import hznu.linxin.cniaoshop.http.BaseCallback;
import hznu.linxin.cniaoshop.http.OkHttpHelper;
import hznu.linxin.cniaoshop.http.SpotsCallBack;


/**
 * @author BacSon
 */

/**
 *  分类页面
 */
public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLaout;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    // 左边分类栏的Adapter
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;


    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();


    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;


    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category,container,false);
        ViewUtils.inject(this, view);

        requestCategoryData(); // 侧边栏
        requestBannerData(); // 轮播图

        initRefreshLayout(); // 商品

        return  view;
    }



    /**
     *  像服务器请求数据
     */
    private  void requestCategoryData(){

        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {

                showCategoryData(categories);
                // 初始化category_id 第一个分类下的数据
                if(categories !=null && categories.size()>0){
                    category_id = categories.get(0).getId();
                }
                requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    /**
     *  显示数据
     *  @param categories
     */
    private void showCategoryData(List<Category> categories) {

        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        // 实现分类的点击事件
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);

                category_id = category.getId();
                // 每次点击的时候要把数据重置 这样才会在顶端
                currPage=1;
                state=STATE_NORMAL;

                requestWares(category_id);
            }
        });
        // 配置RecyclerView
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    /**
     *  请求轮播图数据
     */
    private void requestBannerData() {
        String url = Contants.API.BANNER+"?type=1";

        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                // 显示轮播图
                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    /**
     *  通过SliderViews 显示轮播图
     * @param banners
     */
    private void showSliderViews(List<Banner> banners){

        if(banners !=null){
            // 将获取到的Banner数据赋值给sliderView
            for (Banner banner : banners){
                // DefaultSliderView 是SliderView 的子类 区别与TextSliderView DefaultSliderView没有文字描述
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl())
                    .description(banner.getName())
                    .setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(sliderView);
            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setDuration(3000);

    }


    /**
     *  初始化RefreshLayout
     */
    private  void initRefreshLayout(){
        // 设置支持加载更多
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(currPage <=totalPage)
                    loadMoreData();
                else{
//                    Toast.makeText()
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }


    private  void refreshData(){

        currPage =1;

        state=STATE_REFREH;
        requestWares(category_id);
    }

    private void loadMoreData(){

        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);

    }

    /**
     *  请求分类页面-商品数据
     * @param categoryId
     */
    private void requestWares(long categoryId){

        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;

        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage = waresPage.getCurrentPage();
                totalPage =waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    /**
     *  显示商品数据
     * @param wares
     */
    private  void showWaresData(List<Wares> wares){

        switch (state){

            case  STATE_NORMAL:
                if(mWaresAdatper ==null) {
                    mWaresAdatper = new WaresAdapter(getContext(), wares);

                    mRecyclerviewWares.setAdapter(mWaresAdatper);

                    // 网格布局
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                else{
                    mWaresAdatper.clearData();
                    mWaresAdatper.addData(wares);
                }
                break;

            case STATE_REFREH:
                mWaresAdatper.clearData();
                mWaresAdatper.addData(wares);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(),wares);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }
}



