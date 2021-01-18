package io.renren.modules.website.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.modules.website.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 请修改注释
 *
 * @author zhaoliyuan
 * @date 2021.01.18
 */
@Data
@TableName("website_directory_structure")
public class FileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 目录id
     */
    private Integer uuid = CommonUtils.uuid();

    /**
     * 文件类型，0：目录，1：文件
     */
    private Integer fileType;

    /**
     * 目录名称
     */
    private String fileName;

    /**
     * 路径
     */
    private String rootPath;

    /**
     * 下级目录
     */
    @TableField(exist = false)
    private List<FileEntity> children;
}
