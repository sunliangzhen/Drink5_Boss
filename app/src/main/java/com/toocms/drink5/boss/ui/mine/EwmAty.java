package com.toocms.drink5.boss.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Erwm;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.PreferencesUtils;


/**
 * @author Zero
 * @date 2016/5/20 9:25
 */
public class EwmAty extends BaseAty {

    @ViewInject(R.id.ewm_imgv)
    private ImageView imgv;
    private ImageLoader imageLoader;
    private Erwm erwm;
    private Map<String, String> map;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_ewm;
    }

    @Override
    protected void initialized() {
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        erwm = new Erwm();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        erwm.index(this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        map = JSONUtils.parseDataToMap(result);
        imageLoader.disPlay(imgv, map.get("android"));
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("APP二维码");
    }

    @Event(value = {R.id.fb_ewm_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_ewm_ok:
                String ewm = PreferencesUtils.getString(EwmAty.this, application.getUserInfo().get("c_id"));
                if (ewm != null) {
                    showToast("你已经保存过了");
                    return;
                }
                showProgressDialog();
                downLoadVoice();
                break;
        }
    }

    /**
     * 下载图片
     */
    File tempFile = null;
    String path;

    public void downLoadVoice() {
        try {
            path = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + x.app().getPackageName() + "/cache/sun";
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            tempFile = File.createTempFile("ewm", ".jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(map.get("android"));
        params.setAutoRename(true);

        if (tempFile != null) {
            params.setSaveFilePath(tempFile.getAbsolutePath());
        }

        params.setExecutor(new PriorityExecutor(2, true));
        params.setCancelFast(true);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(RequestParams requestParams, File file) {
                removeProgressDialog();
//                MediaScannerConnection.scanFile(EwmAty.this,
//                        new String[]{file.getAbsolutePath()}, null, null);
                EwmAty.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
                showToast("保存成功");
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
            }

            @Override
            public void onCancelled(CancelledException e) {
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long l, long l1, boolean b) {
            }
        });
    }

}
