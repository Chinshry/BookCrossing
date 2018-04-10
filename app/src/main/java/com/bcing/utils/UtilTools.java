package com.bcing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.utils
 * File Name:UtilTools
 * Describe：工具统一类
 */

public class UtilTools {
    public static void setFont(Context mContext, TextView textview){
        //设置字体
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(),"fonts/LXK.ttf");
        textview.setTypeface(fontType);
    }
//保存图片到ShareUtils
    public static void putImageToShare(Context mContent, ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //1、将Bitmap压缩至字节数组
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        //2、利用Base64将字节数组转换成string
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //3、将String保存到ShareUtils
        ShareUtils.putString(mContent, "image_title", imgString);
    }

//读取图片
    public static void getImageToShare(Context mContent, ImageView imageView){
        String imgString = ShareUtils.getString(mContent, "image_title", "");
        if (!imgString.equals("")) {
            //2、利用Base64将string转化
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
            //3、生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);

        }
    }

}
