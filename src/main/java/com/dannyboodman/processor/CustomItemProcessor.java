package com.dannyboodman.processor;

import com.dannyboodman.model.UserCsv;
import com.dannyboodman.utils.ApplicationPaths;
import com.dannyboodman.utils.FirstLineHolder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

@Component
public class CustomItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        String folderName = item.substring(3, 5);
        createFolderIfNotExists(folderName);

        String fileName = folderName + LocalDate.now() + ".csv";
        File outputFile = new File(ApplicationPaths.OUTPUT_FOLDER + "/" + folderName, fileName);

        // Append the current item to the file
        FileWriter fileWriter = new FileWriter(outputFile, true);

        // Check if the file is newly created and write the header
        if (outputFile.length() == 0) {
            String header = FirstLineHolder.firstLineValue;
            fileWriter.write(header + System.lineSeparator());
        }

        fileWriter.write(item + System.lineSeparator());
        fileWriter.close();

        return item;
    }

    /*@Override
    public String process(String item) throws Exception {

        String folderName = item.substring(3, 5);

        createFolderIfNotExists(folderName);


        String fileName = folderName + LocalDate.now() + ".csv";
        File outputFile = new File(ApplicationPaths.OUTPUT_FOLDER + "/" + folderName, fileName);

        // Append the current item to the file
        FileWriter fileWriter = new FileWriter(outputFile, true);
        fileWriter.write(item + System.lineSeparator());
        fileWriter.close();

        return item;
    }
     */
    private void createFolderIfNotExists(String folderName) {
        File folder = new File(ApplicationPaths.OUTPUT_FOLDER + "/" + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
