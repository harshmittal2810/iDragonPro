# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Rules for Instamojo SDK
-keep class com.instamojo.android.**{*;}

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Application classes from libraries that will be serialized/deserialized over Gson
 -keep class com.idragonpro.andmagnus.responseModels.** { <fields>; }
 -keep class com.idragonpro.andmagnus.models.** { <fields>; }
 -keep class com.idragonpro.andmagnus.** { <fields>; }

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>



-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class com.yausername.youtubedl_android.** { *; }

-keep class com.hjq.permissions.** {*;}

-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

-keepattributes *Annotation*
#-keep @**annotation** class ** {*;}
-keep class org.apache.http.* {*;}
-dontwarn org.apache.http.**
-keep class org.apache.harmony.** {*;}
-dontwarn org.apache.harmony.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class com.sun.mail.imap.protocol.** {*;}
-dontwarn com.sun.mail.imap.protocol.**
-keep class okio.** {*;}
-dontwarn okio.**
-keep class com.squareup.okhttp.** {*;}
-dontwarn com.squareup.okhttp.**
-keep class javax.activation.** {*;}
-dontwarn javax.activation.**
-keep class org.xmlpull.v1.** { *; }

-dontwarn org.json.**
-keep public class * extends android.app.Application
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class javax.** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#UCrop lib
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep, includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }

-keep, includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl {
*;
}


# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


-dontwarn jp.co.cyberagent.android.gpuimage.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


-keepattributes Signature, InnerClasses, EnclosingMethod
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}


-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-keep class com.xoxoday.reward.** { *; }

-dontwarn com.tbruyelle.rxpermissions.**


-keep class android.databinding.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes *Annotation*
#-keepattributes javax.xml.bind.annotation.*
#-keepattributes javax.annotation.processing.*
-keepclassmembers class * extends java.lang.Enum { *; }
-keepclasseswithmembernames class android.**
-keepclasseswithmembernames interface android.**
-dontobfuscate
-dontwarn

-ignorewarnings

-dontwarn com.sendbird.android.shadow.**


-keep class cn.trinea.android.** { *; }
-keepclassmembers class cn.trinea.android.** { *; }
-dontwarn cn.trinea.android.**
-keep class com.otaliastudios.opengl.program.GlFlatProgram

-keep class org.json.** { *;}

-keep class rx.internal.util.unsafe.** { *;}

-keep class androidx.work.WorkerParameters

-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

-keep class com.yausername.** { *; }
-keep class org.apache.commons.compress.archivers.zip.** { *; }

#-dontwarn org.mockito.**
#-dontwarn sun.reflect.**
#-dontwarn android.test.**
