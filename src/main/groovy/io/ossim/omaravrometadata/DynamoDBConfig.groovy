package io.ossim.omaravrometadata

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableDynamoDBRepositories("io.ossim.omaravrometadata")
class DynamoDBConfig
{
    @Autowired
    AWSCredentialsProvider awsCredentialsProvider

    @Bean
    AmazonDynamoDB amazonDynamoDB()
    {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .build()

        return amazonDynamoDB
    }
}
