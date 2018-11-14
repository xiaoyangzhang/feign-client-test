package it.pcan.test.feign.server;

import it.pcan.test.feign.bean.UploadInfo;
import it.pcan.test.feign.bean.UploadMetadata;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author Pierantonio Cangianiello
 */
@RestController
@EnableAutoConfiguration
@Import(value = MultipartAutoConfiguration.class)
public class ServerMain {

    private int i = 0;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    @Resource(name = "eurekaDiscoveryClient")
    private DiscoveryClient discoveryClient;

    @RequestMapping(path = "/test", method = POST, consumes = "application/json")
    public HttpEntity<UploadInfo> upload(@RequestBody UploadMetadata metadata) {
        return ResponseEntity.ok(new UploadInfo(i++, 0, "dummy.tmp"));
    }

    @RequestMapping(path = "/upload/{folder}", method = POST)
    public HttpEntity<UploadInfo> upload(@PathVariable String folder, @RequestParam MultipartFile file, @RequestParam UploadMetadata metadata) {
        return ResponseEntity.ok(new UploadInfo(i++, file.getSize(), folder + "/" + file.getOriginalFilename()));
    }

    @RequestMapping(path = "/uploadSimple/{folder}", method = POST)
    public HttpEntity<UploadInfo> uploadSimple(@PathVariable String folder, @RequestPart MultipartFile file) {
        return ResponseEntity.ok(new UploadInfo(i++, file.getSize(), folder + "/" + file.getOriginalFilename()));
    }

    @RequestMapping(path = "/uploadArray/{folder}", method = POST)
    public HttpEntity<List<UploadInfo>> uploadArray(@PathVariable String folder, @RequestPart MultipartFile[] files, @RequestPart UploadMetadata metadata) {
        List<UploadInfo> response = new ArrayList<>();
        for (MultipartFile file : files) {
            response.add(new UploadInfo(i++, file.getSize(), folder + "/" + file.getOriginalFilename()));
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/media_temp",method = RequestMethod.POST)
    public UploadInfo uploadTempMedia(@NotBlank(message = "媒体文件类型不能为空") @RequestParam("type") String type,
                                                   @NotBlank(message = "appId不能为空") @RequestParam("appId") String appId,
                                                   @RequestParam("file")MultipartFile multipartFile)  {

        System.out.println(multipartFile.getOriginalFilename());
        return null;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServerMain.class, args);
    }


}
