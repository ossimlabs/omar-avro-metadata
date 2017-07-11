package io.ossim.omaravrometadata

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableDynamoDBRepositories("io.ossim.omaravrometadata")
class DynamoDBConfig
{

    @Value('${amazon.dynamodb.endpoint}')
    private String amazonDynamoDBEndpoint

    @Value('${amazon.aws.accesskey}')
    private String amazonAWSAccessKey

    @Value('${amazon.aws.secretkey}')
    private String amazonAWSSecretKey

    @Value('${amazon.aws.region}')
    private String amazonAWSRegion

    @Bean
    AmazonDynamoDB amazonDynamoDB()
    {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(amazonAWSCredentials())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion))
                .build()

        return amazonDynamoDB
    }

    @Bean
    AWSStaticCredentialsProvider amazonAWSCredentials()
    {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey))
    }
}
