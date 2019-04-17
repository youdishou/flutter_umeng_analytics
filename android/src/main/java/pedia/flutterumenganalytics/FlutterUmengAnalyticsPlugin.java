package pedia.flutterumenganalytics;

import android.app.Activity;
import android.content.Context;
import com.umeng.analytics.*;
import com.umeng.commonsdk.UMConfigure;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterUmengAnalyticsPlugin */
public class FlutterUmengAnalyticsPlugin implements MethodCallHandler {
  private Activity activity;
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel =
        new MethodChannel(registrar.messenger(), "flutter_umeng_analytics");
    channel.setMethodCallHandler(new FlutterUmengAnalyticsPlugin(registrar.activity()));
  }

  private FlutterUmengAnalyticsPlugin(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("init")) {
      init(call, result);
    } else if (call.method.equals("beginPageView")) {
      MobclickAgent.onPageStart((String) call.argument("name"));
      result.success(null);
    } else if (call.method.equals("endPageView")) {
      MobclickAgent.onPageEnd((String) call.argument("name"));
      result.success(null);
    } else if (call.method.equals("logEvent")) {
      MobclickAgent.onEvent((Context) activity, (String) call.argument("name"));
      result.success(null);
    } else {
      result.notImplemented();
    }
  }

  public void init(MethodCall call, Result result) {
    UMConfigure.init((Context) activity, (String) call.argument("key"), (String) call.argument("channel"),
            UMConfigure.DEVICE_TYPE_PHONE, null);

    UMConfigure.setEncryptEnabled((Boolean) call.argument("encrypt"));
    UMConfigure.setLogEnabled(false);
    MobclickAgent.setScenarioType((Context) activity, MobclickAgent.EScenarioType.E_DUM_NORMAL);
    MobclickAgent.setSessionContinueMillis((Context) activity, (Integer) call.argument("interval"));

    result.success(true);
  }
}
