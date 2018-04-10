# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\androidrj\sdk\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#使用fastjson，不能混淆泛型和注解
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*

-keep class **.R$* {
    *;
}

-dontwarn android.support.**
-keep class android.support.v4.** { *; }

#不混淆地址选择器的实体类，以便fastjson能正常解析
-keep class cn.qqtheme.framework.entity.** { *;}


