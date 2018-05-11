package com.luswei.terminal.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

import com.luswei.terminal.model.Message;

public class ProtocolDecoder extends LengthFieldBasedFrameDecoder{

    private static final int MESSAGE_SIZE = 10;

    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProtocolDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Message decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {

        ByteBuf in = (ByteBuf) super.decode(ctx, in2);

        if (in == null) {
            return null;
        }

        if(in.readableBytes() < MESSAGE_SIZE) {
            return null;
        }

        Message message = new Message();
        message.setHead(in.readByte());
        byte[] version = {in.readByte(),in.readByte()};
        message.setVersion(version);
        message.setType(in.readByte());
        byte[] address = {in.readByte(),in.readByte()};
        message.setAddress(address);
        message.setSequence(in.readByte());
        message.setCmd(in.readByte());
        byte[] lengthBytes = {in.readByte(),in.readByte()};
        message.setLength(lengthBytes);
        int length = bytesToInt(message.getLength());
        if (length > in.readableBytes()) {
            System.out.println("解析异常");
            return null;
        }
        if (length >= 1) {
            byte[] data = new byte[length];
            in.readBytes(length).getBytes(0, data);
            message.setData(data);
        } else {
            message.setData(null);
        }
        message.setCrc(in.readByte());
//        message.setCrc(crcSum(in.array()));

        return message;
    }

    private int bytesToInt(byte[] bytes) {
        int i;
        i = (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xff));
        return i;
    }

    public byte[] hexStringToBytes(String str) {

        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i=0;i<str.length()/2;i++) {
            String subStr = str.substring(i*2,i*2+2);
            bytes[i] = (byte) Integer.parseInt(subStr,16);
        }

        return bytes;

    }

}
