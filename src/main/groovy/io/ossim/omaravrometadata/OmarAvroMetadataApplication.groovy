package io.ossim.omaravrometadata

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * This application serves as a RESTful interface for adding, retrieving, and deleting AvroMetadata objects from AWS DynamoDB
 */
@SpringBootApplication
class OmarAvroMetadataApplication
{
	static void main(String[] args)
    {
		SpringApplication.run OmarAvroMetadataApplication, args
	}
}
