package com.springawscloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * Created by kevinjanvier on 23/05/2017.
 */
@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private ResourcePatternResolver resolver;

    @RequestMapping(value="/download", method= RequestMethod.GET)
    public void download(@RequestParam String filename) throws IOException {
        Resource resource = this.resourceLoader.getResource("s3://samplechap/adsupload" + filename);
        InputStream input = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String str = new String();
        StringBuilder builder = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            builder.append(str);
        }

//        URL / file name where download file is placed
        File file = new File("/Users/kevinjanvier/Desktop");
        FileWriter writer = new FileWriter(file);
        writer.write(builder.toString());

        writer.close();
        reader.close();
        input.close();
    }


    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public void upload(@RequestParam String filename) throws IOException {
//        Directory of files to be uploaded
        File file = new File("/Users/kevinjanvier/Desktop/kevin.txt" + filename);
        FileInputStream input = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String str = new String();
        StringBuilder builder = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            builder.append(str);
        }


        Resource resource = this.resourceLoader.getResource("s3://samplechap/adsupload" + filename);
        WritableResource writableResource = (WritableResource) resource;

        try (OutputStream output = writableResource.getOutputStream()
        ) {
            output.write(builder.toString().getBytes());
        }
    }

    //Bucket Search
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public String search() throws IOException {
        Resource[] resources = this.resolver.getResources("s3://samplechap/adsupload/**/*");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < resources.length; i++) {
            if (i!=0) {
                builder.append(", ");
            }
            builder.append(resources[i].toString());
        }

        return builder.toString();
    }

}
