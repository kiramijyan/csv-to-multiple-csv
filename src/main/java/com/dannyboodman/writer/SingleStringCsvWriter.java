package com.dannyboodman.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.List;

@Component
public class SingleStringCsvWriter implements ItemWriter<String>, ResourceAware {

    private Resource resource;

    @Override
    public void write(List<? extends String> items) throws Exception {
        try (PrintWriter writer = new PrintWriter(resource.getFile())) {
            // Write the header as a single string
            writer.println("ID,First Name,Last Name,Email,Birthdate");

            // Write the CSV contents as a single string
            StringBuilder csvBuilder = new StringBuilder();
            for (String item : items) {
                csvBuilder.append(item).append("\n");
            }
            writer.println(csvBuilder.toString());
        }
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
