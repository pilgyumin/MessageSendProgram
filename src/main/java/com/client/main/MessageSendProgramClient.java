package com.client.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutException;

@Component
@PropertySource(value = "classpath:/application.properties")
public class MessageSendProgramClient {

	@Value("${testingIP}")
	private String testingIP;
	
	@Value("${tempIP}")
	private String tempIP;

	@Value("${port}")
	private int port;

	@Autowired
	private MessageSendProgramInitializer messageSendProgramInitializer;

	public void run() {

		EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

		Channel channel = null;
		Bootstrap bootstrap = new Bootstrap();
		try {

			bootstrap.group(nioEventLoopGroup)
			.channel(NioSocketChannel.class)
			.handler(messageSendProgramInitializer);

			channel = bootstrap.connect(testingIP, port).sync().channel();
			
			channel.closeFuture().sync();
		} 
//		catch (ReadTimeoutException e) {
//
//		} 
		catch (Exception e) {
			e.printStackTrace();
		} 

		finally {
			nioEventLoopGroup.shutdownGracefully();

		}
	}

}
