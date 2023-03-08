package com.ypt.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ypt.springboot.entity.Files;
import com.ypt.springboot.entity.User;
import com.ypt.springboot.mapper.FileMapper;
import com.ypt.springboot.service.FileService;
import com.ypt.springboot.utils.Res;
import com.ypt.springboot.utils.TokenUtils;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/file")
public class MinioController {
    @Autowired
    private MinioClient minioClient;
    private static final String MINIO_BUCKET = "test";
    @Resource
    private FileMapper fileMapper;
    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public List<Object> list(ModelMap map) throws Exception {
        Iterable<Result<Item>> myObjects = minioClient.listObjects(MINIO_BUCKET);
        Iterator<Result<Item>> iterator = myObjects.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','fileSize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse(String.format(format, item.objectName(), formatFileSize(item.size()))));
        }
        return items;
    }

    @PostMapping("/upload")
    public Res upload(@RequestParam(name = "file", required = false ) MultipartFile[] file ,  HttpServletRequest request) {
        Res res = new Res();
        res.setCode(500);
        if (file == null || file.length == 0) {
            res.setMessage("上传文件不能为空");
            return res;
        }

        List<String> orgfileNameList = new ArrayList<>(file.length);
        User currentUser = TokenUtils.getCurrentUser();

        for (MultipartFile multipartFile : file) {
            String orgfileName = multipartFile.getOriginalFilename();
            orgfileNameList.add(orgfileName);
            String s = orgfileName.split("\\.")[1];
            try {
                InputStream in = multipartFile.getInputStream();
                minioClient.putObject(MINIO_BUCKET, orgfileName, in, new PutObjectOptions(in.available(), -1));
                Files files = new Files();
                files.setUrl(orgfileName);
                files.setUserId(currentUser.getId());
                files.setType(s.toLowerCase());
                files.setName(orgfileName);
                fileMapper.insert(files);
                in.close();
            } catch (Exception e) {
                log.error(e.getMessage());
                res.setMessage("上传失败");
                return res;
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bucketName", MINIO_BUCKET);
        data.put("fileName", orgfileNameList);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);
        return res;
    }

    @RequestMapping("/download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        InputStream in = null;
        try {
            ObjectStat stat = minioClient.statObject(MINIO_BUCKET, fileName);
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            in = minioClient.getObject(MINIO_BUCKET, fileName);
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @GetMapping("/delete/{fileName}")
    public Res delete(@PathVariable("fileName") String fileName) {
        Res res = new Res();
        res.setCode(200);
        try {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("name",fileName);
            fileMapper.delete(qw);
            minioClient.removeObject(MINIO_BUCKET, fileName);
        } catch (Exception e) {
            res.setCode(500);
            log.error(e.getMessage());
        }
        return res;
    }




    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }


    @GetMapping("/page")
    public com.ypt.springboot.common.Result findPage(@RequestParam Integer pageNum,
                                                     @RequestParam Integer pageSize) {
        User currentUser = TokenUtils.getCurrentUser();
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",currentUser.getId());
        Page page = new Page();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Files> list = fileService.page(page, queryWrapper);
        return com.ypt.springboot.common.Result.success(list);
    }
}

