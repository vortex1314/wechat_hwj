package edu.gdkm.wechat.domain.text;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.gdkm.wechat.domain.InMessage;

@XmlAccessorType(XmlAccessType.FIELD) //JAXB从字段获取配置信息
@XmlRootElement(name="xml") //JAXB读取XML时根元素名称
public class TextInMessage extends InMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name="Content")
	private String content;
	
	public TextInMessage() {
		super.setMsgType("text");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "TextInMessage [content=" + content + ", getToUserName()=" + getToUserName() + ", getFromUserName()="
				+ getFromUserName() + ", getCreateTime()=" + getCreateTime() + ", getMsgType()=" + getMsgType()
				+ ", getMsgId()=" + getMsgId() + "]";
	}
}
