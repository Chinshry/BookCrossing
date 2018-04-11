package com.bcing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.entity.ItemBookDetail;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by 57010 on 2017/5/17.
 */

public class AllBookDetailAdapter extends RecyclerView.Adapter<AllBookDetailAdapter.ViewHolder> {
    private Context mContext;
    private List<ItemBookDetail> mBookDetailList;
    private static final String TAG = "BookDetailAdapter";
//    private List<ItemBookDetail> ItemBookDetailList = new ArrayList<>();


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView bookName;
        TextView author;
        TextView publish;
        TextView city;
        ImageView bookImage;
        View bookView;

        public ViewHolder(View view) {
            super(view);
            bookView = view;
            username = (TextView) view.findViewById(R.id.bookset_username);
            bookName = (TextView) view.findViewById(R.id.bookset_title);
            author = (TextView) view.findViewById(R.id.bookset_author);
            publish = (TextView) view.findViewById(R.id.bookset_publisher);
            city = (TextView) view.findViewById(R.id.bookset_city);
            bookImage = (ImageView) view.findViewById(R.id.bookset_image);

        }
    }

    public AllBookDetailAdapter(Context context, List<ItemBookDetail> ItemBookDetailListadpter) {
        mBookDetailList = ItemBookDetailListadpter;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_set_item, parent, false);
        //ViewHolder holder = new ViewHolder(view);
        final ViewHolder holder = new ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("TAG", "別點了");

//                int position = holder.getAdapterPosition();
//                BookDetail bookDetail = mBookDetailList.get(position);
//                Intent intent = new Intent(v.getContext(),ExchangeBookDetailsActivity.class);
//                intent.putExtra("Username",bookDetail.getUsername());
//                intent.putExtra("BookName",bookDetail.getBookName());
//                intent.putExtra("Author",bookDetail.getAuthor());
//                intent.putExtra("Press",bookDetail.getPress());
//                intent.putExtra("RecommendedReason",bookDetail.getRecommendedReason());
//                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        //解析接口
//        String url = "https://api.douban.com/v2/book/isbn/9787540483180";
//        RxVolley.get(url, new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
//                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();
//                parsingJson(t);
//
//
//            }
//        });


        ItemBookDetail bookdetail = mBookDetailList.get(position);
        KLog.e("TAG", "position是多少啊"+position);

        holder.username.setText(bookdetail.getUsername());
        holder.bookName.setText(bookdetail.getBookName());
        holder.author.setText(bookdetail.getAuthor());
        holder.publish.setText(bookdetail.getpublish());
        holder.city.setText(bookdetail.getcity());
        Glide.with(mContext).load(bookdetail.getBookImage()).into(( holder).bookImage);



    }

//    //解析json
//    private String[] parsingJson(String t) {
//        String[] bookData = new String[]{"","","","","",};
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
//            MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
//
//            bookData[0] = userInfo.getUsername();
//            bookData[1] = jsonObject.getString("title");
//            bookData[2] = (String) jsonAuthor.get(0);
//            bookData[3] = jsonObject.getString("publisher");
//            bookData[4] = userInfo.getCity();
//
//
////            bookset_title.setText(jsonObject.getString("title"));
////            bookset_author.setText((String) jsonAuthor.get(0));
////            bookset_publisher.setText(jsonObject.getString("publisher"));
////            bookset_image.setText(jsonImages.getString("medium"));
////
////            BookAdapter adapter = new BookAdapter(getActivity(), mList);
////            mListView.setAdapter(adapter);
//            KLog.e("TAG", "json获取了吗"+bookData[3]);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ItemBookDetail a = new ItemBookDetail(bookData[0], bookData[1], bookData[2], bookData[3], bookData[4]);
//
//
//        ItemBookDetailList.add(a);
//        KLog.e("TAG", "ItemBookDetail进去了吗第0个城市"+ItemBookDetailList.get(0).getcity());
//        return bookData;
//
//    }

    @Override
    public int getItemCount() {
        return mBookDetailList.size();
    }
}
