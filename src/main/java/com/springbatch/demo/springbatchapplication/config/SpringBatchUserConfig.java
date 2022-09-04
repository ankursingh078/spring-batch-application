package com.springbatch.demo.springbatchapplication.config;

import com.springbatch.demo.springbatchapplication.model.User;
import com.springbatch.demo.springbatchapplication.processor.UserProcessor;
import com.springbatch.demo.springbatchapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchUserConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private UserRepository repository;

    @Bean
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new ClassPathResource("users1.csv"));
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setLineMapper(getLineMapper());
        return fileItemReader;
    }

    private DefaultLineMapper<User> getLineMapper() {
        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("id", "first_name", "last_name", "email", "gender");
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<User> filedSetMapper = new BeanWrapperFieldSetMapper<>();
        filedSetMapper.setTargetType(User.class);
        defaultLineMapper.setFieldSetMapper(filedSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    public RepositoryItemWriter<User> writer() {
        RepositoryItemWriter<User> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(repository);
        itemWriter.setMethodName("saveAndFlush");
        return itemWriter;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("user-job")
                .flow(step1())
                .end()
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("user-batch-step1")
                .<User, User>chunk(50)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
