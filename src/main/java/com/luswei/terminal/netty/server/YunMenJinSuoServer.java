package com.dmdh.YunMenJinSuoWeb.netty.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.dmdh.YunMenJinSuoWeb.model.BasicMessage;
import com.dmdh.YunMenJinSuoWeb.model.CommandType;
import com.dmdh.YunMenJinSuoWeb.model.Message;
import com.dmdh.YunMenJinSuoWeb.model.SequenceType;
import com.dmdh.YunMenJinSuoWeb.model.Terminal;
import com.dmdh.YunMenJinSuoWeb.netty.codec.ProtocolDecoder;
import com.dmdh.YunMenJinSuoWeb.netty.codec.ProtocolEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

@Component
public class YunMenJinSuoServer {

	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;

	/**
	 * send 方法：YunMenJinSuoServer.getMap.get(clientIP).writeAndFlush(bytes)
	 */
	private static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
	private static Map<String, Object> terminalMessageMap = new ConcurrentHashMap<String, Object>();
	private static Map<String, Object> messageMap = new ConcurrentHashMap<String, Object>();

	private static final int MAX_FRAME_LENGTH = 1024 * 1024;
	private static final int LENGTH_FIELD_OFFSET = 8;
	private static final int LENGTH_FIELD_LENGTH = 2;
	private static final int LENGTH_ADJUSTMENT = 1;
	private static final int INITIAL_BYTES_TO_STRIP = 0;

	public YunMenJinSuoServer() {
	}

	/**
	 * start the server
	 * 
	 * @param address
	 * @return
	 */
	public ChannelFuture start(int address) {

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				// .childHandler(new NettyServerInitializer(channelGroup))
				.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {

						// 处理日志
						ch.pipeline().addLast("log", new LoggingHandler(LogLevel.INFO));

						// 序列化编解码
						ch.pipeline().addLast("decoder", new ProtocolDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET,
								LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
						ch.pipeline().addLast("encoder", new ProtocolEncoder());

						// 处理心跳
						ch.pipeline().addLast(new LoginAuthRespHandler());
						ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());

						// 编解码
						ch.pipeline().addLast(new WebFrameHandler(channelGroup));

					}
				});

		ChannelFuture channelFuture = bootstrap.bind(address).syncUninterruptibly();
		channel = channelFuture.channel();

		return channelFuture;

	}

	/**
	 * stop the server
	 */
	public void destroy() {

		if (channel != null) {
			channel.close();
		}

		channelGroup.close();
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	public static void main(String[] ags) {
		YunMenJinSuoServer server = new YunMenJinSuoServer();
//		InetSocketAddress address = new InetSocketAddress("192.168.5.222", 20054);
		int port = 20054;
		ChannelFuture channelFuture = server.start(port);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.destroy();
			}
		});

		channelFuture.channel().closeFuture().syncUninterruptibly();
	}

	public static Map<String, Channel> getMap() {
		return map;
	}

	public static void setMap(Map<String, Channel> map) {
		YunMenJinSuoServer.map = map;
	}

	public static Map<String, Object> getMessageMap() {
		return messageMap;
	}

	public static void setMessageMap(Map<String, Object> messageMap) {
		YunMenJinSuoServer.messageMap = messageMap;
	}

	public static Map<String, Object> getTerminalMessageMap() {
		return terminalMessageMap;
	}

	public static void setTerminalMessageMap(Map<String, Object> terminalMessageMap) {
		YunMenJinSuoServer.terminalMessageMap = terminalMessageMap;
	}

	/**
	 * send message to terminal
	 * 
	 * @param sequence
	 * @param cmd
	 * @param length
	 * @param data
	 * @param ip
	 */
	public static void sendMessage(String sequence, String cmd, String length, String data, String id) {

		Message message = new Message();
		message.setHead(hexStringToBytes("fe")[0]);
		message.setVersion(hexStringToBytes("0102"));
		message.setAddress(hexStringToBytes("056F"));
		message.setSequence(hexStringToBytes(sequence)[0]);
		message.setCmd(hexStringToBytes(cmd.trim())[0]);
		message.setLength(hexStringToBytes(length.trim()));
		message.setData(hexStringToBytes(data.trim()));

		byte[] head = { message.getHead() };
		byte[] version = message.getVersion();
		byte[] type = { message.getType() };
		byte[] address = message.getAddress();
		byte[] seq = { message.getSequence() };
		byte[] cmdBytes = { message.getCmd() };
		byte[] lengthBytes = message.getLength();
		byte[] dataBytes = message.getData();

		String hexString = bytesToHexFun(head) + bytesToHexFun(version) + bytesToHexFun(type) + bytesToHexFun(address)
				+ bytesToHexFun(seq) + bytesToHexFun(cmdBytes) + bytesToHexFun(lengthBytes) + bytesToHexFun(dataBytes);
		byte crc = crcSum(hexStringToBytes(hexString + "00"));

		message.setCrc(crc);
		// YunMenJinSuoServer.getMap.get(clientIP).writeAndFlush(bytes)
		if (!YunMenJinSuoServer.getTerminalMessageMap().isEmpty()) {
			
			Terminal terminal = (Terminal) YunMenJinSuoServer.getTerminalMessageMap().get(id);
			Channel channel = (Channel) terminal.getChannelMessage();
			channel.writeAndFlush(message);
			
//			map.get(ip).writeAndFlush(message);
		}

	}
	
	/**
	 * 获取终端列表
	 * 
	 * @param addressMap
	 * @return
	 */
	public static List<Terminal> getAddressList(Map<String, Channel> addressMap) {
		int i = 0;
		String data = "";
		List<Terminal> list = new ArrayList<Terminal>();
		
		if (!addressMap.isEmpty()) {
			for (Map.Entry<String, Channel> entry : addressMap.entrySet()) {
				if (i == 0) {
					data += "client:" + entry.getKey();					
				} else {
					data += ",client:" + entry.getKey();
				}
				i++;
				//设置终端信息
				 Terminal terminal = new Terminal();
				 terminal.setId(i);
				 terminal.setIp(entry.getKey());
				 terminal.setStatus("online");
				 terminal.setChannelMessage(entry.getValue());
				 YunMenJinSuoServer.getTerminalMessageMap().put(i +"", terminal);
				 list.add(terminal);
			}
		} else {
			data += "client:null";
		}
		
		

		return list;

	}

	/**
	 * 合成校验码
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte crcSum(byte[] bytes) {

		int sum = 0;
		byte crc;
		for (int i = 0; i < bytes.length - 1; i++) {
			sum += (int) bytes[i];
		}
		byte[] crcBytes = intToBytes(sum);
		crc = crcBytes[crcBytes.length - 1];
		return crc;

	}

	/**
	 * byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexFun(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			stringBuilder.append(String.format("%02x", new Integer(b & 0xff)));
		}
		return stringBuilder.toString();
	}

	/**
	 * hex string to byte[]
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexStringToBytes(String str) {

		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}

		return bytes;

	}

	/**
	 * byte[] to integer
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int i;
		if (bytes.length > 1) {
			i = (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xff));
		} else {
			i = (bytes[0] & 0xFF) << 8;
		}
		return i;
	}

	/**
	 * integer to byte[]
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] intToBytes(int num) {

		byte[] result = new byte[2];
		result[0] = (byte) ((num >>> 8) & 0xff);
		result[1] = (byte) ((num >>> 0) & 0xff);

		return result;
	}

	/**
	 * string to hex string
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToHexString(String string) {

		String string2 = "";
		for (int i = 0; i < string.length(); i++) {
			int ch = (int) string.charAt(i);
			string2 += Integer.toHexString(ch);
		}
		return string2;

	}

	/**
	 * hex string to hex string
	 * 
	 * @param hex
	 * @return
	 */
	public static String hexStringToString(String hex) {

		if (hex.equals("") || hex == null) {
			return null;
		}

		hex = hex.replace("", "");
		byte[] baKeyword = new byte[hex.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(hex.substring(i * 2, i * 2 + 2),16));
			} catch (NumberFormatException e) {
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		try {
			hex = new String(baKeyword, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hex;

	}
	
//	/**
//	 * 16进制直接转换成为字符串(无需Unicode解码)
//	 * @param hexStr
//	 * @return
//	 */
//	public static String hexStringToString(String hexStr) {
//	    String str = "0123456789ABCDEF";
//	    char[] hexs = hexStr.toCharArray();
//	    byte[] bytes = new byte[hexStr.length() / 2];
//	    int n;
//	    for (int i = 0; i < bytes.length; i++) {
//	        n = str.indexOf(hexs[2 * i]) * 16;
//	        n += str.indexOf(hexs[2 * i + 1]);
//	        bytes[i] = (byte) (n & 0xff);
//	    }
//	    return new String(bytes);
//	}

}
