package edu.gdkm.wechat.controller;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.gdkm.wechat.domain.InMessage;
import edu.gdkm.wechat.service.MessageTypeMapper;

@RestController
@RequestMapping("/hwj/weixin/receiver")
public class MessageReceiverController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageReceiverController.class);
	@Autowired
	private RedisTemplate<String,InMessage> inMessageTemplate;
	
	@GetMapping
	public String echo(
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("echostr") String echostr) {

		return echostr;
	}
	
	@PostMapping
	public String onMessage(@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestBody String xml) 
	{
		LOG.debug("收到用户发送给公众号的信息: \n-----------------------------------------\n"
				+ "{}\n-----------------------------------------\n", xml);

//		if (xml.contains("<MsgType><![CDATA[text]]></MsgType>")) {
//		} else if (xml.contains("<MsgType><![CDATA[image]]></MsgType>")) {
//		} else if (xml.contains("<MsgType><![CDATA[voice]]></MsgType>")) {
//		} else if (xml.contains("<MsgType><![CDATA[video]]></MsgType>")) {
//		} else if (xml.contains("<MsgType><![CDATA[location]]></MsgType>")) {
//		}

		// 截取消息类型
		// <MsgType><![CDATA[text]]></MsgType>
		String type = xml.substring(xml.indexOf("<MsgType><![CDATA[") + 18);
		type = type.substring(0, type.indexOf("]]></MsgType>"));

		Class<InMessage> cla = MessageTypeMapper.getClass(type);

		// 使用JAXB完成XML转换为Java对象的操作
		InMessage inMessage = JAXB.unmarshal(new StringReader(xml), cla);

		LOG.debug("转换得到的消息对象 \n{}\n", inMessage.toString());

		// 把消息放入消息队列
		inMessageTemplate.execute(new RedisCallback<String>() {

			// connection对象表示跟Redis数据库的连接
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					String channel = "hwj" + inMessage.getMsgType();

					// 消息内容要自己序列化才能放入队列中
					ByteArrayOutputStream out = new ByteArrayOutputStream();// 输出流
					ObjectOutputStream oos = new ObjectOutputStream(out);
					oos.writeObject(inMessage);

					Long l = connection.publish(channel.getBytes(), out.toByteArray());
					System.out.println("发布结果：" + l);
				} catch (Exception e) {
					LOG.error("把消息放入队列时出现问题：" + e.getLocalizedMessage(), e);
				}
				return null;
			}
		});

		// 由于后面会把消息放入队列中，所以这里直接返回success。
		return "success";
	}
}
