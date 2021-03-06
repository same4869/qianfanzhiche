package com.xun.qianfanzhiche.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class CommunityItem extends BmobObject {
	private static final long serialVersionUID = 1784714727591697836L;

	private String title;// 帖子标题

	private String content;// 帖子内容

	private User author;// 帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

	private BmobFile image;// 帖子图片

	private BmobRelation likes;// 多对多关系：用于存储喜欢该帖子的所有用户

	private BmobRelation relation;// 评论

	private boolean myFav;// 收藏

	private boolean myLove;// 赞

	private int love;

	private boolean isHaveNewComment;// 如果有新评论，则在消息中心中显示该帖子

	public boolean isHaveNewComment() {
		return isHaveNewComment;
	}

	public void setHaveNewComment(boolean isHaveNewComment) {
		this.isHaveNewComment = isHaveNewComment;
	}

	public boolean isMyFav() {
		return myFav;
	}

	public void setMyFav(boolean myFav) {
		this.myFav = myFav;
	}

	public boolean isMyLove() {
		return myLove;
	}

	public void setMyLove(boolean myLove) {
		this.myLove = myLove;
	}

	public BmobRelation getRelation() {
		return relation;
	}

	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public BmobFile getImage() {
		return image;
	}

	public void setImage(BmobFile image) {
		this.image = image;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

	public int getLove() {
		return love;
	}

	public void setLove(int love) {
		this.love = love;
	}

}
