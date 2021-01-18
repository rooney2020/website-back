package io.renren.modules.website.conf;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请修改注释
 *
 * @author zhaoliyuan
 * @date 2021.01.18
 */
public class CommonConfig {
    public static final String FILE_DELEMITER = "/";
    public static final String URL = "/home/projects/upload/temp";
    public static final String UPLOAD_PATH = "/home/projects/upload/temp";
    public static final String RESOURCE_PREFIX = "/profile";
//    public static final String BASE_PATH = "D:\\upload\\notes";
    public static final String BASE_PATH = "/home/projects/upload/notes";

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public static String getUrl()
    {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
