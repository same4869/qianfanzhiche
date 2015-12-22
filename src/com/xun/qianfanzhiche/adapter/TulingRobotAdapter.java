package com.xun.qianfanzhiche.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.TulingRobotBean;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.utils.TimeUtil;

public class TulingRobotAdapter extends BaseContentAdapter<TulingRobotBean> {
	private List<TulingRobotBean> beans = null;
	private final int ITEM_TYPES = 2, TYPE_0 = 0, TYPE_1 = 1;
	private BmobFile avatarFile;
	private String avaterUrl;

	private ImageLoaderWithCaches mImageLoaderWithCaches;

	public TulingRobotAdapter(Context context, List<TulingRobotBean> list) {
		super(context, list);
		this.beans = list;
		mImageLoaderWithCaches = new ImageLoaderWithCaches(context, null, null);
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			avatarFile = user.getAvatar();
			if (avatarFile != null) {
				avaterUrl = avatarFile.getFileUrl(context);
			}
		}
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		PeopleView s = null;
		TulingRobotBean bean = beans.get(position);
		int type = getItemViewType(position);

		if (convertView == null) {
			s = new PeopleView();
			switch (type) {
			case TYPE_0:
				convertView = mInflater.inflate(R.layout.listview_item_teacher, null);
				break;
			case TYPE_1:
				convertView = mInflater.inflate(R.layout.listview_item_student, null);
				break;
			}

			s.time = (TextView) convertView.findViewById(R.id.Time);
			s.message = (TextView) convertView.findViewById(R.id.Msg);
			s.portrait = (ImageView) convertView.findViewById(R.id.Img);
			convertView.setTag(s);
		} else {
			s = (PeopleView) convertView.getTag();
		}
		s.time.setText(TimeUtil.getCurrentTime(new Date().getTime()));
		s.message.setText(bean.gettMessage());
		if (type == TYPE_0) {
			if (avaterUrl != null) {
				mImageLoaderWithCaches.setImageFromUrl(avaterUrl, s.portrait, R.drawable.user_icon_default);
			} else {
				s.portrait.setImageResource(R.drawable.user_icon_default);
			}
		} else {
			s.portrait.setImageResource(R.drawable.qianfanlengleng);
		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_TYPES;
	}

	@Override
	public int getItemViewType(int position) {
		int tp = beans.get(position).getId();
		if (TYPE_0 == tp)
			return TYPE_0;
		else if (TYPE_1 == tp)
			return TYPE_1;
		return TYPE_0;
	}

	class PeopleView {
		TextView time;
		TextView message;
		ImageView portrait;
	}

	public void addItemNotifiChange(TulingRobotBean bean) {
		beans.add(bean);
		notifyDataSetChanged();
	}

}
