package com.ypt.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ypt.springboot.entity.AlarmLog;
import com.ypt.springboot.entity.LoginLog;
import com.ypt.springboot.mapper.AlarmLogMapper;
import com.ypt.springboot.mapper.LoginMapper;
import com.ypt.springboot.service.AlarmLogService;
import com.ypt.springboot.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kkk
 * @since 2022-10-05
 */
@Service
public class AlarmLogServiceImpl extends ServiceImpl<AlarmLogMapper, AlarmLog> implements AlarmLogService {

}
