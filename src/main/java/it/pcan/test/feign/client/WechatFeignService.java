package it.pcan.test.feign.client;

import feign.Param;
import feign.RequestLine;
import it.pcan.test.feign.bean.UploadInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: cms-api
 * @description:
 * @author: zhangxiaoyang
 * @date: 2018/11/9 6:00 PM
 **/
public interface WechatFeignService {

    @RequestLine("POST /wx/media_temp")
    UploadInfo uploadMedia(@Param("type") String type,
                           @Param("appId") String appId,
                           @Param("file") MultipartFile multipartFile);
}
