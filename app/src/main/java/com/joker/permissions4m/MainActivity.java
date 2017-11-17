package com.joker.permissions4m;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.Wrapper;
import com.joker.permissions4m.other.ToastUtil;

import static com.joker.permissions4m.MainActivity.CALENDAR_CODE;
import static com.joker.permissions4m.MainActivity.LOCATION_CODE;
import static com.joker.permissions4m.MainActivity.SENSORS_CODE;

/**
 * 二次授权时回调，用于解释为何需要此权限——可以传一个也可以传多个解释为何申请权限所对应的int类型的code授权失败时回调————可以传一个也可以传多个失败申请的权限所对应的int类型的code授权成功时候的回调——可以传一个也可以传多个需要申请的权限所对应的int类型的code拒绝权限且不再提示（国产畸形权限适配扩展）情况下调用无论是 @PermissionsCustomRationale 或者 @PermissionsRationale 都不会被调用，无法给予用户提示二次授权时回调，用于解释为何需要此权限——可以传一个也可以传多个解释为何申请权限所对应的int类型的code在activity或者fragment上面填写——会同步请求
 * 授权失败时回调————可以传一个也可以传多个失败申请的权限所对应的int类型的code授权成功时候的回调——可以传一个也可以传多个需要申请的权限所对应的int类型的code拒绝权限且不再提示（国产畸形权限适配扩展）情况下调用无论是 @PermissionsCustomRationale 或者 @PermissionsRationale 都不会被调用，无法给予用户提示二次授权时回调，用于解释为何需要此权限——可以传一个也可以传多个解释为何申请权限所对应的int类型的code在activity或者fragment上面填写——会同步请求
 */
/**授权失败时回调————可以传一个也可以传多个失败申请的权限所对应的int类型的code*/
/**授权成功时候的回调——可以传一个也可以传多个需要申请的权限所对应的int类型的code*/
/**拒绝权限且不再提示（国产畸形权限适配扩展）情况下调用*/
/**无论是 @PermissionsCustomRationale 或者 @PermissionsRationale 都不会被调用，无法给予用户提示*/
/**二次授权时回调，用于解释为何需要此权限——可以传一个也可以传多个解释为何申请权限所对应的int类型的code*/
/**在activity或者fragment上面填写——会同步请求*/

/**
 * 此类需要用到的权限
 */
@PermissionsRequestSync(permission = {Manifest.permission.BODY_SENSORS, Manifest.permission
        .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE})
public class MainActivity extends AppCompatActivity {
    public static final int CALENDAR_CODE = 7;
    public static final int SENSORS_CODE = 8;
    public static final int LOCATION_CODE = 9;

    private static final int CALL_LOG_CODE = 2;
    private static final int AUDIO_CODE = 6;
    private static final int READ_CONTACTS_CODE = 10;


    private Button mCallButton;
    private Button mAudioButton;
    private Button mOneButton;
    private Button mManagerButton;
    private Button mPermissionPageButton;
    private Button mPageListenerButton;


    /**需要重写此方法*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        /**过滤*/
        Permissions4M.onRequestPermissionsResult(MainActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**通话记录的请求*/
        tonghuajilu();
        /**录音*/
        luyin();
        /**传感器、地理位置、日历权限的一键申请*/
        onejianshengqing();
        /**手机管家页面*/
        shoujiguanjia();
        /**系统设置页面*/
        xitongshezhi();
        /**读取通讯录权限——通过监听的方式*/
        tongxunlu_listener();
    }

    /**********************************************1.通讯录界面通过监听的方式start***********************************************************/
    private void tongxunlu_listener() {
        mPageListenerButton = (Button) findViewById(R.id.btn_page_listener);
        mPageListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestPermissions(Manifest.permission.READ_CONTACTS)
                        .requestCodes(READ_CONTACTS_CODE)
                        /**添加申请权限的回调——成功、失败、二次申请*/
                        .requestListener(new ListenerWrapper.PermissionRequestListener() {
                            @Override
                            public void permissionGranted(int code) {
                                ToastUtil.show("读取通讯录权限成功 in activity with listener");
                            }
                            @Override
                            public void permissionDenied(int code) {
                                ToastUtil.show("读取通讯录权失败 in activity with listener");
                            }
                            @Override
                            public void permissionRationale(int code) {
                                ToastUtil.show("请打开读取通讯录权限 in activity with listener");
                            }
                        })
                        /**跳到手机系统设置界面去开启权限*/
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        /**指定了上面的选用系统设置界面，下面的intent就是对应的*/
                        .requestPage(new Wrapper.PermissionPageListener() {
                            @Override
                            public void pageIntent(int code, final Intent intent) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("用户您好，我们需要您开启读取通讯录权限申请：\n请点击前往设置页面\n(in activity with" +
                                                " listener)")
                                        .setPositiveButton("前往设置页面", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /**点击了确认了之后，会直接跳转到对应intent*/
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .request();
            }
        });
    }
    /**********************************************1.通讯录界面通过监听的方式end***********************************************************/

    /**********************************************2.系统设置页面start***********************************************************/
    private void xitongshezhi() {
        mPermissionPageButton = (Button) findViewById(R.id.btn_permission_page);
        mPermissionPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**获取的是系统设置的实例对象*/
                startActivity(PermissionsPageManager.getSettingIntent(MainActivity.this));
            }
        });
    }
    /**********************************************2.系统设置页面end***********************************************************/

    /**********************************************3.手机管家页面start***********************************************************/
    private void shoujiguanjia() {
        mManagerButton = (Button) findViewById(R.id.btn_manager);
        mManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**获取的是手机管家的实例对象*/
                startActivity(PermissionsPageManager.getIntent(MainActivity.this));
            }
        });
    }
    /**********************************************3.手机管家页面end***********************************************************/


    /**********************************************4.传感器、日历、地理位置一键申请start***********************************************************/
    private void onejianshengqing() {
        mOneButton = (Button) findViewById(R.id.btn_one);
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M
                        .get(MainActivity.this)
                        /**调用的是activity上的同步请求——会同时执行3个请求的申请*/
                        .requestSync();
            }
        });
    }
    /**
     * 授权成功时候的回调——此处对应的是授权3个危险权限
     */
    @PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  地理位置权限授权成功 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  传感器权限授权成功 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  读取日历权限授权成功 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  地理位置权限授权失败 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  传感器权限授权失败 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  读取日历权限授权失败 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("请开启地理位置权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启地理位置权限 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtil.show("请开启传感器权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启传感器权限 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启读取日历权限 in activity with annotation");
                break;
            default:
                break;
        }
    }
    /**********************************************4.传感器、日历、地理位置一键申请end***********************************************************/

    /**********************************************5.录音start***********************************************************/
    private void luyin() {
        mAudioButton = (Button) findViewById(R.id.btn_audio);
        // 录音
        mAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        .requestPermissions(Manifest.permission.RECORD_AUDIO)
                        .requestCodes(AUDIO_CODE)
                        .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
                        .request();
            }
        });
    }

    /**
     * 授权成功时候的回调
     */
    @PermissionsGranted(AUDIO_CODE)
    public void smsAndAudioGranted() {
        ToastUtil.show("录音权限申请成功 in activity with annotation");
    }

    /**授权失败*/
    @PermissionsDenied(AUDIO_CODE)
    public void smsAndAudioDenied() {
        ToastUtil.show("录音权限申请失败 in activity with annotation");
    }

    /**二次授权*/
    @PermissionsCustomRationale(AUDIO_CODE)
    public void smsAndAudioCustomRationale() {
        new AlertDialog.Builder(this)
                .setMessage("录音权限申请：\n我们需要您开启录音权限(in activity with annotation)")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Permissions4M.get(MainActivity.this)
                                /**加上这句话，代表二次申请录音的权限*/
                                .requestOnRationale()
                                .requestPermissions(Manifest.permission.RECORD_AUDIO)
                                .requestCodes(AUDIO_CODE)
                                .request();
                    }
                })
                .show();
    }
    /**********************************************5.录音end***********************************************************/




    /**********************************************6.通话记录start***********************************************************/
    private void tonghuajilu() {
        mCallButton = (Button) findViewById(R.id.btn_calendar);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainActivity.this)
                        /**是否强制弹出*/
                        .requestForce(true)
                        .requestPermissions(Manifest.permission.READ_CALL_LOG)
                        .requestCodes(CALL_LOG_CODE)
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .request();
            }
        });
    }

    /**
     * 授权成功时候的回调
     */
    @PermissionsGranted(CALL_LOG_CODE)
    public void storageAndCallGranted() {
        ToastUtil.show("读取通话记录权限授权成功 in activity with annotation");
    }

    /**
     * 授权失败
     */
    @PermissionsDenied(CALL_LOG_CODE)
    public void storageAndCallDenied() {
        ToastUtil.show("读取通话记录权限授权失败 in activity with annotation");
    }

    /**
     * 二次授权
     */
    @PermissionsRationale(CALL_LOG_CODE)
    public void storageAndCallNonRationale() {
        ToastUtil.show("请开启读取通话记录权限授权 in activity with annotation");
    }
    /**********************************************6.通话记录end***********************************************************/

    /**********************************************7.权限申请被禁止后start***********************************************************/
    @PermissionsNonRationale({AUDIO_CODE, CALL_LOG_CODE})
    public void storageAndCallRationale(int code, final Intent intent) {
        switch (code) {
            case AUDIO_CODE:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("用户您好，我们需要您开启读取录音权限\n请点击前往设置页面\n(in activity with listener)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case CALL_LOG_CODE:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("用户您好，我们需要您开启读取通话记录权限\n请点击前往设置页面\n(in activity with listener)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
    /**********************************************7.权限申请被禁止后end***********************************************************/
}
