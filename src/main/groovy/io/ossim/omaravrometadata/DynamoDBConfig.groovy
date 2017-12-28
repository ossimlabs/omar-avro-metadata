package io.ossim.omaravrometadata

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * This class is used to configure the AmazonDynamoDB object to be used by the repository to interface with the DynamoDB instance
 */
@Configuration
@EnableDynamoDBRepositories(basePackages = "io.ossim.omaravrometadata", dynamoDBOperationsRef = "dynamoDBOperations")
class DynamoDBConfig
{
    /**
     * Autowired AWSCredentialsProvider used to authenticate the AmazonDynamoDB object with an AWS account
     */
    @Autowired
    AWSCredentialsProvider awsCredentialsProvider

    @Value('${omar.avro.metadata.tableName:avro-metadata}')
    String tableName

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

    @Bean
    DynamoDBOperations dynamoDBOperations()
    {
        DynamoDBMapperConfig dbMapperConfig = new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableName))
        DynamoDBTemplate dynamoDBTemplate = new DynamoDBTemplate(amazonDynamoDB(), dbMapperConfig)

        return dynamoDBTemplate
    }
}
