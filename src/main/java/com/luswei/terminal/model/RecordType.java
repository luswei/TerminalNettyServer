package com.dmdh.YunMenJinSuoWeb.model;

public enum RecordType {

    NORMOAL((String) "40"), PASSWORD_OPEN((String) "41"), PASSWORD_CARD_OPEN((String) "42"),
    CARD_OPEN((String) "43"),COMMAND_OPEN((String) "44"),ALARM_OPEN((String) "45"),
    BUTTONS_OPEN((String) "46"),ILLEGAL_CARD((String) "47"),TIMEOUT_OPEN((String) "48"),
    BREAK_IN((String) "49"),PRYING_RESISTANT((String) "4A"),TEST_CARD((String) "4B"),
    PAST_CARD((String) "4C"),HOLIDAY_CARD_OPEN((String) "4D"),OUTTIMEPERIOD((String) "4F"),
    LOSSCARD((String) "50"),LOADMASTERCARD((String) "51"),INITMASTERCARD((String) "52"),
    TIMES_CARD_OPEN((String) "53");
    private String value;

    private RecordType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
