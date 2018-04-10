package com.bcing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.entity.BookData;

import java.util.List;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.adapter
 * File Name:BookAdapter
 * Describe：Application
 */

public class BookAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<BookData> mList;
    private BookData data;
    public BookAdapter(Context mContext,List<BookData> mList){

        this.mContext =mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder viewHolder = null;
        //第一次加载
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.book_set_item,null);

            viewHolder.bookset_title = (TextView) convertView.findViewById(R.id.bookset_title);
            viewHolder.bookset_author = (TextView) convertView.findViewById(R.id.bookset_author);
            viewHolder.bookset_publisher = (TextView) convertView.findViewById(R.id.bookset_publisher);
            viewHolder.bookset_image = (ImageView) convertView.findViewById(R.id.bookset_image);
            //设置缓存
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        data = mList.get(position);
        viewHolder.bookset_title.setText(data.getTitle());
        viewHolder.bookset_author.setText(data.getAuthor());
        viewHolder.bookset_publisher.setText(data.getPublisher());
//        viewHolder.bookset_image.setImageBitmap(data.getImage());

        return convertView;
    }


    class ViewHolder{

        private TextView bookset_title;
        private TextView bookset_author;
        private TextView bookset_publisher;
        private ImageView bookset_image;
    }
}
