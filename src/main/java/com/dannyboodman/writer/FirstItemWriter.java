package com.dannyboodman.writer;

import java.util.List;

import com.dannyboodman.model.UserCsv;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<UserCsv> {

	@Override
	public void write(List<? extends UserCsv> items) throws Exception {
		System.out.println("Inside Item Writer");
		items.stream().forEach(System.out::println);
	}

}
