package com.dannyboodman.writer;

import com.dannyboodman.utils.ApplicationPaths;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CustomFlatFileItemReader {

    public FlatFileItemReader<String> flatFileItemReader() {
        FlatFileItemReader<String> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setStrict(false);
        flatFileItemReader.setResource(new FileSystemResource(new File(ApplicationPaths.INPUT_100000)));

        flatFileItemReader.setLineMapper((line, lineNumber) -> line);

        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }
}