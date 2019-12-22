package com.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
public class MessageInboundHandler extends SimpleChannelInboundHandler<String> {

	private final Logger logger = LoggerFactory.getLogger(MessageInboundHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Channel Activate!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Channel Inactivate!");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {

		logger.info("channel read0 !!");
		
		if (message.equals("OACK")) {
			ctx.write("send");
		} 
		else {
			ctx.write("checksend");
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
