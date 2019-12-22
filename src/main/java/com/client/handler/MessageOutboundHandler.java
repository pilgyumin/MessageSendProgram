package com.client.handler;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.client.exception.WrongDataFormatException;
import com.client.exception.WrongHeaderFormatException;
import com.client.exception.WrongStatusCodeFormatException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.ThreadLocalRandom;

@Component
@PropertySource(value = "classpath:/application.properties")
public class MessageOutboundHandler extends SimpleChannelOutboundHandler<String> {

	@Value("${headerLength}")
	private int headerLength;

	@Value("${statusCodeLength}")
	private int statusCodeLength;

	@Value("${dataLength}")
	private int dataLength;

	static int cal = 0;

	private final Logger logger = LoggerFactory.getLogger(MessageOutboundHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void write0(ChannelHandlerContext ctx, String msg, ChannelPromise promise) throws Exception {
		
		logger.info("write0!!");
		
		String packet = makeDataPacket();

		boolean isWrite = true;

		if (msg.equals("checksend")) {
			if (!checkDataPacket(packet)) {
				isWrite = false;
			}
		}

		if (isWrite) {
			ctx.writeAndFlush(packet);
		}
	}

	private String makeDataPacket() {
		// make Packet Header + Packet Status Code 총 8자리
		StringBuffer stringBuffer = new StringBuffer("0008DATA");

		// make Data 총 4자리
		int data = ThreadLocalRandom.current().nextInt(8) + 1;

		stringBuffer.append("000");

		stringBuffer.append(data);

		return stringBuffer.toString();
	}

	private boolean checkDataPacket(String packet) {
		String header = null;
		String statusCode = null;
		String data = null;

		try {
			// packet의 header 검사
			header = packet.substring(0, headerLength);
			if (!isNumber(header)) {
				throw new WrongHeaderFormatException("패킷의 Header가 Number가 아닙니다.");
			}

			// packet의 status code 검사
			statusCode = packet.substring(headerLength + 1, statusCodeLength + headerLength);
			if (!statusCode.equals("DATA")) {
				throw new WrongStatusCodeFormatException("패킷의 Status Code가 DATA가 아닙니다.");
			}

			// packet의 data부분 검사
			data = packet.substring(statusCodeLength + headerLength + 1, statusCodeLength + headerLength + dataLength);

			if (!isNumber(data)) {
				throw new WrongDataFormatException("패킷의 Data가 Number가 아닙니다.");
			}

		}

		catch (WrongHeaderFormatException e) {
			e.printStackTrace();
			logger.error("header(body length) is not number / header : {}", header);
			return false;
		} catch (WrongStatusCodeFormatException e) {
			e.printStackTrace();
			logger.error("status code is not 'DATA' / statusCode : {}", statusCode);
			return false;
		} catch (WrongDataFormatException e) {
			e.printStackTrace();
			logger.error("data is not number / data : {}", data);
			return false;
		}

		return true;
	}

	private boolean isNumber(String input) {

		char[] inputArray = input.toCharArray();

		for (int idx = 0; idx < inputArray.length; idx++) {
			if ('0' > inputArray[idx] || inputArray[idx] > '9') {
				return false;
			}
		}

		return true;
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

}
