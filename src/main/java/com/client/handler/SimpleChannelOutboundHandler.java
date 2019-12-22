package com.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

abstract class SimpleChannelOutboundHandler<I> extends ChannelOutboundHandlerAdapter {
	
	//private final Logger logger = LoggerFactory.getLogger(MessageInboundHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

		// 검증되지 않은 연산자 관련 경고를 표시 안함
		@SuppressWarnings("unchecked")
		I imsg = (I) msg;
        write0(ctx, imsg, promise);
	}

	protected abstract void write0(ChannelHandlerContext ctx, I msg, ChannelPromise promise) throws Exception;
}
