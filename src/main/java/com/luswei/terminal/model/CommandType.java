package com.luswei.terminal.model;

public enum CommandType {

    /**
     * 登录信息 01 data: { send: 12 byte, response: 1 byte
     *                      {send: null},
     *                      {response: {success: 30, failure: 31}}
     *                   }
     * 心跳信息 02 data: { send: 0 byte, response: 1 byte
     *                      {send: null},
     *                      {response: {success: 30, failure: 31}}
     *                   }
     * 提取记录 05 data: { send: 1 byte, response: 43/2 byte
     *                      {send: 01},
     *                      {response: 01, success: {存在记录: Length=41 返回值为 42byte 的记录,
     *                                      {不存在记录: Length=1 返回当前门磁状态（最低位）:
     *                                          {开: 1, 关: 0}}
     *                                     }
     *                                     failure: 32(设备忙)
     *                      }
     *                   }
     * 批量读取记录 05 data: { send: 2 byte, response: ((8/16/32)*42+1)/2 byte
     *                          {send: 02, {number_1: 08, number_2: 10, number_3: 20}},
     *                          {response: 02, {success: {提取成功: Length=number*42+1,
     *                                                    提取失败: 31 Length=2},
     *                                          failure: 32(设备忙)}}
     *                      }
     * 指令开门 06 data: { send: 2 byte, response: 2 byte
     *                      {send: 03, {door_1: 01, door_2: 02}},
     *                      {response: 03, {success: 30, failure: 31}}
     *                   }
     * 读卡模式 09 data: { send: 7 byte, response: 2 byte
     *                      {send: 15, {typeA: 0A, typeB: 0B, typeA and typeB: 0C}},
     *                      {response: 15, {success: 30, failure: 31}}
     *                   }
     * 发行模式 09 data: { send: 7 byte, response: 2 byte
     *                      {send: 18, {正常模式: 0A, 发卡状态: 0B}},
     *                      {response: 18, {success: 30, failure: 31}}
     *                   }
     *
     */
    COMMAND__LOGIN((String) "01"), COMMAND_HEART((String) "02"),COMMAND_TIME((String) "03"),
    COMMAND_USER((String) "04"),COMMAND_RECORD((String) "05"),COMMAND_OPEN((String) "06"),
    COMMAND_NON_STANDARD((String) "07"),COMMAND_UPDATE((String) "08"),
    COMMAND_INTERNAL((String) "09"),COMMAND_UDP((String) "0A");
    private String value;

    private CommandType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
