package io.ossim.omaravrometadata

interface AvroMetadataService
{
    AvroMetadata addAvroMetadata(AvroMetadata avroMetadata)

    AvroMetadata getAvroMetadata(String imageId)

    boolean deleteAvroMetadata(String imageId)

    List<AvroMetadata> listAvroMetadata()
}