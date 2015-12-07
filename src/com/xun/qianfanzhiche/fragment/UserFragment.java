package com.xun.qianfanzhiche.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.ui.PersonalActivity;
import com.xun.qianfanzhiche.ui.ResetPasswordActivity;
import com.xun.qianfanzhiche.ui.UserBindPhoneActivity;
import com.xun.qianfanzhiche.ui.UserHelperActivity;
import com.xun.qianfanzhiche.utils.CacheUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 用户信息页面
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class UserFragment extends BaseFragment implements OnClickListener {
	private ImageView avaterImg;
	private TextView avaterText;
	private ItemBar sexItemBar, changePasswordItemBar, resetPasswordItemBar, logoutItemBar, userHelperItemBar, userCardItemBar, carItemBar, signatureItemBar,
			bindPhoneItemBar;
	private AlertDialog albumDialog, editDialog, modifyPasswordDialog;

	private ImageLoaderWithCaches mImageLoaderWithCaches;

	private boolean isLogin = false;
	private String dateTime;
	private String iconUrl;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user, null);
		avaterImg = (ImageView) root.findViewById(R.id.avater_img);
		avaterImg.setOnClickListener(this);
		avaterText = (TextView) root.findViewById(R.id.avater_text);
		sexItemBar = (ItemBar) root.findViewById(R.id.sex);
		sexItemBar.setItemBarTitle("性别");
		sexItemBar.setOnClickListener(this);
		changePasswordItemBar = (ItemBar) root.findViewById(R.id.update_password);
		changePasswordItemBar.setItemBarTitle("修改密码");
		changePasswordItemBar.setTopLineVisible();
		changePasswordItemBar.setOnClickListener(this);
		resetPasswordItemBar = (ItemBar) root.findViewById(R.id.change_user);
		resetPasswordItemBar.setItemBarTitle("重置密码");
		resetPasswordItemBar.setOnClickListener(this);
		logoutItemBar = (ItemBar) root.findViewById(R.id.logout);
		logoutItemBar.setItemBarTitle("登出");
		logoutItemBar.setOnClickListener(this);
		userHelperItemBar = (ItemBar) root.findViewById(R.id.user_helper);
		userHelperItemBar.setOnClickListener(this);
		userHelperItemBar.setItemBarTitle("帮助中心");
		userCardItemBar = (ItemBar) root.findViewById(R.id.user_card);
		userCardItemBar.setOnClickListener(this);
		userCardItemBar.setItemBarTitle("我的帖子");
		userCardItemBar.setTopLineVisible();
		carItemBar = (ItemBar) root.findViewById(R.id.car);
		carItemBar.setOnClickListener(this);
		carItemBar.setItemBarTitle("座驾");
		signatureItemBar = (ItemBar) root.findViewById(R.id.user_signature);
		signatureItemBar.setOnClickListener(this);
		signatureItemBar.setItemBarTitle("个性签名");
		bindPhoneItemBar = (ItemBar) root.findViewById(R.id.bind_phone);
		bindPhoneItemBar.setOnClickListener(this);
		bindPhoneItemBar.setItemBarTitle("绑定手机");
		loadData();
		return root;
	}

	private void loadData() {
		mImageLoaderWithCaches = new ImageLoaderWithCaches(getContext(), null, null);
		User user = BmobUser.getCurrentUser(getActivity(), User.class);
		if (user != null) {
			avaterText.setText(user.getUsername());
			sexItemBar.setItemBarContent(user.getSex());
			signatureItemBar.setItemBarContent(user.getSignature());
			carItemBar.setItemBarContent(user.getCar());
			if (user.getMobilePhoneNumberVerified() != null && user.getMobilePhoneNumberVerified()) {
				bindPhoneItemBar.setItemBarContent(user.getMobilePhoneNumber());
			}
			BmobFile avatarFile = user.getAvatar();
			if (avatarFile != null) {
				mImageLoaderWithCaches.setImageFromUrl(avatarFile.getFileUrl(getContext()), avaterImg, R.drawable.user_icon_default);
			}
			isLogin = true;
		} else {
			// ToastUtil.show(getContext(), "未登录");
		}
	}

	// type = 1 性别 2 座驾 3 个性签名
	private void updateUserInfo(final String info, final int type) {
		User user = BmobUser.getCurrentUser(getContext(), User.class);
		switch (type) {
		case 1:
			user.setSex(info);
			break;
		case 2:
			user.setCar(info);
			break;
		case 3:
			user.setSignature(info);
			break;
		default:
			return;
		}
		user.update(getContext(), new UpdateListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(getActivity(), "更新信息成功。");
				if (editDialog != null) {
					editDialog.dismiss();
				}
				switch (type) {
				case 1:
					sexItemBar.setItemBarContent(info);
					break;
				case 2:
					carItemBar.setItemBarContent(info);
					break;
				case 3:
					signatureItemBar.setItemBarContent(info);
					break;
				default:
					return;
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtil.show(getActivity(), "更新信息失败。请检查网络~");
			}
		});
	}

	// type = 1 性别 2 座驾 3 个性签名
	public void showEditDialog(String title, final int type) {
		editDialog = new AlertDialog.Builder(getContext()).create();
		editDialog.setCanceledOnTouchOutside(false);
		View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_useredit, null);
		editDialog.show();
		editDialog.setContentView(v);
		editDialog.getWindow().setGravity(Gravity.CENTER);
		// 只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
		editDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// 加上下面这一行弹出对话框时软键盘随之弹出
		editDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		TextView titleTv = (TextView) v.findViewById(R.id.user_edit_title);
		final EditText editText = (EditText) v.findViewById(R.id.user_edit_edittext);
		Button button = (Button) v.findViewById(R.id.user_edit_btn_ok);

		titleTv.setText(title);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String editString = editText.getText().toString();
				if (StringUtil.isStringNullorBlank(editString)) {
					ToastUtil.show(getContext(), "不能为空噢~");
					return;
				}
				updateUserInfo(editString, type);
			}
		});

	}

	public void showAlbumDialog() {
		albumDialog = new AlertDialog.Builder(getContext()).create();
		albumDialog.setCanceledOnTouchOutside(false);
		View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_usericon, null);
		albumDialog.show();
		albumDialog.setContentView(v);
		albumDialog.getWindow().setGravity(Gravity.CENTER);

		TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
		albumPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		cameraPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}

	public void showModifyPasswordDialog() {
		modifyPasswordDialog = new AlertDialog.Builder(getContext()).create();
		modifyPasswordDialog.setCanceledOnTouchOutside(false);
		View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modifypassword, null);
		modifyPasswordDialog.show();
		modifyPasswordDialog.setContentView(v);
		modifyPasswordDialog.getWindow().setGravity(Gravity.CENTER);
		// 只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
		modifyPasswordDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// 加上下面这一行弹出对话框时软键盘随之弹出
		modifyPasswordDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		final EditText resEditText = (EditText) v.findViewById(R.id.user_edit_edittext);
		final EditText tarEditText = (EditText) v.findViewById(R.id.user_edit_edittext2);
		Button button = (Button) v.findViewById(R.id.user_edit_btn_ok);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String resString = resEditText.getText().toString();
				String tarString = tarEditText.getText().toString();
				if (StringUtil.isStringNullorBlank(resString) || StringUtil.isStringNullorBlank(tarString)) {
					ToastUtil.show(getContext(), "新旧密码都不能为空噢");
					return;
				}
				modifyPassword(resString, tarString);
			}
		});
	}

	public void showLogoutDialog() {
		Builder logoutDialogBuilder = new AlertDialog.Builder(getContext());
		logoutDialogBuilder.setTitle("登出");
		logoutDialogBuilder.setMessage("确定要登出吗？");
		logoutDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				BmobUser.logOut(getContext()); // 清除缓存用户对象
				BmobUser currentUser = BmobUser.getCurrentUser(getContext()); // 现在的currentUser是null了
				if (currentUser == null) {
					ToastUtil.show(getContext(), "登出成功啦");
					isLogin = false;
					resetStatus();
				}
			}
		});
		logoutDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		logoutDialogBuilder.show();
	}

	private void resetStatus() {
		if (!isLogin) {
			avaterText.setText("未登录,点击登录");
			sexItemBar.setItemBarContent("");
			signatureItemBar.setItemBarContent("");
			carItemBar.setItemBarContent("");
			bindPhoneItemBar.setItemBarContent("");
			avaterImg.setImageResource(R.drawable.user_icon_default);
		} else {
			loadData();
		}
	}

	private void modifyPassword(String resPassword, String tarPassword) {
		BmobUser.updateCurrentUserPassword(getContext(), resPassword, tarPassword, new UpdateListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(getContext(), "密码修改成功，可以用新密码进行登录啦");
				modifyPasswordDialog.dismiss();
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getContext(), "密码修改失败,请检查网络或旧密码输入是否正确");
				modifyPasswordDialog.dismiss();
			}
		});
	}

	private void resetPasswordByEmail(final String email) {
		BmobUser.resetPasswordByEmail(getContext(), email, new ResetPasswordByEmailListener() {
			@Override
			public void onSuccess() {
				ToastUtil.show(getContext(), "重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
			}

			@Override
			public void onFailure(int code, String e) {
				ToastUtil.show(getContext(), "重置密码失败:" + e);
			}
		});
	}

	private void getAvataFromCamera() {
		File f = new File(CacheUtil.getCacheDirectory(getContext(), true, "icon") + dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");

		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}

	private void getAvataFromAlbum() {
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private void updateIcon(String avataPath) {
		if (avataPath != null) {
			final BmobFile file = new BmobFile(new File(avataPath));
			file.upload(getContext(), new UploadFileListener() {

				@Override
				public void onSuccess() {
					User currentUser = BmobUser.getCurrentUser(getContext(), User.class);
					currentUser.setAvatar(file);
					currentUser.update(getContext(), new UpdateListener() {

						@Override
						public void onSuccess() {
							ToastUtil.show(getActivity(), "更改头像成功。");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							ToastUtil.show(getActivity(), "更新头像失败。请检查网络~");
						}
					});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					ToastUtil.show(getActivity(), "上传头像失败。请检查网络~");
					LogUtil.d(LogUtil.TAG, "onFailure --> arg0 --> " + arg0 + " arg1 --> " + arg1);
				}
			});
		}
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtil.getCacheDirectory(getContext(), true, "icon") + dateTime + "_12.jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avater_img:
			if (!isLogin) {
				Intent intent = new Intent(getContext(), LoginActivity.class);
				startActivityForResult(intent, 100);
			} else {
				showAlbumDialog();
			}
			break;
		case R.id.sex:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				showEditDialog("设置性别", 1);
			}
			break;
		case R.id.update_password:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				showModifyPasswordDialog();
			}
			break;
		case R.id.change_user:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				User user = BmobUser.getCurrentUser(getActivity(), User.class);
				if (user.getMobilePhoneNumberVerified() != null && user.getMobilePhoneNumberVerified()) {
					Intent resetPwdIntent = new Intent(getActivity(), ResetPasswordActivity.class);
					startActivity(resetPwdIntent);
				} else {
					ToastUtil.show(getContext(), "必须绑定了手机才能重置密码噢");
				}
			}
			break;
		case R.id.user_helper:
			Intent userHelperIntent = new Intent(getActivity(), UserHelperActivity.class);
			startActivity(userHelperIntent);
			break;
		case R.id.user_card:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				Intent intent = new Intent(getContext(), PersonalActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("isFromUserCenter", true);
				getContext().startActivity(intent);
			}
			break;
		case R.id.logout:
			if (isLogin) {
				showLogoutDialog();
			} else {
				ToastUtil.show(getContext(), "没登陆不能登出噢");
			}
			break;
		case R.id.car:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				showEditDialog("您的座驾", 2);
			}
			break;
		case R.id.user_signature:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				showEditDialog("设置个性签名", 3);
			}
			break;
		case R.id.bind_phone:
			if (!isLogin) {
				ToastUtil.show(getContext(), "未登录，请先登录");
			} else {
				User user = BmobUser.getCurrentUser(getActivity(), User.class);
				if (user.getMobilePhoneNumberVerified() != null && user.getMobilePhoneNumberVerified()) {
					ToastUtil.show(getContext(), "这个账号已经绑定了手机号了哟");
				} else {
					Intent bindPhoneIntent = new Intent(getActivity(), UserBindPhoneActivity.class);
					startActivity(bindPhoneIntent);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:
				String files = CacheUtil.getCacheDirectory(getContext(), true, "icon") + dateTime;
				File file = new File(files);
				if (file.exists() && file.length() > 0) {
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				} else {

				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						iconUrl = saveToSdCard(bitmap);
						avaterImg.setImageBitmap(bitmap);
						updateIcon(iconUrl);
					}
				}
				break;
			case 100:
				isLogin = true;
				resetStatus();
				break;
			default:
				break;
			}
		}
	}

}
