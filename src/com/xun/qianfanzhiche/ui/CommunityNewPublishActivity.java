package com.xun.qianfanzhiche.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.CacheUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.view.ZhiCheActionBar;
import com.xun.qianfanzhiche.view.ZhiCheActionBar.ActionBarListener;

public class CommunityNewPublishActivity extends BaseActivity implements ActionBarListener, OnClickListener {
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;

	private EditText communityEt;
	private ZhiCheActionBar actionBar;
	private LinearLayout openLayout, takeLayout;
	private ImageView selectedImg;

	private String dateTime;
	private Bitmap bmp = null;
	private String targeturl = null;// 图片路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_publish);
		initView();
	}

	private void initView() {
		communityEt = (EditText) findViewById(R.id.edit_content);
		actionBar = (ZhiCheActionBar) findViewById(R.id.actionbar);
		actionBar.setOnActionBarListener(this);
		openLayout = (LinearLayout) findViewById(R.id.open_layout);
		openLayout.setOnClickListener(this);
		takeLayout = (LinearLayout) findViewById(R.id.take_layout);
		takeLayout.setOnClickListener(this);
		selectedImg = (ImageView) findViewById(R.id.selectedImg);
	}

	// 发表纯文本内容,第一个参数为空，不然调下个函数
	private void publish(String content, BmobFile figureFile) {
		User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);

		final CommunityItem communityItem = new CommunityItem();
		communityItem.setAuthor(user);
		communityItem.setContent(content);
		communityItem.setTitle("我是测试标题");
		if (figureFile != null) {
			communityItem.setImage(figureFile);
		}
		communityItem.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(CommunityNewPublishActivity.this, "发表成功！");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtil.show(CommunityNewPublishActivity.this, "发表失败！yg" + arg1);
			}
		});
	}

	// 发表图文内容
	private void publishWithImg(final String content, String targeturl) {
		final BmobFile figureFile = new BmobFile(new File(targeturl));
		figureFile.upload(getApplicationContext(), new UploadFileListener() {

			@Override
			public void onSuccess() {
				LogUtil.d(LogUtil.TAG, "文件上传成功");
				publish(content, figureFile);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				LogUtil.d(LogUtil.TAG, "文件上传失败 --> " + arg1);
			}
		});
	}

	@Override
	public void onBackImgClick() {
		finish();
	}

	@Override
	public void onAddImgClick() {
		String commitContent = communityEt.getText().toString().trim();
		if (TextUtils.isEmpty(commitContent)) {
			ToastUtil.show(getApplicationContext(), "内容不能为空");
			return;
		}
		if (targeturl == null) {
			publish(commitContent, null);
		} else {
			publishWithImg(commitContent, targeturl);
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.open_layout:
			Date date1 = new Date(System.currentTimeMillis());
			dateTime = date1.getTime() + "";
			Intent intent = null;
			if (Build.VERSION.SDK_INT >= 19) {
				intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			} else {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
			}
			intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, REQUEST_CODE_ALBUM);
			break;
		case R.id.take_layout:

			break;

		default:
			break;
		}
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtil.getCacheDirectory(getApplicationContext(), true, "pic") + dateTime + ".jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ALBUM:
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				if (bmp != null) {
					bmp.recycle();
				}
				try {
					bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				targeturl = saveToSdCard(bmp);
				selectedImg.setImageBitmap(bmp);

				break;
			}
		}
	}
}
