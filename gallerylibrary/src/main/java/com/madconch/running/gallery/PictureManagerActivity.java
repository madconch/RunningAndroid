package com.madconch.running.gallery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.base.widget.adapter.SimpleAdapter;
import com.madconch.running.base.widget.adapter.ViewHolder;
import com.madconch.running.ui.toast.MadToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/9.
 * Email:496349136@qq.com
 */

public class PictureManagerActivity extends BaseActivity {
    private static int REQUEST_CODE_CAMERA = 100;
    private int maxCount = 9;
    private String currentPicturePath = null;
    private ArrayList<String> allImages = new ArrayList<>();
    private HashMap<String, ArrayList<String>> folders = new HashMap<>();
    private ArrayList<String> selectedImages = new ArrayList<>();
    private ISelectedFolderListener selectedFolderListener = new ISelectedFolderListener() {
        @Override
        public void onSelectedFolder(FolderBean folderBean) {
            File file = new File(folderBean.getPath());
            adapter.getDatas().clear();
            adapter.getDatas().addAll(folderBean.getChilds());
            adapter.notifyDataSetChanged();
            tvFolder.setText(file.getName());

            hideFolderFragment();
        }
    };

    int themeColor;
    RecyclerView recyclerView;
    SimpleAdapter<String> adapter = new SimpleAdapter<String>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ViewHolder.createViewHolder(parent, R.layout.item_layout_gallery);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String path = getDatas().get(position);
            final ImageView imageView = holder.getView(R.id.iv_image);
            final View mask = holder.getView(R.id.v_mask);
            Glide.with(getContext()).load(getDatas().get(position)).centerCrop().placeholder(R.mipmap.default_project_icon).error(R.mipmap.default_project_icon).into(imageView);
            final AppCompatCheckBox checkBox = holder.getView(R.id.cb_select);

            checkBox.setSupportButtonTintMode(PorterDuff.Mode.SRC_IN);
            ColorStateList colorStateList = ColorStateList.valueOf(themeColor);
            checkBox.setSupportButtonTintList(colorStateList);

            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(selectedImages.contains(path));
            if (checkBox.isChecked()) {
                mask.setAlpha(0.5f);
            } else {
                mask.setAlpha(0.1f);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!selectedImages.contains(path)) {
                            boolean isAddSuccess = trySelectImage(path);
                            if (!isAddSuccess) {
                                checkBox.setChecked(false);
                                return;
                            }
                        }
                    } else {
                        if (selectedImages.contains(path)) {
                            selectedImages.remove(path);
                            updateSelectedText();
                        }
                    }

                    if (isChecked) {
                        mask.setAlpha(0.5f);
                    } else {
                        mask.setAlpha(0.1f);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(">>>preview:" + path);
                }
            });
        }
    };

    TextView tvFolder;
    TextView tvPreview;
    FolderFragment folderFragment = new FolderFragment();
    SimpleAdapter<FolderBean> folderAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        TypedArray typedArray = getContext().obtainStyledAttributes(R.styleable.UIConfigStyle);
        themeColor = typedArray.getColor(R.styleable.UIConfigStyle_uiThemeColor, getContext().getResources().getColor(R.color.theme_color));
        int themeTextColor = typedArray.getColor(R.styleable.UIConfigStyle_uiThemeTextColor, Color.WHITE);
        typedArray.recycle();

        tvFolder = getTitleBar().getTitleView();
        tvFolder.setMaxEms(5);
        tvFolder.setMaxLines(1);
        tvFolder.setEllipsize(TextUtils.TruncateAt.END);
        tvFolder.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        Drawable arrowDrawable = getResources().getDrawable(R.mipmap.arrow_down);
        tvFolder.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);
        DrawableCompat.setTint(arrowDrawable, tvFolder.getCurrentTextColor());
        DrawableCompat.setTintMode(arrowDrawable, PorterDuff.Mode.SRC_IN);


        folderFragment.setSelectedFolderListener(selectedFolderListener);
        folderAdapter = folderFragment.getFolderAdapter();

        setTitle(getResources().getString(R.string.all_picture_folder));
        showBackButton();
        getTitleBar().addRightTextButton(R.string.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", selectedImages);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        tvPreview = (TextView) findViewById(R.id.btn_preview);
        updateSelectedText();
        AppCompatImageView iconCamera = (AppCompatImageView) findViewById(R.id.iv_camera_icon);
        DrawableCompat.setTint(iconCamera.getDrawable(), themeTextColor);
        DrawableCompat.setTintMode(iconCamera.getDrawable(), PorterDuff.Mode.SRC_IN);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/MadConch/" + System.currentTimeMillis() + ".jpg");
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    boolean flag = file.getParentFile().mkdirs();
                    if (!flag) {
                        return;
                    }
                }
                currentPicturePath = file.getAbsolutePath();
                System.out.println(">>>file:" + file.getAbsolutePath());
                Uri uri = FileProvider.getUriForFile(getContext(), getPackageName() + ".fileProvider", file);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())), true));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_SETTLING) {
                    try {
                        Glide.with(getContext()).resumeRequests();
                    } catch (Exception e) {

                    }

                } else {
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });
        initDatas();
        tvFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderFragment.isVisible()) {
                    hideFolderFragment();
                } else {
                    showFolderFragment();
                }
            }
        });
    }

    private boolean trySelectImage(String path) {
        if (selectedImages.size() == maxCount) {
            MadToast.info(getResources().getString(R.string.tips_select_max_count, maxCount));
            return false;
        }

        selectedImages.add(path);
        adapter.notifyDataSetChanged();
        updateSelectedText();
        return true;
    }

    private void updateSelectedText() {
        tvPreview.setText(getResources().getString(R.string.select_format, selectedImages.size(), maxCount));
    }

    private void showFolderFragment() {
        Drawable arrowDrawable = getResources().getDrawable(R.mipmap.arrow_up);
        tvFolder.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);
        DrawableCompat.setTint(arrowDrawable, tvFolder.getCurrentTextColor());
        DrawableCompat.setTintMode(arrowDrawable, PorterDuff.Mode.SRC_IN);
        if (folderFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_in_from_top, R.anim.anim_out_from_top, R.anim.anim_in_from_top, R.anim.anim_out_from_top)
                    .show(folderFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_folder_fragment_container, folderFragment)
                    .setCustomAnimations(R.anim.anim_in_from_top, R.anim.anim_out_from_top, R.anim.anim_in_from_top, R.anim.anim_out_from_top)
                    .show(folderFragment)
                    .commit();
        }
    }

    private void hideFolderFragment() {
        Drawable arrowDrawable = getResources().getDrawable(R.mipmap.arrow_down);
        tvFolder.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);
        DrawableCompat.setTint(arrowDrawable, tvFolder.getCurrentTextColor());
        DrawableCompat.setTintMode(arrowDrawable, PorterDuff.Mode.SRC_IN);
        if (folderFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_in_from_top, R.anim.anim_out_from_top, R.anim.anim_in_from_top, R.anim.anim_out_from_top)
                    .hide(folderFragment)
                    .commit();
        }
    }

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_picture_manager, container, false);
    }

    private void initDatas() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
        } else {
            permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
        }
        new RxPermissions(this)
                .request(permissions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            scanImages();
                        } else {
                            MadToast.info("没有获取到存储或相机权限");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        MadToast.info("没有获取到存储或相机权限");
                    }
                });
    }

    private void scanImages() {
        Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> e) throws Exception {
                HashMap<String, ArrayList<String>> folders = new HashMap<>();
                allImages.clear();
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        File file = new File(path);
                        if (file.exists() && file.canRead()) {
                            String folderPath = file.getParentFile().getAbsolutePath();
                            if (!folders.containsKey(folderPath)) {
                                folders.put(folderPath, new ArrayList<String>());
                            }
                            folders.get(folderPath).add(path);
                            allImages.add(path);
                        }
                    }
                    cursor.close();
                }
                e.onNext(folders);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HashMap<String, ArrayList<String>>>() {
                    @Override
                    public void accept(@NonNull HashMap<String, ArrayList<String>> folders) throws Exception {
                        PictureManagerActivity.this.folders = folders;
                        updateUI();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        MadToast.error("初始化失败");
                    }
                });
    }

    private void updateUI() {
        setTitle(getResources().getString(R.string.all_picture_folder));
        adapter.getDatas().clear();
        adapter.getDatas().addAll(allImages);
        adapter.notifyDataSetChanged();

        updateFolderFragment();
    }

    private void updateFolderFragment() {
        folderAdapter.getDatas().clear();
        FolderBean folderBean = new FolderBean();
        folderBean.setPath(getResources().getString(R.string.all_picture_folder)).setChilds(allImages);
        folderAdapter.getDatas().add(folderBean);
        for (String folderPath : folders.keySet()) {
            folderAdapter.getDatas().add(new FolderBean().setPath(folderPath).setChilds(folders.get(folderPath)));
        }
        folderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            MediaScannerConnection.scanFile(getContext(), new String[]{currentPicturePath}, new String[]{"image/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(final String path, Uri uri) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            trySelectImage(path);
                            initDatas();
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_picture_manager_btn_select, menu);
//        MenuItem itemSelect = menu.getItem(0);
//        tvPreview = (TextView) itemSelect.getActionView().findViewById(R.id.btn_select);
//
//        updateSelectedText();
        return super.onCreateOptionsMenu(menu);
    }
}
