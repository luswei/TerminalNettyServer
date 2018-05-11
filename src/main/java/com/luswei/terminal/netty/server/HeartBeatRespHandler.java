package com.luswei.terminal.netty.server;

import com.luswei.terminal.model.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Message) {
            Message message = (Message) msg;
            byte[] cmd = {message.getCmd()};
            if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("02")) {
                System.out.println(ctx.channel().remoteAddress() + " 的心跳信息!");

                message.setSequence(YunMenJinSuoServer.hexStringToBytes(SequenceType.SEQUENCE_HEART.value())[0]);
                message.setLength(YunMenJinSuoServer.hexStringToBytes("0001"));
                message.setData(YunMenJinSuoServer.hexStringToBytes("30"));

                byte[] head = {message.getHead()};
                byte[] version = message.getVersion();
                byte[] type = {message.getType()};
                byte[] address = message.getAddress();
                byte[] seq = {message.getSequence()};
//                byte[] cmd = {message.getCmd()};
                byte[] length = message.getLength();
                byte[] data = message.getData();
//
//                /**
//                 * crc 算法
//                 */
//                int crc = YunMenJinSuoServer.bytesToInt(head) + YunMenJinSuoServer.bytesToInt(version)
//                        + YunMenJinSuoServer.bytesToInt(type) + YunMenJinSuoServer.bytesToInt(address)
//                        + YunMenJinSuoServer.bytesToInt(seq) + YunMenJinSuoServer.bytesToInt(cmd)
//                        + YunMenJinSuoServer.bytesToInt(length) + YunMenJinSuoServer.bytesToInt(data);

//                int crc = (int) message.getHead() + YunMenJinSuoServer.bytesToInt(message.getVersion())
//                        + (int)message.getType() + YunMenJinSuoServer.bytesToInt(message.getAddress())
//                        + (int)message.getSequence() + (int)message.getCmd()
//                        + YunMenJinSuoServer.bytesToInt(message.getLength());
//                if (message.getData().length > 1) {
//                    crc +=  YunMenJinSuoServer.bytesToInt(message.getData());
//                } else {
//                    crc += (int) message.getData()[0];
//                }
//                byte[] crcBytes = YunMenJinSuoServer.intToBytes(crc);
//
//                message.setCrc(crcBytes[crcBytes.length - 1]);

                String hexString = YunMenJinSuoServer.bytesToHexFun(head) + YunMenJinSuoServer.bytesToHexFun(version)
                        + YunMenJinSuoServer.bytesToHexFun(type) + YunMenJinSuoServer.bytesToHexFun(address)
                        + YunMenJinSuoServer.bytesToHexFun(seq) + YunMenJinSuoServer.bytesToHexFun(cmd)
                        + YunMenJinSuoServer.bytesToHexFun(length) + YunMenJinSuoServer.bytesToHexFun(data);
                byte crc = YunMenJinSuoServer.crcSum(YunMenJinSuoServer.hexStringToBytes(hexString + "00"));
                message.setCrc(crc);
                YunMenJinSuoServer.getMessageMap().put("heartBeat_1",message);
                System.out.println(" 发送心跳信息 " +  new java.util.Date(System.currentTimeMillis()).toString());
                ctx.channel().write(message);
            } else {
                /**
                 * 一定要执行这一条
                 * 相当于重新接受到一条消息，下一个 Handler 才能执行 channelRead
                 */
                ctx.fireChannelRead(msg);
            }
        }

    }

}
