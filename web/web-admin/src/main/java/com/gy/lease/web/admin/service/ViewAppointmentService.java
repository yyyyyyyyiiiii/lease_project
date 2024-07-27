package com.gy.lease.web.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.lease.model.entity.ViewAppointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.gy.lease.web.admin.vo.appointment.AppointmentVo;

/**
* @author liubo
* @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ViewAppointmentService extends IService<ViewAppointment> {

    Page<AppointmentVo> pageAppointmentVo(Page<AppointmentVo> appointmentVoPage, AppointmentQueryVo queryVo);
}
