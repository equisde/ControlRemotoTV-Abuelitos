-keep class com.remotetv.control.** { *; }
-keep class androidx.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep class com.google.gson.** { *; }

-dontwarn android.**
-dontwarn androidx.**
-dontwarn com.squareup.okhttp3.**
-dontwarn com.google.gson.**

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
