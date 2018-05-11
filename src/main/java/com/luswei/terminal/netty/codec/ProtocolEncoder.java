package com.luswei.terminal.netty.codec;

import com.luswei.terminal.model.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<Message> {

    public ProtocolEncoder() {

    }

    public ProtocolEncoder(Class<? extends Message> outBoundMessageType) {
        super(outBoundMessageType);
    }

    /**
     * @param preferDirect
     */
    public ProtocolEncoder(boolean preferDirect) {
        super(preferDirect);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param outboundMessageType
     * @param preferDirect
     */
    public ProtocolEncoder(Class<? extends Message> outboundMessageType,
                           boolean preferDirect) {
        super(outboundMessageType, preferDirect);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {

        if (msg == null) {
            throw new Exception("the encode message is null");
        }

        out.writeByte(msg.getHead());
        out.writeBytes(msg.getVersion());
        out.writeByte(msg.getType());
        out.writeBytes(msg.getAddress());
        out.writeByte(msg.getSequence());
        out.writeByte(msg.getCmd());
        out.writeBytes(msg.getLength());
        out.writeBytes(msg.getData());
        out.writeByte(msg.getCrc());

    }
}
