# ProGuard rules for LifeHub

# Keep Retrofit and OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Keep Retrofit API interfaces
-keep class com.lifehub.app.api.** { *; }

# Keep Gson models
-keep class com.lifehub.app.api.** { <fields>; }

# Keep Android components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends androidx.fragment.app.Fragment

# Keep ViewModel
-keep class * extends androidx.lifecycle.ViewModel

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
