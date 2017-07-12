package io.ossim.omaravrometadata

/**
 * An interface that defines the methods needed for any AvroMetadataService class
 */
interface AvroMetadataService
{
    /**
     * Used to add a single AvroMetadata object to the database
     * @param avroMetadata the AvroMetadata object to store in the database
     * @return the AvroMetadata object successfully stored in the database
     */
    AvroMetadata addAvroMetadata(AvroMetadata avroMetadata)

    /**
     * Used to get a single AvroMetadata object from the database using the imageId as the key
     * @param imageId the imageId to use as the key when retrieving the AvroMetadata object from the database
     * @return the AvroMetadata object successfully retrieved from the database
     */
    AvroMetadata getAvroMetadata(String imageId)

    /**
     * Used to delete a single AvroMetadata object from the database using the imageId as the key
     * @param imageId the imageId to use as the key of the AvroMetadata objec to delete from the database
     * @return true if successfully delete, false if not
     */
    boolean deleteAvroMetadata(String imageId)

    /**
     * Used to get a list of all AvroMetadata objects stored in the database
     * @return a list of all AvroMetadata objects stored in the database
     */
    List<AvroMetadata> listAvroMetadata()
}