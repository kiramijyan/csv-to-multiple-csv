package com.dannyboodman.config;

import com.dannyboodman.processor.CustomItemProcessor;
import com.dannyboodman.task.DirectoryCleanupTasklet;
import com.dannyboodman.model.UserCsv;
import com.dannyboodman.writer.CustomFlatFileItemReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Autowired
	private CustomFlatFileItemReader customFlatFileItemReader;


	@Bean
	public Job csvToMultipleCsv() {
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(directoryCleanupStep())
				.next(writeCsvToMultipleCsv())
				.build();
	}

	@Bean
	public Step directoryCleanupStep() {
		return stepBuilderFactory.get("Directory Cleanup")
				.tasklet(directoryCleanupTasklet)
				.build();
	}

	@Bean
	public Step writeCsvToMultipleCsv() {
		return stepBuilderFactory.get("Write CSV")
				.<UserCsv, UserCsv>chunk(10)
				.reader(customFlatFileItemReader.flatFileItemReader())
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
}
