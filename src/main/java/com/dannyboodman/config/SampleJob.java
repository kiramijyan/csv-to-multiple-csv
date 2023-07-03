package com.dannyboodman.config;

import com.dannyboodman.processor.CustomItemProcessor;
import com.dannyboodman.task.DirectoryCleanupTasklet;
import com.dannyboodman.utils.ApplicationPaths;
import com.dannyboodman.model.UserCsv;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.List;

@Configuration
public class SampleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private CustomItemProcessor customItemProcessor;

	@Autowired
	private DirectoryCleanupTasklet directoryCleanupTasklet;

	@Bean
	public Step directoryCleanupStep() {
		System.err.println("Cleanup");
		return stepBuilderFactory.get("directoryCleanupStep")
				.tasklet(directoryCleanupTasklet)
				.build();
	}

	@Scheduled(cron = "0 * * * * *")
	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(directoryCleanupStep())
				.next(firstChunkStep())
				.build();
	}

	private Step firstChunkStep() {
		System.err.println("firstChunkStep");
		return stepBuilderFactory.get("First Chunk Step")
				.<UserCsv, UserCsv>chunk(10)
				.reader(flatFileItemReader())
				.processor(customItemProcessor)
				.writer(myItemWriter())
				.build();
	}

	@Bean
	public ItemWriter<UserCsv> myItemWriter() {
		return new ItemWriter<UserCsv>() {
			@Override
			public void write(List<? extends UserCsv> items) throws Exception {}
		};
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

}
