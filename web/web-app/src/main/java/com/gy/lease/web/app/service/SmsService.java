package com.gy.lease.web.app.service;

import javax.sql.rowset.spi.SyncResolver;

public interface SmsService {
    void Sms(String phone,String code);


}
