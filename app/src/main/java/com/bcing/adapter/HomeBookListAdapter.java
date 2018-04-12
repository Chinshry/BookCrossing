package com.bcing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.bean.http.douban.BookInfoResponse;
import com.bcing.entity.CrossBookData;
import com.bcing.ui.activity.CrossBookDetailActivity;
import com.bcing.utils.common.UIUtils;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.util.List;

import static com.bcing.utils.common.UIUtils.startActivity;

/**
 * Created by 57010 on 2017/5/17.
 */

public class HomeBookListAdapter extends RecyclerView.Adapter<HomeBookListAdapter.ViewHolder> {
    private Context mContext;
    private String array[] = {""};
    private float rating;

    private List<CrossBookData> mBookList;
//    private final BookInfoResponse bookInfoResponses = new BookInfoResponse();
    private  List<BookInfoResponse> bookData;

    private static final String TAG = "booklistAdapter";
//    private List<ItemBookDetail> ItemBookList = new ArrayList<>();


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

    public HomeBookListAdapter(Context context, List<CrossBookData> ItemBookListadpter) {
        this.mBookList = ItemBookListadpter;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_set_item, parent, false);
        //ViewHolder holder = new ViewHolder(view);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //注意 排序混乱解决
        final CrossBookData booklist = mBookList.get(position);

//        final BookInfoResponse bookInfo = bookInfoResponses.get(position);

        KLog.e("TAG", "position是多少啊"+position);

        holder.username.setText(booklist.getUsername());
        holder.bookName.setText(booklist.getBookName());
        holder.author.setText(booklist.getAuthor());
        holder.publish.setText(booklist.getpublish());
        holder.city.setText(booklist.getcity());
        Glide.with(mContext).load(booklist.getBookImage()).into((holder).bookImage);
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                KLog.e("TAG", "別點了"+ booklist.getIsbn() + booklist.getBookName());
//                //解析接口
//                String url = URL.HOST_URL_DOUBAN_ISBN + booklist.getIsbn();
//                KLog.e("TAG", "URL " + url);

//                bookData = new ArrayList<>((booklist) .getBooks());
//                final BookInfoResponse bookInfoResponses =bookData.get(0);
//                bookData.addAll((booklist) .getBooks());

                KLog.e("TAG", "查看详情" + booklist.getBookName()+booklist.getPubdate());

                Bundle b = new Bundle();
                b.putSerializable(CrossBookData.serialVersionName, booklist);

                //书籍详情
                Intent intent = new Intent(UIUtils.getContext(), CrossBookDetailActivity.class);
                intent.putExtras(b);
                startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
