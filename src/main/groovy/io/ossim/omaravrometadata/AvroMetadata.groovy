package io.ossim.omaravrometadata

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "avro-metadata")
class AvroMetadata
{
    private String imageId
    private String avroMetadata

    @DynamoDBHashKey
    String getImageId()
    {
        return imageId
    }

    @DynamoDBAttribute
    String getAvroMetadata()
    {
        return avroMetadata
    }

    void setImageId(String imageId)
    {
        this.imageId = imageId
    }

    void setAvroMetadata(String avroMetadata)
    {
        this.avroMetadata = avroMetadata
    }
}