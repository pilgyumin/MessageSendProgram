package com.client.codec;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

@Component
@PropertySource(value = "classpath:/application.properties")
public class MessageCodec extends ByteToMessageCodec<String> {

	@Value("${headerLength}")
	private int headerLength;
	
	@Value("${statusCodeLength}")
	private int statusCodeLength;
	
	@Value("${errorCodeLength}")
	private int errorCodeLength;
	
	private final Logger logger = LoggerFactory.getLogger(MessageCodec.class);
   
	@Override
	protected void encode(ChannelHandlerContext ctx, String message, ByteBuf buf) throws Exception {
		buf.writeBytes(message.getBytes());		
		logger.debug("send Packet : {}", message);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> in) throws Exception {

		logger.debug("insert decode!!");

		if (buf.readableBytes() < headerLength) {
			return;
		}

		String header = buf.readBytes(headerLength).toString(Charset.defaultCharset());

		int bodyLength = Integer.parseInt(header);

		// 헤더에 설정된 바디의 길이 만큼 데이터가 들어오지 않았을 경우
		if (buf.readableBytes() < bodyLength) {
			buf.resetReaderIndex();
		}

		String statusCode = buf.readBytes(statusCodeLength).toString(Charset.defaultCharset());
		// get statusCode

		checkPacketStatus(buf, statusCode);
		
		in.add(statusCode);
	}

	// packet의 Status code 검사
	private void checkPacketStatus(ByteBuf buf, String statusCode) throws Exception {

		if (statusCode.equals("NACK")) {
			String errorCode = buf.readBytes(errorCodeLength).toString(Charset.defaultCharset());
			String description = getErrorStatus(errorCode);
			logger.error("Packet Request Error : {}", description);
		}

		else {
			logger.debug("Packet Request Success : {}", statusCode);
		}
	}

	private String getErrorStatus(String errorCode) {
		StringBuilder errorStatus = new StringBuilder();

		if (errorCode.equals("WRDF")) {
			errorStatus.append("Wrong Data Format");
		} else if (errorCode.equals("WRDC")) {
			errorStatus.append("Wrong Data Content");
		} else if (errorCode.equals("ORDA")) {
			errorStatus.append("Out of Range Data");
		}

		return errorStatus.toString();
	}

}
