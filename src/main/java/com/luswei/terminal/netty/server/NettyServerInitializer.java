package com.luswei.terminal.netty.server;

import com.luswei.terminal.netty.codec.MarshallingCodecFactory;
import com.luswei.terminal.netty.codec.ProtocolDecoder;
import com.luswei.terminal.netty.codec.ProtocolEncoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServerInitializer extends ChannelInitializer<Channel> {

    private ChannelGroup channelGroup;

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_FIELD_LENGTH = 2;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    public NettyServerInitializer(ChannelGroup channelGroup) {

        this.channelGroup = channelGroup;

    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        //处理日志
        channelPipeline.addLast("log",new LoggingHandler(LogLevel.INFO));

        //序列化编解码
        channelPipeline.addLast("decoder",new ProtocolDecoder(MAX_FRAME_LENGTH,
                LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,
                LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
        channelPipeline.addLast("encoder",new ProtocolEncoder());
//        channelPipeline.addLast(MarshallingCodecFactory.buildMarshallingDecoder());
//        channelPipeline.addLast(MarshallingCodecFactory.buildMarshallingEncoder());

        //处理心跳
        channelPipeline.addLast(new LoginAuthRespHandler());
        channelPipeline.addLast(new HeartBeatRespHandler());

        //编解码
        channelPipeline.addLast(new WebFrameHandler(channelGroup));
    }
}
