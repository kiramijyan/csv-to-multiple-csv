package com.dannyboodman.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class HeaderRecordListener implements StepExecutionListener {

    private String headerRecord;

    public HeaderRecordListener(String headerRecord) {
        this.headerRecord = headerRecord;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Do nothing
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        executionContext.put("headerRecord", headerRecord);
        return null;
    }
}
