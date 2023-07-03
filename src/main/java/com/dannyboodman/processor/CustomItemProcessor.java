package com.dannyboodman.processor;

import com.dannyboodman.model.UserCsv;
import com.dannyboodman.utils.ApplicationPaths;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

@Component
public class CustomItemProcessor implements ItemProcessor<UserCsv, UserCsv> {


    @Override
    public UserCsv process(UserCsv item) throws Exception {

        String folderName = item.getId().substring(3, 5);
        String firstName = item.getFirstName();
        String lastName = item.getLastName();
        String email = item.getEmail();
        String birthDate = item.getBirthDate();

        createFolderIfNotExists(folderName);

        String fileName = folderName + LocalDate.now() + ".cvs";
        FileWriter fileWriter = new FileWriter(new File(ApplicationPaths.OUTPUT_FOLDER + "/" + folderName, fileName), true);
        fileWriter.write(item.getId() + "," + firstName + "," + lastName + "," + email + "," + birthDate + "\n");
        fileWriter.close();

        return new UserCsv(item.getId(), firstName, lastName, email, birthDate);
    }

    private void createFolderIfNotExists(String folderName) {
        File folder = new File(ApplicationPaths.OUTPUT_FOLDER + "/" + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
