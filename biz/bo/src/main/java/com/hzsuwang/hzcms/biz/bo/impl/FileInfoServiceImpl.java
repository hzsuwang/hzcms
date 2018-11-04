package com.hzsuwang.hzcms.biz.bo.impl;

import com.hzsuwang.hzcms.biz.bo.FileInfoService;
import com.hzsuwang.hzcms.common.base.BaseService;
import com.hzsuwang.hzcms.domain.FileInfoDO;
import org.springframework.stereotype.Service;

@Service("fileInfoService")
public class FileInfoServiceImpl extends BaseService implements FileInfoService {

    @Override
    public FileInfoDO getByMd5File(String fileMd5) {
        return null;
    }

    @Override
    public String addFileInfo(FileInfoDO fileInfoDO) {
        return null;
    }

    @Override
    public void deleteFileById(long id) {

    }

    @Override
    public void deleteFileByMd5(String md5) {

    }

    @Override
    public FileInfoDO getFileInfoById(Long id) {
        return null;
    }
}
