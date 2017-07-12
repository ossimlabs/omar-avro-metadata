package io.ossim.omaravrometadata

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * This class is used to configure the AmazonDynamoDB object to be used by the repository to interface with the DynamoDB instance
 */
@Configuration
@EnableDynamoDBRepositories("io.ossim.omaravrometadata")
class DynamoDBConfig
{
    /**
     * Autowired AWSCredentialsProvider used to authenticate the AmazonDynamoDB object with an AWS account
     */
    @Autowired
    AWSCredentialsProvider awsCredentialsProvider

    /**
     * Used by the AvroMetadataRepository (through Spring Boot magic) to access the DynamoDB instance on AWS
     * @return
     */
    @Bean
    AmazonDynamoDB amazonDynamoDB()
    {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .build()

        return amazonDynamoDB
    }
}
