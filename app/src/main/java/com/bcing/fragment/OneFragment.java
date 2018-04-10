package com.bcing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.entity.BookData;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author fml
 *         created at 2016/6/20 13:41
 *         description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class OneFragment extends LazyFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private ListView mListView;
    private List<BookData> mList = new ArrayList<>();

    private TextView bookset_title;
    private TextView bookset_author;
    private TextView bookset_publisher;
    private ImageView bookset_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_one, container, false);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "oneFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
        }

//        View view = inflater.inflate(R.layout.book_set_item, null);
//        findView(view);
        return view;
    }

//    //初始化View
//    private void findView(View view) {
//
//
//        bookset_title = (TextView)view.findViewById(R.id.bookset_title);
//        bookset_author = (TextView) view.findViewById(R.id.bookset_author);
//        bookset_publisher = (TextView) view.findViewById(R.id.bookset_publisher);
//        bookset_image = (ImageView) view.findViewById(R.id.bookset_image);
//
//
//
//        mListView = (ListView) view.findViewById(R.id.mListView);
//
//        //解析接口
//        String url = "https://api.douban.com/v2/book/isbn/9787540483180";
//        RxVolley.get(url, new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
//                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();
//                parsingJson(t);
//
//            }
//        });
//    }
//
//    //解析json
//    private void parsingJson(String t) {
//        try {
//            JSONObject jsonObject = new JSONObject(t);
//            JSONArray jsonAuthor = jsonObject.getJSONArray("author");
//            JSONObject jsonImages = jsonObject.getJSONObject("images");
////            for (int i = 0; i < 1; i++) {
////                BookData data = new BookData();
////                data.setTitle(jsonObject.getString("title"));
////                data.setAuthor((String) jsonAuthor.get(0));
////                data.setPublisher(jsonObject.getString("publisher"));
////                data.setImage(jsonImages.getString("medium"));
////
////                mList.add(data);
////            }
//            bookset_title.setText(jsonObject.getString("title"));
//            bookset_author.setText((String) jsonAuthor.get(0));
//            bookset_publisher.setText(jsonObject.getString("publisher"));
////            bookset_image.setText(jsonImages.getString("medium"));
////
////            BookAdapter adapter = new BookAdapter(getActivity(), mList);
////            mListView.setAdapter(adapter);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG", "oneFragment--lazyLoad");
    }

//    @Override
//    protected void initEvents() {
//
//    }
//
//    @Override
//    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//    }

}
