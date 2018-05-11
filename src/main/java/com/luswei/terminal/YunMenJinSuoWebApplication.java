package com.dmdh.YunMenJinSuoWeb;

import com.dmdh.YunMenJinSuoWeb.netty.server.YunMenJinSuoServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@EnableAutoConfiguration
@SpringBootApplication
public class YunMenJinSuoWebApplication implements CommandLineRunner {

	@Autowired
	private YunMenJinSuoServer server;

	public static void main(String[] args) {
		SpringApplication.run(YunMenJinSuoWebApplication.class, args);
	}

	@Bean
	public YunMenJinSuoServer yunMenJinSuoServer() {
		return new YunMenJinSuoServer();
	}

	/**
	 * CommandLineRunner---在项目启动时加载
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {

		
//		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),20054);
		int port = 20054;
		ChannelFuture channelFuture = server.start(port);

		//JVM关闭时，销毁
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.destroy();
			}
		});

		channelFuture.channel().closeFuture().syncUninterruptibly();

	}
}
