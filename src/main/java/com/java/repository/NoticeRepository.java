package com.java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java.entity.Notice;

/**
 * 通知Repository接口
 * @author zxy
 *
 */
public interface NoticeRepository extends JpaRepository<Notice, Integer> ,JpaSpecificationExecutor<Notice>{

}
