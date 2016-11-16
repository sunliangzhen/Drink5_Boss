package com.toocms.drink5.boss.config;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.toocms.drink5.boss.R;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

import cn.zero.android.common.util.FileManager;
import cn.zero.android.common.util.JSONUtils;

public class UpdateManager {

    private static UpdateManager manager = new UpdateManager();
    private ProgressDialog progressDialog;

    /**
     * 检查更新
     *
     * @param url     效验url
     * @param hasHint 如果是最新版本是否显示提示
     */
    public static final void checkUpdate(String url, boolean hasHint) {
        manager.check(url, hasHint);
    }

    private void check(String url, final boolean hasHint) {
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("package", x.app().getResources().getString(R.string.app_name));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, new ApiListener() {
            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onComplete(RequestParams params, String result) {
                final Map<String, String> map = JSONUtils.parseDataToMap(result);
                // 判断版本号
                if (getVersionCode() >= Integer.parseInt(map.get("version"))) {
                    if (hasHint) Toast.makeText(x.app(), "已是最新版本", Toast.LENGTH_SHORT).show();
                } else {
                    View view = View.inflate(AppManager.getInstance().getTopActivity(), R.layout.dialog_update, null);
                    ((TextView) view.findViewById(R.id.update_description)).setText(map.get("description"));
                    new AlertDialog.Builder(AppManager.getInstance().getTopActivity()).setTitle("发现新版本").setView(view).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownload(map.get("url"));
                        }
                    }).setNegativeButton("以后再说", null).create().show();
                }
            }

            @Override
            public void onError(Map<String, String> error) {
                LogUtil.e(error.get("message"));
            }

            @Override
            public void onException(Throwable ex) {
                ex.printStackTrace();
            }
        });
    }

    private void startDownload(String url) {
        progressDialog = new ProgressDialog(AppManager.getInstance().getTopActivity());
        // 设置不可取消
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setMessage("正在下载...");
        progressDialog.show();
        downloadFile(url);
    }

    // 下载文件
    private synchronized void downloadFile(String url) {
        String path = FileManager.getCompressFilePath() + x.app().getResources().getString(R.string.app_name) + ".apk";
//        File file = new File(path);
//        if (!file.exists()) file.createNewFile();
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(path);
        params.setAutoResume(true);
        params.setExecutor(new PriorityExecutor(2, true));
        params.setCancelFast(false);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgress((int) (current * 100 / total));
            }

            @Override
            public void onSuccess(RequestParams params, File result) {
                installApk(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    // 获取版本号
    private int getVersionCode() {
        try {
            PackageInfo packageInfo = x.app().getPackageManager().getPackageInfo(x.app().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 安装apk
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        x.app().startActivity(intent);
    }
}
