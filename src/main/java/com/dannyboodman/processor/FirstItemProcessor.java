package com.dannyboodman.processor;

import com.dannyboodman.model.UserCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<UserCsv, UserCsv> {

	@Override
	public UserCsv process(UserCsv item) throws Exception {
		System.out.println("Inside Item Processor");
		return item;
	}

}
