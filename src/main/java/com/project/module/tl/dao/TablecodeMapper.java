package com.project.module.tl.dao;

import java.util.Map;

import com.project.common.util.MyMapper;
import com.project.entity.Tablecode;

public interface TablecodeMapper extends MyMapper<Tablecode> {

	int createTable(Map<String, Object> map);
}