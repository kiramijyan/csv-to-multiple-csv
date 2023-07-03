package com.dannyboodman.config;

import com.dannyboodman.processor.CustomItemProcessor;
import com.dannyboodman.utils.ApplicationPaths;
import com.dannyboodman.model.UserCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dannyboodman.processor.FirstItemProcessor;
import com.dannyboodman.reader.FirstItemReader;
import com.dannyboodman.writer.FirstItemWriter;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;

@Configuration
public class SampleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private FirstItemReader firstItemReader;

	@Autowired
	private CustomItemProcessor customItemProcessor;

	@Autowired
	private FirstItemProcessor firstItemProcessor;

	@Autowired
	private FirstItemWriter firstItemWriter;

	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())
				.build();
	}

	private Step firstChunkStep() {
		return stepBuilderFactory.get("First Chunk Step")
				.<UserCsv, UserCsv>chunk(10)
				.reader(flatFileItemReader())
				.processor(customItemProcessor)
				.writer(flatItemWriter())
				.build();
	}


	public FlatFileItemReader<UserCsv> flatFileItemReader() {
		FlatFileItemReader<UserCsv> flatFileItemReader = new FlatFileItemReader<UserCsv>();
		flatFileItemReader.setStrict(false);
		flatFileItemReader.setResource(new FileSystemResource(new File(ApplicationPaths.INPUT)));

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

	/*
	public FlatFileItemWriter<UserCsv> flatItemWriter() {
		FlatFileItemWriter<UserCsv> itemWriter = new FlatFileItemWriter<>();

		// Set the output file path
		itemWriter.setResource(new FileSystemResource(ApplicationPaths.OUTPUT));

		// Set the line aggregator to format the output as CSV
		DelimitedLineAggregator<UserCsv> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<UserCsv> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[]{"id", "firstName", "lastName", "email", "birthDate"});
		lineAggregator.setFieldExtractor(fieldExtractor);
		itemWriter.setLineAggregator(lineAggregator);

		return itemWriter;
	}
	 */

	@Bean
	public FlatFileItemWriter<UserCsv> flatItemWriter() {
		FlatFileItemWriter<UserCsv> itemWriter = new FlatFileItemWriter<>();

		// Set the output folder
		String folderPath = ApplicationPaths.OUTPUT_FOLDER;
		File folder = new File(folderPath);

		// Check if the output folder exists
		if (!folder.exists() || !folder.isDirectory()) {
			// Create the output folder if it doesn't exist
			folder.mkdirs();
		}

		// Get the existing CSV file in the output folder
		File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
		File outputFile;
		if (files != null && files.length > 0) {
			// Use the first existing CSV file in the folder
			outputFile = files[0];
		} else {
			// Create a new CSV file in the output folder
			outputFile = new File(folder, "output.csv");
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				// Handle file creation exception
			}
		}

		// Set the resource for the FlatFileItemWriter
		itemWriter.setResource(new FileSystemResource(outputFile));

		// Set the line aggregator to format the output as CSV
		DelimitedLineAggregator<UserCsv> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<UserCsv> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[]{"id", "firstName", "lastName", "email", "birthDate"});
		lineAggregator.setFieldExtractor(fieldExtractor);

		itemWriter.setLineAggregator(lineAggregator);

		return itemWriter;
	}
}
