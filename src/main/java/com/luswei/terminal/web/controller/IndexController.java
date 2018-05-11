package com.luswei.terminal.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luswei.terminal.model.ResponseMessage;
import com.luswei.terminal.netty.server.YunMenJinSuoServer;

import io.netty.channel.Channel;

@Controller
public class IndexController {

	/**
	 * 设置默认首页
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "forward:dojoDemo.html";
	}
	
	/**
	 * 获取地址列表
	 * 
	 * @return
	 */
	@RequestMapping("/address")
	public ResponseEntity<ResponseMessage> addressList() {
		ResponseMessage responseMessage = new ResponseMessage();
		
		responseMessage.setHead("addressList");
		responseMessage.setBody(YunMenJinSuoServer.getAddressList(YunMenJinSuoServer.getMap()));

		
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}
	
	
}
