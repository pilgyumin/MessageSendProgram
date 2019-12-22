package com.client.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.client.codec.MessageCodec;
import com.client.handler.MessageInboundHandler;
import com.client.handler.MessageOutboundHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
class MessageSendProgramInitializer extends ChannelInitializer<Channel> {
	
	@Autowired
	private MessageCodec messageCodec;
	
	@Autowired
	private MessageInboundHandler messageInboundHandler;
	
	@Autowired
	private MessageOutboundHandler messageOutboundHandler;

	@Override
	protected void initChannel(Channel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		try {
			pipeline.addLast(new LoggingHandler(LogLevel.INFO));
			pipeline.addLast(messageCodec);
			pipeline.addLast(messageOutboundHandler);
			pipeline.addLast(messageInboundHandler);
			
		}
		catch(NullPointerException e) {
			e.printStackTrace();
		}
	}

}
