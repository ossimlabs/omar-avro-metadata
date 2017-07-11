package io.ossim.omaravrometadata

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface AvroMetadataRepository extends CrudRepository<AvroMetadata, String>
{
    AvroMetadata findByImageId(String imageId)
}