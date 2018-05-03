package com.dmdh.YunMenJinSuoWeb.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmdh.YunMenJinSuoWeb.model.CommandType;
import com.dmdh.YunMenJinSuoWeb.model.ResponseMessage;
import com.dmdh.YunMenJinSuoWeb.model.SequenceType;
import com.dmdh.YunMenJinSuoWeb.netty.server.YunMenJinSuoServer;

@RestController
@RequestMapping("/terminal/{id}")
public class CommandController {

	@RequestMapping("/test/{user}/{name}")
	public Map<String, String> testController(@PathVariable String user, @PathVariable String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(user, name);
		map.put(user + "2", name + "2");
		// YunMenJinSuoServer.getMap().get("192.168.5.111").toString();
		map.put("haha", YunMenJinSuoServer.getMap().get("192.168.5.111").toString());
		return map;
	}

	/**
	 * 测试命令
	 * 
	 * @param seq
	 * @param cmd
	 * @param length
	 * @param data
	 */
	@RequestMapping("/command/{seq}/{cmd}/{length}/{data}")
	public void sendCommand(@PathVariable String seq, @PathVariable String cmd, @PathVariable String length,
			@PathVariable String data, @PathVariable String id) {
		YunMenJinSuoServer.sendMessage(seq, cmd, length, data, id);
	}
	
	/**
	 * 发送命令 controller
	 * seq,cmd,length,data,
	 * @param commandString
	 * @param id
	 */
	@RequestMapping("/command/{commandString}")
	public void sendCommand(@PathVariable String commandString, @PathVariable String id) {
//		String[] cmdString =  commandString.split(",");
		if (commandString.split(",").length != 0) {
			YunMenJinSuoServer.sendMessage(commandString.split(",")[0], commandString.split(",")[1], commandString.split(",")[2], commandString.split(",")[3], id);
		}
	}
	
	/**
	 * 获取数据 controller
	 * 
	 * @param seqString
	 */
	@RequestMapping("/{seqString}")
	public ResponseEntity<?> getResponseData(@PathVariable String seqString) {
//		String[] cmdString =  commandString.split(",");
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(seqString);
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

//	/**
//	 * 获取地址列表
//	 * 
//	 * @return
//	 */
//	@RequestMapping("/address")
//	public ResponseEntity<ResponseMessage> addressList() {
//		ResponseMessage responseMessage = new ResponseMessage();
//		
//		responseMessage.setHead("addressList");
//		responseMessage.setBody(YunMenJinSuoServer.getAddressList(YunMenJinSuoServer.getMap()));
//
//		
//		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
//	}

	/**
	 * time command
	 */
	@RequestMapping("/readTime")
	public void readTime(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_TIME_READ.value(), CommandType.COMMAND_TIME.value(),
				"0001", "03", id);
	}
	
	/**
	 * 获取时间数据
	 * @return
	 */
	@RequestMapping("/timeData")
	public ResponseEntity<ResponseMessage> timeData() {
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(SequenceType.SEQUENCE_TIME_READ.value());
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	/**
	 * user command
	 */
	@RequestMapping("/readUserCount")
	public void readUserCount(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_USER_READ.value(), CommandType.COMMAND_USER.value(),
				"0001", "04", id);
	}

	/**
	 * 获取用户数数据
	 * @return
	 */
	@RequestMapping("/userCountData")
	public ResponseEntity<ResponseMessage> userCountData() {
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(SequenceType.SEQUENCE_USER_READ.value());
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	
	/**
	 * record command
	 */
	@RequestMapping("/read")
	public void readOne(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_RECORD_ONE.value(), CommandType.COMMAND_RECORD.value(),
				"0001", "01", id);
	}
	
	/**
	 * 获取记录数据
	 * @return
	 */
	@RequestMapping("/recordData")
	public ResponseEntity<ResponseMessage> recordData() {
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(SequenceType.SEQUENCE_RECORD_ONE.value());
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	@RequestMapping("/batchFetch")
	public void batchFetch(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_RECORD_MANY.value(), CommandType.COMMAND_RECORD.value(),
				"0002", "0208", id);
	}

	/**
	 * product command
	 */
	@RequestMapping("/open/{doorNumber}")
	public void openDoorNumber(@PathVariable String doorNumber, @PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_PRODUCT.value(), CommandType.COMMAND_OPEN.value(), "0002",
				"03" + doorNumber, id);
	}

	@RequestMapping("/open")
	public void openDoor(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_PRODUCT.value(), CommandType.COMMAND_OPEN.value(), "0002",
				"0301", id);
	}
	
	/**
	 * 获取开门数据
	 * @return
	 */
	@RequestMapping("/openData")
	public ResponseEntity<ResponseMessage> openData() {
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(SequenceType.SEQUENCE_PRODUCT_OPEN.value());
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	/**
	 * internal command
	 */
	@RequestMapping("/extractBasicMessage")
	public void extractBasicMessage(@PathVariable String id) {
		YunMenJinSuoServer.sendMessage(SequenceType.SEQUENCE_INTERNAL_BASIC.value(),
				CommandType.COMMAND_INTERNAL.value(), "0001", "0E", id);
	}
	
	/**
	 * 获取基本信息数据
	 * @return
	 */
	@RequestMapping("/basicMessageData")
	public ResponseEntity<ResponseMessage> basicMessageData() {
		ResponseMessage responseMessage = (ResponseMessage) YunMenJinSuoServer.getMessageMap().get(SequenceType.SEQUENCE_INTERNAL_BASIC.value());
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}
	

}
