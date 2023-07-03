package com.dannyboodman.writer;

import com.dannyboodman.model.UserCsv;
import com.dannyboodman.utils.ApplicationPaths;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CustomFlatFileItemReader {

    public FlatFileItemReader<UserCsv> flatFileItemReader() {
        FlatFileItemReader<UserCsv> flatFileItemReader = new FlatFileItemReader<UserCsv>();
        flatFileItemReader.setStrict(false);
        flatFileItemReader.setResource(new FileSystemResource(new File(ApplicationPaths.INPUT_100000)));

        DefaultLineMapper<UserCsv> defaultLineMapper = new DefaultLineMapper<UserCsv>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email", "Birthdate");

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        BeanWrapperFieldSetMapper<UserCsv> fieldSetMapper = new BeanWrapperFieldSetMapper<UserCsv>();
        fieldSetMapper.setTargetType(UserCsv.class);

        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }
}