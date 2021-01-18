package io.renren.modules.website.controller;

import io.renren.common.utils.R;
import io.renren.modules.website.conf.CommonConfig;
import io.renren.modules.website.entity.FileEntity;
import org.apache.commons.io.monitor.FileEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 请修改注释
 *
 * @author zhaoliyuan
 * @date 2021.01.18
 */
@RestController
@RequestMapping("/index")
public class WebsiteController {

    @GetMapping("/list")
    public R list() throws Exception {
        File file = new File(CommonConfig.BASE_PATH);
        FileEntity files = getInstance(0, file.getName(), file.getPath());
        getFiles(CommonConfig.BASE_PATH, files);
        return R.ok().put("data", files);
    }

    public void getFiles(String path, FileEntity origin) throws Exception {
        //目标集合fileList
        File file = new File(path);
        if (file.isDirectory()) {
            List<FileEntity> subs = new ArrayList<>();
            for (File fileIndex : file.listFiles()) {
                if (fileIndex.isDirectory()) {
                    FileEntity sub = getInstance(0, fileIndex.getName(), fileIndex.getPath());
                    subs.add(sub);
                    getFiles(fileIndex.getPath(), sub);
                } else {
                    FileEntity sub = getInstance(0, fileIndex.getName(), fileIndex.getPath());
                    subs.add(sub);
                }
            }
            origin.setChildren(subs);
        }
    }

    public FileEntity getInstance(Integer fileType, String fileName, String rootPath) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileType(fileType);
        fileEntity.setFileName(fileName);
        fileEntity.setRootPath(rootPath);
        return  fileEntity;
    }
}
