package com.xun.qianfanzhiche.bean;

import java.io.Serializable;
import java.util.List;

public class ShiTuBean implements Serializable {

	private static final long serialVersionUID = 697078583886343896L;

	private ExtraBean extra;
	private DataBean data;

	public ExtraBean getExtra() {
		return extra;
	}

	public void setExtra(ExtraBean extra) {
		this.extra = extra;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public class ExtraBean implements Serializable {

		private static final long serialVersionUID = -6480588623806178800L;
		private String samenum;
		private String errno;

		public String getSamenum() {
			return samenum;
		}

		public void setSamenum(String samenum) {
			this.samenum = samenum;
		}

		public String getErrno() {
			return errno;
		}

		public void setErrno(String errno) {
			this.errno = errno;
		}

	}

	public class DataBean implements Serializable {

		private static final long serialVersionUID = -8897159552517505940L;
		private List<String> guessWord;
		private List<SameInfoBean> sameInfo;

		public List<String> getGuessWord() {
			return guessWord;
		}

		public void setGuessWord(List<String> guessWord) {
			this.guessWord = guessWord;
		}

		public List<SameInfoBean> getSameInfo() {
			return sameInfo;
		}

		public void setSameInfo(List<SameInfoBean> sameInfo) {
			this.sameInfo = sameInfo;
		}

		public class SameInfoBean implements Serializable {

			private static final long serialVersionUID = 7680393903195506828L;
			private String objURL;
			private String fromURL;
			private String fromPageTitle;
			private String width;
			private String height;
			private String fromURLHost;

			public String getObjURL() {
				return objURL;
			}

			public void setObjURL(String objURL) {
				this.objURL = objURL;
			}

			public String getFromURL() {
				return fromURL;
			}

			public void setFromURL(String fromURL) {
				this.fromURL = fromURL;
			}

			public String getFromPageTitle() {
				return fromPageTitle;
			}

			public void setFromPageTitle(String fromPageTitle) {
				this.fromPageTitle = fromPageTitle;
			}

			public String getWidth() {
				return width;
			}

			public void setWidth(String width) {
				this.width = width;
			}

			public String getHeight() {
				return height;
			}

			public void setHeight(String height) {
				this.height = height;
			}

			public String getFromURLHost() {
				return fromURLHost;
			}

			public void setFromURLHost(String fromURLHost) {
				this.fromURLHost = fromURLHost;
			}

		}

	}

}
