# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Retrofit interfaces
-keepattributes Signature
-keepattributes Exceptions

# Keep Gson model classes
-keep class com.example.photogalleryapp.data.remote.dto.** { *; }
