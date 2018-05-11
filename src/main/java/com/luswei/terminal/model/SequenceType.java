package com.dmdh.YunMenJinSuoWeb.model;

public enum SequenceType {

    SEQUENCE_LOGIN((String) "01"), SEQUENCE_HEART((String) "02"), SEQUENCE_TIME((String) "03"),
    SEQUENCE_USER((String) "04"), SEQUENCE_RECORD((String) "05"), SEQUENCE_PRODUCT((String) "06"),
    SEQUENCE_NON_STANDARD((String) "07"), SEQUENCE_UPDATE((String) "08"), SEQUENCE_INTERNAL((String) "09"), SEQUENCE_UDP((String) "0A"),
    SEQUENCE_TIME_READ((String) "03"),
    SEQUENCE_USER_SELECT((String) "03"), SEQUENCE_USER_READ((String) "04"),
    SEQUENCE_RECORD_ONE((String) "01"), SEQUENCE_RECORD_MANY((String) "02"),
    SEQUENCE_PRODUCT_OPEN((String) "03"),
    SEQUENCE_INTERNAL_BASIC((String) "14");
    private String value;

    private SequenceType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
