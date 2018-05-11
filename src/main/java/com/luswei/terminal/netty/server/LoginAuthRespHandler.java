package com.luswei.terminal.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

import com.luswei.terminal.model.Message;
import com.luswei.terminal.model.ResponseMessage;
import com.luswei.terminal.model.SequenceType;
import com.luswei.terminal.model.Terminal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(LoginAuthRespHandler.class);
	private static Map<String, Message> map = new ConcurrentHashMap<>();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		logger.info("Client " + getRemoteAddressString(ctx) + "接入连接");
		System.out.println("Client " + getRemoteAddressString(ctx) + "接入连接");
		// 往 channel map 中添加 channel 信息
		// YunMenJinSuoServer.getMap().put(getIPString(ctx),ctx.channel());

		// 生成
		// ResponseMessage responseMessage = new ResponseMessage();
		// responseMessage.setHead("login");
		// responseMessage.setBody(getIPString(ctx));
		//
		// YunMenJinSuoServer.getMessageMap().put(getIPString(ctx),responseMessage);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 删除 channel map 中的失效 channel
		YunMenJinSuoServer.getMap().remove(getIPString(ctx));
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof Message) {
			Message message = (Message) msg;
			byte[] cmd = { message.getCmd() };
			if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("01")) {
				System.out.println(ctx.channel().remoteAddress() + " 的登录验证!");

				// message.setSequence(YunMenJinSuoServer.hexStringToBytes(SequenceType.SEQUENCE_LOGIN.value())[0]);
				message.setLength(YunMenJinSuoServer.hexStringToBytes("0001"));
				message.setData(YunMenJinSuoServer.hexStringToBytes("30"));

				byte[] head = { message.getHead() };
				byte[] version = message.getVersion();
				byte[] type = { message.getType() };
				byte[] address = message.getAddress();
				byte[] seq = { message.getSequence() };
				// byte[] cmd = {message.getCmd()};
				byte[] length = message.getLength();
				byte[] data = message.getData();

				String hexString = YunMenJinSuoServer.bytesToHexFun(head) + YunMenJinSuoServer.bytesToHexFun(version)
						+ YunMenJinSuoServer.bytesToHexFun(type) + YunMenJinSuoServer.bytesToHexFun(address)
						+ YunMenJinSuoServer.bytesToHexFun(seq) + YunMenJinSuoServer.bytesToHexFun(cmd)
						+ YunMenJinSuoServer.bytesToHexFun(length) + YunMenJinSuoServer.bytesToHexFun(data);
				byte crc = YunMenJinSuoServer.crcSum(YunMenJinSuoServer.hexStringToBytes(hexString + "00"));
				message.setCrc(crc);
				/**
				 * crc 算法
				 */
				// int crc = YunMenJinSuoServer.bytesToInt(head) +
				// YunMenJinSuoServer.bytesToInt(version)
				// + YunMenJinSuoServer.bytesToInt(type) +
				// YunMenJinSuoServer.bytesToInt(address)
				// + YunMenJinSuoServer.bytesToInt(seq) + YunMenJinSuoServer.bytesToInt(cmd)
				// + YunMenJinSuoServer.bytesToInt(length) +
				// YunMenJinSuoServer.bytesToInt(data);
				// int crc = (int) message.getHead() +
				// YunMenJinSuoServer.bytesToInt(message.getVersion())
				// + (int)message.getType() +
				// YunMenJinSuoServer.bytesToInt(message.getAddress())
				// + (int)message.getSequence() + (int)message.getCmd()
				// + YunMenJinSuoServer.bytesToInt(message.getLength());
				// if (message.getData().length > 1) {
				// crc += YunMenJinSuoServer.bytesToInt(message.getData());
				// } else {
				// crc += (int) message.getData()[0];
				// }

				// byte[] crcBytes = YunMenJinSuoServer.intToBytes(crc);
				//
				// message.setCrc(crcBytes[crcBytes.length - 1]);
				// 往 channel map 中添加 channel 信息
				YunMenJinSuoServer.getMap().put(getIPString(ctx), ctx.channel());
				YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_LOGIN.value(), message);

//				 //设置终端信息
//				 Terminal terminal = new Terminal();
//				 terminal.setIp(getIPString(ctx));
//				 terminal.setStatus("online");
//				 YunMenJinSuoServer.getTerminalMessageMap().put(getIPString(ctx), terminal);

				System.out.println(" 返回登录验证 " + new java.util.Date(System.currentTimeMillis()).toString());
				ctx.channel().write(message);
			} else {
				/**
				 * 一定要执行这一条 相当于重新接受到一条消息，下一个 Handler 才能执行 channelRead
				 */
				ctx.fireChannelRead(msg);
			}
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public static String getIPString(ChannelHandlerContext ctx) {

		String ipString = "";
		String socketString = ctx.channel().remoteAddress().toString();
		int colonAt = socketString.indexOf(":");
		ipString = socketString.substring(1, colonAt);

		return ipString;
	}

	public static String getRemoteAddressString(ChannelHandlerContext ctx) {

		String socketString = ctx.channel().remoteAddress().toString();

		return socketString;
	}

	private String getKeyFromArray(byte[] addressDomain) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			stringBuffer.append(addressDomain[i]);
		}

		return stringBuffer.toString();
	}

	protected String to8BitString(String binaryString) {

		int length = binaryString.length();
		for (int i = 0; i < length; i++) {
			binaryString = "0" + binaryString;
		}
		return binaryString;

	}

	protected static byte[] combine2Byte(byte[] bytes1, byte[] bytes2) {

		byte[] bytesResult = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, bytesResult, 0, bytes1.length);
		System.arraycopy(bytes2, 0, bytesResult, bytes1.length, bytes2.length);
		return bytesResult;

	}

}
