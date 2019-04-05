package edu.gdkm.wechat.domain.Image;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.gdkm.wechat.domain.InMessage;

@XmlAccessorType(XmlAccessType.FIELD) //JAXB从字段获取配置信息
@XmlRootElement(name="xml") //JAXB读取XML时根元素名称
public class ImageInMessage extends InMessage {
	@XmlElement(name="PicUrl")
	private String url;
	
	@XmlElement(name="Mediaid")
	private String mediaId;
	
	public ImageInMessage() {
		super.setMsgType("image");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public String toString() {
		return "ImageInMessage [url=" + url + ", mediaId=" + mediaId + ", getToUserName()=" + getToUserName()
				+ ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
				+ ", getMsgType()=" + getMsgType() + ", getMsgId()=" + getMsgId() + "]";
	}
}
