package it.pcan.test.feign.client;

import feign.Feign;
import it.pcan.test.feign.bean.UploadInfo;
import it.pcan.test.feign.bean.UploadMetadata;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author Pierantonio Cangianiello
 */
@RestController
@EnableAutoConfiguration
@Import(value = MultipartAutoConfiguration.class)
public class FeignClientController {


    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    @Resource(name = "eurekaDiscoveryClient")
    private DiscoveryClient discoveryClient;



    @RequestMapping(value = "/uploadMedia", method = RequestMethod.POST)
    public UploadInfo uploadMedia(@RequestParam("file") MultipartFile multipartFile) {
        List<ServiceInstance> services = discoveryClient.getInstances("instance-id");
        Random random = new Random(0);
        ServiceInstance serviceInstance = services.get(random.nextInt(services.size()));
        WechatFeignService wechatFeignService = Feign.builder().decoder(new SpringDecoder(messageConverters))
                                                     .encoder(new FeignSpringFormEncoder())
                                                     .target(WechatFeignService.class,
                                                             "http://" + serviceInstance.getHost() + ":" + serviceInstance
                                                                     .getPort() + "/");
        //前两个参数是自己业务的需要，可以删除
        UploadInfo result = wechatFeignService.uploadMedia("image", "xxx", multipartFile);
        return result;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FeignClientController.class, args);
    }

}
