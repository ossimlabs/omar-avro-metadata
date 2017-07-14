package io.ossim.omaravrometadata

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.wordnik.swagger.annotations.ApiModelProperty

/**
 * The AvroMetadata class is used to store the AVRO Metadata of an image.
 * AvroMetadata objects will be stored in a AWS DynamoDB instance to be
 * queryable by apps such as the OMAR UI.
 */
@DynamoDBTable(tableName = "avro-metadata")
class AvroMetadata
{
    @ApiModelProperty(value = "imageId key", required = true)
    private String imageId

    @ApiModelProperty(value = "avroMetadata value", required = true)
    private String avroMetadata

    /**
     * The imageId of the AvroMetadata object
     * This is the key for the object being stored in the DynamoDB instance
     * @return the imageId
     */
    @DynamoDBHashKey
    String getImageId()
    {
        return imageId
    }

    /**
     * The String representation of the AvroMetadata object
     * This is the value of the object being stored in the DynamoDB instance
     * @return the avroMetadata String
     */
    @DynamoDBAttribute
    String getAvroMetadata()
    {
        return avroMetadata
    }

    void setImageId(final String imageId)
    {
        this.imageId = imageId
    }

    void setAvroMetadata(final String avroMetadata)
    {
        this.avroMetadata = avroMetadata
    }
}