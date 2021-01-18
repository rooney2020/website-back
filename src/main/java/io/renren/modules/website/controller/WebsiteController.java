package io.renren.modules.website.controller;

import io.renren.common.exception.RRException;
import io.renren.common.utils.R;
import io.renren.modules.website.conf.CommonConfig;
import io.renren.modules.website.entity.FileEntity;
import io.renren.modules.website.utils.FileUploadUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        FileEntity files = getInstance(0, file.getName(), file.getName());
        getFiles(CommonConfig.BASE_PATH, files);
        return R.ok().put("data", files);
    }

    @PostMapping("/directory/add")
    public R addDirectory(@RequestParam("path") String path) {
        if (!isLegal(path)) {
            return R.error("非法路径");
        }
        path = realPath(path);
        File file = new File(path);
        if (file.exists()) {
            return R.error("目录已存在");
        }
        if (file.isDirectory()) {
            file.mkdirs();
        }
        return R.ok();
    }

    @PostMapping("/directory/delete")
    public R removeDirectory(@RequestParam("path") String path) {
        if (!isLegal(path)) {
            return R.error("非法路径");
        }
        path = realPath(path);
        File file = new File(path);
        if (!file.isDirectory()) {
            return R.error("所选目标不是目录");
        }
        if (file.exists()) {
            deleteDirectory(path);
        } else {
            return R.error("目录不存在");
        }
        return R.ok();
    }

    @PostMapping("/file/delete")
    public R removeFile(@RequestParam("path") String path) {
        if (!isLegal(path)) {
            return R.error("非法路径");
        }
        path = realPath(path);
        File file = new File(path);
        if (file.isDirectory()) {
            return R.error("所选目标不是文件");
        }
        if (file.exists()) {
            deleteFile(path);
        } else {
            return R.error("目录不存在");
        }
        return R.ok();
    }

    @PostMapping("/upload")
    public R uploadFile(@RequestParam String path, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        if (!isLegal(path)) {
            return R.error("非法路径");
        }
        try {
            // 文件路径
            String fileName = FileUploadUtils.upload(CommonConfig.UPLOAD_PATH + path, file);
            // 返回
            return R.ok();
        } catch (Exception e) {
            // 返回错误信息
            return R.error(e.getMessage());
        }
    }

    public void getFiles(String path, FileEntity origin) throws Exception {
        //目标集合fileList
        File file = new File(path);
        if (file.isDirectory()) {
            List<FileEntity> subs = new ArrayList<>();
            for (File fileIndex : file.listFiles()) {
                if (fileIndex.isDirectory()) {
                    FileEntity sub = getInstance(0, fileIndex.getName(), origin.getRootPath() + CommonConfig.FILE_DELEMITER + fileIndex.getName());
                    subs.add(sub);
                    getFiles(fileIndex.getPath(), sub);
                } else {
                    FileEntity sub = getInstance(1, fileIndex.getName(), origin.getRootPath() + CommonConfig.FILE_DELEMITER + fileIndex.getName());
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
        return fileEntity;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     */
    public boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    public boolean isLegal(String path) {
        return path.startsWith(CommonConfig.RESOURCE_PREFIX);
    }

    public String realPath(String path) {
        return CommonConfig.UPLOAD_PATH + path.substring(CommonConfig.RESOURCE_PREFIX.length());
    }
}
