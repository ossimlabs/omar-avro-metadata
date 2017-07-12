package io.ossim.omaravrometadata

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

/**
 * A simple Spring Data CrudRepository interface that allows for access of the AvroMetadata objects in the "avro-metadata" table in DynamoDB
 */
@EnableScan
interface AvroMetadataRepository extends CrudRepository<AvroMetadata, String>
{
    /**
     * This method allows for finding an AvroMetadata object by imageId using the repository
     * @param imageId the imageId to use as the key of the AvroMetadata object to find
     * @return the AvroMetadata object associated with the imageId key
     */
    AvroMetadata findByImageId(String imageId)
}