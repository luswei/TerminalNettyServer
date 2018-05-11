package com.dmdh.YunMenJinSuoWeb.netty.server;

import com.dmdh.YunMenJinSuoWeb.model.BasicMessage;
import com.dmdh.YunMenJinSuoWeb.model.Message;
import com.dmdh.YunMenJinSuoWeb.model.RecordMessage;
import com.dmdh.YunMenJinSuoWeb.model.ResponseMessage;
import com.dmdh.YunMenJinSuoWeb.model.SequenceType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WebFrameHandler extends ChannelInboundHandlerAdapter {

//    private Logger logger = LogManager.getLogManager();
    private final ChannelGroup channelGroup;

    public WebFrameHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Message) {
            Message message = (Message) msg;
            byte[] cmd = {message.getCmd()};
            if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("03") || YunMenJinSuoServer.bytesToHexFun(cmd).equals("04")
                    || YunMenJinSuoServer.bytesToHexFun(cmd).equals("05") || YunMenJinSuoServer.bytesToHexFun(cmd).equals("06")
                    || YunMenJinSuoServer.bytesToHexFun(cmd).equals("09")) {
            	
            	if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("03")) {
            		
//            		YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_TIME_READ.value(),message);
            		ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setHead("timeData");
					byte[] timeData = new byte[message.getData().length - 1];
					System.arraycopy(message.getData(), 1, timeData, 0, message.getData().length - 1);
					responseMessage.setBody(YunMenJinSuoServer.hexStringToString((YunMenJinSuoServer.bytesToHexFun(timeData))));
					
					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_TIME_READ.value(),responseMessage);
            		
				} else if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("04")) {
					
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setHead("userCountData");
					byte[] userCountData = new byte[message.getData().length - 1];
					System.arraycopy(message.getData(), 1, userCountData, 0, message.getData().length - 1);
					responseMessage.setBody(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(userCountData)));
					
					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_USER_READ.value(),responseMessage);
					
				} else if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("05")) {
					
					//YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_RECORD_ONE.value(),message);
					
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setHead("recordData");
					byte[] recordData = new byte[message.getData().length - 1];
					System.arraycopy(message.getData(), 1, recordData, 0, message.getData().length - 1);
					List<RecordMessage> list = buildRecordMessage(recordData, 1);
					responseMessage.setBody(list.toArray()[0]);
					
					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_RECORD_ONE.value(),responseMessage);
					
				} else if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("06")) {
					
//					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_PRODUCT_OPEN.value(),message);
					
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setHead("openData");
					byte[] openData = new byte[message.getData().length - 1];
					System.arraycopy(message.getData(), 1, openData, 0, message.getData().length - 1);
					responseMessage.setBody(YunMenJinSuoServer.bytesToHexFun(openData));
					
					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_PRODUCT_OPEN.value(),responseMessage);
					
				} else if (YunMenJinSuoServer.bytesToHexFun(cmd).equals("09")) {
					
//					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_INTERNAL_BASIC.value(),message);
					
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setHead("BasicMessageData");
					byte[] BasicMessageData = new byte[message.getData().length - 1];
					System.arraycopy(message.getData(), 1, BasicMessageData, 0, message.getData().length - 1);
					BasicMessage basicMessage = buildBasicMessage(BasicMessageData);
					responseMessage.setBody(basicMessage);
					
					YunMenJinSuoServer.getMessageMap().put(SequenceType.SEQUENCE_INTERNAL_BASIC.value(),responseMessage);
					
				}
//                YunMenJinSuoServer.getMessageMap().put("1",message);
                
            } else {
                ctx.fireChannelRead(msg);
            }
        }

    }
    
    /**
	 * 构建记录信息
	 * @param data
	 * @param length
	 * @return
	 */
	private List<RecordMessage> buildRecordMessage(byte[] data, int length) {
//        int dataLength = (length * 42) + 1 ;
//        if (dataLength > data.length)
//            dataLength = data.length;
//        byte[] recordData = new byte[dataLength];
//        System.arraycopy(data, 1, recordData, 0, dataLength);

        List<RecordMessage> list = new ArrayList<RecordMessage>();

        int i=0;
        for (int j =0; j<length; j++) {
        	if (i < data.length) {
                RecordMessage recordMessage = new RecordMessage();
                byte[] userNumber = new byte[3];
                byte[] cardID = new byte[12];
                byte[] name = new byte[10];
                byte[] type = new byte[1];
                byte[] time = new byte[6];
                byte[] timesCard = new byte[2];
                byte[] remark = new byte[8];

                System.arraycopy(data, i, userNumber, 0, 3);
                recordMessage.setUserNumber(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(userNumber)));
                i = i + 3;

                System.arraycopy(data, i, cardID, 0, 12);
                recordMessage.setCardID(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(cardID)));
                i = i + 12;

                System.arraycopy(data, i, name, 0, 10);
                recordMessage.setName(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(name)));
                i = i + 10;

                System.arraycopy(data, i, type, 0, 1);
                recordMessage.setType(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(type)));
                i = i + 1;

                System.arraycopy(data, i, time, 0, 6);
                recordMessage.setTime(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(time)));
                i = i + 6;

                System.arraycopy(data, i, timesCard, 0, 2);
                recordMessage.setTimesCard(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(timesCard)));
                i = i + 2;

                System.arraycopy(data, i, remark, 0, 8);
                recordMessage.setRemark(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(remark)));
                i = i + 8;

                list.add(recordMessage);
        	}

        }
        return list;
    }

	/**
	 * 构建基本消息
	 * @param data
	 * @return
	 */
	private BasicMessage buildBasicMessage(byte[] data) {
        BasicMessage basicMessage = new BasicMessage();
        int i = 0;
        byte[] productID = new byte[10];
        byte[] jobID = new byte[10];
        byte[] productionTime = new byte[6];
        byte[] currentTime = new byte[6];
        byte[] localIP = new byte[4];
        byte[] localGatway = new byte[4];
        byte[] localSubnetMask = new byte[4];
        byte[] mac = new byte[6];
        byte[] serverIP = new byte[4];
        byte[] recordCount = new byte[3];
        byte[] userCount = new byte[3];
        byte[] openDelay = new byte[1];

        System.arraycopy(data,0,productID,0,10);
        basicMessage.setProductID(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(productID)));
        i = i + 10;

        System.arraycopy(data,i,jobID,0,10);
        basicMessage.setJobID(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(jobID)));
        i = i + 10;

        System.arraycopy(data,i,productionTime,0,6);
        basicMessage.setProductionTime(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(productionTime)));
        i = i + 6;

        System.arraycopy(data,i,currentTime,0,6);
        basicMessage.setCurrentTime(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(currentTime)));
        i = i + 6;

        System.arraycopy(data,i,localIP,0,4);
        basicMessage.setLocalIP(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(localIP)));
        i = i + 4;

        System.arraycopy(data,i,localGatway,0,4);
        basicMessage.setLocalGatway(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(localGatway)));
        i = i + 4;

        System.arraycopy(data,i,localSubnetMask,0,4);
        basicMessage.setLocalSubnetMask(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(localSubnetMask)));
        i = i + 4;

        System.arraycopy(data,i,mac,0,6);
        basicMessage.setCurrentTime(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(mac)));
        i = i + 6;

        System.arraycopy(data,i,serverIP,0,4);
        basicMessage.setLocalSubnetMask(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(serverIP)));
        i = i + 4;

        System.arraycopy(data,i,recordCount,0,3);
        basicMessage.setLocalSubnetMask(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(recordCount)));
        i = i + 3;

        System.arraycopy(data,i,userCount,0,3);
        basicMessage.setLocalSubnetMask(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(userCount)));
        i = i + 3;

        System.arraycopy(data,i,openDelay,0,1);
        basicMessage.setLocalSubnetMask(YunMenJinSuoServer.hexStringToString(YunMenJinSuoServer.bytesToHexFun(openDelay)));
        i = i + 1;

        return basicMessage;
    }


}
