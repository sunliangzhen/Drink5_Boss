package com.toocms.drink5.boss.ui.myselectorimg;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.myselectorimg.adapter.FolderAdapter;
import com.toocms.drink5.boss.ui.myselectorimg.adapter.ImageGridAdapter;
import com.toocms.drink5.boss.ui.myselectorimg.bean.Folder;
import com.toocms.drink5.boss.ui.myselectorimg.bean.Image;
import com.toocms.frame.config.Settings;
import com.toocms.frame.ui.imageselector.utils.FileUtils;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import cn.zero.android.common.util.FileManager;
import cn.zero.android.common.util.ListUtils;
import cn.zero.android.common.view.ucrop.UCrop;

/**
 * @author Zero
 * @date 2016/5/26 14:55
 */
public class Myselectorimg extends BaseAty {

    @ViewInject(R.id.myimg_cbox_all)
    private CheckBox myimg_cbox_all;
    @ViewInject(R.id.myimg_cbox_next)
    private TextView myimg_cbox_next;
    @ViewInject(R.id.myimg_gv)
    private GridView myimg_gv;
    @ViewInject(R.id.myimg_relay_footer)
    private RelativeLayout relay_footer;

    private ArrayList<String> resultList = new ArrayList();
    private ArrayList<Folder> resultFolder = new ArrayList();
    private ImageGridAdapter imageAdapter;
    private FolderAdapter folderAdapter;
    private boolean hasFolderGened = false;
    private boolean isShowCamera = true;
    private int mode = 1;
    private int desireImageCount = 9;
    private File tmpFile;
    private ListPopupWindow folderPopupWindow;
    private boolean isCamerato;

    private File outPutFile;
    private float ratioX;
    private float ratioY;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_myimg;
    }

    @Override
    protected void initialized() {
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        inti();
        imageAdapter = new ImageGridAdapter(this, true);
        imageAdapter.showSelectIndicator(this.mode == 1);
        this.folderAdapter = new FolderAdapter(this);
        this.getSupportLoaderManager().initLoader(0, (Bundle) null, this.loaderCallback);
        myimg_gv.setAdapter(this.imageAdapter);
        myimg_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Image image;
                if (Myselectorimg.this.imageAdapter.isShowCamera()) {
                    if (i == 0) {
                        if (mode == 1) {
                            if (resultList.size() < desireImageCount) {
                                Myselectorimg.this.showCameraAction();
                            } else {
                                showToast("已达最高数量");
                            }
                        }else{
                            Myselectorimg.this.showCameraAction();
                        }
                    } else {
                        image = (Image) adapterView.getAdapter().getItem(i);
                        Myselectorimg.this.selectImageFromGrid(image, Myselectorimg.this.mode);
                    }
                } else {
                    image = (Image) adapterView.getAdapter().getItem(i);
                    Myselectorimg.this.selectImageFromGrid(image, Myselectorimg.this.mode);
                }

            }
        });

        if (savedInstanceState != null) {
            this.tmpFile = (File) savedInstanceState.getSerializable("key_temp_file");
        }
        myimg_cbox_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (folderPopupWindow == null) {
                    createPopupFolderList();
                } else {
                    if (isCamerato) {
                        folderPopupWindow = null;
                        createPopupFolderList();
                        isCamerato = false;
                    }
                }
                if (folderPopupWindow.isShowing()) {
                    folderPopupWindow.dismiss();
                } else {
                    if (folderPopupWindow != null) {
                        folderPopupWindow.show();
                        int index = folderAdapter.getSelectIndex();
                        index = index == 0 ? index : index - 1;
                        folderPopupWindow.getListView().setSelection(index);
                    } else {
                    }
                }

            }
        });
        myimg_cbox_all.setText("选择相片");
    }

    private void inti() {
        Intent intent = this.getIntent();
        this.desireImageCount = intent.getIntExtra("max_select_count", 1);
        this.mode = intent.getIntExtra("select_count_mode", 1);
//        this.isShowCamera = intent.getBooleanExtra("show_camera", true);
//        if(this.mode == 1 && intent.hasExtra("default_list")) {
//        this.resultList = intent.getStringArrayListExtra("default_list");
//        }
        this.ratioX = intent.getFloatExtra("com.toocms.frame.ui.AspectRatioX", 1.0F);
        this.ratioY = intent.getFloatExtra("com.toocms.frame.ui.AspectRatioY", 1.0F);
    }

    private void createPopupFolderList() {
        Myselectorimg.this.resultFolder.clear();
        Myselectorimg.this.getSupportLoaderManager().restartLoader(0, (Bundle) null, Myselectorimg.this.loaderCallback);
        int width = Settings.displayWidth;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int dimensionPixelSize = 0;
        if (resourceId > 0) {
            dimensionPixelSize = getResources().getDimensionPixelSize(resourceId);
        }
        int height = (int) ((float) Settings.displayHeight) - AutoUtils.getPercentHeightSize(88) - dimensionPixelSize;
        this.folderPopupWindow = new ListPopupWindow(this);
        this.folderPopupWindow = new ListPopupWindow(this);
        this.folderPopupWindow.setBackgroundDrawable(new ColorDrawable(0xffc8c8c8));
//        this.folderPopupWindow.setBackgroundDrawable(getResources().getColor(R.color.aaa));
        this.folderPopupWindow.setAdapter(this.folderAdapter);
        this.folderPopupWindow.setContentWidth(width);
        this.folderPopupWindow.setWidth(width);
        this.folderPopupWindow.setHeight(height);
        this.folderPopupWindow.setAnchorView(this.relay_footer);
        this.folderPopupWindow.setModal(true);
        this.folderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                myimg_cbox_all.setChecked(false);
            }
        });
        this.folderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                Myselectorimg.this.folderAdapter.setSelectIndex(i);
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        Myselectorimg.this.folderPopupWindow.dismiss();
                        if (i == 0) {
                            Myselectorimg.this.resultFolder.clear();
                            Myselectorimg.this.getSupportLoaderManager().restartLoader(0, (Bundle) null, Myselectorimg.this.loaderCallback);
//                            Myselectorimg.this.tvCategory.setText("所有图片");
                            if (Myselectorimg.this.isShowCamera) {
                                Myselectorimg.this.imageAdapter.setShowCamera(true);
                            } else {
                                Myselectorimg.this.imageAdapter.setShowCamera(false);
                            }
                        } else {
                            Folder folder = (Folder) adapterView.getAdapter().getItem(i);
                            if (null != folder) {
                                Myselectorimg.this.imageAdapter.setData(folder.images);
//                                Myselectorimg.this.myimg_cbox_all.setText("选择图片");
                                if (Myselectorimg.this.resultList != null && Myselectorimg.this.resultList.size() > 0) {
                                    Myselectorimg.this.imageAdapter.setDefaultSelected(Myselectorimg.this.resultList);
                                }
                            }

                            Myselectorimg.this.imageAdapter.setShowCamera(false);
                        }

                        Myselectorimg.this.myimg_gv.smoothScrollToPosition(0);
                    }
                }, 100L);
            }
        });
    }

    private void showCameraAction() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            try {
                this.tmpFile = FileUtils.createTmpFile(this);
            } catch (IOException var3) {
                var3.printStackTrace();
            }
            if (this.tmpFile != null && this.tmpFile.exists()) {
                cameraIntent.putExtra("output", Uri.fromFile(this.tmpFile));
                this.startActivityForResult(cameraIntent, 100);
            } else {
                Toast.makeText(this, "图片错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "没有系统相机", Toast.LENGTH_SHORT).show();
        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 100);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case -1:
                switch (requestCode) {
                    case 69:
                        this.handleCrop(data);
                        return;
                    case 100:
                        isCamerato = true;
                        if (this.tmpFile != null) {
                            this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(this.tmpFile)));
                            if (this.mode == 0) {
                                this.beginCrop(this.tmpFile);
                            } else {
                                Uri localUri = Uri.fromFile(tmpFile);
                                Intent cropError1 = new Intent();
                                this.resultList.add(this.tmpFile.getAbsolutePath());
                                myimg_cbox_next.setText(String.format("%s(%d/%d)", new Object[]{"完成", Integer.valueOf(this.resultList.size()), Integer.valueOf(this.desireImageCount)}));
//                                cropError1.putStringArrayListExtra("select_result", this.resultList);
//                                this.setResult(-1, cropError1);
//                                this.finish();
                            }
                            return;
                        }

                        return;
                    default:
                        return;
                }
            case 96:
                Throwable cropError = UCrop.getError(data);
                if (cropError != null) {
                    LogUtil.e(cropError.getMessage());
                } else {
                    LogUtil.e("纯属意外，再试一次！");
                }
                break;
            default:
                while (this.tmpFile != null && this.tmpFile.exists()) {
                    boolean success = this.tmpFile.delete();
                    if (success) {
                        this.tmpFile = null;
                    }
                }
        }

    }

    private void handleCrop(Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        Intent data = new Intent();
        this.resultList.add(resultUri.getPath());
        data.putStringArrayListExtra("select_result", this.resultList);
        this.setResult(-1, data);
        this.finish();
    }

    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            if (mode == 1) {
                if (this.resultList.contains(image.path)) {
                    this.resultList.remove(image.path);
                    myimg_cbox_next.setText(String.format("%s(%d/%d)", new Object[]{"完成", Integer.valueOf(this.resultList.size()), Integer.valueOf(this.desireImageCount)}));
                    if (ListUtils.isEmpty(this.resultList)) {
                        myimg_cbox_next.setText("完成");
                        myimg_cbox_next.setEnabled(false);
                    }
                } else {
                    if (this.desireImageCount == this.resultList.size()) {
                        Toast.makeText(this, "已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!this.resultList.contains(image.path)) {
                        this.resultList.add(image.path);
                    }
                    if (!ListUtils.isEmpty(this.resultList)) {
                        myimg_cbox_next.setText(String.format("%s(%d/%d)", new Object[]{"完成", Integer.valueOf(this.resultList.size()), Integer.valueOf(this.desireImageCount)}));
                        if (!myimg_cbox_next.isEnabled()) {
                            myimg_cbox_next.setEnabled(true);
                        }
                    }
                }
                this.imageAdapter.select(image);
            } else if (mode == 0) {
                this.beginCrop(new File(image.path));
            }
        }
    }

    private void beginCrop(File sourceFile) {
        if (this.outPutFile == null) {
            try {
                this.outPutFile = new File(FileManager.getCompressFilePath() + System.currentTimeMillis() + ".jpg");
                this.outPutFile.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        Uri outputUri = Uri.fromFile(this.outPutFile);
        UCrop.of(Uri.fromFile(sourceFile), outputUri).withAspectRatio(this.ratioX, this.ratioY).start(this);
    }

    @Event(value = {R.id.myimg_imgv_back, R.id.myimg_cbox_all, R.id.myimg_cbox_next})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.myimg_imgv_back:
                finish();
                break;
            case R.id.myimg_cbox_all:

                break;
            case R.id.myimg_cbox_next:
                if (resultList != null && resultList.size() > 0) {
                    Intent data = new Intent();
                    data.putStringArrayListExtra("select_result", resultList);
                    this.setResult(-1, data);
                    this.finish();
                }
                break;
        }
    }


    LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = new String[]{"_data", "_display_name", "date_added", "mime_type", "_size", "_id"};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            resultFolder.clear();
            CursorLoader cursorLoader;
            if (id == 0) {
                cursorLoader = new CursorLoader(Myselectorimg.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, this.IMAGE_PROJECTION[4] + ">0 AND " + this.IMAGE_PROJECTION[3] + "=? OR " + this.IMAGE_PROJECTION[3] + "=? ", new String[]{"image/jpeg", "image/png"}, this.IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == 1) {
                cursorLoader = new CursorLoader(Myselectorimg.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, this.IMAGE_PROJECTION[4] + ">0 AND " + this.IMAGE_PROJECTION[0] + " like \'%" + args.getString("path") + "%\'", (String[]) null, this.IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else {
                return null;
            }
        }

        private boolean fileExist(String path) {
            return !TextUtils.isEmpty(path) ? (new File(path)).exists() : false;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() > 0) {
                ArrayList images = new ArrayList();
                data.moveToFirst();
                do {
                    String path = data.getString(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[2]));
                    Image image = null;
                    if (this.fileExist(path)) {
                        image = new Image(path, name, dateTime);
                        images.add(image);
                    }
                    if (!Myselectorimg.this.hasFolderGened) {
//                        resultFolder.clear();
                        File folderFile = (new File(path)).getParentFile();
                        if (folderFile != null && folderFile.exists()) {
                            String fp = folderFile.getAbsolutePath();
                            Folder f = Myselectorimg.this.getFolderByPath(fp);
                            if (f == null) {
                                Folder folder = new Folder();
                                folder.name = folderFile.getName();
                                folder.path = fp;
                                folder.cover = image;
                                ArrayList imageList = new ArrayList();
                                imageList.add(image);
                                folder.images = imageList;
                                Myselectorimg.this.resultFolder.add(folder);
                            } else {
                                f.images.add(image);
                            }
                        }
                    }
                } while (data.moveToNext());
                Myselectorimg.this.imageAdapter.setData(images);
                if (Myselectorimg.this.resultList != null && Myselectorimg.this.resultList.size() > 0) {
                    Myselectorimg.this.imageAdapter.setDefaultSelected(Myselectorimg.this.resultList);
                }

                if (!Myselectorimg.this.hasFolderGened) {
                    Myselectorimg.this.folderAdapter.setData(Myselectorimg.this.resultFolder);
//                    Myselectorimg.this.hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            resultFolder.clear();
        }
    };

    private Folder getFolderByPath(String path) {
        if (this.resultFolder != null) {
            Iterator var2 = this.resultFolder.iterator();
            while (var2.hasNext()) {
                Folder folder = (Folder) var2.next();
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }
}
