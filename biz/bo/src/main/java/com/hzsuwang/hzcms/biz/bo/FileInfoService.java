package com.hzsuwang.hzcms.biz.bo;

import com.hzsuwang.hzcms.domain.FileInfoDO;

public interface FileInfoService {

    /**
     * @param fileMd5
     * @return
     */

    public FileInfoDO getByMd5File(String fileMd5);

    /**
     * @param fileInfoDO
     * @return
     */
    public String addFileInfo(FileInfoDO fileInfoDO);

    /**
     * @param id
     */
    public void deleteFileById(long id);

    /**
     * @param md5
     */
    public void deleteFileByMd5(String md5);

    /**
     * @param id
     * @return
     */
    public FileInfoDO getFileInfoById(Long id);
}
